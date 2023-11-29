#!/bin/bash

PROJECT_ID=$(gcloud config get-value project)
REGION_ID=$(gcloud config get-value compute/region)

function upgrade_partitioned_table {
	local asah_project_id=${1}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" -e "s/\${REGION_ID}/$REGION_ID/g" upgrade_partitioned_table.sql > new_upgrade_partitioned_table.sql

	echo "Upgrade Partitioned Table for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_partitioned_table.sql
}

function upgrade_partition_expiration {
	local asah_project_id=${1}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" -e "s/\${REGION_ID}/$REGION_ID/g" upgrade_partition_expiration.sql > new_upgrade_partition_expiration.sql

	echo "Upgrade Partition expiration for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_partition_expiration.sql
}

for i in $(bq ls --datasets=true --max_results=1000 | grep "asah" | grep -v "osbasah" | awk '{$1=$1;print}')
do :
	upgrade_partitioned_table_script ${i}

	upgrade_partition_expiration ${i}
done