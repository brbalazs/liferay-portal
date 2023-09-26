/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository;

import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TimeRange;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.lang.Nullable;

/**
 * @author Marcellus Tavares
 */
public interface PageAssetMetricRepository
	extends AssetMetricRepository<PageMetric> {

	public List<HistogramMetric> getExperimentHistogramMetrics(
		Long experimentId, PageMetricType pageMetricType, TimeRange timeRange,
		@Nullable String variantId);

	public Optional<PageMetric> getExperimentPageMetric(
		Long experimentId, Set<PageMetricType> pageMetricTypes,
		TimeRange timeRange, String variantId);

	public Optional<PageMetric> getExperimentPageMetric(
		String canonicalUrl, Set<PageMetricType> pageMetricTypes,
		TimeRange timeRange);

	public Long getUniqueSessionsCount(Long experimentId, TimeRange timeRange);

	public Long getVariantUniqueVisitors(
		Long experimentId, TimeRange timeRange, String variantId);

}