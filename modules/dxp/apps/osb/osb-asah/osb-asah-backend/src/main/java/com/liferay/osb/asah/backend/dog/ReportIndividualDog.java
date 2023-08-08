/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.Individual;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.ResultBag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 * @author Marcellus Tavares
 */
@Component
public class ReportIndividualDog {

	@Autowired
	public ReportIndividualDog(
		List<AssetMetricRepository> assetMetricRepositories) {

		assetMetricRepositories.forEach(
			assetMetricAssetMetricRepository -> _assetMetricRepositoryMap.put(
				assetMetricAssetMetricRepository.getAssetType(),
				assetMetricAssetMetricRepository));
	}

	public ResultBag<Individual> getIndividualResultBag(
		String keywords, MetricType metricType,
		SearchQueryContext searchQueryContext, int size, int start) {

		AssetMetricRepository assetMetricRepository =
			_assetMetricRepositoryMap.get(searchQueryContext.getAssetType());

		if (assetMetricRepository == null) {
			throw new IllegalArgumentException(
				"There is no asset metric repository for asset type " +
					searchQueryContext.getAssetType());
		}

		Sort sort = Sort.by(
			Sort.Order.asc("firstName"), Sort.Order.asc("lastName"));

		return new ResultBag<>(
			assetMetricRepository.getKnownIndividuals(
				searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
				searchQueryContext.getChannelIdAsLong(), metricType,
				PageRequest.of(start / size, size, sort), keywords,
				searchQueryContext.getTimeRange()),
			assetMetricRepository.getKnownIndividualsCount(
				searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
				searchQueryContext.getChannelIdAsLong(), metricType, keywords,
				searchQueryContext.getTimeRange()));
	}

	private final Map<AssetType, AssetMetricRepository>
		_assetMetricRepositoryMap = new HashMap<>();

}