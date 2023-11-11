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

from abc import abstractmethod

from liferay.common.spark import BaseSparkJob

class BaseBigQueryDataFrameReaderSparkJob(BaseSparkJob):

	def __init__(
		self,
		spark_application,
		table_name,
		cache=True,
	):
		super(BaseBigQueryDataFrameReaderSparkJob, self).__init__(
			spark_application
		)

		self.cache = cache
		self.table_name = table_name

	@abstractmethod
	def _get_sql_query(self):
		raise NotImplementedError()

	def _post_process(self, data_frame):
		return data_frame

	def run(self):
		data_frame_reader = self.spark_session.read

		data_frame = data_frame_reader.format(
			"bigquery"
		).load(
			self._get_sql_query()
		)

		data_frame = self._post_process(data_frame)

		data_frame.createOrReplaceTempView(self.table_name)

		if self.cache:
			self.spark_session.catalog.cacheTable(self.table_name)


class BaseJSONDataFrameWriterSparkJob(BaseSparkJob):

	def __init__(self, spark_application, root_path, target_folder, table_name):
		super(BaseJSONDataFrameWriterSparkJob, self).__init__(spark_application)

		self._root_path = root_path
		self.table_name = table_name
		self.target_folder = target_folder

	def _get_bucket_path(self):
		configuration = self.spark_application_configuration

		return '{}/{}/{}/{}/'.format(
			self._root_path,
			self.spark_application.args.ac_project_id,
			self.spark_application_args.data_source_id, self.target_folder
		)

	def _pre_process(self, data_frame):
		return data_frame

	def run(self):
		data_frame = self.spark_session.table(self.table_name)

		data_frame = self._pre_process(data_frame)

		data_frame_writer = data_frame.write

		data_frame_writer.format("json").mode("overwrite").save(
			self._get_bucket_path()
		)