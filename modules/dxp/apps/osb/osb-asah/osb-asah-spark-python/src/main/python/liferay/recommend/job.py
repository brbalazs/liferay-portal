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

from liferay.common.job import BaseBigQueryDataFrameReaderSparkJob, \
	BaseJSONDataFrameWriterSparkJob
from liferay.common.spark import BaseSparkJob
from liferay.ml.job import CollaborativeFilteringSparkJob

from pyspark.sql import Window, \
	functions as F

import logging

class AssetEntityBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(AssetEntityBigQueryDataFrameReaderSparkJob, self).__init__(
			spark_application,
			table_name='asset_entity'
		)

	def _get_sql_query(self):
		return f"""
			SELECT
				assetCategoryIds,
				className,
				classPK,
				id AS assetEntryId
			FROM
				`{self.spark_application_args.ac_project_id}`.assetentity 
			WHERE
				dataSourceId = {self.spark_application_args.data_source_id}			
		"""

class ContentInteractionRecommendationJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		configuration = spark_application.configuration

		super(
			ContentInteractionRecommendationJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			configuration.get('google.storage.bucket'),
			'com.liferay.analytics.dxp.entity.rest.dto.v1_0.'
			'AnalyticsContentInteractionRecommendation',
			'content_interaction_recommendation'
		)

	def _pre_process(self, data_frame):
		catalog = self.spark_session._jsparkSession.catalog()

		if not catalog.tableExists('asset_entity'):
			return data_frame

		asset_entity_data_frame = self.spark_session.table(
			'asset_entity'
		).selectExpr('assetEntryId as entryClassPK', 'assetCategoryIds')

		return data_frame.join(asset_entity_data_frame, on='entryClassPK')

class ContentInteractionRecommendationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			ContentInteractionRecommendationSparkJob,
			self
		).__init__(spark_application)

	def run(self):
		configuration = self.spark_application_configuration
		spark_context = self.spark_session.sparkContext

		# Item-Item Collaborative Filtering

		item_factors_data_frame = self.spark_session.table('item_factors')

		score_function = configuration.get(
			'content.interaction.recommendation.score.function'
		)

		window = Window.partitionBy(F.col('id')).orderBy(F.col('score').desc())

		recommendations_data_frame = item_factors_data_frame.crossJoin(
			item_factors_data_frame.selectExpr(
				'id as id2', 'features as features2'
			)
		)

		recommendations_data_frame = recommendations_data_frame.selectExpr(
			'id', 'toDenseVector(features) as features', 'id2',
			'toDenseVector(features2) as features2'
		)

		recommendations_data_frame = recommendations_data_frame.selectExpr(
			'*', score_function + '(features, features2) AS score'
		)

		recommendations_data_frame = recommendations_data_frame.select(
			'*',
			F.dense_rank().over(window).alias('rank')
		)

		max_rank = self.spark_application_configuration.get(
			'content.interaction.recommendation.max.rank'
		)

		recommendations_data_frame = recommendations_data_frame.filter(
			'rank > 1 AND rank <= {}'.format(max_rank)
		)

		recommendations_data_frame = recommendations_data_frame.withColumn(
			'createDate', F.current_date()
		)
		recommendations_data_frame = recommendations_data_frame.withColumn(
			'jobId', F.lit(spark_context.applicationId)
		)
		recommendations_data_frame = recommendations_data_frame.withColumn(
			'entryClassPK',
			F.col('id').cast('long')
		)
		recommendations_data_frame = recommendations_data_frame.withColumn(
			'recommendedEntryClassPK',
			F.col('id2').cast('long')
		)

		recommendations_data_frame = recommendations_data_frame.drop('features')
		recommendations_data_frame = recommendations_data_frame.drop(
			'features2'
		)
		recommendations_data_frame = recommendations_data_frame.drop('id')
		recommendations_data_frame = recommendations_data_frame.drop('id2')

		recommendations_data_frame.createOrReplaceTempView(
			"content_interaction_recommendation"
		)

		self.spark_session.catalog.cacheTable(
			'content_interaction_recommendation'
		)

class MostViewedContentRecommendationEventsBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(
			MostViewedContentRecommendationEventsBigQueryDataFrameReaderSparkJob,
			self
		).__init__(
			spark_application,
			table_name='most_viewed_content_recommendation_event'
		)

		configuration = self.spark_application_configuration

		self._event_ids = [
			'assetViewed', 'blogViewed', 'documentDownloaded',
			'documentPreviewed', 'webContentViewed'
		]

		self._window_days = configuration.get(
			'most.viewed.content.recommendation.window.days'
		)

	def _get_sql_query(self):
		end_date = self.spark_application_args.end_date

		start_date = self.spark_application_args.start_date

		time_zone = self.spark_application_args.time_zone

		if end_date and start_date:
			end_date_sql_string = f'"{end_date}"'
			start_date_sql_string = \
				f'DATE_SUB("{start_date}", INTERVAL {self._window_days} DAY)'
		else:
			end_date_sql_string = f'CURRENT_DATE("{time_zone}")'
			start_date_sql_string = \
				'DATE_SUB(CURRENT_DATE("{}"), INTERVAL {} DAY)'.format(
					time_zone,
					self._window_days
				)

		return f"""
			WITH EventProperty AS (
			SELECT
				id,
				SAFE_CAST(value AS INT) AS webContentResourcePk
			FROM
				`{self.spark_application_args.ac_project_id}`.eventproperty
			WHERE
				name = 'webContentResourcePk'
			)
			SELECT
				applicationId,
				CASE
					WHEN
						applicationId = 'WebContent' THEN webContentResourcePk
					ELSE
						SAFE_CAST(assetId AS INT)
				END AS entryClassPK,
				count(*) as score
			FROM 
				`{self.spark_application_args.ac_project_id}`.event
			LEFT JOIN
				EventProperty
			ON
				event.id = EventProperty.id
			WHERE
				assetId IS NOT NULL AND
				dataSourceId = {self.spark_application_args.data_source_id} AND
				event.eventId IN ({'"' + '","'.join(self._event_ids) + '"'}) AND
				DATE(event.eventDate, "{time_zone}") >= {start_date_sql_string} AND
				DATE(event.eventDate, "{time_zone}") <= {end_date_sql_string}
			GROUP BY
				applicationId, entryClassPK
			ORDER BY
				score DESC
		"""

	def _post_process(self, data_frame):
		lookup_data_frame = self.spark_session.createDataFrame(
			[
				('Blog', 'com.liferay.blogs.model.BlogsEntry'),
				('Document', 'com.liferay.document.library.kernel.model.DLFileEntry'),
				('WebContent', 'com.liferay.journal.model.JournalArticle')
			],
			['applicationId', 'className']
		)

		return data_frame.join(
			lookup_data_frame,
			on=['applicationId'],
			how='left'
		)

class MostViewedContentRecommendationJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		configuration = spark_application.configuration

		super(
			MostViewedContentRecommendationJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			configuration.get('google.storage.bucket'),
			'com.liferay.analytics.dxp.entity.rest.dto.v1_0.'
			'AnalyticsMostViewedContentRecommendation',
			'most_viewed_content_recommendation_event'
		)

	def _pre_process(self, data_frame):
		catalog = self.spark_session._jsparkSession.catalog()

		data_frame = data_frame.withColumn('createDate', F.current_date())
		data_frame = data_frame.withColumn(
			'jobId',
			F.lit(self.spark_application_configuration.get('spark.app.id'))
		)

		if not catalog.tableExists('asset_entity'):
			return data_frame

		asset_entity_data_frame = self.spark_session.table(
			'asset_entity'
		).selectExpr(
			'assetEntryId AS recommendedEntryClassPK',
			'classPK AS entryClassPK',
			'assetCategoryIds'
		)

		data_frame = data_frame.join(
			asset_entity_data_frame,
			on='entryClassPK',
			how='left'
		)

		data_frame = data_frame.filter(
			'recommendedEntryClassPK IS NOT NULL'
		)

		return data_frame

class UserContentRecommendationCollaborativeFilteringSparkJob(CollaborativeFilteringSparkJob):

	def __init__(self, spark_application):
		configuration = spark_application.configuration

		als_checkpoint_interval = configuration.get(
			'user.content.recommendation.als.checkpoint.interval'
		)

		cross_validator_num_folds = configuration.get(
			'user.content.recommendation.tuning.cross.validator.'
			'num.folds', 3
		)

		cross_validator_parallelism = configuration.get(
			'user.content.recommendation.tuning.cross.validator.'
			'parallelism', 2
		)
		product_interaction_recommendation_enable = configuration.get(
			'content.interaction.recommendation.enable'
		)

		train_split_ratio = configuration.get(
			'user.content.recommendation.train.split.ratio'
		)

		tuning_alpha = configuration.get_list(
			'user.content.recommendation.tuning.alpha'
		)
		tuning_max_iterations = configuration.get_list(
			'user.content.recommendation.tuning.max.iteration'
		)
		tuning_rank = configuration.get_list(
			'user.content.recommendation.tuning.rank'
		)
		tuning_regularization_parameter = configuration.get_list(
			'user.content.recommendation.tuning.regularization.parameter'
		)

		super(
			UserContentRecommendationCollaborativeFilteringSparkJob,
			self
		).__init__(
			spark_application,
			item_column='assetEntryId',
			user_column='dxpUserId',
			als_checkpoint_interval=als_checkpoint_interval,
			create_item_factors_table=product_interaction_recommendation_enable,
			cross_validator_num_folds=cross_validator_num_folds,
			cross_validator_parallelism=cross_validator_parallelism,
			input_table='user_item_rating',
			recommendation_table='user_content_recommendation',
			train_split_ratio=train_split_ratio,
			tuning_alpha=tuning_alpha,
			tuning_max_iterations=tuning_max_iterations,
			tuning_rank=tuning_rank,
			tuning_reg_parameter=tuning_regularization_parameter
		)

		self._default_count_approx_timeout = 5000

		self._log = logging.getLogger(self.__class__.__name__)

	def augment_recommendations(self, recommendations_data_frame):
		catalog = self.spark_session._jsparkSession.catalog()

		if not catalog.tableExists('asset_entity'):
			return recommendations_data_frame.selectExpr(
				self._user_column,
				'explode(recommendations) as recommendations',
			).selectExpr(
				self._user_column,
				'recommendations.assetEntryId as assetEntryId',
				'recommendations.rating as score'
			)

		asset_entity_data_frame = self.spark_session.table(
			'asset_entity'
		).select('assetEntryId', 'assetCategoryIds')

		return recommendations_data_frame.selectExpr(
			self._user_column,
			'explode(recommendations) as recommendations',
		).selectExpr(
			self._user_column,
			'recommendations.assetEntryId as assetEntryId',
			'recommendations.rating as score'
		).join(asset_entity_data_frame, on='assetEntryId')

	def get_user_recommendation_count(self):
		catalog = self.spark_session._jsparkSession.catalog()

		if catalog.tableExists('asset_entity'):
			asset_data_frame = self.spark_session.table(
				'asset_entity'
			).select('assetEntryId')
		else:
			asset_data_frame = self.spark_session.table(
				self._input_table
			).select('assetId')

		catalog_coverage = float(
			self.spark_application_configuration.get(
				'user.content.recommendation.content.coverage'
			)
		)

		asset_data_frame = asset_data_frame.distinct()

		catalog_count = asset_data_frame.rdd.countApprox(
			self._default_count_approx_timeout
		)

		return int(catalog_count * catalog_coverage)

class UserContentRecommendationDataPreparationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(UserContentRecommendationDataPreparationSparkJob, self).__init__(
			spark_application
		)

	def run(self):
		analytics_event_data_frame = self.spark_session.table('analytics_event')

		asset_entity_data_frame = self.spark_session.table('asset_entity')

		user_item_rating_data_frame = analytics_event_data_frame.join(
			asset_entity_data_frame,
			on=['className', 'classPK'],
			how='left'
		).select(
			'assetEntryId',
			'dxpUserId',
			'rating'
		).filter(
			'assetEntryId IS NOT NULL'
		)

		user_item_rating_data_frame.createOrReplaceTempView('user_item_rating')

class UserContentRecommendationEventsBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(
			UserContentRecommendationEventsBigQueryDataFrameReaderSparkJob,
			self
		).__init__(
			spark_application,
			table_name='analytics_event'
		)

		self._event_ids = [
			'assetViewed', 'blogViewed', 'documentDownloaded',
			'documentPreviewed', 'webContentViewed'
		]

		self._initial_run_day_range = 7

		self._max_days_delta = 30

	def _get_sql_query(self):
		end_date = self.spark_application_args.end_date

		start_date = self.spark_application_args.start_date

		time_zone = self.spark_application_args.time_zone

		if end_date and start_date:
			end_date_sql_string = f'"{end_date}"'
			start_date_sql_string = \
				f'DATE_SUB("{start_date}", INTERVAL {self._max_days_delta} DAY)'
		else:
			end_date_sql_string = f'CURRENT_DATE("{time_zone}")'
			start_date_sql_string = \
				'DATE_SUB(CURRENT_DATE("{}"), INTERVAL {} DAY)'.format(
					time_zone,
					self._max_days_delta + self._initial_run_day_range
				)

		return f"""
			WITH EventProperty AS (
			SELECT
				id,
				SAFE_CAST(value AS INT) AS webContentResourcePk
			FROM
				`{self.spark_application_args.ac_project_id}`.eventproperty
			WHERE
				name = 'webContentResourcePk'
			),
			Individual AS (
				SELECT DISTINCT
				identity.id as userId,
				(
					SELECT
						SAFE_CAST(value AS INT)
					FROM
						UNNEST(individual.fields)
					WHERE
						name = 'userId'
				) AS dxpUserId
				FROM
					`{self.spark_application_args.ac_project_id}`.identity
				JOIN
					`{self.spark_application_args.ac_project_id}`.individual
				ON
					identity.individualId = individual.id
			)
			SELECT
				applicationId,
				CASE
					WHEN
						applicationId = 'WebContent' THEN webContentResourcePk
					ELSE
						SAFE_CAST(assetId AS INT)
				END AS classPK,
				dxpUserId,
				eventdate,
				1 as rating
			FROM
				`{self.spark_application_args.ac_project_id}`.event
			LEFT JOIN
				EventProperty
			ON
				event.id = EventProperty.id
			LEFT JOIN
				Individual
			ON
				event.userId = Individual.userId
			WHERE
				assetId IS NOT NULL AND
				dxpUserId IS NOT NULL AND
				dataSourceId = {self.spark_application_args.data_source_id} AND
				event.eventId IN ({'"' + '","'.join(self._event_ids) + '"'}) AND
				DATE(event.eventDate, "{time_zone}") >= {start_date_sql_string} AND
				DATE(event.eventDate, "{time_zone}") <= {end_date_sql_string}
		"""

	def _post_process(self, data_frame):
		lookup_data_frame = self.spark_session.createDataFrame(
			[
				('Blog', 'com.liferay.blogs.model.BlogsEntry'),
				('Document', 'com.liferay.document.library.kernel.model.DLFileEntry'),
				('WebContent', 'com.liferay.journal.model.JournalArticle')
			],
			['applicationId', 'className']
		)

		return data_frame.join(
			lookup_data_frame,
			on=['applicationId'],
			how='left'
		)


class UserContentRecommendationJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		configuration = spark_application.configuration

		super(
			UserContentRecommendationJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			configuration.get('google.storage.bucket'),
			'com.liferay.analytics.dxp.entity.rest.dto.v1_0.'
			'AnalyticsUserContentRecommendation', 'user_content_recommendation'
		)

	def _pre_process(self, data_frame):
		data_frame = data_frame.withColumn('createDate', F.current_date())
		data_frame = data_frame.withColumn(
			'jobId',
			F.lit(self.spark_application_configuration.get('spark.app.id'))
		)
		data_frame = data_frame.withColumn(
			'entryClassPK',
			F.col('dxpUserId').cast('long')
		)
		data_frame = data_frame.withColumn(
			'recommendedEntryClassPK',
			F.col('assetEntryId').cast('long')
		)

		data_frame = data_frame.drop('dxpUserId')
		data_frame = data_frame.drop('assetEntryId')

		return data_frame