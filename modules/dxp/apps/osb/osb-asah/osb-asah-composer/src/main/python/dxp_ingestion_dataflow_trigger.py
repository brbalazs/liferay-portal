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
from airflow.models.baseoperator import chain
from airflow.providers.google.cloud.operators.dataflow import DataflowCreateJavaJobOperator

from liferay.bigquery import BigQueryInsertJobFromTemplateOperator

import airflow
import datetime
import os
import requests

DATAFLOW_BUCKET = 'gs://{}-dataflow'.format(os.environ['GOOGLE_PROJECT_ID'])

def create_big_query_jobs(task_ids):
	big_query_jobs = []

	for task_id in task_ids:
		if type(task_id) == list:
			big_query_jobs_map = map(
				lambda id: BigQueryInsertJobFromTemplateOperator(task_id=id),
				task_id
			)

			big_query_jobs.append(list(big_query_jobs_map))
		else:
			big_query_jobs.append(
				BigQueryInsertJobFromTemplateOperator(task_id=task_id)
			)

	return big_query_jobs

def create_dag(
	ac_project_id, dag_id, dag_description, dataflow_job_class,
	dataflow_job_name, downstream_task_ids, task_id):

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
			job_class=dataflow_job_class,
			job_name=dataflow_job_name,
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

		chain(
			dataflow_create_java_job_operator,
			create_big_query_jobs(downstream_task_ids))

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

	#
	# DXP Entity
	#

	downstream_task_ids = []

	if project.get('accountsSelected'):
		downstream_task_ids.append(['account_entry_merge', 'account_group_merge'])

	if project.get('contactsSelected'):
		downstream_task_ids.append([
			'expando_column_merge', 'expando_value_delete',
			'expando_value_merge', 'group_merge', 'organization_merge',
			'role_merge', 'team_merge', 'user_group_merge', 'user_merge'
		])

		downstream_task_ids.append('individual_merge')

	if len(downstream_task_ids) > 0:
		dag_id = 'dxp_entity_ingestion_dataflow_trigger_{}'.format(
			project.get('id')
		)

		globals()[dag_id] = create_dag(
			project.get('id'), dag_id,
			'DXP Entity Ingestion Dataflow Trigger For {}'.format(
				project.get('id')
			),
			'com.liferay.osb.asah.dataflow.ingestion.dxp.DXPEntityIngestionPipeline',
			'dxpentityingestionpipeline-{}'.format(project.get('id')),
			downstream_task_ids,
			'dxp_entity_merge'
			'dxp_entity_ingestion_dataflow_trigger'
		)

	if project.get('commerceChannelsSelected'):

		#
		# Order
		#

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
			['order_merge'],
			'dxp_order_ingestion_dataflow_trigger'
		)

		#
		# Product
		#

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
			['product_merge'],
			'dxp_product_ingestion_dataflow_trigger'
		)