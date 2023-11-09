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

from airflow import DAG
from airflow.models import DagRun
from airflow.operators.python import ShortCircuitOperator
from airflow.providers.google.cloud.hooks.dataproc import DataprocHook
from airflow.providers.google.cloud.operators.dataproc import \
	ClusterGenerator, \
	DataprocCreateClusterOperator, \
	DataprocSubmitPySparkJobOperator
from airflow.utils.context import Context
from airflow.utils.state import DagRunState

from liferay.common import BaseOperator

import json
import pendulum
import time

class DataprocClusterGetOrCreateOperator(BaseOperator):

	def __init__(self, **kwargs):
		super().__init__(**kwargs)

		self.dataproc_hook = DataprocHook()

	def do_execute(self, dag: DAG, dag_run: DagRun, **kwargs):
		dag_configuration = kwargs[dag.dag_id]

		cluster_name = dag_configuration['dataproc.cluster.name']

		self.log.info("cluster_name: {}".format(cluster_name))

		cluster_config = ClusterGenerator(
			idle_delete_ttl=dag_configuration[
				'dataproc.cluster.idle_delete_ttl'
			],
			image_version=dag_configuration['dataproc.cluster.image_version'],
			master_disk_size=dag_configuration[
				'dataproc.cluster.master.disk_size'
			],
			master_disk_type=dag_configuration[
				'dataproc.cluster.master.disk_type'
			],
			master_machine_type=dag_configuration[
				'dataproc.cluster.master.machine_type'
			],
			metadata=json.loads(
				dag_configuration['dataproc.cluster.metadata']
			),
			num_masters=dag_configuration['dataproc.cluster.master.count'],
			num_workers=dag_configuration['dataproc.cluster.worker.count'],
			project_id=dag.default_args['project_id'],
			properties=json.loads(
				dag_configuration['dataproc.cluster.properties']
			),
			region=dag.default_args['region'],
			use_if_exists=True,
			worker_disk_size=dag_configuration[
				'dataproc.cluster.worker.disk_size'
			],
			worker_disk_type=dag_configuration[
				'dataproc.cluster.worker.disk_type'
			],
			worker_machine_type=dag_configuration[
				'dataproc.cluster.worker.machine_type'
			]
		).make()

		dataproc_create_cluster_operator = DataprocCreateClusterOperator(
			task_id='dataproc_cluster_create',
			region=dag.default_args['region'],
			cluster_name=cluster_name,
			cluster_config=cluster_config
		)

		return dataproc_create_cluster_operator.execute(Context(kwargs))

class DataprocSubmitCommercePySparkJobOperator(BaseOperator):

	RESOURCE_NAME_APPLICATION_MAP = {
		'com.liferay.headless.commerce.machine.learning.dto.v1_0.Order':
			'liferay.commerce.recommend.UserInteractionRecommendationApplication',
		'com.liferay.headless.commerce.machine.learning.dto.v1_0.Product':
			'liferay.commerce.recommend.ProductContentRecommendationApplication',
	}

	def __init__(self, cluster_name: str, data_source_id: str, resource_name: str, **kwargs):
		super().__init__(**kwargs)

		self.log.warning(cluster_name)

		self._cluster_name = cluster_name

		self._data_source_id = data_source_id

		self._resource_name = resource_name

	def _get_application_name(
		self, ac_project_id: str, data_source_id: str, resource_name: str
	):

		application_name = self.RESOURCE_NAME_APPLICATION_MAP.get(resource_name)

		if application_name is not None:
			application_name = application_name.split('.')[-1]

		application_name = application_name[0:20]

		return '{}-{}-{}'.format(
			ac_project_id,
			application_name.lower(),
			data_source_id
		)

	def do_execute(self, dag: DAG, dag_run: DagRun, **kwargs):
		cluster_name = self.render_template(
			content=self._cluster_name, context=Context(kwargs)
		)

		dag_configuration = kwargs[dag.dag_id]

		dataproc_submit_pyspark_job_operator = DataprocSubmitPySparkJobOperator(
			archives=[
				'gs://{}/resources/{}'.format(
					dag_configuration['dataproc.bucket'],
					dag_configuration['dataproc.pyspark.configuration']
				)
			],
			arguments=[
				self.RESOURCE_NAME_APPLICATION_MAP.get(
					self._resource_name
				),
				'--ac-project-id',
				dag.default_args['ac_project_id'],
				'--configuration',
				dag_configuration['dataproc.pyspark.configuration'],
				'--data-source-id',
				self._data_source_id
			],
			cluster_name=cluster_name,
			dataproc_properties=json.loads(
				dag_configuration['dataproc.pyspark.properties']
			),
			job_name=self._get_application_name(
				ac_project_id=dag.default_args['ac_project_id'],
				data_source_id=self._data_source_id,
				resource_name=self._resource_name
			),
			main='gs://{}/osb-asah-spark-python-driver.py'.format(
				dag_configuration['dataproc.bucket']
			),
			project_id=dag.default_args['project_id'],
			pyfiles=[
				'gs://{}/osb-asah-spark-python.zip'.format(
					dag_configuration['dataproc.bucket']
				)
			],
			region=dag.default_args['region'],
			task_id='dataproc_submit_pyspark_job'
		)

		dataproc_submit_pyspark_job_operator.execute(Context(kwargs))

class DataprocSubmitContentRecommenderPySparkJobOperator(BaseOperator):

	APPLICATION_MAP = {
		'MostViewedContentRecommendation':
			'liferay.recommend.MostViewedContentRecommendationApplication',
		'UserContentRecommendation':
			'liferay.recommend.UserContentRecommendationApplication',
	}

	def __init__(self, application_name: str, cluster_name: str, data_source_id: str, **kwargs):
		super().__init__(**kwargs)

		self._application_name = application_name

		self.log.warning(cluster_name)

		self._cluster_name = cluster_name

		self._data_source_id = data_source_id

	def do_execute(self, dag: DAG, dag_run: DagRun, **kwargs):
		cluster_name = self.render_template(
			content=self._cluster_name, context=Context(kwargs)
		)

		dag_configuration = kwargs[dag.dag_id]

		time_zone_id = dag.default_args['ac_project_time_zone_id']

		arguments = [
			self.APPLICATION_MAP.get(
				self._application_name
			),
			'--ac-project-id',
			dag.default_args['ac_project_id'],
			'--configuration',
			dag_configuration['dataproc.pyspark.configuration'],
			'--data-source-id',
			self._data_source_id,
			'--time-zone',
			time_zone_id
		]

		end_date = dag_run.conf.get("endDate", None)

		start_date = dag_run.conf.get("startDate", None)

		if end_date and start_date:
			arguments += [
				'--end-date',
				end_date,
				'--start-date',
				start_date
			]

		dataproc_submit_pyspark_job_operator = DataprocSubmitPySparkJobOperator(
			archives=[
				'gs://{}/resources/{}'.format(
					dag_configuration['dataproc.bucket'],
					dag_configuration['dataproc.pyspark.configuration']
				)
			],
			arguments=arguments,
			cluster_name=cluster_name,
			dataproc_properties=json.loads(
				dag_configuration['dataproc.pyspark.properties']
			),
			job_name='{}-{}-{}'.format(
				dag.default_args['ac_project_id'],
				self._application_name.lower(),
				self._data_source_id
			),
			main='gs://{}/osb-asah-spark-python-driver.py'.format(
				dag_configuration['dataproc.bucket']
			),
			project_id=dag.default_args['project_id'],
			pyfiles=[
				'gs://{}/osb-asah-spark-python.zip'.format(
					dag_configuration['dataproc.bucket']
				)
			],
			region=dag.default_args['region'],
			task_id='dataproc_submit_pyspark_job'
		)

		dataproc_submit_pyspark_job_operator.execute(Context(kwargs))

class DataprocSubmitInterestScorePySparkJobOperator(BaseOperator):

	def __init__(self, cluster_name: str, **kwargs):
		super().__init__(**kwargs)

		self.log.warning(cluster_name)

		self._cluster_name = cluster_name

	def do_execute(self, dag: DAG, dag_run: DagRun, **kwargs):
		context = Context(kwargs)

		cluster_name = self.render_template(
			content=self._cluster_name, context=context
		)

		dag_configuration = context[dag.dag_id]

		arguments = [
			'liferay.interest_score.InterestScoreApplication',
			'--ac-project-id',
			dag.default_args['ac_project_id'],
			'--configuration',
			dag_configuration['dataproc.pyspark.configuration'],
			'--job-parameters',
			self._get_job_parameters(dag, dag_run)
		]

		if context.get('params') is not None:
			try:
				environment_variables = context.get('params')

				self.log.warning(str(environment_variables))

				for environment_variable in environment_variables.items():
					environment_key, environment_value = environment_variable

					arguments += [
						'--environment-variable',
						f'{environment_key}={environment_value}'
					]
			except Exception as e:
				self.log.warning("Error parsing parameters: " + str(e))

		dataproc_submit_pyspark_job_operator = DataprocSubmitPySparkJobOperator(
			archives=[
				'gs://{}/resources/{}'.format(
					dag_configuration['dataproc.bucket'],
					dag_configuration['dataproc.pyspark.configuration']
				)
			],
			arguments=arguments,
			cluster_name=cluster_name,
			dataproc_properties=json.loads(
				dag_configuration['dataproc.pyspark.properties']
			),
			job_name=self._get_application_name(
				ac_project_id=dag.default_args['ac_project_id']
			),
			main='gs://{}/osb-asah-spark-python-driver.py'.format(
				dag_configuration['dataproc.bucket']
			),
			project_id=dag.default_args['project_id'],
			pyfiles=[
				'gs://{}/osb-asah-spark-python.zip'.format(
					dag_configuration['dataproc.bucket']
				)
			],
			region=dag.default_args['region'],
			task_id='dataproc_submit_pyspark_job'
		)

		dataproc_submit_pyspark_job_operator.execute(context)

	def _get_application_name(self, ac_project_id: str):

		return 'interest-score-{}-{}'.format(
			ac_project_id,
			int(time.time())
		)

	def _get_job_parameters(self, dag: DAG, dag_run: DagRun) -> str:
		parameters = list()

		time_zone_id = dag.default_args['ac_project_time_zone_id']

		now = pendulum.now(tz=time_zone_id)

		today = now.date()

		end_date = dag_run.conf.get("endDate", today.isoformat())

		start_date = dag_run.conf.get("startDate", None)

		if start_date is None:
			dag_runs = dag_run.find(
				dag_id=dag.dag_id,
				execution_start_date=pendulum.now(tz=time_zone_id) - pendulum.duration(days=30),
				state=DagRunState.SUCCESS
			)

			if dag_runs is not None and len(dag_runs) > 0:
				dag_run = dag_runs[-1]

				data_interval_end = dag_run.data_interval_end

				start_date = data_interval_end.date()

				if end_date == start_date:
					start_date = today.subtract(days=1)

				start_date = start_date.isoformat()

		if start_date:
			parameters.append(
				{
					"name": "endDate",
					"value": end_date
				}
			)

			parameters.append(
				{
					"name": "startDate",
					"value": start_date
				}
			)

		parameters.append(
			{
				"name": "timeZone",
				"value": time_zone_id
			}
		)

		self.log.info(
			"Using parameters: {}".format(
				json.dumps(parameters)
			)
		)

		return json.dumps(parameters)