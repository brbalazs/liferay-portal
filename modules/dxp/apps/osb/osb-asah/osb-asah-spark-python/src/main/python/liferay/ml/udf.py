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

from pyspark.ml.linalg import DenseVector, \
	VectorUDT, \
	Vectors
from pyspark.sql import functions as F, \
	types as T

import numpy as np

class BaseUDFFunction(object, metaclass=ABCMeta):

	def __init__(self, spark_session):
		udf_function = F.udf(self.udf_function, self.get_return_type())

		spark_session.udf.register(self.get_function_name(), udf_function)

	@staticmethod
	@abstractmethod
	def get_function_name():
		raise NotImplementedError()

	@staticmethod
	@abstractmethod
	def get_return_type():
		raise NotImplementedError()

	@staticmethod
	@abstractmethod
	def udf_function(*args, **kwargs):
		raise NotImplementedError()

class TanimotoCoefficientUDFFunction(BaseUDFFunction):

	def __init__(self, spark_session):
		super(TanimotoCoefficientUDFFunction, self).__init__(spark_session)

	@staticmethod
	def get_function_name():
		return 'tanimoto_coefficient'

	@staticmethod
	def get_return_type():
		return T.DoubleType()

	@staticmethod
	def udf_function(col1, col2):
		dot_product = np.dot(col1, col2)

		norm1 = np.linalg.norm(col1, 2)
		norm2 = np.linalg.norm(col2, 2)

		norm1_squared = norm1**2
		norm2_squared = norm2**2

		return (dot_product /
				(norm1_squared + norm2_squared - dot_product)).item()

class ToDenseVectorUDFFunction(BaseUDFFunction):

	def __init__(self, spark_session):
		super(ToDenseVectorUDFFunction, self).__init__(spark_session)

	@staticmethod
	def get_function_name():
		return 'toDenseVector'

	@staticmethod
	def get_return_type():
		return VectorUDT()

	@staticmethod
	def udf_function(vector):
		return DenseVector(list(vector))

class VectorDotProductUDFFunction(BaseUDFFunction):

	def __init__(self, spark_session):
		super(VectorDotProductUDFFunction, self).__init__(spark_session)

	@staticmethod
	def get_function_name():
		return 'vectorDotProduct'

	@staticmethod
	def get_return_type():
		return T.DoubleType()

	@staticmethod
	def udf_function(vector1, vector2):
		return float(vector1.dot(vector2))

class VectorMergeUDFFunction(BaseUDFFunction):

	def __init__(self, spark_session):
		super(VectorMergeUDFFunction, self).__init__(spark_session)

	@staticmethod
	def get_function_name():
		return 'vectorMerge'

	@staticmethod
	def get_return_type():
		return VectorUDT()

	@staticmethod
	def udf_function(vectors):
		data = dict()

		vector_size = 0

		for vector in vectors:
			vector_size = int(vector.size)

			if isinstance(vector, DenseVector):
				vector = Vectors.sparse(
					vector_size,
					range(vector_size),
					vector.values
				)

			for index in vector.indices:
				index = int(index)

				if data.get(index) is None:
					data[index] = vector[index]

		return Vectors.sparse(vector_size, data)

class VectorNormUDFFunction(BaseUDFFunction):

	def __init__(self, spark_session):
		super(VectorNormUDFFunction, self).__init__(spark_session)

	@staticmethod
	def get_function_name():
		return 'vectorNorm'

	@staticmethod
	def get_return_type():
		return T.DoubleType()

	@staticmethod
	def udf_function(vector, order=2):
		return float(vector.norm(p=order))