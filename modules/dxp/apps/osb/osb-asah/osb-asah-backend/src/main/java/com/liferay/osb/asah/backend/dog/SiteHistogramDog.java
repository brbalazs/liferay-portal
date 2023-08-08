/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.MetricHelper;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.SiteMetricType;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.SiteVisitorBehaviorMetric;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQSessionRepository;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class SiteHistogramDog {

	public HistogramMetricBag getHistogramMetricBag(
		SearchQueryContext searchQueryContext, SiteMetricType siteMetricType) {

		TimeRange timeRange = searchQueryContext.getTimeRange();
		ZoneId zoneId = _timeZoneDog.getZoneId();

		List<SiteVisitorBehaviorMetric> siteVisitorBehaviorMetrics =
			_bqSessionRepository.
				getSiteVisitorBehaviorMetricsGroupedBySessionStart(
					searchQueryContext.getChannelIdAsLong(),
					searchQueryContext.isIncludePrevious(),
					searchQueryContext.getInterval(), timeRange, zoneId);

		if (siteVisitorBehaviorMetrics.isEmpty()) {
			return new HistogramMetricBag();
		}

		Stream<SiteVisitorBehaviorMetric> stream =
			siteVisitorBehaviorMetrics.stream();

		Map<String, SiteVisitorBehaviorMetric> siteVisitorBehaviorMetricsMap =
			stream.collect(
				Collectors.toMap(
					siteVisitorBehaviorMetric -> String.valueOf(
						DateUtil.toLocalDateTime(
							siteVisitorBehaviorMetric.getEventDate(),
							ZoneOffset.UTC)),
					Function.identity()));

		HistogramMetricBag histogramMetricBag =
			_metricHelper.createHistogramMetricBag(
				Clock.system(zoneId), searchQueryContext.isIncludePrevious(),
				searchQueryContext.getInterval(), siteMetricType, timeRange);

		Map<String, Metric> metrics = _getMetrics(histogramMetricBag);

		for (Map.Entry<String, Metric> entry : metrics.entrySet()) {
			Metric metric = entry.getValue();

			String dateString = entry.getKey();

			metric.setValue(
				_getMetricValue(
					siteMetricType,
					siteVisitorBehaviorMetricsMap.get(dateString)));

			if (searchQueryContext.isIncludePrevious()) {
				metric.setPreviousValue(
					_getMetricValue(
						siteMetricType,
						siteVisitorBehaviorMetricsMap.get(
							_getPreviousDateString(dateString, timeRange))));
			}
		}

		return histogramMetricBag;
	}

	private Map<String, Metric> _getMetrics(
		HistogramMetricBag histogramMetricBag) {

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		Stream<HistogramMetric> histogramMetricsStream =
			histogramMetrics.stream();

		return histogramMetricsStream.collect(
			Collectors.toMap(HistogramMetric::getKey, Function.identity()));
	}

	private Double _getMetricValue(
		SiteMetricType siteMetricType,
		SiteVisitorBehaviorMetric siteVisitorBehaviorMetric) {

		if (siteVisitorBehaviorMetric == null) {
			return 0.0;
		}

		if (siteMetricType == SiteMetricType.ANONYMOUS_VISITORS) {
			return Double.parseDouble(
				String.valueOf(
					siteVisitorBehaviorMetric.getAnonymousVisitors()));
		}

		if (siteMetricType == SiteMetricType.BOUNCE_RATE) {
			return siteVisitorBehaviorMetric.getBounceRate();
		}

		if (siteMetricType == SiteMetricType.KNOWN_VISITORS) {
			return Double.parseDouble(
				String.valueOf(siteVisitorBehaviorMetric.getKnownVisitors()));
		}

		if (siteMetricType == SiteMetricType.SESSION_DURATION) {
			return Double.parseDouble(
				String.valueOf(
					siteVisitorBehaviorMetric.getAverageSessionDuration()));
		}

		if (siteMetricType == SiteMetricType.SESSIONS_PER_VISITOR) {
			return Double.parseDouble(
				String.valueOf(
					siteVisitorBehaviorMetric.getSessionsPerVisitor()));
		}

		if (siteMetricType == SiteMetricType.VISITORS) {
			return Double.parseDouble(
				String.valueOf(siteVisitorBehaviorMetric.getVisitors()));
		}

		return 0.0;
	}

	private String _getPreviousDateString(
		String dateString, TimeRange timeRange) {

		return String.valueOf(
			DateUtil.toLocalDateTime(
				DateUtil.toUTCDate(
					DateUtil.addDays(dateString, -timeRange.getDeltaDays())),
				ZoneId.of("UTC")));
	}

	@Autowired
	private BQSessionRepository _bqSessionRepository;

	@Autowired
	private MetricHelper _metricHelper;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}