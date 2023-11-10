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

from liferay.commerce.forecast.constants import CommerceMLForecastPeriod, \
	CommerceMLForecastScope, \
	CommerceMLForecastTarget
from liferay.commerce.forecast.prophet import ProphetCommerceForecast
from liferay.commerce.forecast.sarima import SARIMAXCommerceForecast
from liferay.common.configuration import Configuration

from pyspark.sql import types as T

import logging
import os

class ForecastUDFHelper(object):

	def __init__(
		self, configuration: Configuration,
		period: CommerceMLForecastPeriod, scope: CommerceMLForecastScope,
		target: CommerceMLForecastTarget
	):
		self._configuration = configuration
		self._log = logging.getLogger(self.__class__.__name__)
		self._period = period
		self._scope = scope
		self._target = target

	def _get_parallel_count(self):
		# number of threads to use in parallel regions

		num_threads = self._configuration.get('OMP_NUM_THREADS', None)

		# number of thread used by OpenBLAS

		num_threads = self._configuration.get(
			'OPENBLAS_NUM_THREADS', num_threads
		)

		num_threads = self._configuration.get('MKL_NUM_THREADS', num_threads)

		cpu_count = os.cpu_count()

		if num_threads is None:
			num_threads = cpu_count

		if cpu_count is None:
			self._log.warning(
				"Unable to determine CPU count. Setting parallel count to 1"
			)

			return 1

		parallelism = cpu_count // num_threads

		self._log.info(
			"CPU count: {}. Threads: {}. Parallelism: {}".format(
				cpu_count, num_threads, parallelism
			)
		)

		return parallelism

	@staticmethod
	def get_schema():
		schema = T.StructType(
			[
				T.StructField('assetCategoryId', T.LongType()),
				T.StructField('commerceAccountId', T.LongType()),
				T.StructField('sku', T.StringType()),
				T.StructField('scope', T.StringType()),
				T.StructField('period', T.StringType()),
				T.StructField('target', T.StringType()),
				T.StructField('forecast', T.DoubleType()),
				T.StructField('timestamp', T.LongType()),
				T.StructField('forecastLowerBound', T.DoubleType()),
				T.StructField('forecastUpperBound', T.DoubleType()),
				T.StructField('modelAttributes', T.StringType()),
				T.StructField('error', T.StringType())
			]
		)

		return schema

	def get_instance(self):
		algorithm = self._configuration.get('forecast.algorithm')

		if algorithm == 'prophet':
			return self.get_prophet_instance()
		else:
			return self.get_sarima_instance()

	def get_prophet_instance(self):
		return ProphetCommerceForecast(
			period=self._period,
			scope=self._scope,
			target=self._target,
			changepoint_prior_scale=self._configuration.get_list(
				'forecast.prophet.checkpoint_prior_scale'
			),
			changepoint_range=self._configuration.get(
				'forecast.prophet.changepoint_range'
			),
			cross_validation_steps=self._configuration.get(
				'forecast.cross.validation.steps'
			),
			daily_seasonality=self._configuration.get(
				'forecast.prophet.daily_seasonality'
			),
			growth=self._configuration.get('forecast.prophet.growth'),
			holidays_prior_scale=self._configuration.get_list(
				'forecast.prophet.holidays_prior_scale'
			),
			horizon_steps=self._configuration.get('forecast.horizon.steps'),
			interval_width=self._configuration.get(
				'forecast.prophet.interval_width'
			),
			mcmc_samples=self._configuration.get(
				'forecast.prophet.mcmc_samples'
			),
			model_selection_criterion=self._configuration.get(
				'forecast.model.selection.criterion'
			),
			n_changepoints=self._configuration.get(
				'forecast.prophet.n_changepoints'
			),
			parallel_count=self._configuration.get(
				'forecast.force.parallelism', self._get_parallel_count()
			),
			seasonality_mode=self._configuration.get_list(
				'forecast.prophet.seasonality_mode'
			),
			seasonality_prior_scale=self._configuration.get_list(
				'forecast.prophet.seasonality_prior_scale'
			),
			weekly_seasonality=self._configuration.get(
				'forecast.prophet.weekly_seasonality'
			),
			yearly_seasonality=self._configuration.get(
				'forecast.prophet.yearly_seasonality'
			)
		)

	def get_sarima_instance(self):
		return SARIMAXCommerceForecast(
			period=self._period,
			scope=self._scope,
			target=self._target,
			cross_validation_steps=self._configuration.get(
				'forecast.cross.validation.steps'
			),
			dynamic=self._configuration.get(
				'forecast.model.prediction.dynamic.flag'
			),
			horizon_steps=self._configuration.get('forecast.horizon.steps'),
			max_order=self._configuration.get('forecast.model.max.order'),
			model_selection_criterion=self._configuration.get(
				'forecast.model.selection.criterion'
			),
			order_d_params=self._configuration.get_list('forecast.model.order.d'),
			order_p_params=self._configuration.get_list('forecast.model.order.p'),
			order_q_params=self._configuration.get_list('forecast.model.order.q'),
			parallel_count=self._configuration.get(
				'forecast.force.parallelism', self._get_parallel_count()
			),
			seasonal_d_params=self._configuration.get_list(
				'forecast.model.seasonal.d'
			),
			seasonal_m_params=self._configuration.get_list('forecast.model.period'),
			seasonal_p_params=self._configuration.get_list(
				'forecast.model.seasonal.p'
			),
			seasonal_q_params=self._configuration.get_list(
				'forecast.model.seasonal.q'
			),
			trend_params=self._configuration.get_list('forecast.model.trend')
		)