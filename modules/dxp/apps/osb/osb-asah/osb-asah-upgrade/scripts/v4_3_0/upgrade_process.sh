#!/bin/bash

PROJECT_ID=$(gcloud config get-value project)
REGION_ID=$(gcloud config get-value compute/region)

if [ ! -f project_time_zones ]
then
	echo "File project_time_zones does not exist. Please provide file with project ID as the first column and time zone ID as the second column.";

	exit
fi

function upgrade_page_daily_merge_statement {
	local asah_project_id=${1}
	local asah_project_timezone=${2}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s~\${asah_project_time_zone}~$asah_project_timezone~g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" -e "s/\${REGION_ID}/$REGION_ID/g" upgrade_page_daily_merge_statement.sql > new_upgrade_page_daily_merge_statement.sql

	echo "Upgrade Page Daily for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}, Asah Project Time Zone: ${asah_project_timezone}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_page_daily_merge_statement.sql
}

for i in $(bq ls --datasets=true --max_results=1000 | grep "asah" | grep -v "osbasah" | awk '{$1=$1;print}')
do :
	project_time_zone=$(cat project_time_zones | grep "$i" | awk '{print $2}')

	if [ -n "$project_time_zone" ]
	then
		upgrade_page_daily_merge_statement $i $project_time_zone
	else
		echo "Unable to find time zone for $i. Skipping project.";
	fi
done