#
# Copyright (c) 2000-present Liferay, Inc. All rights reserved.
#
# The contents of this file are subject to the terms of the Liferay Enterprise
# Subscription License ("License"). You may not use this file except in
# compliance with the License. You can obtain a copy of the License by
# contacting Liferay, Inc. See the License for the specific language governing
# permissions and limitations under the License, including but not limited to
# distribution rights of the Software.
#

from airflow.models import Variable
from airflow.providers.google.cloud.operators.dataflow import DataflowCreateJavaJobOperator

from liferay.bigquery import BigQueryInsertJobFromTemplateOperator

import airflow
import datetime
import os
import requests

DATAFLOW_BUCKET = 'gs://{}-dataflow'.format(os.environ['GOOGLE_PROJECT_ID'])

def create_dag(
	ac_project_id, dag_id, dag_description, job_class, job_name,
	merge_job_task_id, task_id
):
	with (airflow.DAG(
		dag_id=dag_id,
		default_args={
			'ac_project_id': ac_project_id,
			'owner': 'Liferay',
			'dataflow_default_options': {
				'project': os.environ['GOOGLE_PROJECT_ID'],
				'stagingLocation': DATAFLOW_BUCKET.concat('/staging/temp'),
			}
		},
		description=dag_description,
		max_active_runs=1,
		schedule_interval=None
	) as dag):
		dataflow_create_java_job_operator = DataflowCreateJavaJobOperator(
			dag=dag,
			jar=DATAFLOW_BUCKET.concat('/pipeline/osb-asah-dataflow-java.jar'),
			job_class=job_class,
			job_name=job_name,
			location= os.environ['GOOGLE_REGION'],
			options={
				"zipFilePath": "{{ params['zipFilePath'] }}",
				"projectId": ac_project_id,
				"bigQueryWriterTempLocation": DATAFLOW_BUCKET.concat(
					'/bigquery/temp'
				)
			},
			start_date=datetime.datetime.now(),
			task_id=task_id
		)

		dataflow_create_java_job_operator >> BigQueryInsertJobFromTemplateOperator(task_id=merge_job_task_id)

		return dag

response = requests.get(
	Variable.get('osb.asah.backend.url'),
	headers={
		'OSB-Asah-Faro-Backend-Security-Signature': Variable.get('osb.asah.faro.backend.security.signature'),
		'OSB-Asah-Project-ID': 'osbasah',
		'User-Agent': 'LiferayAnalyticsCloud'
	}
)

for project in response.json():
	commerce_channels_selected = project.get('commerceChannelsSelected')

	if commerce_channels_selected:
		# Order

		dag_id = 'dxp_order_ingestion_dataflow_trigger_{}'.format(
			project.get('id')
		)

		globals()[dag_id] = create_dag(
			project.get('id'), dag_id,
			'DXP Order Ingestion Dataflow Trigger For {}'.format(
				project.get('id')
			),
			'com.liferay.osb.asah.dataflow.ingestion.dxp.DXPOrderIngestionPipeline',
			'dxporderingestionpipeline-{}'.format(project.get('id')),
			'order_merge'
			'dxp_order_ingestion_dataflow_trigger'
		)

		# Product

		dag_id = 'dxp_product_ingestion_dataflow_trigger_{}'.format(
			project.get('id')
		)

		globals()[dag_id] = create_dag(
			project.get('id'), dag_id,
			'DXP Product Ingestion Dataflow Trigger For {}'.format(
				project.get('id')
			),
			'com.liferay.osb.asah.dataflow.ingestion.dxp.DXPProductIngestionPipeline',
			'dxpproductingestionpipeline-{}'.format(project.get('id')),
			'product_merge'
			'dxp_product_ingestion_dataflow_trigger'
		)