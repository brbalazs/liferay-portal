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

from liferay.common.spark import BaseSparkJob
from liferay.ml.common import MAPEvaluator

from pyspark.ml.recommendation import ALS
from pyspark.ml.tuning import CrossValidator, \
	ParamGridBuilder

import logging

class CollaborativeFilteringSparkJob(BaseSparkJob):

	def __init__(
		self,
		spark_application,
		item_column,
		user_column,
		als_checkpoint_interval=5,
		create_item_factors_table=True,
		cross_validator_num_folds=3,
		cross_validator_parallelism=2,
		input_table='user_item_rating_table',
		item_factors_table='item_factors',
		prediction_column='prediction',
		rating_column='rating',
		recommendation_table='user_item_recommendations',
		train_split_ratio=0.8,
		tuning_alpha=None,
		tuning_max_iterations=None,
		tuning_rank=None,
		tuning_reg_parameter=None
	):
		super(
			CollaborativeFilteringSparkJob,
			self
		).__init__(spark_application)

		self._als_checkpoint_interval = als_checkpoint_interval

		self._create_item_factors_table = create_item_factors_table

		self._cross_validator_num_folds = cross_validator_num_folds

		self._cross_validator_parallelism = cross_validator_parallelism

		self._default_count_approx_timeout = 5000

		self._input_table = input_table

		self._item_column = item_column

		self._item_factors_table = item_factors_table

		self._log = logging.getLogger(self.__class__.__name__)

		self._prediction_column = prediction_column

		self._rating_column = rating_column

		self._recommendation_table = recommendation_table

		self._train_split_ratio = train_split_ratio

		self._tuning_alpha = tuning_alpha

		self._tuning_max_iterations = tuning_max_iterations

		self._tuning_rank = tuning_rank

		self._tuning_reg_parameter = tuning_reg_parameter

		self._user_column = user_column

	def augment_recommendations(self, recommendations_data_frame):
		return recommendations_data_frame

	def get_user_recommendation_count(self):
		return 100

	def _get_evaluator(self):
		return MAPEvaluator(
			label_column_name=self._rating_column,
			prediction_column_name=self._prediction_column,
			query_column_name=self._user_column
		)

	def _get_training_pipeline(self):
		als = ALS(
			userCol=self._user_column,
			itemCol=self._item_column,
			ratingCol=self._rating_column,
			coldStartStrategy='drop',
			implicitPrefs=True,
			nonnegative=True
		)

		als.setCheckpointInterval(self._als_checkpoint_interval)

		param_grid_builder = ParamGridBuilder()

		param_grid_builder.addGrid(als.alpha, self._tuning_alpha)
		param_grid_builder.addGrid(als.maxIter, self._tuning_max_iterations)
		param_grid_builder.addGrid(als.rank, self._tuning_rank)
		param_grid_builder.addGrid(als.regParam, self._tuning_reg_parameter)

		return CrossValidator(
			estimator=als,
			estimatorParamMaps=param_grid_builder.build(),
			evaluator=self._get_evaluator(),
			numFolds=self._cross_validator_num_folds,
			parallelism=self._cross_validator_parallelism
		)

	def _log_model_details(self, model):
		if not self._log.isEnabledFor(logging.INFO):
			return

		self._log.info("Best model details:")
		self._log.info(
			"Max Iterations: {}".format(model._java_obj.parent().getMaxIter())
		)
		self._log.info(
			"Reg Parameter: {}".format(model._java_obj.parent().getRegParam())
		)
		self._log.info("Alpha: {}".format(model._java_obj.parent().getAlpha()))
		self._log.info("Rank: {}".format(model._java_obj.parent().getRank()))
		self._log.info(
			"Non-negative feedback: {}".format(
				model._java_obj.parent().getNonnegative()
			)
		)

	def _log_model_performance(self, model, test_data_frame, train_data_frame):
		if not self._log.isEnabledFor(logging.INFO):
			return

		test_map = self._get_evaluator().evaluate(
			model.transform(test_data_frame)
		)

		self._log.info("Model performance on TEST set: {}".format(test_map))

		train_map = self._get_evaluator().evaluate(
			model.transform(train_data_frame)
		)

		self._log.info("Model performance on TRAIN set: {}".format(train_map))

	def _split_train_test(self, user_item_data_frame):
		train_data_frame, test_data_frame = user_item_data_frame.randomSplit(
			[self._train_split_ratio, 1 - self._train_split_ratio]
		)

		train_data_frame.cache()

		test_data_frame.cache()

		return train_data_frame, test_data_frame

	def run(self):
		user_item_rating_data_frame = self.spark_session.table(
			self._input_table
		)

		train_data_frame, test_data_frame = self._split_train_test(
			user_item_rating_data_frame
		)

		training_pipeline = self._get_training_pipeline()

		training_pipeline_model = training_pipeline.fit(train_data_frame)

		best_model = training_pipeline_model.bestModel

		self._log_model_details(best_model)

		self._log_model_performance(
			best_model, test_data_frame, train_data_frame
		)

		user_recommendation_count = self.get_user_recommendation_count()

		self._log.info(
			"Generating {} recommendations items per user".
			format(user_recommendation_count)
		)

		recommendations_data_frame = best_model.recommendForAllUsers(
			user_recommendation_count
		)

		self.augment_recommendations(
			recommendations_data_frame
		).createOrReplaceTempView(self._recommendation_table)

		self.spark_session.catalog.cacheTable(self._recommendation_table)

		if self._create_item_factors_table:
			item_factors = best_model.itemFactors

			item_factors.createOrReplaceTempView(self._item_factors_table)

			self.spark_session.catalog.cacheTable(self._item_factors_table)