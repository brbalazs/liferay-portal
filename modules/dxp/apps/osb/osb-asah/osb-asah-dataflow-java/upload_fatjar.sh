#!/bin/bash

PROJECT_ID=$(gcloud config get-value project)

GCS_BUCKET=gs://${PROJECT_ID}-dataflow/pipeline/

../gradlew clean shadowJar

if [ -f build/libs/osb-asah-dataflow-java-all.jar ]
then
	gcloud storage cp build/libs/osb-asah-dataflow-java-all.jar ${GCS_BUCKET}
else
	echo "Unable to find jar file. Skipping."

	exit 1
fi