/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.entity.BQExpandoColumn;
import com.liferay.osb.asah.common.entity.BQExpandoValue;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.ChannelDataSource;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.model.ExpandoField;
import com.liferay.osb.asah.common.repository.BQExpandoColumnRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.util.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcos Martins
 */
public abstract class BaseBQDXPEntityDog {

	protected List<Long> getDataSourceIds(Long channelId) {
		if (channelId == null) {
			return Collections.emptyList();
		}

		Channel channel = _channelDog.fetchChannel(channelId);

		if (channel == null) {
			return Collections.emptyList();
		}

		return ListUtil.map(
			channel.getChannelDataSources(),
			ChannelDataSource::getDataSourceId);
	}

	protected Map<Long, String> getDataSourceNames(List<Long> dataSourceIds) {
		List<DataSource> dataSources = IterableUtils.toList(
			_dataSourceRepository.findAllById(dataSourceIds));

		Stream<DataSource> stream = dataSources.stream();

		return stream.collect(
			Collectors.toMap(DataSource::getId, DataSource::getName));
	}

	protected List<ExpandoField> getExpandoFields(
		List<BQExpandoValue> bqExpandoValues) {

		List<ExpandoField> expandoFields = new ArrayList<>();

		Map<String, BQExpandoValue> bqExpandoValuesMap = new HashMap<>();

		for (BQExpandoValue bqExpandoValue : bqExpandoValues) {
			bqExpandoValuesMap.put(
				bqExpandoValue.getColumnId(), bqExpandoValue);
		}

		for (Map.Entry<String, BQExpandoValue> entry :
				bqExpandoValuesMap.entrySet()) {

			Optional<BQExpandoColumn> bqExpandoColumnOptional =
				_bqExpandoColumnRepository.findById(entry.getKey());

			if (!bqExpandoColumnOptional.isPresent()) {
				continue;
			}

			BQExpandoValue bqExpandoValue = bqExpandoValuesMap.get(
				entry.getKey());

			if (bqExpandoValue == null) {
				continue;
			}

			expandoFields.add(
				new ExpandoField(
					bqExpandoColumnOptional.get(), bqExpandoValue));
		}

		return expandoFields;
	}

	@Autowired
	private BQExpandoColumnRepository _bqExpandoColumnRepository;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

}