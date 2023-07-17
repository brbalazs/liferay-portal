#!/bin/bash

PROJECT_ID=$(gcloud config get-value project)
REGION_ID=$(gcloud config get-value compute/region)

function upgrade_identity_interest_page {
	local asah_project_id=${1}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" upgrade_identity_interest_page_statement.sql > new_upgrade_identity_interest_page_statement.sql

	echo "Upgrade Interest Keywords for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_identity_interest_page_statement.sql
}

function upgrade_identity_interest_score {
	local asah_project_id=${1}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" upgrade_identity_interest_score_statement.sql > new_upgrade_identity_interest_score_statement.sql

	echo "Upgrade Interest Keywords for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_identity_interest_score_statement.sql
}

function upgrade_membershipchange {
	local asah_project_id=${1}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" -e "s/\${REGION_ID}/$REGION_ID/g" upgrade_membershipchange_statement.sql > new_upgrade_membershipchange_statement.sql

	echo "Upgrade MembershipChange for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_membershipchange_statement.sql
}

function upgrade_membershipindividual {
	local asah_project_id=${1}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" -e "s/\${REGION_ID}/$REGION_ID/g" upgrade_membershipindividual_statement.sql > new_upgrade_membershipindividual_statement.sql

	echo "Upgrade MembershipIndividual for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_membershipindividual_statement.sql
}

function upgrade_session_interest_score {
	local asah_project_id=${1}

	sed -e "s/\${asah_project_id}/$asah_project_id/g" -e "s/\${PROJECT_ID}/$PROJECT_ID/g" upgrade_session_interest_score_statement.sql > new_upgrade_session_interest_score_statement.sql

	echo "Upgrade Interest Keywords for Project ID: ${PROJECT_ID}, Asah Project ID: ${asah_project_id}"

	bq --project_id ${PROJECT_ID} query --use_legacy_sql=false < new_upgrade_session_interest_score_statement.sql
}

for i in $(bq ls --datasets=true --max_results=1000 | grep "asah" | grep -v "osbasah" | awk '{$1=$1;print}')
do :
	upgrade_identity_interest_page $1
	upgrade_identity_interest_score $1
	upgrade_membershipchange $i
	upgrade_membershipindividual $i
	upgrade_session_interest_score $1
done