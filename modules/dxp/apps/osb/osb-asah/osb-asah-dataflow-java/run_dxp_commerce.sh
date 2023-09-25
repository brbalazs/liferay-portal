#!/bin/bash

if [ "$#" -ne 1 ]
then
	echo "Usage: run-dxp-commerce.sh [dxp-cloud-project]"
	exit 1
fi

PROJECT_ID=$(gcloud config get-value project)

DXP_CLOUD_PROJECT=${1}
GCS_BUCKET=gs://${PROJECT_ID}-dxp-entities/
MAIN_CLASS_NAME=com.liferay.osb.asah.dataflow.ingestion.dxp.DXPCommerceEntitiesIngestionPipeline
ORDER_BIGQUERY_TABLE=order_raw
PRODUCT_BIGQUERY_TABLE=product_raw
REGION=$(gcloud config get-value compute/region)
RUNNER=DataflowRunner

../gradlew clean assemble execute \
	-Dexec.args=" \
	--GCSBucket=${GCS_BUCKET} \
		--jobName=dxpcommerceentitiesingestionpipeline-${DXP_CLOUD_PROJECT}-latest \
		--orderBigQueryTable=${ORDER_BIGQUERY_TABLE} \
		--productBigQueryTable=${PRODUCT_BIGQUERY_TABLE} \
		--project=${PROJECT_ID} \
		--region=${REGION} \
		--runner=${RUNNER} \
	-Dexec.cleanupDaemonThreads=false \
	-Dexec.mainClass=${MAIN_CLASS_NAME}