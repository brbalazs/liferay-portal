/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 * @author André Miranda
 */
@Component
public class MetricDog {

	@Autowired
	public MetricDog(List<AssetMetricRepository> assetMetricRepositories) {
		assetMetricRepositories.forEach(
			assetMetricAssetMetricRepository -> _assetMetricRepositoryMap.put(
				assetMetricAssetMetricRepository.getAssetType(),
				assetMetricAssetMetricRepository));
	}

	public <T extends AssetMetric> List<T> getAppearsOnMetrics(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository =
			(AssetMetricRepository<T>)_assetMetricRepositoryMap.get(
				searchQueryContext.getAssetType());

		return assetMetricRepository.getAppearsOnMetrics(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());
	}

	public <T extends AssetMetric> T getAssetMetric(
		SearchQueryContext searchQueryContext) {

		return getAssetMetric(searchQueryContext, Collections.emptySet());
	}

	public <T extends AssetMetric> T getAssetMetric(
		SearchQueryContext searchQueryContext, Set<String> selectedMetrics) {

		AssetMetricRepository<T> assetMetricRepository =
			(AssetMetricRepository<T>)_getAssetMetricRepository(
				searchQueryContext.getAssetType());

		String assetTitle = null;

		if (searchQueryContext.getAssetType() != AssetType.CUSTOM) {
			assetTitle = searchQueryContext.getTitle();
		}

		return assetMetricRepository.getAssetMetric(
			searchQueryContext.getAssetId(), assetTitle,
			searchQueryContext.getChannelIdAsLong(), selectedMetrics,
			searchQueryContext.getTimeRange());
	}

	public <T extends AssetMetric> List<T> getAssetMetrics(
		int page, SearchQueryContext searchQueryContext,
		Set<String> selectedMetrics, int size, Sort sort) {

		return _getAssetMetrics(
			page, searchQueryContext, selectedMetrics, size, sort);
	}

	public long getAssetMetricsCount(SearchQueryContext searchQueryContext) {
		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		return assetMetricRepository.getAssetMetricsCount(
			searchQueryContext.getChannelIdAsLong(),
			searchQueryContext.getKeywords(), searchQueryContext.getTerms(),
			searchQueryContext.getTimeRange());
	}

	public List<Metric> getBrowserMetrics(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		return assetMetricRepository.getBrowserMetrics(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());
	}

	public List<Metric> getDeviceMetrics(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		return assetMetricRepository.getDeviceMetrics(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());
	}

	public List<Metric> getGeolocationMetrics(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		return assetMetricRepository.getGeolocationMetrics(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());
	}

	private AssetMetricRepository _getAssetMetricRepository(
		AssetType assetType) {

		AssetMetricRepository assetMetricRepository =
			_assetMetricRepositoryMap.get(assetType);

		if (assetMetricRepository == null) {
			throw new IllegalArgumentException(
				"There is no asset metric repository for asset type " +
					assetType);
		}

		return assetMetricRepository;
	}

	private <T extends AssetMetric> List<T> _getAssetMetrics(
		int page, SearchQueryContext searchQueryContext,
		Set<String> selectedMetrics, int size, Sort sort) {

		AssetMetricRepository<T> assetMetricRepository =
			(AssetMetricRepository<T>)_getAssetMetricRepository(
				searchQueryContext.getAssetType());

		return assetMetricRepository.getAssetMetrics(
			searchQueryContext.getChannelIdAsLong(),
			searchQueryContext.getKeywords(), searchQueryContext.getTerms(),
			PageRequest.of(page, size, sort), selectedMetrics,
			searchQueryContext.getTimeRange());
	}

	private final Map<AssetType, AssetMetricRepository>
		_assetMetricRepositoryMap = new HashMap<>();

}