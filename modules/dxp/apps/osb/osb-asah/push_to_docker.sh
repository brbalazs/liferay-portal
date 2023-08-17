#!/bin/bash

set -e

function build_and_push_docker_images {
	if [ "$(docker images -q liferaycloud/com-liferay-osb-asah-private 2> /dev/null)" ]
	then
		echo ""
		echo "Removing local images."

		docker rmi -f $(docker images -q liferaycloud/com-liferay-osb-asah-private) >/dev/null
	fi

	for file_name in `ls`
	do
		if [ -z "$(ls -A ${file_name}/LCP.*.json 2> /dev/null)" ] ||
		   [ ! -e ${file_name}/Dockerfile ]
		then
			continue
		fi

		local docker_image_tag=$(get_docker_image_tag ${file_name})

		build_docker_image ${docker_image_tag} ${file_name}

		echo ""
		echo "Pushing ${docker_image_tag}."
		echo ""

		docker push ${docker_image_tag}
	done
}

function build_docker_image {
	local docker_image_tag=${1}
	local file_name=${2}

	echo ""
	echo "Building ${docker_image_tag}."
	echo ""

	if [ ${file_name} == osb-asah-backend ] ||
	   [ ${file_name} == osb-asah-batch-curator ] ||
	   [ ${file_name} == osb-asah-publisher ] ||
	   [ ${file_name} == osb-asah-stream-curator ] ||
	   [ ${file_name} == osb-asah-upgrade ]
	then
		cp ~/.asah/client.zip ${file_name}/build/client.zip

		echo "" >> ${file_name}/Dockerfile
		echo "COPY ./build/client.zip client.zip" >> ${file_name}/Dockerfile
		echo "RUN unzip client.zip" >> ${file_name}/Dockerfile

		cp ~/.asah/asia-south1/gcp_credentials.json ${file_name}/build/asia_south1_gcp_credentials.json
		cp ~/.asah/europe-west2/gcp_credentials.json ${file_name}/build/europe_west2_gcp_credentials.json
		cp ~/.asah/europe-west3/gcp_credentials.json ${file_name}/build/europe_west3_gcp_credentials.json
		cp ~/.asah/southamerica-east1/gcp_credentials.json ${file_name}/build/southamerica_east1_gcp_credentials.json
		cp ~/.asah/uat/gcp_credentials.json ${file_name}/build/uat_gcp_credentials.json
		cp ~/.asah/us-west1/gcp_credentials.json ${file_name}/build/us_west1_gcp_credentials.json

		echo "" >> ${file_name}/Dockerfile
		echo "COPY ./build/asia_south1_gcp_credentials.json asia_south1_gcp_credentials.json" >> ${file_name}/Dockerfile
		echo "COPY ./build/europe_west2_gcp_credentials.json europe_west2_gcp_credentials.json" >> ${file_name}/Dockerfile
		echo "COPY ./build/europe_west3_gcp_credentials.json europe_west3_gcp_credentials.json" >> ${file_name}/Dockerfile
		echo "COPY ./build/southamerica_east1_gcp_credentials.json southamerica_east1_gcp_credentials.json" >> ${file_name}/Dockerfile
		echo "COPY ./build/uat_gcp_credentials.json uat_gcp_credentials.json" >> ${file_name}/Dockerfile
		echo "COPY ./build/us_west1_gcp_credentials.json us_west1_gcp_credentials.json" >> ${file_name}/Dockerfile
		echo "" >> ${file_name}/Dockerfile
		echo "ENV SPRING_PROFILES_ACTIVE=prod" >> ${file_name}/Dockerfile
	elif [ ${file_name} == osb-asah-elasticsearch-data-node ] ||
		   [ ${file_name} == osb-asah-elasticsearch-master-node ]
	then
		mkdir -p ${file_name}/build

		cp ~/.asah/server.zip ${file_name}/build/server.zip

		echo "" >> ${file_name}/Dockerfile
		echo "COPY --chown=elasticsearch:root ./build/server.zip /certs/" >> ${file_name}/Dockerfile
		echo "RUN mkdir /usr/share/elasticsearch/config/certificates && unzip /certs/server.zip -d /usr/share/elasticsearch/config/certificates" >> ${file_name}/Dockerfile
	fi

	docker build \
		--build-arg LABEL_BUILD_DATE=$(date "${CURRENT_DATE}" +'%Y-%m-%dT%H:%M:%SZ') \
		--build-arg LABEL_VCS_REF=$(git rev-parse HEAD) \
		--build-arg LABEL_VCS_URL=$(git config --get remote.origin.url) \
		--tag ${docker_image_tag} \
		${file_name}

	git checkout --quiet ${file_name}/Dockerfile
}

function check_repository {
	if [ ! -f ~/.asah/client.zip ]
	then
		echo "${HOME}/.asah/client.zip does not exist.";

		exit
	fi

	if [ ! -f ~/.asah/asia-south1/gcp_credentials.json ] ||
	   [ ! -f ~/.asah/europe-west2/gcp_credentials.json ] ||
	   [ ! -f ~/.asah/europe-west3/gcp_credentials.json ] ||
	   [ ! -f ~/.asah/southamerica-east1/gcp_credentials.json ] ||
	   [ ! -f ~/.asah/uat/gcp_credentials.json ] ||
	   [ ! -f ~/.asah/us-west1/gcp_credentials.json ]
	then
		echo "Verify gcp_credentials.json files.";

		exit
	fi

	if [ ! -f ~/.asah/server.zip ]
	then
		echo "${HOME}/.asah/server.zip does not exist.";

		exit
	fi

	gradlew formatSource

	if [ -n "$(git status --porcelain -uno)" ]
	then
		echo "There are source formatter changes. Please fix them and try again.";

		exit
	fi
}

function compile_repository {
	gradlew clean assemble
}

function date {
	export LC_ALL=en_US.UTF-8
	export TZ=America/Los_Angeles

	if [ -z ${1+x} ] || [ -z ${2+x} ]
	then
		if [ "$(uname)" == "Darwin" ]
		then
			/bin/date
		elif [ -e /bin/date ]
		then
			/bin/date --iso-8601=seconds
		else
			/usr/bin/date --iso-8601=seconds
		fi
	else
		if [ "$(uname)" == "Darwin" ]
		then
			/bin/date -jf "%a %b %e %H:%M:%S %Z %Y" "${1}" "${2}"
		elif [ -e /bin/date ]
		then
			/bin/date -d "${1}" "${2}"
		else
			/usr/bin/date -d "${1}" "${2}"
		fi
	fi
}

function generate_wedeploy_profile {
	local profile_name=${1}
	local service_name=${2}
	local file_path=${3}

	local destination_file_path=.wedeploy_profiles/${profile_name}/${service_name}

	mkdir -p ${destination_file_path}

	cp ${file_path} ${destination_file_path}/LCP.json

	local file_content=$(<${destination_file_path}/LCP.json)

	if [[ ${file_content} != *\"image\"* ]]
	then
		sed "s@\"id\"@\"image\": \"$(get_docker_image_tag ${service_name})\", \"id\"@" ${destination_file_path}/LCP.json

		python -m json.tool --sort-keys ${destination_file_path}/LCP.json > ${destination_file_path}/LCP.json.formatted

		mv ${destination_file_path}/LCP.json.formatted ${destination_file_path}/LCP.json

		sed $'s/    /\t/g' ${destination_file_path}/LCP.json

		perl -e 'chomp if eof' -pi ${destination_file_path}/LCP.json

		rm -f ${destination_file_path}/LCP.json.bak
	fi
}

function generate_wedeploy_profiles {
	rm -fr .wedeploy_profiles

	for file_path in **/LCP.*.json
	do
		local service_name="${file_path%%/*}"
		local profile_name="$(basename ${file_path#$service_name/LCP.} .json)"

		generate_wedeploy_profile ${profile_name} ${service_name} ${file_path}

		if [ ${profile_name} != "dev" ] &&
		   [ ${profile_name} != "prd" ] &&
		   [ ${profile_name} != "uat" ]
		then
			continue;
		fi

		if [ ! -f ${service_name}/build.gradle ]
		then
			continue;
		fi
	done

	git add .wedeploy_profiles

	git commit -m "Generate WeDeploy profiles at $(date "${CURRENT_DATE}" +'%Y%m%d')-${GIT_HASH}" .wedeploy_profiles
}

function generate_tag {
	git tag $(date "${CURRENT_DATE}" +'%Y%m%d')-${GIT_HASH} HEAD
}

function get_docker_image_tag {
	echo "liferaycloud/com-liferay-osb-asah-private:${1}-$(date "${CURRENT_DATE}" +'%Y%m%d')-${GIT_HASH}"
}

function gradlew {
	./gradlew "$@"

	if [ $? -ne 0 ]
	then
		exit 1
	fi
}

function main {
	check_repository

	compile_repository

	build_and_push_docker_images

	generate_wedeploy_profiles

	generate_tag

	push_to_github
}

function push_to_github {
	git push origin
	git push upstream
	git push upstream $(date "${CURRENT_DATE}" +'%Y%m%d')-${GIT_HASH}
}

function sed {
	if [ "$(uname)" == "Darwin" ]
	then
		/usr/bin/sed -i .bak "${1}" "${2}"
	else
		/usr/bin/sed -i "${1}" "${2}"
	fi
}

CURRENT_DATE=$(date)
GIT_HASH=$(git rev-parse --short=7 HEAD)

main