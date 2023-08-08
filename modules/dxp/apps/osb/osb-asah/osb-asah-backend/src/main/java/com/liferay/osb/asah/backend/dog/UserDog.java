/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.AudienceReport;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.model.MetricType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component("com.liferay.osb.asah.backend.dog.UserDog")
public class UserDog {

	@Autowired
	public UserDog(List<AssetMetricRepository> assetMetricRepositories) {
		assetMetricRepositories.forEach(
			assetMetricAssetMetricRepository -> _assetMetricRepositoryMap.put(
				assetMetricAssetMetricRepository.getAssetType(),
				assetMetricAssetMetricRepository));
	}

	public Long getAnonymousUsersCount(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		AudienceReport audienceReport = assetMetricRepository.getAudienceReport(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());

		return audienceReport.getAnonymousIndividualsCount();
	}

	public AudienceReport getAudienceReport(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		return assetMetricRepository.getAudienceReport(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());
	}

	public Long getKnownUsersCount(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		AudienceReport audienceReport = assetMetricRepository.getAudienceReport(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());

		return audienceReport.getKnownIndividualsCount();
	}

	public Long getNonsegmentedIndividualsCount(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		AudienceReport audienceReport = assetMetricRepository.getAudienceReport(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());

		return audienceReport.getNonsegmentedIndividualsCount();
	}

	public Long getSegmentedIndividualsCount(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		AssetMetricRepository assetMetricRepository = _getAssetMetricRepository(
			searchQueryContext.getAssetType());

		AudienceReport audienceReport = assetMetricRepository.getAudienceReport(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(), metricType,
			searchQueryContext.getTimeRange());

		return audienceReport.getSegmentedIndividualsCount();
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

	private final Map<AssetType, AssetMetricRepository>
		_assetMetricRepositoryMap = new HashMap<>();

}