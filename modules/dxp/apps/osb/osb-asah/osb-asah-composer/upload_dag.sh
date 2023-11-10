#!/bin/bash

LCP_PROJECT_ID=${1:=asahdev}
PROJECT_ID=$(gcloud config get-value project)
REGION=$(gcloud config get-value compute/region)

COMPOSER_ENVIRONMENT_NAME=ac-composer-${LCP_PROJECT_ID}

cd src/main/python

for i in `ls`;
do
	if [ $i = "content_recommenders_dataproc_trigger.py" ] &&
		 [ $PROJECT_ID != "liferaycloud-ac-uat" ]
	then
		continue
	fi

	gcloud composer environments storage dags import \
		--environment ${COMPOSER_ENVIRONMENT_NAME} \
		--location ${REGION} \
		--project ${PROJECT_ID} \
		--source ${i};
done

cd -

gcloud composer environments storage dags import \
	--environment ${COMPOSER_ENVIRONMENT_NAME} \
	--location ${REGION} \
	--project ${PROJECT_ID} \
	--source src/main/resources/