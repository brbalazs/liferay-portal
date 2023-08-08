/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.EventHistogramDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
public class EventHistogramDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(
		resourcePath = "test_events_count_histogram_last_24_hours.sql"
	)
	@Test
	public void testTotalEventHistogramMetricsLast24Hours() {
		List<HistogramMetric> histogramMetrics = _getEventsCountHistogram(
			Interval.HOUR, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(
			24, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2, 0, 1,
			0
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@BQSQLResource(
		resourcePath = "test_events_count_histogram_last_30_days.sql"
	)
	@Test
	public void testTotalEventHistogramMetricsLast30Days() {
		List<HistogramMetric> histogramMetrics = _getEventsCountHistogram(
			Interval.DAY, TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			30, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
			0, 0, 0, 0, 0, 1, 0
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@BQSQLResource(
		resourcePath = "test_events_count_histogram_grouped_by_week.sql"
	)
	@Test
	public void testTotalEventHistogramMetricsLast30DaysGroupedByWeek() {
		double[] expectedValues = {1, 2, 1};

		List<HistogramMetric> histogramMetrics = _getEventsCountHistogram(
			Interval.WEEK, TimeRange.LAST_30_DAYS);

		Stream<HistogramMetric> stream = histogramMetrics.stream();

		Assertions.assertArrayEquals(
			expectedValues,
			_getActualValues(
				stream.filter(
					histogramMetric -> histogramMetric.getValue() > 0
				).collect(
					Collectors.toList()
				)),
			0);
	}

	@BQSQLResource(
		resourcePath = "test_events_count_histogram_grouped_by_month.sql"
	)
	@Test
	public void testTotalEventHistogramMetricsLast90DaysGroupedByMonth() {
		double[] expectedValues = {1, 3};

		List<HistogramMetric> histogramMetrics = _getEventsCountHistogram(
			Interval.MONTH, TimeRange.LAST_90_DAYS);

		Stream<HistogramMetric> stream = histogramMetrics.stream();

		Assertions.assertArrayEquals(
			expectedValues,
			_getActualValues(
				stream.filter(
					histogramMetric -> histogramMetric.getValue() > 0
				).collect(
					Collectors.toList()
				)),
			0);
	}

	@BQSQLResource(
		resourcePath = "test_event_sessions_count_histogram_last_24_hours.sql"
	)
	@Test
	public void testTotalEventSessionHistogramMetricsLast24Hours() {
		List<HistogramMetric> histogramMetrics = _getSessionsCountHistogram(
			Interval.HOUR, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(
			24, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 2, 0, 1,
			0
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@BQSQLResource(
		resourcePath = "test_event_sessions_count_histogram_last_30_days.sql"
	)
	@Test
	public void testTotalEventSessionHistogramMetricsLast30Days() {
		List<HistogramMetric> histogramMetrics = _getSessionsCountHistogram(
			Interval.DAY, TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(
			30, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
			0, 0, 0, 0, 0, 1, 0
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	private double[] _getActualValues(List<HistogramMetric> histogramMetrics) {
		double[] actualValues = new double[histogramMetrics.size()];

		for (int i = 0; i < histogramMetrics.size(); i++) {
			HistogramMetric histogramMetric = histogramMetrics.get(i);

			actualValues[i] = histogramMetric.getValue();
		}

		return actualValues;
	}

	private List<HistogramMetric> _getEventsCountHistogram(
		Interval interval, TimeRange timeRange) {

		HistogramMetricBag histogramMetricBag =
			_eventHistogramDog.getEventsCountHistogram(
				new SearchQueryContext() {
					{
						setChannelId("1");
						setEntityId("1");
						setInterval(interval.getKey());
						setTimeRange(timeRange);
					}
				});

		return histogramMetricBag.getMetrics();
	}

	private List<HistogramMetric> _getSessionsCountHistogram(
		Interval interval, TimeRange timeRange) {

		HistogramMetricBag histogramMetricBag =
			_eventHistogramDog.getSessionsCountHistogram(
				new SearchQueryContext() {
					{
						setChannelId("1");
						setEntityId("1");
						setInterval(interval.getKey());
						setTimeRange(timeRange);
					}
				});

		return histogramMetricBag.getMetrics();
	}

	@Autowired
	private EventHistogramDog _eventHistogramDog;

}