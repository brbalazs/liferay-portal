/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.backend.repository.PageAssetMetricRepository;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.dog.SuppressionDog;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TimeRange;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
public class PageDog {

	public long getIndirectAccessMetricValue(
		SearchQueryContext searchQueryContext) {

		PageMetric pageMetric = _pageAssetMetricRepository.getAssetMetric(
			searchQueryContext.getAssetId(), searchQueryContext.getTitle(),
			searchQueryContext.getChannelIdAsLong(),
			Collections.singleton(PageMetricType.INDIRECT_ACCESS.getName()),
			searchQueryContext.getTimeRange());

		return _getLongValue(pageMetric.getIndirectAccessMetric());
	}

	public long getReadsMetricValue(@Nullable String canonicalUrl) {
		PageMetric pageMetric = _getPageMetric(
			canonicalUrl, PageMetricType.READS);

		return _getLongValue(pageMetric.getReadsMetric());
	}

	public long getReadsMetricValue(
		@Nullable String canonicalUrl, LocalDate fromLocalDate,
		LocalDate toLocalDate) {

		PageMetric pageMetric = _getPageMetric(
			canonicalUrl, fromLocalDate, PageMetricType.READS, toLocalDate);

		return _getLongValue(pageMetric.getReadsMetric());
	}

	public long getViewsMetricValue(@Nullable String canonicalUrl) {
		PageMetric pageMetric = _getPageMetric(
			canonicalUrl, PageMetricType.VIEWS);

		return _getLongValue(pageMetric.getViewsMetric());
	}

	public long getViewsMetricValue(
		@Nullable String canonicalUrl, LocalDate fromLocalDate,
		LocalDate toLocalDate) {

		PageMetric pageMetric = _getPageMetric(
			canonicalUrl, fromLocalDate, PageMetricType.VIEWS, toLocalDate);

		return _getLongValue(pageMetric.getViewsMetric());
	}

	public long getViewsMetricValue(
		@Nullable String canonicalUrl, LocalDateTime fromLocalDateTime,
		LocalDateTime toLocalDateTime) {

		PageMetric pageMetric = _getPageMetric(
			canonicalUrl, fromLocalDateTime, PageMetricType.VIEWS,
			toLocalDateTime);

		return _getLongValue(pageMetric.getViewsMetric());
	}

	private long _getLongValue(Metric metric) {
		Double value = metric.getValue();

		return value.longValue();
	}

	private PageMetric _getPageMetric(
		@Nullable String canonicalUrl, LocalDate fromLocalDate,
		PageMetricType pageMetricType, LocalDate toLocalDate) {

		return _getPageMetric(
			canonicalUrl, pageMetricType,
			TimeRange.of(toLocalDate, fromLocalDate));
	}

	private PageMetric _getPageMetric(
		@Nullable String canonicalUrl, LocalDateTime fromLocalDateTime,
		PageMetricType pageMetricType, LocalDateTime toLocalDateTime) {

		return _getPageMetric(
			canonicalUrl, pageMetricType,
			TimeRange.of(toLocalDateTime, fromLocalDateTime));
	}

	private PageMetric _getPageMetric(
		@Nullable String canonicalUrl, PageMetricType pageMetricType) {

		LocalDate toLocalDate = LocalDate.now(_timeZoneDog.getZoneId());

		LocalDate fromLocalDate = toLocalDate.minusMonths(13);

		return _getPageMetric(
			canonicalUrl, pageMetricType,
			TimeRange.of(toLocalDate, fromLocalDate));
	}

	private PageMetric _getPageMetric(
		@Nullable String canonicalUrl, PageMetricType pageMetricType,
		TimeRange timeRange) {

		return _pageAssetMetricRepository.getAssetMetric(
			canonicalUrl, null, null,
			Collections.singleton(pageMetricType.getName()), timeRange);
	}

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Autowired
	private PageAssetMetricRepository _pageAssetMetricRepository;

	@Autowired
	private SuppressionDog _suppressionDog;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}