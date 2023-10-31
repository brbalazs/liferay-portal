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

from pyspark.ml.feature import CountVectorizer, \
	IDF, \
	OneHotEncoder, \
	StopWordsRemover, \
	StringIndexer, \
	Tokenizer, \
	VectorAssembler

import logging

class CommerceFeatureExtractor(object):

	def __init__(self):
		self._categorical_column_names = ['companyId', 'productTypeName']
		self._count_vectorizer_column_names = ['assetCategoryIds']
		self._text_column_names = ['name', 'description']

		self._log = logging.getLogger(self.__class__.__name__)

	def _extract_features(self, column_name):
		if column_name in self._categorical_column_names:
			return self.categorical_encoder(column_name)
		elif column_name in self._count_vectorizer_column_names:
			return self.count_vectorizer_encoder(column_name)
		elif column_name in self._text_column_names:
			return self.text_encoder(column_name)
		elif column_name.startswith('SPECIFICATION_'):
			return self.categorical_encoder(column_name, handle_invalid='keep')
		else:
			self._log.warn(
				"Unable to find feature extractor for: " + column_name
			)

		return None

	@staticmethod
	def categorical_encoder(column_name, handle_invalid='skip'):
		feature_column_name = column_name + 'Feature'

		stages = [
			StringIndexer(
				inputCol=column_name,
				outputCol=column_name + 'Index',
				handleInvalid=handle_invalid
			)
		]

		stages += [
			OneHotEncoder(
				dropLast=False,
				inputCol=column_name + 'Index',
				outputCol=feature_column_name
			)
		]

		return {'feature_column': feature_column_name, 'stages': stages}

	@staticmethod
	def count_vectorizer_encoder(column_name):
		feature_column_name = column_name + 'Feature'

		stages = [
			CountVectorizer(
				inputCol=column_name, outputCol=feature_column_name
			)
		]

		return {'feature_column': feature_column_name, 'stages': stages}

	def extract_features(self, feature_column_names):
		columns = []
		stages = []

		for feature_column_name in feature_column_names:
			extracted_features = self._extract_features(feature_column_name)

			if extracted_features is not None:
				columns += [extracted_features['feature_column']]
				stages += extracted_features['stages']

		stages += [VectorAssembler(inputCols=columns, outputCol='features')]

		return {'feature_column': 'features', 'stages': stages}

	@staticmethod
	def text_encoder(column_name):
		feature_column_name = column_name + 'Feature'

		stages = [
			Tokenizer(inputCol=column_name, outputCol=column_name + 'Token')
		]

		stages += [
			StopWordsRemover(
				inputCol=column_name + 'Token',
				outputCol=column_name + 'TokenFiltered'
			)
		]

		stages += [
			CountVectorizer(
				inputCol=column_name + 'TokenFiltered',
				outputCol=column_name + 'Count'
			)
		]

		stages += [
			IDF(inputCol=column_name + 'Count', outputCol=feature_column_name)
		]

		return {'feature_column': feature_column_name, 'stages': stages}