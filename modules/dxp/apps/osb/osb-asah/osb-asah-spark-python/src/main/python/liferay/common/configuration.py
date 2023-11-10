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

import os
import yaml

class Configuration:

	def __init__(self, configuration_path=None):
		self._dict = self._create_dict(configuration_path)

	def _create_dict(self, configuration_path):
		if configuration_path is None:
			return {}

		with open(configuration_path) as configuration_file:
			return self._flatten_dict(yaml.safe_load(configuration_file))

	def _flatten_dict(self, dictionary, flat_dict={}, parent_key=None):
		for key, value in dictionary.items():
			new_key = key

			if parent_key is not None:
				new_key = parent_key + '.' + new_key

			if isinstance(value, dict):
				self._flatten_dict(value, flat_dict, new_key)
			else:
				flat_dict[new_key] = value

		return flat_dict

	def _get_environment_variable(self, key):
		environment_key = key.upper()

		return os.environ.get(environment_key.replace('.', '_'))

	def get(self, key, default_value=None):
		value = self._get_environment_variable(key)

		if value is not None:
			return self._cast_value(value)

		if self._dict is not None:
			value = self._dict.get(key)

		if value is not None:
			return value

		return default_value

	def get_list(self, key=None, default=None):
		value = self.get(key, default_value=default)

		if isinstance(value, list):
			return value

		if isinstance(value, str):
			return [self._cast_value(v.strip()) for v in value.split(',')]

		if value is None:
			return None

		return [value]

	def _cast_value(self, value):
		if value.lower() in ['true', 'false']:
			return value.lower() == 'true'

		if value.replace('.', '', 1).isdigit():
			return float(value) if '.' in value else int(value)

		return value