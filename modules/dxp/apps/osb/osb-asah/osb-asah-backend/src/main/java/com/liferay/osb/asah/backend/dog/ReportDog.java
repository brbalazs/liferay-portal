/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetricType;
import com.liferay.osb.asah.backend.model.FormMetricType;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class ReportDog {

	@Autowired
	public ReportDog(
		List<AssetMetricRepository> assetMetricRepositories,
		BQIndividualRepository bqIndividualRepository, ChannelDog channelDog) {

		assetMetricRepositories.forEach(
			assetMetricAssetMetricRepository -> _assetMetricRepositoryMap.put(
				assetMetricAssetMetricRepository.getAssetType(),
				assetMetricAssetMetricRepository));

		_bqIndividualRepository = bqIndividualRepository;
		_channelDog = channelDog;
	}

	public File getCSVReport(
			@Nullable String assetId, @Nullable String assetType,
			Long channelId, @Nullable String query, @Nullable String[] sorts,
			@Nullable TimeRange timeRange, String type)
		throws Exception {

		List<String[]> rows = null;

		if (StringUtils.equals(type, "blog")) {

			// TODO

		}
		else if (StringUtils.equals(type, "document")) {

			// TODO

		}
		else if (StringUtils.equals(type, "form")) {

			// TODO

		}
		else if (StringUtils.equals(type, "individual") &&
				 StringUtils.isEmpty(assetId) &&
				 StringUtils.isEmpty(assetType)) {

			rows = _getIndividualRows(channelId, query, sorts);
		}
		else if (StringUtils.equals(type, "individual") &&
				 !StringUtils.isEmpty(assetType)) {

			rows = _getAssetIndividualRows(
				assetId, assetType, channelId, query, sorts, timeRange);
		}
		else if (StringUtils.equals(type, "journal")) {

			// TODO

		}
		else if (StringUtils.equals(type, "page")) {

			// TODO

		}

		File file = File.createTempFile("report", ".csv");

		if (rows != null) {
			try (CSVWriter csvWriter = new CSVWriter(
					new BufferedWriter(
						new OutputStreamWriter(
							new FileOutputStream(file, true),
							StandardCharsets.UTF_8)))) {

				for (String[] row : rows) {
					csvWriter.writeNext(row);
				}
			}
		}

		return file;
	}

	private List<String[]> _getAssetIndividualRows(
		@Nullable String assetId, String assetType, Long channelId,
		@Nullable String query, @Nullable String[] sorts,
		@Nullable TimeRange timeRange) {

		List<String[]> rows = new ArrayList<>();

		rows.add(_columnHeader.get("individual"));

		AssetMetricRepository assetMetricRepository =
			_assetMetricRepositoryMap.get(AssetType.of(assetType));

		List<com.liferay.osb.asah.backend.model.Individual> individuals =
			assetMetricRepository.getKnownIndividuals(
				assetId, null, channelId,
				_getMetricType(AssetType.of(assetType)),
				PageRequest.of(0, _MAX_SIZE, _getSort(sorts)), query,
				timeRange);

		Channel channel = _channelDog.getChannel(channelId);

		for (com.liferay.osb.asah.backend.model.Individual individual :
				individuals) {

			rows.add(
				new String[] {
					individual.getName(), individual.getEmailAddress(),
					channel.getName()
				});
		}

		return rows;
	}

	private List<String[]> _getIndividualRows(
		Long channelId, @Nullable String query, @Nullable String[] sorts) {

		List<String[]> rows = new ArrayList<>();

		rows.add(_columnHeader.get("individual"));

		List<Individual> individuals =
			_bqIndividualRepository.searchBQIndividuals(
				null, channelId, null, null, null,
				PageRequest.of(0, _MAX_SIZE, _getSort(sorts)), query, null);

		Channel channel = _channelDog.getChannel(channelId);

		for (Individual individual : individuals) {
			Individual.Demographics demographics = individual.getDemographics();

			Map<String, List<Field>> fieldMap = demographics.getField();

			Object givenNameFiledValue = fieldMap.get(
				"givenName"
			).get(
				0
			).getValue();

			Object familyNameFieldValue = fieldMap.get(
				"familyName"
			).get(
				0
			).getValue();

			rows.add(
				new String[] {
					givenNameFiledValue + " " + familyNameFieldValue,
					String.valueOf(
						fieldMap.get(
							"email"
						).get(
							0
						).getValue()),
					channel.getName()
				});
		}

		return rows;
	}

	private MetricType _getMetricType(AssetType assetType) {
		if (assetType == AssetType.BLOG) {
			return PageMetricType.VIEWS;
		}
		else if (assetType == AssetType.DOCUMENT) {
			return DocumentLibraryMetricType.DOWNLOADS;
		}
		else if (assetType == AssetType.FORM) {
			return FormMetricType.VIEWS;
		}
		else if (assetType == AssetType.JOURNAL) {
			return JournalMetricType.VIEWS;
		}
		else if (assetType == AssetType.PAGE) {
			return PageMetricType.VIEWS;
		}

		return null;
	}

	private org.springframework.data.domain.Sort _getSort(String[] sorts) {
		if (ArrayUtils.isEmpty(sorts)) {
			return org.springframework.data.domain.Sort.by(
				org.springframework.data.domain.Sort.Order.asc("id"));
		}

		List<Sort.Order> orders = new ArrayList<>();

		for (int i = 0; i < sorts.length; i++) {
			String sort = sorts[i];

			String orderString = null;

			String[] properties = sort.split(",");

			if (properties.length == 1) {
				orderString = sorts[++i];
			}
			else {
				orderString = properties[1];
			}

			Sort.Order order = null;

			if (Objects.equals(orderString, "asc")) {
				order = Sort.Order.asc(properties[0]);
			}
			else {
				order = Sort.Order.desc(properties[0]);
			}

			if (StringUtils.containsIgnoreCase(properties[0], "date")) {
				orders.add(order);
			}
			else {
				orders.add(order.ignoreCase());
			}
		}

		return Sort.by(orders);
	}

	private static final int _MAX_SIZE = 10000;

	private final Map<AssetType, AssetMetricRepository>
		_assetMetricRepositoryMap = new HashMap<>();
	private final BQIndividualRepository _bqIndividualRepository;
	private final ChannelDog _channelDog;
	private final Map<String, String[]> _columnHeader =
		new HashMap<String, String[]>() {
			{
				put(
					"individual",
					new String[] {"Name", "Email", "Property Name"});
			}
		};

}