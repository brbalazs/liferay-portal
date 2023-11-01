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

from liferay.commerce.recommend.job import \
	ContextUserInteractionRecommendationJSONDataFrameWriterSparkJob, \
	CutLineageSparkJob, \
	FrequentPatternDataPreparationSparkJob, \
	FrequentPatternOrderBigQueryDataFrameReaderSparkJob, \
	FrequentPatternOrderJSONDataFrameReaderSparkJob, \
	FrequentPatternPostProcessRecommendationSparkJob, \
	FrequentPatternProductBigQueryDataFrameReaderSparkJob, \
	FrequentPatternProductJSONDataFrameReaderSparkJob, \
	FrequentPatternRecommendationJSONDataFrameWriterSparkJob, \
	FrequentPatternRecommendationSparkJob, \
	OrderInteractionBigQueryDataFrameReaderSparkJob, \
	OrderInteractionJSONDataFrameReaderSparkJob, \
	ProductContentJSONDataFrameReaderSparkJob, \
	ProductContentPipelineSparkJob, \
	ProductContentBigQueryDataFrameReaderSparkJob, \
	ProductContentRecommendationJSONDataFrameWriter, \
	ProductContentRecommendationPreparationSparkJob, \
	ProductContentRecommendationSparkJob, \
	ProductInteractionBigQueryDataFrameReaderSparkJob, \
	ProductInteractionJSONDataFrameReaderSparkJob, \
	ProductInteractionRecommendationJSONDataFrameWriterSparkJob, \
	ProductInteractionRecommendationSparkJob, \
	UserInteractionCollaborativeFilteringSparkJob, \
	UserInteractionDataPreparationSparkJob
from liferay.commerce.common import BaseCommerceSparkApplication
from liferay.common.spark import SparkJobPipeline
from liferay.ml.udf import TanimotoCoefficientUDFFunction, \
	ToDenseVectorUDFFunction, \
	VectorDotProductUDFFunction, \
	VectorMergeUDFFunction, \
	VectorNormUDFFunction

class FrequentPatternRecommendationApplication(BaseCommerceSparkApplication):

	def __init__(self):
		super(FrequentPatternRecommendationApplication, self).__init__()

	def _create_spark_job_pipeline(self):
		jobs = list()

		commerce_data_source = self.configuration.get('commerce.data.source')

		if commerce_data_source == 'bigquery':
			self.log.info("Loading data from: BigQuery")

			jobs.append(
				FrequentPatternOrderBigQueryDataFrameReaderSparkJob(self)
			)

			jobs.append(
				FrequentPatternProductBigQueryDataFrameReaderSparkJob(self)
			)
		else:
			self.log.info("Loading data from: GCS")

			jobs.append(FrequentPatternOrderJSONDataFrameReaderSparkJob(self))

			jobs.append(FrequentPatternProductJSONDataFrameReaderSparkJob(self))

		jobs.append(FrequentPatternDataPreparationSparkJob(self))

		jobs.append(FrequentPatternRecommendationSparkJob(self))

		jobs.append(FrequentPatternPostProcessRecommendationSparkJob(self))

		jobs.append(
			FrequentPatternRecommendationJSONDataFrameWriterSparkJob(self)
		)

		return SparkJobPipeline(jobs)

class ProductContentRecommendationApplication(BaseCommerceSparkApplication):

	def __init__(self):
		super(ProductContentRecommendationApplication, self).__init__()

		VectorDotProductUDFFunction(self.spark_session)

		VectorMergeUDFFunction(self.spark_session)

		VectorNormUDFFunction(self.spark_session)

	def _create_spark_job_pipeline(self):
		jobs = list()

		commerce_data_source = self.configuration.get('commerce.data.source')

		if commerce_data_source == 'bigquery':
			self.log.info("Loading data from: BigQuery")

			jobs.append(ProductContentBigQueryDataFrameReaderSparkJob(self))
		else:
			self.log.info("Loading data from: GCS")

			jobs.append(ProductContentJSONDataFrameReaderSparkJob(self))

		jobs.append(ProductContentPipelineSparkJob(self))

		jobs.append(CutLineageSparkJob(self, 'pipeline_data'))

		jobs.append(ProductContentRecommendationPreparationSparkJob(self))

		jobs.append(ProductContentRecommendationSparkJob(self))

		jobs.append(ProductContentRecommendationJSONDataFrameWriter(self))

		return SparkJobPipeline(jobs)

class UserInteractionRecommendationApplication(BaseCommerceSparkApplication):

	def __init__(self):
		super(UserInteractionRecommendationApplication, self).__init__()

		TanimotoCoefficientUDFFunction(self.spark_session)
		ToDenseVectorUDFFunction(self.spark_session)

	def _create_spark_job_pipeline(self):
		jobs = list()

		commerce_data_source = self.configuration.get('commerce.data.source')

		if commerce_data_source == 'bigquery':
			self.log.info("Loading data from: BigQuery")

			jobs.append(ProductInteractionBigQueryDataFrameReaderSparkJob(self))

			jobs.append(OrderInteractionBigQueryDataFrameReaderSparkJob(self))
		else:
			self.log.info("Loading data from: GCS")

			jobs.append(ProductInteractionJSONDataFrameReaderSparkJob(self))

			jobs.append(OrderInteractionJSONDataFrameReaderSparkJob(self))

		jobs.append(UserInteractionDataPreparationSparkJob(self))

		jobs.append(UserInteractionCollaborativeFilteringSparkJob(self))

		jobs.append(
			ContextUserInteractionRecommendationJSONDataFrameWriterSparkJob(
				self
			)
		)

		product_interaction_recommendation_enable = self.configuration.get(
			'product.interaction.recommendation.enable'
		)

		if product_interaction_recommendation_enable:
			jobs.append(ProductInteractionRecommendationSparkJob(self))

			jobs.append(
				ProductInteractionRecommendationJSONDataFrameWriterSparkJob(
					self
				)
			)

		return SparkJobPipeline(jobs)