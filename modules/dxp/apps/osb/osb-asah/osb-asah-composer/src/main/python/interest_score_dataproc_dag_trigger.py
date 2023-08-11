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

from liferay.dataproc import DataprocClusterGetOrCreateOperator, \
	DataprocSubmitInterestScorePySparkJobOperator

import airflow
import os
import pendulum
import requests

def create_dag(ac_project_id, ac_project_time_zone_id, dag_id, dag_description, params=None):
	with airflow.DAG(
		dag_id=dag_id,
		default_args={
			'ac_project_id': ac_project_id,
			'ac_project_time_zone_id': ac_project_time_zone_id,
			'dag_configuration_key': 'interest_score_dag_trigger',
			'owner': 'Liferay',
			'project_id': os.environ['GOOGLE_PROJECT_ID'],
			'region': os.environ['GOOGLE_REGION']
		},
		description=dag_description,
		max_active_runs=1,
		params=params,
		schedule_interval='0 1 * * *',
		start_date=pendulum.now(ac_project_time_zone_id) - pendulum.duration(days=2)
	) as dag:
		cluster_get_or_create = DataprocClusterGetOrCreateOperator(
			task_id='dataproc_cluster_get_or_create'
		)

		submit_interest_score_pyspark_job = \
			DataprocSubmitInterestScorePySparkJobOperator(
				task_id='dataproc_submit_interest_score_pyspark_job',
				cluster_name="{{ ti.xcom_pull(task_ids='dataproc_cluster_get_or_create')['cluster_name'] }}"
			)

		cluster_get_or_create >> submit_interest_score_pyspark_job

		return dag

response = requests.get(
	Variable.get('osb.asah.backend.url'),
	headers={
		'OSB-Asah-Faro-Backend-Security-Signature': Variable.get('osb.asah.faro.backend.security.signature'),
		'OSB-Asah-Project-ID': 'osbasah',
		'User-Agent': 'LiferayAnalyticsCloud'
	}
)

dl_keyword_extraction_enabled_workspaces = [
	'asah884f47d2c9e847fb83ee50b3614d3991',
	'asahda2fcb11192544c8ad6c4d14cfd37214',
	'uatb71b092fd41e44a2aeda2f9d24569edf'
]

for project in response.json():
	project_id = project.get('id')

	dag_id = 'interest_score_{}'.format(project_id)

	params = None

	if project_id in dl_keyword_extraction_enabled_workspaces:
		params = {
			'INTEREST_KEYWORDS_EXTRACTION_METHOD': 'dl'
		}

	if project.get('sitesSelected'):
		globals()[dag_id] = create_dag(
			project_id, project.get('timeZoneId'), dag_id,
			'Interest Score DAG For {}'.format(project_id), params
		)