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

from liferay.commerce.common import BaseBigQueryDataFrameReaderSparkJob, \
	BaseJSONDataFrameReaderSparkJob, \
	BaseJSONDataFrameWriterSparkJob
from liferay.commerce.recommend.feature import CommerceFeatureExtractor
from liferay.common.spark import BaseSparkJob

from pyspark import StorageLevel
from pyspark.ml import Pipeline
from pyspark.ml.fpm import FPGrowth
from pyspark.sql import Window, \
	functions as F
from pyspark.sql.utils import AnalysisException

import logging

from liferay.ml.job import CollaborativeFilteringSparkJob

class ContextUserInteractionRecommendationJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		super(
			ContextUserInteractionRecommendationJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.'
			'UserRecommendation', 'context_user_interaction_recommendation'
		)

	def _pre_process(self, data_frame):
		data_frame = data_frame.withColumn('createDate', F.current_date())
		data_frame = data_frame.withColumn(
			'jobId',
			F.lit(self.spark_application_configuration.get('spark.app.id'))
		)
		data_frame = data_frame.withColumn(
			'entryClassPK',
			F.col('commerceAccountId').cast('long')
		)
		data_frame = data_frame.withColumn(
			'recommendedEntryClassPK',
			F.col('CPDefinitionId').cast('long')
		)

		data_frame = data_frame.drop('commerceAccountId')
		data_frame = data_frame.drop('CPDefinitionId')

		return data_frame

class CutLineageSparkJob(BaseSparkJob):

	def __init__(self, spark_application, table_name):
		super().__init__(spark_application)

		self._table_name = table_name

	def run(self):
		data_frame = self.spark_session.table(self._table_name)

		data_frame = self.spark_session.createDataFrame(
			data_frame.rdd,
			data_frame.schema
		)

		data_frame.createOrReplaceTempView(self._table_name)

		self.spark_session.catalog.cacheTable(self._table_name)

class FrequentPatternDataPreparationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			FrequentPatternDataPreparationSparkJob,
			self
		).__init__(spark_application)

		self._log = logging.getLogger(self.__class__.__name__)

	def run(self):
		order_data_frame = self.spark_session.table('order')

		product_data_frame = self.spark_session.table('product')

		items_data_frame = order_data_frame.join(product_data_frame, on='sku')

		items_grouped_data = items_data_frame.groupBy('orderId')

		items_data_frame = items_grouped_data.agg(
			F.collect_set('CPDefinitionId').alias('items')
		)

		items_data_frame.createOrReplaceTempView('items')

		self.spark_session.catalog.cacheTable('items')

class FrequentPatternOrderBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(FrequentPatternOrderBigQueryDataFrameReaderSparkJob, self).__init__(
			spark_application,
			'order'
		)

	def _get_sql_query(self):
		configuration = self.spark_application_configuration

		return f"""
			SELECT
				order_.id AS orderId,
				orderItems.sku,
			FROM
				`{self.spark_application_args.ac_project_id}`.order AS order_,
				UNNEST(orderItems) AS orderItems
			WHERE
				datasourceId = {self.spark_application_args.data_source_id}
		"""

class FrequentPatternOrderJSONDataFrameReaderSparkJob(BaseJSONDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(
			FrequentPatternOrderJSONDataFrameReaderSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.Order', 'order'
		)

	def _post_process(self, data_frame):
		return data_frame.selectExpr(
			'id as orderId', 'EXPLODE(orderItems.sku) as sku'
		)

class FrequentPatternPostProcessRecommendationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			FrequentPatternPostProcessRecommendationSparkJob,
			self
		).__init__(spark_application)

	def run(self):
		association_rules_data_frame = self.spark_session.table(
			'association_rules'
		)

		frequent_itemsets_data_frame = self.spark_session.table(
			'frequent_itemsets'
		)

		order_data_frame = self.spark_session.table('order')

		order_count = order_data_frame.count()

		association_rules_data_frame = association_rules_data_frame.filter(
			'lift > 1.0'
		)

		association_rules_data_frame = association_rules_data_frame.join(
			frequent_itemsets_data_frame.withColumnRenamed(
				'items', 'antecedent'
			),
			on='antecedent'
		)

		association_rules_data_frame = association_rules_data_frame.withColumnRenamed(
			'freq', 'antecedent_freq'
		)

		association_rules_data_frame = association_rules_data_frame.withColumn(
			'antecedent_support',
			F.expr('antecedent_freq / {}'.format(order_count))
		)

		association_rules_data_frame = association_rules_data_frame.withColumn(
			'consequent_support', F.expr('confidence/lift')
		)

		association_rules_data_frame = association_rules_data_frame.withColumn(
			'lambda',
			F.expr(
				'GREATEST(antecedent_support + consequent_support - 1, '
				'1 / {})'.format(order_count)
			)
		)

		association_rules_data_frame = association_rules_data_frame.withColumn(
			'delta',
			F.expr('1 / GREATEST(antecedent_support, consequent_support)')
		)

		association_rules_data_frame = association_rules_data_frame.withColumn(
			'stdLift', F.expr('(lift - lambda)/(delta - lambda)')
		)

		association_rules_data_frame = association_rules_data_frame.select(
			'antecedent',
			F.explode('consequent').alias('consequent'), 'confidence', 'lift',
			'stdLift'
		)

		window_spec = Window.partitionBy('antecedent')

		window_spec = window_spec.orderBy(
			F.col('stdLift').desc(),
			F.col('lift').desc()
		)

		association_rules_data_frame = association_rules_data_frame.withColumn(
			'row_num',
			F.row_number().over(window_spec)
		)

		itemset_items_max_count = self.spark_application_configuration.get(
			'frequent.pattern.recommendation.itemset.items.max.count'
		)

		association_rules_data_frame = association_rules_data_frame.filter(
			'row_num <= {}'.format(itemset_items_max_count)
		)

		frequent_pattern_recommendation_data_frame = association_rules_data_frame.drop(
			'row_num'
		)

		frequent_pattern_recommendation_data_frame.createOrReplaceTempView(
			'frequent_pattern_recommendation'
		)

		self.spark_session.catalog.cacheTable('frequent_pattern_recommendation')

class FrequentPatternProductBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(FrequentPatternProductBigQueryDataFrameReaderSparkJob, self).__init__(
			spark_application,
			'product'
		)

	def _get_sql_query(self):
		configuration = self.spark_application_configuration

		return f"""
			SELECT
				product.id AS CPDefinitionId,
				skus.sku,
			FROM
				`{self.spark_application_args.ac_project_id}`.product AS product,
				UNNEST(skus) AS skus
			WHERE
				datasourceId = {self.spark_application_args.data_source_id}
		"""

class FrequentPatternProductJSONDataFrameReaderSparkJob(BaseJSONDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(
			FrequentPatternProductJSONDataFrameReaderSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.Product',
			'product'
		)

	def _post_process(self, data_frame):
		return data_frame.selectExpr(
			'id AS CPDefinitionId', 'EXPLODE(skus.sku) AS sku'
		)

class FrequentPatternRecommendationJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		super(
			FrequentPatternRecommendationJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.'
			'FrequentPatternRecommendation', 'frequent_pattern_recommendation'
		)

	def _pre_process(self, data_frame):
		data_frame = data_frame.withColumnRenamed('antecedent', 'antecedentIds')
		data_frame = data_frame.withColumn(
			'antecedentIdsLength', F.size('antecedentIds')
		)
		data_frame = data_frame.drop('confidence')
		data_frame = data_frame.drop('lift')
		data_frame = data_frame.withColumnRenamed(
			'consequent', 'recommendedEntryClassPK'
		)
		data_frame = data_frame.withColumnRenamed('stdLift', 'score')

		return data_frame

class FrequentPatternRecommendationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			FrequentPatternRecommendationSparkJob,
			self
		).__init__(spark_application)

		self._log = logging.getLogger(self.__class__.__name__)

	def run(self):
		order_data_frame = self.spark_session.table('order')

		distinct_items_data_frame = order_data_frame.select('sku')

		distinct_items_data_frame = distinct_items_data_frame.distinct()

		distinct_items_count = distinct_items_data_frame.count()

		ordered_items_count = order_data_frame.count()

		self._log.info(
			"There {} distinct items over {} ordered items".format(
				distinct_items_count, ordered_items_count
			)
		)

		min_support_factor = self.spark_application_configuration.get(
			'frequent.pattern.recommendation.min.support.factor'
		)

		min_support = (distinct_items_count / ordered_items_count)
		min_support = min_support * min_support_factor

		min_confidence = self.spark_application_configuration.get(
			'frequent.pattern.recommendation.min.confidence'
		)

		fp_growth = FPGrowth(
			minSupport=min_support, minConfidence=min_confidence
		)

		items_data_frame = self.spark_session.table('items')

		fp_growth_model = fp_growth.fit(items_data_frame)

		association_rules = fp_growth_model.associationRules

		association_rules.createOrReplaceTempView('association_rules')

		self.spark_session.catalog.cacheTable('association_rules')

		frequent_itemsets = fp_growth_model.freqItemsets

		frequent_itemsets.createOrReplaceTempView('frequent_itemsets')

		self.spark_session.catalog.cacheTable('frequent_itemsets')

class OrderInteractionBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(OrderInteractionBigQueryDataFrameReaderSparkJob, self).__init__(
			spark_application,
			'order_interactions'
		)

	def _get_sql_query(self):
		configuration = self.spark_application_configuration

		user_interaction_order_history_length = configuration.get(
			'user.interaction.order.history.length'
		)

		return f"""
			WITH temp_order AS
			(
				SELECT
					accountId AS commerceAccountId,
					createDate,
					orderItems,
					ROW_NUMBER() OVER(
						PARTITION BY
							accountId
						ORDER BY
							createDate DESC, id DESC
					) AS row_num
				FROM
					`{self.spark_application_args.ac_project_id}`.order
				WHERE
					datasourceId = {self.spark_application_args.data_source_id}
			)
			SELECT
				temp_order.commerceAccountId,
				temp_order.createDate,
				orderItems.sku,
			FROM
				temp_order, UNNEST(temp_order.orderItems) as orderItems
			WHERE
				row_num <= {user_interaction_order_history_length}
		"""

class OrderInteractionJSONDataFrameReaderSparkJob(BaseJSONDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(
			OrderInteractionJSONDataFrameReaderSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.Order',
			'order_interactions'
		)

	def _post_process(self, data_frame):
		configuration = self.spark_application_configuration

		return data_frame.selectExpr(
			'accountId AS commerceAccountId', 'createDate',
			'orderItems.sku AS sku_list',
			'ROW_NUMBER() OVER('
			'	PARTITION BY '
			'		accountId '
			'	ORDER BY '
			'		createDate DESC, id DESC'
			') AS rank'
		).where(
			'rank <= {}'.format(
					configuration.get('user.interaction.order.history.length')
			)
		).selectExpr(
			'*',
			'EXPLODE(sku_list) AS sku'
		).drop(
			'sku_list', 'rank'
		)

class ProductContentBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(ProductContentBigQueryDataFrameReaderSparkJob, self).__init__(
			spark_application,
			'products'
		)

	def _get_specification_keys(self):
		configuration = self.spark_application_configuration

		data_frame_reader = self.spark_session.read

		data_frame = data_frame_reader.format(
			"bigquery"
		).load(
			f"""
			SELECT
				DISTINCT(ps.specificationKey) AS specificationKey
			FROM
				`{self.spark_application_args.ac_project_id}`.product,
				UNNEST(productSpecifications) AS ps
			WHERE
				dataSourceId = {self.spark_application_args.data_source_id}
			"""
		)

		data_frame = data_frame.dropna()

		return data_frame.rdd.map(lambda r: r[0]).collect()

	def _get_sql_query(self):
		configuration = self.spark_application_configuration

		locale = configuration.get("commerce.ml.locale")

		query_parts = [
			f"""
			SELECT
				id AS entryClassPK, categoryIds AS assetCategoryIds,
				SAFE_CAST(JSON_VALUE(description, '$.{locale}') AS STRING) AS description,
				SAFE_CAST(JSON_VALUE(metaDescription, '$.{locale}') AS STRING) AS metaDescription,
				SAFE_CAST(JSON_VALUE(metaTitle, '$.{locale}') AS STRING) AS metaTitle,
				SAFE_CAST(JSON_VALUE(name, '$.{locale}') AS STRING) AS name,
				productType AS productTypeName,
			"""
		]

		for specificationKey in self._get_specification_keys():
			query_parts += [
				f"""
				(
					SELECT
						STRING_AGG(
							SAFE_CAST(
								JSON_VALUE(value, '$.{locale}') AS STRING
							)
						)
					FROM
						UNNEST(productSpecifications)
					WHERE
						specificationKey = '{specificationKey}'
				) AS SPECIFICATION_{specificationKey.replace('-', '_')},
				"""
			]

		query_parts += [
			f"""
			FROM
				`{self.spark_application_args.ac_project_id}`.product
			WHERE
				datasourceId = {self.spark_application_args.data_source_id}
			"""
		]

		return ''.join(query_parts)

	def _post_process(self, data_frame):
		return data_frame.withColumn(
			'assetCategoryIds',
			F.col('assetCategoryIds').cast('array<string>')
		)

class ProductContentJSONDataFrameReaderSparkJob(BaseJSONDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(ProductContentJSONDataFrameReaderSparkJob, self).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.Product',
			'products'
		)

	def _get_product_specification_keys(self, products_data_frame):
		data_frame = products_data_frame.select(
			F.explode('productSpecifications').alias('productSpecifications')
		)

		data_frame = data_frame.select('productSpecifications.specificationKey')

		data_frame = data_frame.distinct()

		return data_frame.rdd.map(lambda r: r[0]).collect()

	def _get_product_specifications_data_frame(self, products_data_frame):
		product_specification_keys = self._get_product_specification_keys(
			products_data_frame
		)

		product_specifications_data_frame = products_data_frame.select(
			"id",
			F.explode("productSpecifications").alias("productSpecifications")
		)

		locale = self.spark_application_configuration.get("commerce.ml.locale")

		specification_data_frames = []

		for product_specification_key in product_specification_keys:
			specification_column_name = "SPECIFICATION_" + str(
				product_specification_key
			)

			data_frame = product_specifications_data_frame.withColumn(
				specification_column_name,
				F.when(
					product_specifications_data_frame[
						'productSpecifications.specificationKey'] ==
					product_specification_key,
					F.col('productSpecifications.value').getItem(locale)
				)
			)

			data_frame = data_frame.dropna(subset=specification_column_name)
			data_frame = data_frame.drop('productSpecifications')

			specification_data_frames += [data_frame]

		product_specification_data_frame = products_data_frame.select(
			"id"
		).distinct()

		for specification_data_frame in specification_data_frames:
			product_specification_data_frame = product_specification_data_frame.join(
				specification_data_frame,
				how='left_outer',
				on=['id']
			)

		return product_specification_data_frame

	def _has_column(self, data_frame, column_name):
		try:
			data_frame[column_name]

			return True
		except AnalysisException:
			return False

	def _post_process(self, product_data_frame):
		locale = self.spark_application_configuration.get("commerce.ml.locale")

		products_data_frame = product_data_frame.selectExpr(
			"CAST(categoryIds AS array<string>) assetCategoryIds",
			"description.{} AS description".format(locale),
			"metaDescription.{} AS metaDescription".format(locale),
			"metaTitle.{} AS metaTitle".format(locale),
			"name.{} AS name".format(locale), "productType AS productTypeName",
			"id"
		)

		if self._has_column(
			product_data_frame, 'productSpecifications.specificationKey'
		):
			return products_data_frame.join(
				self._get_product_specifications_data_frame(product_data_frame),
				how='left_outer',
				on=["id"]
			).withColumnRenamed("id", 'entryClassPK')
		else:
			return products_data_frame.withColumnRenamed("id", 'entryClassPK')

class ProductContentPipelineSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(ProductContentPipelineSparkJob, self).__init__(spark_application)

		self._commerce_exclude_cp_columns = [
			'entryClassPK', 'metaKeywords', 'specificationOptionsNames',
			'specificationOptionsIds'
		]
		self._commerce_feature_extractor = CommerceFeatureExtractor()

	def run(self):
		products_data_frame = self.spark_session.table('products')

		feature_column_names = [
			column for column in products_data_frame.columns
			if column not in self._commerce_exclude_cp_columns
		]

		products_data_frame = products_data_frame.withColumn(
			'description', F.coalesce(F.col('description'), F.col('name'))
		).withColumn(
			'description', F.regexp_replace('description', r'[^\w\s]', '')
		).persist(StorageLevel.MEMORY_AND_DISK_2)

		extracted_features = self._commerce_feature_extractor.extract_features(
			feature_column_names
		)

		pipeline = Pipeline(stages=extracted_features['stages'])

		pipeline_model = pipeline.fit(products_data_frame)

		pipeline_data = pipeline_model.transform(products_data_frame).select(
			'entryClassPK', 'features'
		)

		pipeline_data = pipeline_data.groupBy('entryClassPK').agg(
			F.collect_list('features').alias('vector_list')
		).withColumn(
			'features', F.expr('vectorMerge(vector_list)')
		).drop('vector_list')

		pipeline_data.createOrReplaceTempView('pipeline_data')

		self.spark_session.catalog.cacheTable('pipeline_data')

class ProductContentRecommendationJSONDataFrameWriter(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		super(
			ProductContentRecommendationJSONDataFrameWriter,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.'
			'ProductContentRecommendation', 'product_content_recommendations'
		)

class ProductContentRecommendationPreparationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			ProductContentRecommendationPreparationSparkJob,
			self
		).__init__(spark_application)

	def run(self):
		coalesce_partition_count = self.spark_application_configuration.get(
			'product.content.recommendation.coalesce.partition.count'
		)

		pipeline_data_frame = self.spark_session.table('pipeline_data')

		all_pairs_data_frame = pipeline_data_frame.select(
			'entryClassPK'
		).crossJoin(
			pipeline_data_frame.selectExpr(
				'entryClassPK AS entryClassPK2'
			)
		).withColumn(
			'key',
			F.array_sort(
				F.array_distinct(
					F.array(['entryClassPK', 'entryClassPK2'])
				)
			)
		)

		if coalesce_partition_count:
			all_pairs_data_frame = all_pairs_data_frame.coalesce(
				coalesce_partition_count
			)

		all_pairs_data_frame.createOrReplaceTempView('all_pairs')

		self.spark_session.catalog.cacheTable('all_pairs')

		score_pairs_data_frame = all_pairs_data_frame.dropDuplicates(
			subset=['key']
		).filter(
			F.size('key') > 1
		)

		score_pairs_data_frame = score_pairs_data_frame.join(
			pipeline_data_frame,
			on='entryClassPK'
		).join(
			pipeline_data_frame.selectExpr(
				'entryClassPK AS entryClassPK2',
				'features AS features2'
			),
			on='entryClassPK2'
		)

		score_pairs_data_frame.createOrReplaceTempView('score_pairs')

		self.spark_session.catalog.cacheTable('score_pairs')

class ProductContentRecommendationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			ProductContentRecommendationSparkJob,
			self
		).__init__(spark_application)

	def run(self):
		score_pairs_data_frame = self.spark_session.table('score_pairs')

		score_pairs_data_frame = score_pairs_data_frame.selectExpr(
		   'key', 'features', 'features2'
		)
		score_pairs_data_frame = score_pairs_data_frame.withColumn(
			'dotProduct', F.expr('vectorDotProduct(features, features2)')
		)
		score_pairs_data_frame = score_pairs_data_frame.withColumn(
			'norm1', F.expr('vectorNorm(features)')
		)
		score_pairs_data_frame = score_pairs_data_frame.withColumn(
			'norm2', F.expr('vectorNorm(features2)')
		)
		score_pairs_data_frame = score_pairs_data_frame.select(
			'key',
			F.expr(
				'dotProduct / (pow(norm1, 2) + pow(norm2, 2) - dotProduct)'
				' AS score'
			)
		)

		all_pairs_data_frame = self.spark_session.table('all_pairs')

		recommendations_data_frame = all_pairs_data_frame.join(
			score_pairs_data_frame,
			on=['key']
		)

		window = Window.partitionBy('entryClassPK').orderBy(
			F.col('score').desc()
		)

		recommendations_data_frame = recommendations_data_frame.select(
			'*',
			F.dense_rank().over(window).alias('rank')
		)

		max_rank = self.spark_application_configuration.get(
			'product.content.recommendation.max.rank'
		)

		recommendations_data_frame = recommendations_data_frame.filter(
			'rank >= 1 AND rank <= {}'.format(max_rank)
		)
		recommendations_data_frame = recommendations_data_frame.withColumn(
			'createDate', F.current_date()
		)
		recommendations_data_frame = recommendations_data_frame.withColumn(
			'jobId',
			F.lit(self.spark_application_configuration.get('spark.app.id'))
		)
		recommendations_data_frame = recommendations_data_frame.withColumn(
			'entryClassPK',
			F.col('entryClassPK').cast('long')
		)
		recommendations_data_frame = recommendations_data_frame.withColumn(
			'recommendedEntryClassPK',
			F.col('entryClassPK2').cast('long')
		)
		recommendations_data_frame = recommendations_data_frame.drop(
			'entryClassPK2'
		)
		recommendations_data_frame = recommendations_data_frame.drop('key')

		recommendations_data_frame.createOrReplaceTempView(
			'product_content_recommendations'
		)

		self.spark_session.catalog.cacheTable('product_content_recommendations')

class ProductInteractionBigQueryDataFrameReaderSparkJob(BaseBigQueryDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(ProductInteractionBigQueryDataFrameReaderSparkJob, self).__init__(
			spark_application,
			'product_interactions'
		)

	def _get_sql_query(self):
		configuration = self.spark_application_configuration

		return f"""
			SELECT
				id AS CPDefinitionId,
				categoryIds AS assetCategoryIds,
				(
					SELECT
						ARRAY_AGG(sku)
					FROM
						UNNEST(skus)
				) AS sku_list
			FROM
				`{self.spark_application_args.ac_project_id}`.product
			WHERE
				datasourceId = {self.spark_application_args.data_source_id}
		"""

	def _post_process(self, data_frame):
		return data_frame.withColumn(
			'assetCategoryIds',
			F.col('assetCategoryIds').cast('array<string>')
		)

class ProductInteractionJSONDataFrameReaderSparkJob(BaseJSONDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(
			ProductInteractionJSONDataFrameReaderSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.Product',
			'product_interactions'
		)

	def _post_process(self, data_frame):
		return data_frame.selectExpr(
			"id AS CPDefinitionId",
			"CAST(categoryIds AS array<string>) assetCategoryIds",
			"skus.sku AS sku_list"
		)

class ProductInteractionRecommendationJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		super(
			ProductInteractionRecommendationJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.'
			'ProductInteractionRecommendation',
			'product_interaction_recommendations'
		)

class ProductInteractionRecommendationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			ProductInteractionRecommendationSparkJob,
			self
		).__init__(spark_application)

	def run(self):
		configuration = self.spark_application_configuration
		spark_context = self.spark_session.sparkContext

		# Item-Item Collaborative Filtering

		item_factors_data_frame = self.spark_session.table('item_factors')

		score_function = configuration.get(
			'product.interaction.recommendation.score.function'
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
			'product.content.recommendation.max.rank'
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
			"product_interaction_recommendations"
		)

		self.spark_session.catalog.cacheTable(
			'product_interaction_recommendations'
		)

class UserInteractionCollaborativeFilteringSparkJob(CollaborativeFilteringSparkJob):

	def __init__(self, spark_application):
		configuration = spark_application.configuration

		als_checkpoint_interval = configuration.get(
			'user.interaction.recommendation.als.checkpoint.interval'
		)

		cross_validator_num_folds = configuration.get(
			'user.interaction.recommendation.tuning.cross.validator.'
			'num.folds', 3
		)

		cross_validator_parallelism = configuration.get(
			'user.interaction.recommendation.tuning.cross.validator.'
			'parallelism', 2
		)
		product_interaction_recommendation_enable = configuration.get(
			'product.interaction.recommendation.enable'
		)

		train_split_ratio = configuration.get(
			'user.interaction.recommendation.train.split.ratio'
		)

		tuning_alpha = configuration.get_list(
			'user.interaction.recommendation.tuning.alpha'
		)
		tuning_max_iterations = configuration.get_list(
			'user.interaction.recommendation.tuning.max.iteration'
		)
		tuning_rank = configuration.get_list(
			'user.interaction.recommendation.tuning.rank'
		)
		tuning_reg_parameter = configuration.get_list(
			'user.interaction.recommendation.tuning.regularization.parameter'
		)

		super(
			UserInteractionCollaborativeFilteringSparkJob,
			self
		).__init__(
			spark_application,
			item_column='CPDefinitionId',
			user_column='commerceAccountId',
			als_checkpoint_interval=als_checkpoint_interval,
			create_item_factors_table=product_interaction_recommendation_enable,
			cross_validator_num_folds=cross_validator_num_folds,
			cross_validator_parallelism=cross_validator_parallelism,
			recommendation_table='context_user_interaction_recommendation',
			train_split_ratio=train_split_ratio,
			tuning_alpha=tuning_alpha,
			tuning_max_iterations=tuning_max_iterations,
			tuning_rank=tuning_rank,
			tuning_reg_parameter=tuning_reg_parameter
		)

		self._default_count_approx_timeout = 5000

		self._log = logging.getLogger(self.__class__.__name__)

	def augment_recommendations(self, recommendations_data_frame):
		product_interaction_data_frame = self.spark_session.table(
			'product_interactions'
		).select('CPDefinitionId', 'assetCategoryIds')

		return recommendations_data_frame.selectExpr(
			'commerceAccountId',
			'explode(recommendations) as recommendations',
		).selectExpr(
			'commerceAccountId',
			'recommendations.CPDefinitionId as CPDefinitionId',
			'recommendations.rating as score'
		).join(product_interaction_data_frame, on='CPDefinitionId')

	def get_user_recommendation_count(self):
		product_interaction_data_frame = self.spark_session.table(
			'product_interactions'
		)

		catalog_coverage = float(
			self.spark_application_configuration.
			get('user.interaction.recommendation.catalog.coverage')
		)

		product_interaction_data_frame = product_interaction_data_frame.select(
			'CPDefinitionId'
		).distinct()

		catalog_count = product_interaction_data_frame.rdd.countApprox(
			self._default_count_approx_timeout
		)

		return int(catalog_count * catalog_coverage)

class UserInteractionDataPreparationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			UserInteractionDataPreparationSparkJob,
			self
		).__init__(spark_application)

	def run(self):
		cp_instance_data_frame = self.spark_session.table(
			'product_interactions'
		).selectExpr('CPDefinitionId', 'explode(sku_list) as sku')

		order_interaction_data_frame = self.spark_session.table(
			'order_interactions'
		)

		user_item_ratings_data_frame = order_interaction_data_frame.join(
			cp_instance_data_frame, on=["sku"]
		)

		user_item_ratings_data_frame = user_item_ratings_data_frame.drop("sku")
		user_item_ratings_data_frame = user_item_ratings_data_frame.withColumn(
			'rating', F.lit(1.0)
		)

		user_item_ratings_data_frame.createOrReplaceTempView(
			'user_item_rating_table'
		)

		self.spark_session.catalog.cacheTable('user_item_rating_table')