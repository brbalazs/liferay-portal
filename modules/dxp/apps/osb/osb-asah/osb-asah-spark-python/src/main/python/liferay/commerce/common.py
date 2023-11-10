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

from abc import ABCMeta, \
	abstractmethod

from datetime import datetime

from dateutil import parser

from liferay.common.configuration import Configuration
from liferay.common.spark import BaseSparkApplication, \
	BaseSparkJob

from pyspark import SparkConf
from pyspark.sql.window import Window

import argparse
import os
import pyspark.sql.functions as F
import pytz
import sys

class BaseCommerceSparkApplication(BaseSparkApplication, metaclass=ABCMeta):

	def __init__(self):
		super(BaseCommerceSparkApplication, self).__init__()

		self.configuration = Configuration(self.args.configuration)

		spark_context = self.spark_session.sparkContext

		spark_conf = spark_context.getConf()

		for key, value in spark_conf.getAll():
			if key.startswith('spark.yarn.appMasterEnv.'):
				env_key = key[len('spark.yarn.appMasterEnv.'):]

				os.environ[env_key] = value

	def _create_argument_parser(self):
		argument_parser = argparse.ArgumentParser(
			usage='{} liferay.commerce.recommend.<ApplicationName> '
			'--ac-project-id <AC Project ID> '
			'--configuration <Configuration Path> '
			'--data-source-id <Data Source ID>'.format(sys.argv[0])
		)

		argument_parser.add_argument('application')
		argument_parser.add_argument('--ac-project-id', required=True)
		argument_parser.add_argument('--configuration', required=True)
		argument_parser.add_argument('--data-source-id', required=True)

		return argument_parser

	def _create_spark_conf(self):
		spark_conf = SparkConf()

		spark_conf.set('materializationDataset', self.args.ac_project_id)
		spark_conf.set(
			'temporaryGcsBucket',
			'{}/temp_write_bucket_for_bq_writes'.format(
				self.configuration.get(
					'google.storage.bucket'
				)
			))
		spark_conf.set('viewsEnabled', 'true')

		return spark_conf

	@abstractmethod
	def _create_spark_job_pipeline(self):
		pass

	def start(self):
		spark_job_pipeline = self._create_spark_job_pipeline()

		try:
			spark_job_pipeline.run()
		except Exception as e:
			self.log.error(e)

			raise e

class BaseJSONDataFrameReaderSparkJob(BaseSparkJob):

	def __init__(
		self,
		spark_application,
		file_pattern,
		table_name,
		cache=True,
		id_column='id',
		latest=True,
		modified_date_column='modifiedDate'
	):
		super(BaseJSONDataFrameReaderSparkJob, self).__init__(spark_application)

		self.cache = cache
		self.file_pattern = file_pattern
		self.id_column=id_column
		self.latest = latest
		self.modified_date_column = modified_date_column
		self.table_name = table_name

	def _get_bucket_path(self):
		configuration = self.spark_application_configuration

		bucket_path = '{}/{}/{}/{}/'.format(
			configuration.get('google.storage.bucket'),
			self.spark_application.args.ac_project_id,
			self.spark_application_args.data_source_id, self.file_pattern
		)

		jvm = self.spark_session._jvm
		spark_context = self.spark_session.sparkContext

		file_system = jvm.org.apache.hadoop.fs.FileSystem

		file_system_instance = file_system.get(
			jvm.java.net.URI(bucket_path),
			spark_context._jsc.hadoopConfiguration()
		)

		located_file_statuses = file_system_instance.listFiles(
			jvm.org.apache.hadoop.fs.Path(
				'{}/FULL/'.format(bucket_path)
			),
			True
		)

		file_statuses = []
		while (located_file_statuses.hasNext()):
			file_statuses.append(located_file_statuses.next())

		file_statuses_sorted = sorted(
			file_statuses, key=lambda f: f.getModificationTime()
		)

		full_bucket_path = str(file_statuses_sorted[-1].getPath())

		full_bucket_path = full_bucket_path[:full_bucket_path.rindex('/') + 1]

		self.spark_application.log.info(
			'Loading data from: {}'.format(full_bucket_path)
		)

		try:
			located_file_statuses = file_system_instance.listFiles(
				jvm.org.apache.hadoop.fs.Path(
					'{}/INCREMENTAL/'.format(bucket_path)
				),
				True
			)
		except Exception as e:
			self.spark_application.log.warning(e)

			return full_bucket_path

		full_bucket_path_date = parser.isoparse(full_bucket_path.split('/')[-2])

		bucket_paths = [full_bucket_path]

		while (located_file_statuses.hasNext()):
			file_status = located_file_statuses.next()

			modification_datetime = datetime.fromtimestamp(
				int(file_status.getModificationTime() / 1000), tz=pytz.UTC)

			if modification_datetime > full_bucket_path_date:
				file_bucket_path = str(file_status.getPath())

				file_bucket_path = file_bucket_path[:file_bucket_path.rindex('/') + 1]

				self.spark_application.log.info(
					'Loading data from: {}'.format(file_bucket_path)
				)

				bucket_paths.append(file_bucket_path)

		return bucket_paths

	def _get_latest(self, data_frame):

		window_spec = Window.partitionBy(self.id_column)

		window_spec = window_spec.orderBy(
			F.col('modifiedDate').desc()
		)

		return data_frame.withColumn(
				'row_number', F.row_number().over(window_spec)
			).where(
				'row_number=1'
			).drop(
				'row_number'
		)

	@abstractmethod
	def _post_process(self, data_frame):
		raise NotImplementedError()

	def run(self):
		data_frame_reader = self.spark_session.read

		data_frame = data_frame_reader.json(self._get_bucket_path())

		if self.latest:
			data_frame = self._get_latest(data_frame)
		data_frame = self._post_process(data_frame)

		data_frame.createOrReplaceTempView(self.table_name)

		if self.cache:
			self.spark_session.catalog.cacheTable(self.table_name)