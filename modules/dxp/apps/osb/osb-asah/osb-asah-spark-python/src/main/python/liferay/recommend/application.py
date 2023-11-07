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

from liferay.common.spark import \
	BaseSparkApplication, \
	SparkJobPipeline
from liferay.ml.udf import TanimotoCoefficientUDFFunction, \
	ToDenseVectorUDFFunction
from liferay.recommend.job import \
	AssetEntityBigQueryDataFrameReaderSparkJob, \
	ContentInteractionRecommendationSparkJob, \
	ContentInteractionRecommendationJSONDataFrameWriterSparkJob, \
	MostViewedContentRecommendationEventsBigQueryDataFrameReaderSparkJob, \
	MostViewedContentRecommendationJSONDataFrameWriterSparkJob, \
	UserContentRecommendationCollaborativeFilteringSparkJob, \
	UserContentRecommendationDataPreparationSparkJob, \
	UserContentRecommendationEventsBigQueryDataFrameReaderSparkJob, \
	UserContentRecommendationJSONDataFrameWriterSparkJob

from pyspark import SparkConf

import argparse
import sys

class MostViewedContentRecommendationApplication(BaseSparkApplication):

	def __init__(self):
		super(MostViewedContentRecommendationApplication, self).__init__()

	def _create_argument_parser(self):
		argument_parser = argparse.ArgumentParser(
			usage='{} liferay.recommend.<ApplicationName> '
				  '--ac-project-id <AC Project ID> '
				  '--configuration <Configuration Path> '
				  '--end-date <End Date> '
				  '--data-source-id <Data Source ID>'
				  '--start-date <Start Date> '
				  '--time-zone <Time Zone> '.format(sys.argv[0])
		)

		argument_parser.add_argument('application')
		argument_parser.add_argument('--ac-project-id', required=True)
		argument_parser.add_argument('--configuration', required=True)
		argument_parser.add_argument('--end-date', required=False)
		argument_parser.add_argument('--data-source-id', required=True)
		argument_parser.add_argument('--start-date', required=False)

		argument_parser.add_argument(
			'--time-zone',
			default='UTC',
			required=False
		)

		return argument_parser

	def _create_spark_conf(self):
		spark_conf = SparkConf()

		spark_conf.set('materializationDataset', self.args.ac_project_id)
		spark_conf.set(
			'spark.jars.packages',
			self.configuration.get('spark.jars.packages')
		)
		spark_conf.set(
			'temporaryGcsBucket',
			self.configuration.get('google.storage.path.temporaryGcsBucket')
		)
		spark_conf.set('viewsEnabled', 'true')

		return spark_conf

	def _create_spark_job_pipeline(self):
		jobs = list()

		jobs.append(
			MostViewedContentRecommendationEventsBigQueryDataFrameReaderSparkJob(
				self
			)
		)

		jobs.append(AssetEntityBigQueryDataFrameReaderSparkJob(self))

		jobs.append(
			MostViewedContentRecommendationJSONDataFrameWriterSparkJob(self)
		)

		return SparkJobPipeline(jobs)

	def start(self):
		spark_job_pipeline = self._create_spark_job_pipeline()

		try:
			spark_job_pipeline.run()
		except Exception as e:
			self.log.exception(e)

			raise e

class UserContentRecommendationApplication(BaseSparkApplication):

	def __init__(self):
		super(UserContentRecommendationApplication, self).__init__()

		TanimotoCoefficientUDFFunction(self.spark_session)
		ToDenseVectorUDFFunction(self.spark_session)

	def _create_argument_parser(self):
		argument_parser = argparse.ArgumentParser(
			usage='{} liferay.recommend.<ApplicationName> '
			'--ac-project-id <AC Project ID> '
			'--configuration <Configuration Path> '
			'--end-date <End Date> '
			'--data-source-id <Data Source ID>'
			'--start-date <Start Date> '
			'--time-zone <Time Zone> '.format(sys.argv[0])
		)

		argument_parser.add_argument('application')
		argument_parser.add_argument('--ac-project-id', required=True)
		argument_parser.add_argument('--configuration', required=True)
		argument_parser.add_argument('--end-date', required=False)
		argument_parser.add_argument('--data-source-id', required=True)
		argument_parser.add_argument('--start-date', required=False)

		argument_parser.add_argument(
			'--time-zone',
			default='UTC',
			required=False
		)

		return argument_parser

	def _create_spark_conf(self):
		spark_conf = SparkConf()

		spark_conf.set('materializationDataset', self.args.ac_project_id)
		spark_conf.set(
			'spark.jars.packages',
			self.configuration.get('spark.jars.packages')
		)
		spark_conf.set(
			'temporaryGcsBucket',
			self.configuration.get('google.storage.path.temporaryGcsBucket')
		)
		spark_conf.set('viewsEnabled', 'true')

		return spark_conf

	def _create_spark_job_pipeline(self):
		jobs = list()

		jobs.append(
			UserContentRecommendationEventsBigQueryDataFrameReaderSparkJob(self)
		)

		jobs.append(AssetEntityBigQueryDataFrameReaderSparkJob(self))

		jobs.append(UserContentRecommendationDataPreparationSparkJob(self))

		jobs.append(
			UserContentRecommendationCollaborativeFilteringSparkJob(self)
		)

		jobs.append(UserContentRecommendationJSONDataFrameWriterSparkJob(self))

		content_interaction_recommendation_enable = self.configuration.get(
			'content.interaction.recommendation.enable'
		)

		if content_interaction_recommendation_enable:
			jobs.append(ContentInteractionRecommendationSparkJob(self))

			jobs.append(
				ContentInteractionRecommendationJSONDataFrameWriterSparkJob(
					self
				)
			)

		return SparkJobPipeline(jobs)
	
	def start(self):
		spark_job_pipeline = self._create_spark_job_pipeline()

		try:
			spark_job_pipeline.run()
		except Exception as e:
			self.log.exception(e)

			raise e