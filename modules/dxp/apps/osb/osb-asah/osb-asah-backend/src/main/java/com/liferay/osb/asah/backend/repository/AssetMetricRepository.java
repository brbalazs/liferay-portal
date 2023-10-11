/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository;

import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.AudienceReport;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.Individual;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
public interface AssetMetricRepository<T extends AssetMetric> {

	public List<T> getAppearsOnMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange);

	public T getAssetMetric(
		@Nullable String assetId, @Nullable String assetTitle,
		@Nullable Long channelId, Set<String> selectedMetrics,
		TimeRange timeRange);

	public List<T> getAssetMetrics(
		@Nullable Long channelId, @Nullable String keywords,
		@Nullable String terms, Pageable pageable, Set<String> selectedMetrics,
		TimeRange timeRange);

	public Long getAssetMetricsCount(
		@Nullable Long channelId, @Nullable String keywords,
		@Nullable String terms, TimeRange timeRange);

	public AssetType getAssetType();

	public AudienceReport getAudienceReport(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange);

	public List<Metric> getBrowserMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange);

	public List<Metric> getDeviceMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange);

	public List<Metric> getGeolocationMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange);

	public List<HistogramMetric> getHistogramMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		boolean includePrevious, Interval interval, MetricType metricType,
		TimeRange timeRange);

	public List<Individual> getKnownIndividuals(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, Pageable pageable, @Nullable String keywords,
		TimeRange timeRange);

	public long getKnownIndividualsCount(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, @Nullable String keywords, TimeRange timeRange);

	public List<Metric> getSegmentMetrics(
		String assetId, @Nullable String assetTitle, @Nullable Long channelId,
		MetricType metricType, TimeRange timeRange);

}