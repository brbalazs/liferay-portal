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

from liferay.commerce.common import BaseJSONDataFrameReaderSparkJob
from liferay.commerce.forecast.constants import CommerceMLForecastPeriod, \
	CommerceMLForecastScope, \
	CommerceMLForecastTarget
from liferay.commerce.forecast.udf import ForecastUDFHelper
from liferay.common.job import BaseJSONDataFrameWriterSparkJob
from liferay.common.spark import BaseSparkJob

from pyspark.sql import Window, \
	functions as F

class AccountCategoryForecastJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		super(
			AccountCategoryForecastJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.'
			'AccountCategoryForecast', 'forecast_prediction'
		)

class AccountForecastJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		super(
			AccountForecastJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.'
			'AccountForecast', 'forecast_prediction'
		)

class ForecastDataPrepareSparkJob(BaseSparkJob):

	def __init__(
		self, spark_application, period: CommerceMLForecastPeriod,
		scope: CommerceMLForecastScope, target: CommerceMLForecastTarget
	):
		super(ForecastDataPrepareSparkJob, self).__init__(spark_application)

		self._period = period
		self._scope = scope
		self._target = target

	def run(self):
		order_forecast_data_frame = self.spark_session.table('order_forecast')

		# Bucket data

		data_frame_grouped = order_forecast_data_frame.groupBy(
			self._scope.columns + ['timestamp']
		)

		order_forecast_data_frame = data_frame_grouped.agg(
			F.sum('actual').alias('actual')
		)

		window_spec = Window.partitionBy(self._scope.columns)

		window_spec = window_spec.orderBy(F.col('timestamp').desc())

		order_forecast_data_frame = order_forecast_data_frame.withColumn(
			'row_number',
			F.row_number().over(window_spec)
		)

		forecast_data_minimum_length = self.spark_application_configuration.get(
			'forecast.data.minimum.length'
		)

		forecast_data_history_length = self.spark_application_configuration.get(
			'forecast.data.history.length'
		)

		data_length = max(
			forecast_data_history_length, forecast_data_minimum_length
		)

		order_forecast_data_frame = order_forecast_data_frame.filter(
			'row_number <= {}'.format(data_length)
		)

		data_frame_grouped = order_forecast_data_frame.groupBy(
			self._scope.columns
		)

		order_forecast_data_frame_count = data_frame_grouped.agg(
			F.count(
				'*'
			).alias(
				'count'
			)
		)

		order_forecast_data_frame = order_forecast_data_frame.join(
			order_forecast_data_frame_count, on=self._scope.columns
		)

		order_forecast_data_frame = order_forecast_data_frame.filter(
			'count >= {}'.format(forecast_data_minimum_length)
		)

		forecast_data_data_frame = order_forecast_data_frame.drop(
			'row_number'
		).drop(
			'count'
		)

		forecast_data_data_frame.createOrReplaceTempView('forecast_data')

		self.spark_session.catalog.cacheTable('forecast_data')

class ForecastOrderJSONDataFrameReader(BaseJSONDataFrameReaderSparkJob):

	def __init__(
		self, spark_application, period: CommerceMLForecastPeriod,
		scope: CommerceMLForecastScope, target: CommerceMLForecastTarget
	):
		super(ForecastOrderJSONDataFrameReader, self).__init__(
			spark_application,
			'com.liferay.headless.commerce.admin.order.dto.v1_0.Order',
			'order_forecast'
		)

		self._period = period
		self._scope = scope
		self._target = target

	def _post_process(self, data_frame):
		data_frame = data_frame.selectExpr(
			'id as orderId', 'accountId as commerceAccountId', 'orderDate',
			'EXPLODE(orderItems) AS orderItem'
		)

		data_frame = data_frame.withColumn(
			'orderDate', F.date_trunc(self._period.label, F.col('orderDate'))
		)

		return data_frame.selectExpr(
			'orderId', 'commerceAccountId',
			'TO_TIMESTAMP(orderDate) AS timestamp',
			'orderItem.id AS orderItemId', 'orderItem.sku AS sku',
			'orderItem.{} as actual'.format(self._target.column)
		)

class ForecastProductCategoryAugmentationSparkJob(BaseSparkJob):

	def __init__(self, spark_application):
		super(
			ForecastProductCategoryAugmentationSparkJob,
			self
		).__init__(spark_application)

	def run(self):
		order_forecast_data_frame = self.spark_session.table('order_forecast')

		product_forecast_data_frame = self.spark_session.table(
			'product_forecast'
		)

		order_forecast_data_frame = order_forecast_data_frame.join(
			product_forecast_data_frame, on=['sku']
		)

		order_forecast_data_frame.createOrReplaceTempView('order_forecast')

		self.spark_session.catalog.cacheTable('order_forecast')

class ForecastProductJSONDataFrameReaderSparkJob(BaseJSONDataFrameReaderSparkJob):

	def __init__(self, spark_application):
		super(ForecastProductJSONDataFrameReaderSparkJob, self).__init__(
			spark_application,
			'com.liferay.headless.commerce.admin.catalog.dto.v1_0.Product',
			'product_forecast'
		)

	def _post_process(self, data_frame):
		data_frame = data_frame.selectExpr(
			'categories.id AS assetCategoryId', 'EXPLODE(skus.sku) AS sku'
		)

		return data_frame.selectExpr(
			'EXPLODE(assetCategoryId) AS assetCategoryId', 'sku'
		)

class ForecastSparkJob(BaseSparkJob):

	def __init__(
		self, spark_application, period: CommerceMLForecastPeriod,
		scope: CommerceMLForecastScope, target: CommerceMLForecastTarget
	):
		super(ForecastSparkJob, self).__init__(spark_application)

		self._period = period
		self._scope = scope
		self._target = target

	def _get_train_udf(self):
		forecast_udf_helper = ForecastUDFHelper(
			self.spark_application_configuration, self._period, self._scope,
			self._target
		)

		forecast_function = forecast_udf_helper.get_instance()

		return F.pandas_udf(
			f=lambda df: forecast_function.fit_predict(df),
			returnType=forecast_udf_helper.get_schema(),
			functionType=F.PandasUDFType.GROUPED_MAP
		)

	def run(self):
		forecast_data_frame = self.spark_session.table('forecast_data')

		forecast_grouped_data = forecast_data_frame.groupBy(self._scope.columns)

		forecast_prediction = forecast_grouped_data.apply(self._get_train_udf())

		forecast_prediction.createOrReplaceTempView('forecast_prediction')

		self.spark_session.catalog.cacheTable('forecast_prediction')

class MergeHistorySparkJob(BaseSparkJob):

	def __init__(
		self, spark_application, period: CommerceMLForecastPeriod,
		scope: CommerceMLForecastScope, target: CommerceMLForecastTarget
	):
		super(MergeHistorySparkJob, self).__init__(spark_application)

		self._period = period
		self._scope = scope
		self._target = target

	def _add_missing_columns(self, data_frame, column_set):
		missing_column_names = list(column_set - set(data_frame.columns))

		for column_name in missing_column_names:
			if column_name == 'period':
				data_frame = data_frame.withColumn(
					'period', F.lit(self._period.label)
				)
			elif column_name == 'scope':
				data_frame = data_frame.withColumn(
					'scope', F.lit(self._scope.label)
				)
			elif column_name == 'target':
				data_frame = data_frame.withColumn(
					'target', F.lit(self._target.label)
				)
			else:
				data_frame = data_frame.withColumn(column_name, F.lit(None))

		return data_frame

	def run(self):
		forecast_data_frame = self.spark_session.table('forecast_data')

		window_spec = Window.partitionBy(self._scope.columns)

		window_spec = window_spec.orderBy(F.col('timestamp').desc())

		order_forecast_data_frame = forecast_data_frame.withColumn(
			'row_number',
			F.row_number().over(window_spec)
		)

		forecast_data_history_length = self.spark_application_configuration.get(
			'forecast.data.history.length'
		)

		forecast_data_frame = order_forecast_data_frame.filter(
			'row_number <= {}'.format(forecast_data_history_length)
		).drop(
			'row_number'
		)

		forecast_prediction_data_frame = self.spark_session.table(
			'forecast_prediction'
		)

		column_set = set(
			forecast_data_frame.columns +
			forecast_prediction_data_frame.columns
		)

		forecast_data_frame = forecast_data_frame.withColumn(
			'timestamp',
			F.col('timestamp').cast('long') * 1000
		)

		forecast_data_frame = self._add_missing_columns(
			data_frame=forecast_data_frame,
			column_set=column_set
		)

		forecast_prediction_data_frame = self._add_missing_columns(
			data_frame=forecast_prediction_data_frame,
			column_set=column_set
		)

		forecast_prediction_data_frame = forecast_prediction_data_frame.unionByName(
			forecast_data_frame
		).orderBy(
			self._scope.columns + ['timestamp']
		)

		forecast_prediction_data_frame.createOrReplaceTempView('forecast_prediction')

		self.spark_session.catalog.cacheTable('forecast_prediction')

class SkuForecastJSONDataFrameWriterSparkJob(BaseJSONDataFrameWriterSparkJob):

	def __init__(self, spark_application):
		super(
			SkuForecastJSONDataFrameWriterSparkJob,
			self
		).__init__(
			spark_application,
			'com.liferay.headless.commerce.machine.learning.dto.v1_0.'
			'SkuForecast', 'forecast_prediction'
		)