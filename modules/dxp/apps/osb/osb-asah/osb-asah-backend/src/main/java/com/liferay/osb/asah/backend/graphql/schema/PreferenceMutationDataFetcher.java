/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
@GraphQLTypeWiring(fieldName = "preference", typeName = "MutationType")
public class PreferenceMutationDataFetcher implements DataFetcher<Preference> {

	@Override
	public Preference get(DataFetchingEnvironment dataFetchingEnvironment) {
		String key = dataFetchingEnvironment.getArgument("key");
		String value = dataFetchingEnvironment.getArgument("value");

		_validate(key, value);

		return _preferenceDog.savePreference(key, value);
	}

	private void _validate(String key, String value) {
		Function<String, Boolean> function = _validatorFunctions.get(key);

		if ((function == null) || !function.apply(value)) {
			throw new RuntimeException(
				String.format("Invalid preference: %s=%s", key, value));
		}
	}

	private static final Log _log = LogFactory.getLog(
		PreferenceMutationDataFetcher.class);

	private static final Map<String, Function<String, Boolean>>
		_validatorFunctions = new HashMap<String, Function<String, Boolean>>() {
			{
				put(
					"data-retention-period",
					value -> {
						long longValue = Long.parseLong(value);

						if ((longValue <= 0) ||
							(longValue > (13 * DateUtil.MONTH))) {

							return false;
						}

						return true;
					});
				put(
					"search-query-strings",
					value -> {
						try {
							new JSONArray(value);
						}
						catch (Exception exception) {
							_log.error(exception, exception);

							return false;
						}

						return true;
					});
				put("time-zone-id", value -> true);
			}
		};

	@Autowired
	private PreferenceDog _preferenceDog;

}