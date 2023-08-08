/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.MetricHelper;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.EventMetricType;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQEventRepository;

import java.time.Clock;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Component
public class EventHistogramDog {

	public HistogramMetricBag getEventsCountHistogram(
		SearchQueryContext searchQueryContext) {

		TimeRange timeRange = searchQueryContext.getTimeRange();

		return _createHistogramBag(
			EventMetricType.TOTAL_EVENTS, searchQueryContext,
			_bqEventRepository.getBQEventsCountGroupByEventDate(
				searchQueryContext.getChannelIdAsLong(),
				searchQueryContext.getEntityId(),
				_getInterval(searchQueryContext),
				searchQueryContext.getKeywords(),
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId()));
	}

	public HistogramMetricBag getSessionsCountHistogram(
		SearchQueryContext searchQueryContext) {

		TimeRange timeRange = searchQueryContext.getTimeRange();

		return _createHistogramBag(
			EventMetricType.TOTAL_SESSIONS, searchQueryContext,
			_bqEventRepository.getEventSessionsCountGroupByEventDate(
				searchQueryContext.getChannelIdAsLong(),
				searchQueryContext.getEntityId(),
				_getInterval(searchQueryContext),
				searchQueryContext.getKeywords(),
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId()));
	}

	private HistogramMetricBag _createHistogramBag(
		MetricType metricType, SearchQueryContext searchQueryContext,
		Map<String, Integer> histogramMetricValues) {

		HistogramMetricBag histogramMetricBag =
			_metricHelper.createHistogramMetricBag(
				Clock.system(_timeZoneDog.getZoneId()),
				searchQueryContext.isIncludePrevious(),
				searchQueryContext.getInterval(), metricType,
				searchQueryContext.getTimeRange());

		for (HistogramMetric histogramMetric :
				histogramMetricBag.getMetrics()) {

			if (histogramMetricValues.containsKey(histogramMetric.getKey())) {
				histogramMetric.setValue(
					(double)histogramMetricValues.get(
						histogramMetric.getKey()));
			}
		}

		return histogramMetricBag;
	}

	private Interval _getInterval(SearchQueryContext searchQueryContext) {
		TimeRange timeRange = searchQueryContext.getTimeRange();

		if (timeRange.equals(TimeRange.LAST_24_HOURS) ||
			timeRange.equals(TimeRange.YESTERDAY)) {

			return Interval.HOUR;
		}

		return searchQueryContext.getInterval();
	}

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private MetricHelper _metricHelper;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}