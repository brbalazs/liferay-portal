/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.converter.helper;

import com.liferay.osb.asah.common.converter.helper.DefaultFilterStringConverterHelper;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
public class DataSourceFilterStringConverterHelper
	extends DefaultFilterStringConverterHelper {

	@Override
	public Map<String, String> getFieldNameConversionMap() {
		return new HashMap<String, String>() {
			{
				put("credentials/type", "credentialType");
				put("provider/type", "providerType");
			}
		};
	}

	@Override
	public String getFilterType() {
		return "dataSourceAccountPKs";
	}

	@Override
	public Condition getLogicFunctionCondition(
		String fieldName, String operator, boolean processString,
		String valueString) {

		if (fieldName.equals("channelId") && operator.equals("eq")) {
			Set<Long> dataSourceIds = _getDataSourceIds(valueString);

			if (!dataSourceIds.isEmpty()) {
				Field<Long> field = DSL.field("id", Long.class);

				return field.in(dataSourceIds);
			}
		}

		return null;
	}

	private Set<Long> _getDataSourceIds(String valueString) {
		Object value = StringUtil.toObject(valueString);

		List<Long> channelIds;

		if (value instanceof JSONArray) {
			channelIds = JSONUtil.toLongList((JSONArray)value);
		}
		else {
			channelIds = Collections.singletonList((Long)value);
		}

		List<Channel> channels = _channelDog.getChannels(channelIds);

		Stream<Channel> stream = channels.stream();

		return stream.flatMap(
			channel -> channel.getChannelDataSources(
			).stream()
		).map(
			ChannelDataSource::getDataSourceId
		).collect(
			Collectors.toSet()
		);
	}

	@Autowired
	private ChannelDog _channelDog;

}