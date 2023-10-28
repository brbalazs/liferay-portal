#!/bin/bash

PROJECT_ID=$(gcloud config get-value project)

GCS_BUCKET=gs://${PROJECT_ID}-dataflow/pipeline/

../gradlew clean shadowJar

if [ -f build/libs/osb-asah-dataflow-java-all.jar ]
then
	mv build/libs/osb-asah-dataflow-java-all.jar build/libs/osb-asah-dataflow-java.jar

	gcloud storage cp build/libs/osb-asah-dataflow-java.jar ${GCS_BUCKET}
else
	echo "Unable to find JAR file. Skipping."

	exit 1
fi