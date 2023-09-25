#!/bin/bash

if [ "$#" -ne 1 ]
then
	echo "Usage: create_pubsub_subscriptions [dxp-cloud-project]"

	exit 1
fi

DXP_CLOUD_PROJECT=${1}
PROJECT_ID=$(gcloud config get-value project)

function create_subscription {
	local project=${1}
	local subscription=${3}
	local topic=${2}

	gcloud pubsub topics describe --project ${project} ${topic} &>/dev/null

	if [ $? -ne 0 ]
	then
		echo "Unable not find topic ${topic}. Skipping subscription ${subscription}."

		return
	fi

	gcloud pubsub subscriptions create --project ${project} --topic ${topic} ${subscription} 2>/dev/null

	if [ $? -eq 0 ]
	then
		echo "Subscription ${subscription} created successfully."
	else
		echo "Unable to create subscription ${subscription}."
	fi
}

function main {

	# DXP Entities

	create_subscription ${PROJECT_ID} ${DXP_CLOUD_PROJECT}_dxp_entities_default ${DXP_CLOUD_PROJECT}_dxp_entities_default

	# Events

	create_subscription ${PROJECT_ID} ${DXP_CLOUD_PROJECT}_analytics_events ${DXP_CLOUD_PROJECT}_analytics_events_dataflow
	create_subscription ${PROJECT_ID} ${DXP_CLOUD_PROJECT}_analytics_events ${DXP_CLOUD_PROJECT}_analytics_events_dataflow_backup
}

main