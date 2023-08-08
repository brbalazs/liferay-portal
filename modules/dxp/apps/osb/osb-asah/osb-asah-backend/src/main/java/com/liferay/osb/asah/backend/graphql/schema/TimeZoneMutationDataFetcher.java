/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.http.NanitesHttp;
import com.liferay.osb.asah.common.spring.annotation.CacheEvict;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
@GraphQLTypeWiring(fieldName = "timeZone", typeName = "MutationType")
public class TimeZoneMutationDataFetcher implements DataFetcher<String> {

	@CacheEvict(evictAll = true)
	@Override
	public String get(DataFetchingEnvironment dataFetchingEnvironment) {
		String value = dataFetchingEnvironment.getArgument("value");

		_preferenceDog.savePreference("time-zone-id", value);

		_nanitesHttp.rescheduleNanites();

		return value;
	}

	@Autowired
	private NanitesHttp _nanitesHttp;

	@Autowired
	private PreferenceDog _preferenceDog;

}