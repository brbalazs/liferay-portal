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

from pyspark.ml.evaluation import Evaluator
from pyspark.sql import functions as F

class MAPEvaluator(Evaluator):

	def __init__(
		self,
		label_column_name,
		prediction_column_name,
		query_column_name,
		threshold=0.5
	):
		super(MAPEvaluator, self).__init__()

		self._label_column_name = label_column_name
		self._prediction_column_name = prediction_column_name
		self._query_column_name = query_column_name
		self._threshold = threshold

	def _evaluate(self, data_frame):
		binary_expression = self._get_binary_expression(
			self._prediction_column_name, self._threshold
		)

		data_frame = data_frame.withColumn(
			'binary', binary_expression.cast('double')
		)

		data_frame = data_frame.withColumn(
			self._prediction_column_name,
			F.col(self._prediction_column_name).cast('double')
		)

		data_frame = data_frame.groupBy(self._query_column_name)

		data_frame = data_frame.agg(
			F.collect_list(self._label_column_name).alias('gt'),
			F.collect_list('binary').alias('pred')
		)

		rdd = data_frame.rdd

		return rdd.map(lambda r: sum(r.pred) / sum(r.gt)).mean()

	def _get_binary_expression(self, column_name, threshold):
		return F.when(F.col(column_name) > threshold, 1.0).otherwise(0.0)