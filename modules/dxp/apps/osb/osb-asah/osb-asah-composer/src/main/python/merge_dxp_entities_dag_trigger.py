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

from liferay.bigquery import BigQueryInsertJobFromTemplateOperator

import airflow
import datetime
import requests

def create_dag(
	ac_project_id, accounts_selected, contacts_selected, dag_id, dag_description
):

	with airflow.DAG(
		dag_id=dag_id,
		default_args={
			'ac_project_id': ac_project_id,
			'owner': 'Liferay'
		},
		description=dag_description,
		max_active_runs=1,
		schedule_interval='@hourly',
		start_date=datetime.datetime.now() - datetime.timedelta(hours=1)
	) as dag:
		big_query_jobs = []

		if accounts_selected:
			big_query_jobs += [
				BigQueryInsertJobFromTemplateOperator(task_id='account_entry_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='account_group_merge')
			]

		if contacts_selected:
			big_query_jobs += [
				BigQueryInsertJobFromTemplateOperator(task_id='expando_column_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='expando_value_delete'),
				BigQueryInsertJobFromTemplateOperator(task_id='expando_value_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='group_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='organization_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='role_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='team_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='user_group_merge'),
				BigQueryInsertJobFromTemplateOperator(task_id='user_merge')
			]

			big_query_jobs >> BigQueryInsertJobFromTemplateOperator(task_id='individual_merge')
		else:
			big_query_jobs

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
	dag_id = 'merge_dxp_entity_{}'.format(project.get('id'))

	accounts_selected = project.get('accountsSelected')
	contacts_selected = project.get('contactsSelected')

	if accounts_selected or contacts_selected:
		globals()[dag_id] = create_dag(
			project.get('id'), accounts_selected, commerce_channels_selected,
			contacts_selected, dag_id,
			'DXP Entity Merge DAG For {}'.format(project.get('id'))
		)