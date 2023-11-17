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

from liferay.bigquery import BigQueryShortCircuitOperator
from liferay.dataproc import DataprocClusterGetOrCreateOperator, \
	DataprocSubmitContentRecommenderPySparkJobOperator

import airflow
import os
import pendulum
import requests

def create_dag(ac_project_id, ac_project_time_zone_id, dag_id, dag_description, data_source_ids, application_name, schedule_interval, table_name: str):
	with airflow.DAG(
		dag_id=dag_id,
		default_args={
			'ac_project_id': ac_project_id,
			'ac_project_time_zone_id': ac_project_time_zone_id,
			'dag_configuration_key': 'content_recommenders_dag_trigger',
			'data_source_ids': data_source_ids,
			'owner': 'Liferay',
			'project_id': os.environ['GOOGLE_PROJECT_ID'],
			'region': os.environ['GOOGLE_REGION'],
			'application_name': application_name
		},
		description=dag_description,
		max_active_runs=1,
		schedule_interval=schedule_interval,
		start_date=pendulum.now(ac_project_time_zone_id) - pendulum.duration(days=2)
	) as dag:
		bigquery_short_circuit_operator_all = BigQueryShortCircuitOperator(
			task_id='bigquery_short_circuit_operator_all',
			sql=f"""
				SELECT
					COUNT(*)
				FROM
					{dag.default_args['ac_project_id']}.{table_name}
				WHERE
					datasourceId in ({', '.join(map(lambda d: str(d), data_source_ids))})
			"""
		)

		cluster_get_or_create = DataprocClusterGetOrCreateOperator(
			task_id='dataproc_cluster_get_or_create'
		)

		for data_source_id in data_source_ids:
			bigquery_short_circuit_operator = BigQueryShortCircuitOperator(
				task_id='bigquery_short_circuit_operator_{}'.format(data_source_id),
				sql=f"""
					SELECT
						COUNT(*)
					FROM
						{ dag.default_args['ac_project_id'] }.{table_name}
					WHERE
						datasourceId = {str(data_source_id)}
				"""
			)

			dataproc_submit_content_recommender_pyspark_job = \
				DataprocSubmitContentRecommenderPySparkJobOperator(
					task_id='dataproc_submit_content_recommender_pyspark_job_{}'.format(data_source_id),
					cluster_name="{{ ti.xcom_pull(task_ids='dataproc_cluster_get_or_create')['cluster_name'] }}",
					data_source_id=str(data_source_id),
					application_name=application_name
				)

			bigquery_short_circuit_operator_all >> cluster_get_or_create >> bigquery_short_circuit_operator >> dataproc_submit_content_recommender_pyspark_job

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
	data_source_ids = project.get('dataSourceIds')

	if data_source_ids is None:
		continue

	dag_id = 'most_viewed_content_recommender_{}'.format(project.get('id'))

	globals()[dag_id] = create_dag(
		project.get('id'), project.get('timeZoneId'), dag_id,
		'Most Viewed Content Recommender DAG For {}'.format(project.get('id')),
		data_source_ids,
		'MostViewedContentRecommendation',
		None,
		'assetentity'
	)

	dag_id = 'user_content_recommender_{}'.format(project.get('id'))

	globals()[dag_id] = create_dag(
		project.get('id'), project.get('timeZoneId'), dag_id,
		'User Content Recommender DAG For {}'.format(project.get('id')),
		data_source_ids,
		'UserContentRecommendation',
		None,
		'assetentity'
	)