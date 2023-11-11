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

from liferay.common.configuration import Configuration

from pyspark import SparkConf
from pyspark.sql import SparkSession

import logging

class BaseSparkApplication(object):

	__metaclass__ = ABCMeta

	def __init__(self):
		self.log = self._initialize_logging()

		argument_parser = self._create_argument_parser()

		self.args = argument_parser.parse_args()

		self.configuration = Configuration(self.args.configuration)
		self.spark_session = self._build_spark_session()

	def _build_spark_session(self):
		spark_session_builder = SparkSession.Builder()

		spark_session_builder.config(conf=self._create_spark_conf())

		return spark_session_builder.getOrCreate()

	@abstractmethod
	def _create_argument_parser(self):
		return

	def _create_spark_conf(self):
		return SparkConf()

	def _initialize_logging(self):
		logging.basicConfig(
			format='%(asctime)s %(name)-12s %(levelname)-8s %(message)s',
			level=logging.INFO
		)

		return logging.getLogger(self.__class__.__name__)

	@abstractmethod
	def start(self):
		pass

class BaseSparkJob(object, metaclass=ABCMeta):

	def __init__(self, spark_application):
		self.spark_application = spark_application
		self.spark_application_args = spark_application.args
		self.spark_application_configuration = spark_application.configuration
		self.spark_session = spark_application.spark_session

	@abstractmethod
	def run(self):
		pass

class SparkJobPipeline:

	def __init__(self, jobs):
		self.jobs = jobs

	def run(self):
		for job in self.jobs:
			job.run()