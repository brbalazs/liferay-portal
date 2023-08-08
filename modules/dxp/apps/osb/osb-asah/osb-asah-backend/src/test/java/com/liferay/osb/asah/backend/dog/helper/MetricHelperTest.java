/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.helper;

import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

/**
 * @author Marcellus Tavares
 */
public class MetricHelperTest {

	@BeforeEach
	public void setUp() {
		ProjectIdThreadLocal.setProjectId("test");
	}

	@Test
	public void testCreateHistogramMetricBagAsymmetricComparison() {
		Clock clock = _createFixedClock(LocalDateTime.of(2018, 4, 1, 10, 30));

		TimeRange timeRange = TimeRange.of(
			LocalDate.parse("2020-10-09"), LocalDate.parse("2020-10-05"));

		HistogramMetricBag histogramMetricBag =
			_metricHelper.createHistogramMetricBag(
				clock, true, Interval.WEEK, _metricType,
				timeRange.withClock(clock));

		Assertions.assertTrue(histogramMetricBag.isAsymmetricComparison());

		histogramMetricBag = _metricHelper.createHistogramMetricBag(
			clock, true, Interval.MONTH, _metricType,
			timeRange.withClock(clock));

		Assertions.assertTrue(histogramMetricBag.isAsymmetricComparison());

		histogramMetricBag = _metricHelper.createHistogramMetricBag(
			clock, true, Interval.DAY, _metricType, timeRange.withClock(clock));

		Assertions.assertFalse(histogramMetricBag.isAsymmetricComparison());

		histogramMetricBag = _metricHelper.createHistogramMetricBag(
			clock, true, Interval.DAY, _metricType, TimeRange.LAST_24_HOURS);

		Assertions.assertFalse(histogramMetricBag.isAsymmetricComparison());
	}

	@Test
	public void testCreateHistogramMetricBagCustomRange() {
		List<String> expectedKeys = Arrays.asList(
			"2018-12-02T00:00", "2018-12-03T00:00", "2018-12-04T00:00",
			"2018-12-05T00:00", "2018-12-06T00:00", "2018-12-07T00:00",
			"2018-12-08T00:00", "2018-12-09T00:00", "2018-12-10T00:00",
			"2018-12-11T00:00", "2018-12-12T00:00");

		List<String> actualKeys = _createMetricsKeys(
			Interval.DAY, LocalDateTime.of(2018, 12, 14, 10, 30),
			TimeRange.of(
				LocalDate.parse("2018-12-12"), LocalDate.parse("2018-12-02")));

		Assertions.assertEquals(expectedKeys, actualKeys);
	}

	@Test
	public void testCreateHistogramMetricBagExcludePrevious() {
		Clock clock = _createFixedClock(LocalDateTime.of(2018, 4, 1, 10, 30));

		TimeRange timeRange = TimeRange.of(
			LocalDate.parse("2020-11-20"), LocalDate.parse("2020-11-02"));

		HistogramMetricBag histogramMetricBag =
			_metricHelper.createHistogramMetricBag(
				clock, false, Interval.WEEK, _metricType,
				timeRange.withClock(clock));

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		Assertions.assertEquals(
			3, histogramMetrics.size(), histogramMetrics.toString());

		histogramMetricBag = _metricHelper.createHistogramMetricBag(
			clock, false, Interval.MONTH, _metricType,
			timeRange.withClock(clock));

		histogramMetrics = histogramMetricBag.getMetrics();

		Assertions.assertEquals(
			1, histogramMetrics.size(), histogramMetrics.toString());
	}

	@Test
	public void testCreateHistogramMetricBagLast7Days() {
		List<String> expectedKeys = Arrays.asList(
			"2018-03-25T00:00", "2018-03-26T00:00", "2018-03-27T00:00",
			"2018-03-28T00:00", "2018-03-29T00:00", "2018-03-30T00:00",
			"2018-03-31T00:00");

		List<String> actualKeys = _createMetricsKeys(
			Interval.DAY, LocalDateTime.of(2018, 4, 1, 10, 30),
			TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(
			expectedKeys, actualKeys, expectedKeys.toString());
	}

	@Test
	public void testCreateHistogramMetricBagLast24Hours() {
		List<String> expectedKeys = Arrays.asList(
			"2018-03-31T11:00", "2018-03-31T12:00", "2018-03-31T13:00",
			"2018-03-31T14:00", "2018-03-31T15:00", "2018-03-31T16:00",
			"2018-03-31T17:00", "2018-03-31T18:00", "2018-03-31T19:00",
			"2018-03-31T20:00", "2018-03-31T21:00", "2018-03-31T22:00",
			"2018-03-31T23:00", "2018-04-01T00:00", "2018-04-01T01:00",
			"2018-04-01T02:00", "2018-04-01T03:00", "2018-04-01T04:00",
			"2018-04-01T05:00", "2018-04-01T06:00", "2018-04-01T07:00",
			"2018-04-01T08:00", "2018-04-01T09:00", "2018-04-01T10:00");

		List<String> actualKeys = _createMetricsKeys(
			Interval.DAY, LocalDateTime.of(2018, 4, 1, 10, 30),
			TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(
			expectedKeys, actualKeys, expectedKeys.toString());
	}

	@Test
	public void testCreateHistogramMetricBagLast28Days() {
		List<String> expectedKeys = Arrays.asList(
			"2018-03-04T00:00", "2018-03-05T00:00", "2018-03-06T00:00",
			"2018-03-07T00:00", "2018-03-08T00:00", "2018-03-09T00:00",
			"2018-03-10T00:00", "2018-03-11T00:00", "2018-03-12T00:00",
			"2018-03-13T00:00", "2018-03-14T00:00", "2018-03-15T00:00",
			"2018-03-16T00:00", "2018-03-17T00:00", "2018-03-18T00:00",
			"2018-03-19T00:00", "2018-03-20T00:00", "2018-03-21T00:00",
			"2018-03-22T00:00", "2018-03-23T00:00", "2018-03-24T00:00",
			"2018-03-25T00:00", "2018-03-26T00:00", "2018-03-27T00:00",
			"2018-03-28T00:00", "2018-03-29T00:00", "2018-03-30T00:00",
			"2018-03-31T00:00");

		List<String> actualKeys = _createMetricsKeys(
			Interval.DAY, LocalDateTime.of(2018, 4, 1, 10, 30),
			TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(expectedKeys, actualKeys);
	}

	@Test
	public void testCreateHistogramMetricBagLast30Days() {
		List<String> expectedKeys = Arrays.asList(
			"2018-03-02T00:00", "2018-03-03T00:00", "2018-03-04T00:00",
			"2018-03-05T00:00", "2018-03-06T00:00", "2018-03-07T00:00",
			"2018-03-08T00:00", "2018-03-09T00:00", "2018-03-10T00:00",
			"2018-03-11T00:00", "2018-03-12T00:00", "2018-03-13T00:00",
			"2018-03-14T00:00", "2018-03-15T00:00", "2018-03-16T00:00",
			"2018-03-17T00:00", "2018-03-18T00:00", "2018-03-19T00:00",
			"2018-03-20T00:00", "2018-03-21T00:00", "2018-03-22T00:00",
			"2018-03-23T00:00", "2018-03-24T00:00", "2018-03-25T00:00",
			"2018-03-26T00:00", "2018-03-27T00:00", "2018-03-28T00:00",
			"2018-03-29T00:00", "2018-03-30T00:00", "2018-03-31T00:00");

		List<String> actualKeys = _createMetricsKeys(
			Interval.DAY, LocalDateTime.of(2018, 4, 1, 10, 30),
			TimeRange.LAST_30_DAYS);

		Assertions.assertEquals(expectedKeys, actualKeys);
	}

	@Test
	public void testCreateHistogramMetricBagLast90DaysTodayOnSaturday() {
		List<String> expectedKeys = Arrays.asList(
			"2018-08-26T00:00", "2018-09-02T00:00", "2018-09-09T00:00",
			"2018-09-16T00:00", "2018-09-23T00:00", "2018-09-30T00:00",
			"2018-10-07T00:00", "2018-10-14T00:00", "2018-10-21T00:00",
			"2018-10-28T00:00", "2018-11-04T00:00", "2018-11-11T00:00",
			"2018-11-18T00:00");

		Map<String, Metric> metrics = _createMetrics(
			Interval.WEEK, LocalDateTime.of(2018, 11, 24, 10, 30),
			TimeRange.LAST_90_DAYS);

		Assertions.assertEquals(
			expectedKeys, new ArrayList<>(metrics.keySet()));

		List<String> expectedPreviousValueKeys = Arrays.asList(
			"2018-05-27/2018-06-02", "2018-06-03/2018-06-09",
			"2018-06-10/2018-06-16", "2018-06-17/2018-06-23",
			"2018-06-24/2018-06-30", "2018-07-01/2018-07-07",
			"2018-07-08/2018-07-14", "2018-07-15/2018-07-21",
			"2018-07-22/2018-07-28", "2018-07-29/2018-08-04",
			"2018-08-05/2018-08-11", "2018-08-12/2018-08-18",
			"2018-08-19/2018-08-25");

		List<String> actualPreviousValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getPreviousValueKey);

		Assertions.assertEquals(
			expectedPreviousValueKeys, actualPreviousValueKeys);

		List<String> expectedValueKeys = Arrays.asList(
			"2018-08-26/2018-09-01", "2018-09-02/2018-09-08",
			"2018-09-09/2018-09-15", "2018-09-16/2018-09-22",
			"2018-09-23/2018-09-29", "2018-09-30/2018-10-06",
			"2018-10-07/2018-10-13", "2018-10-14/2018-10-20",
			"2018-10-21/2018-10-27", "2018-10-28/2018-11-03",
			"2018-11-04/2018-11-10", "2018-11-11/2018-11-17",
			"2018-11-18/2018-11-24");

		List<String> actualValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getValueKey);

		Assertions.assertEquals(expectedValueKeys, actualValueKeys);
	}

	@Test
	public void testCreateHistogramMetricBagLast90DaysTodayOnSundayOrWeekday1() {
		List<String> expectedKeys = Arrays.asList(
			"2018-08-26T00:00", "2018-09-02T00:00", "2018-09-09T00:00",
			"2018-09-16T00:00", "2018-09-23T00:00", "2018-09-30T00:00",
			"2018-10-07T00:00", "2018-10-14T00:00", "2018-10-21T00:00",
			"2018-10-28T00:00", "2018-11-04T00:00", "2018-11-11T00:00",
			"2018-11-18T00:00", "2018-11-25T00:00");

		Map<String, Metric> metrics = _createMetrics(
			Interval.WEEK, LocalDateTime.of(2018, 11, 29, 10, 30),
			TimeRange.LAST_90_DAYS);

		Assertions.assertEquals(
			expectedKeys, new ArrayList<>(metrics.keySet()));

		List<String> expectedPreviousValueKeys = Arrays.asList(
			"2018-05-27/2018-06-02", "2018-06-03/2018-06-09",
			"2018-06-10/2018-06-16", "2018-06-17/2018-06-23",
			"2018-06-24/2018-06-30", "2018-07-01/2018-07-07",
			"2018-07-08/2018-07-14", "2018-07-15/2018-07-21",
			"2018-07-22/2018-07-28", "2018-07-29/2018-08-04",
			"2018-08-05/2018-08-11", "2018-08-12/2018-08-18",
			"2018-08-19/2018-08-25", "2018-08-26/2018-09-01");

		List<String> actualPreviousValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getPreviousValueKey);

		Assertions.assertEquals(
			expectedPreviousValueKeys, actualPreviousValueKeys);

		List<String> expectedValueKeys = Arrays.asList(
			"2018-08-26/2018-09-01", "2018-09-02/2018-09-08",
			"2018-09-09/2018-09-15", "2018-09-16/2018-09-22",
			"2018-09-23/2018-09-29", "2018-09-30/2018-10-06",
			"2018-10-07/2018-10-13", "2018-10-14/2018-10-20",
			"2018-10-21/2018-10-27", "2018-10-28/2018-11-03",
			"2018-11-04/2018-11-10", "2018-11-11/2018-11-17",
			"2018-11-18/2018-11-24", "2018-11-25/2018-12-01");

		List<String> actualValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getValueKey);

		Assertions.assertEquals(expectedValueKeys, actualValueKeys);
	}

	@Test
	public void testCreateHistogramMetricBagLast90DaysTodayOnSundayOrWeekday2() {
		List<String> expectedKeys = Arrays.asList(
			"2018-09-09T00:00", "2018-09-16T00:00", "2018-09-23T00:00",
			"2018-09-30T00:00", "2018-10-07T00:00", "2018-10-14T00:00",
			"2018-10-21T00:00", "2018-10-28T00:00", "2018-11-04T00:00",
			"2018-11-11T00:00", "2018-11-18T00:00", "2018-11-25T00:00",
			"2018-12-02T00:00", "2018-12-09T00:00");

		Map<String, Metric> metrics = _createMetrics(
			Interval.WEEK, LocalDateTime.of(2018, 12, 14, 10, 30),
			TimeRange.LAST_90_DAYS);

		Assertions.assertEquals(
			expectedKeys, new ArrayList<>(metrics.keySet()));

		List<String> expectedPreviousValueKeys = Arrays.asList(
			"2018-06-10/2018-06-16", "2018-06-17/2018-06-23",
			"2018-06-24/2018-06-30", "2018-07-01/2018-07-07",
			"2018-07-08/2018-07-14", "2018-07-15/2018-07-21",
			"2018-07-22/2018-07-28", "2018-07-29/2018-08-04",
			"2018-08-05/2018-08-11", "2018-08-12/2018-08-18",
			"2018-08-19/2018-08-25", "2018-08-26/2018-09-01",
			"2018-09-02/2018-09-08", "2018-09-09/2018-09-15");

		List<String> actualPreviousValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getPreviousValueKey);

		Assertions.assertEquals(
			expectedPreviousValueKeys, actualPreviousValueKeys);

		List<String> expectedValueKeys = Arrays.asList(
			"2018-09-09/2018-09-15", "2018-09-16/2018-09-22",
			"2018-09-23/2018-09-29", "2018-09-30/2018-10-06",
			"2018-10-07/2018-10-13", "2018-10-14/2018-10-20",
			"2018-10-21/2018-10-27", "2018-10-28/2018-11-03",
			"2018-11-04/2018-11-10", "2018-11-11/2018-11-17",
			"2018-11-18/2018-11-24", "2018-11-25/2018-12-01",
			"2018-12-02/2018-12-08", "2018-12-09/2018-12-15");

		List<String> actualValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getValueKey);

		Assertions.assertEquals(expectedValueKeys, actualValueKeys);
	}

	@Test
	public void testCreateHistogramMetricBagLast180Days() {
		List<String> expectedKeys = Arrays.asList(
			"2018-06-17T00:00", "2018-06-24T00:00", "2018-07-01T00:00",
			"2018-07-08T00:00", "2018-07-15T00:00", "2018-07-22T00:00",
			"2018-07-29T00:00", "2018-08-05T00:00", "2018-08-12T00:00",
			"2018-08-19T00:00", "2018-08-26T00:00", "2018-09-02T00:00",
			"2018-09-09T00:00", "2018-09-16T00:00", "2018-09-23T00:00",
			"2018-09-30T00:00", "2018-10-07T00:00", "2018-10-14T00:00",
			"2018-10-21T00:00", "2018-10-28T00:00", "2018-11-04T00:00",
			"2018-11-11T00:00", "2018-11-18T00:00", "2018-11-25T00:00",
			"2018-12-02T00:00", "2018-12-09T00:00");

		Map<String, Metric> metrics = _createMetrics(
			Interval.WEEK, LocalDateTime.of(2018, 12, 14, 10, 30),
			TimeRange.LAST_180_DAYS);

		Assertions.assertEquals(
			expectedKeys, new ArrayList<>(metrics.keySet()));

		List<String> expectedPreviousValueKeys = Arrays.asList(
			"2017-12-17/2017-12-23", "2017-12-24/2017-12-30",
			"2017-12-31/2018-01-06", "2018-01-07/2018-01-13",
			"2018-01-14/2018-01-20", "2018-01-21/2018-01-27",
			"2018-01-28/2018-02-03", "2018-02-04/2018-02-10",
			"2018-02-11/2018-02-17", "2018-02-18/2018-02-24",
			"2018-02-25/2018-03-03", "2018-03-04/2018-03-10",
			"2018-03-11/2018-03-17", "2018-03-18/2018-03-24",
			"2018-03-25/2018-03-31", "2018-04-01/2018-04-07",
			"2018-04-08/2018-04-14", "2018-04-15/2018-04-21",
			"2018-04-22/2018-04-28", "2018-04-29/2018-05-05",
			"2018-05-06/2018-05-12", "2018-05-13/2018-05-19",
			"2018-05-20/2018-05-26", "2018-05-27/2018-06-02",
			"2018-06-03/2018-06-09", "2018-06-10/2018-06-16");

		List<String> actualPreviousValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getPreviousValueKey);

		Assertions.assertEquals(
			expectedPreviousValueKeys, actualPreviousValueKeys);

		List<String> expectedValueKeys = Arrays.asList(
			"2018-06-17/2018-06-23", "2018-06-24/2018-06-30",
			"2018-07-01/2018-07-07", "2018-07-08/2018-07-14",
			"2018-07-15/2018-07-21", "2018-07-22/2018-07-28",
			"2018-07-29/2018-08-04", "2018-08-05/2018-08-11",
			"2018-08-12/2018-08-18", "2018-08-19/2018-08-25",
			"2018-08-26/2018-09-01", "2018-09-02/2018-09-08",
			"2018-09-09/2018-09-15", "2018-09-16/2018-09-22",
			"2018-09-23/2018-09-29", "2018-09-30/2018-10-06",
			"2018-10-07/2018-10-13", "2018-10-14/2018-10-20",
			"2018-10-21/2018-10-27", "2018-10-28/2018-11-03",
			"2018-11-04/2018-11-10", "2018-11-11/2018-11-17",
			"2018-11-18/2018-11-24", "2018-11-25/2018-12-01",
			"2018-12-02/2018-12-08", "2018-12-09/2018-12-15");

		List<String> actualValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getValueKey);

		Assertions.assertEquals(expectedValueKeys, actualValueKeys);
	}

	@Test
	public void testCreateHistogramMetricBagLastYear() {
		List<String> expectedKeys = Arrays.asList(
			"2017-12-01T00:00", "2018-01-01T00:00", "2018-02-01T00:00",
			"2018-03-01T00:00", "2018-04-01T00:00", "2018-05-01T00:00",
			"2018-06-01T00:00", "2018-07-01T00:00", "2018-08-01T00:00",
			"2018-09-01T00:00", "2018-10-01T00:00", "2018-11-01T00:00",
			"2018-12-01T00:00");

		Map<String, Metric> metrics = _createMetrics(
			Interval.MONTH, LocalDateTime.of(2018, 12, 14, 10, 30),
			TimeRange.LAST_YEAR);

		Assertions.assertEquals(
			expectedKeys, new ArrayList<>(metrics.keySet()));

		List<String> expectedPreviousValueKeys = Arrays.asList(
			"2016-12-01/2016-12-31", "2017-01-01/2017-01-31",
			"2017-02-01/2017-02-28", "2017-03-01/2017-03-31",
			"2017-04-01/2017-04-30", "2017-05-01/2017-05-31",
			"2017-06-01/2017-06-30", "2017-07-01/2017-07-31",
			"2017-08-01/2017-08-31", "2017-09-01/2017-09-30",
			"2017-10-01/2017-10-31", "2017-11-01/2017-11-30",
			"2017-12-01/2017-12-31");

		List<String> actualPreviousValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getPreviousValueKey);

		Assertions.assertEquals(
			expectedPreviousValueKeys, actualPreviousValueKeys);

		List<String> expectedValueKeys = Arrays.asList(
			"2017-12-01/2017-12-31", "2018-01-01/2018-01-31",
			"2018-02-01/2018-02-28", "2018-03-01/2018-03-31",
			"2018-04-01/2018-04-30", "2018-05-01/2018-05-31",
			"2018-06-01/2018-06-30", "2018-07-01/2018-07-31",
			"2018-08-01/2018-08-31", "2018-09-01/2018-09-30",
			"2018-10-01/2018-10-31", "2018-11-01/2018-11-30",
			"2018-12-01/2018-12-31");

		List<String> actualValueKeys = _getMetricValueKeys(
			metrics.values(), Metric::getValueKey);

		Assertions.assertEquals(expectedValueKeys, actualValueKeys);
	}

	@Test
	public void testCreateHistogramMetricBagYesterday() {
		List<String> expectedKeys = Arrays.asList(
			"2018-03-31T00:00", "2018-03-31T01:00", "2018-03-31T02:00",
			"2018-03-31T03:00", "2018-03-31T04:00", "2018-03-31T05:00",
			"2018-03-31T06:00", "2018-03-31T07:00", "2018-03-31T08:00",
			"2018-03-31T09:00", "2018-03-31T10:00", "2018-03-31T11:00",
			"2018-03-31T12:00", "2018-03-31T13:00", "2018-03-31T14:00",
			"2018-03-31T15:00", "2018-03-31T16:00", "2018-03-31T17:00",
			"2018-03-31T18:00", "2018-03-31T19:00", "2018-03-31T20:00",
			"2018-03-31T21:00", "2018-03-31T22:00", "2018-03-31T23:00");

		List<String> actualKeys = _createMetricsKeys(
			Interval.DAY, LocalDateTime.of(2018, 4, 1, 10, 30),
			TimeRange.YESTERDAY);

		Assertions.assertEquals(expectedKeys, actualKeys);
	}

	private Clock _createFixedClock(LocalDateTime localDateTime) {
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);

		return Clock.fixed(zonedDateTime.toInstant(), ZoneOffset.UTC);
	}

	private Map<String, Metric> _createMetrics(
		Interval interval, LocalDateTime localDateTime, TimeRange timeRange) {

		Clock clock = _createFixedClock(localDateTime);

		HistogramMetricBag histogramMetricBag =
			_metricHelper.createHistogramMetricBag(
				clock, true, interval, _metricType, timeRange.withClock(clock));

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		Stream<HistogramMetric> stream = histogramMetrics.stream();

		return stream.collect(
			Collectors.toMap(
				HistogramMetric::getKey, Function.identity(),
				(oldMetric, newMetric) -> {
					throw new RuntimeException(
						"Created histogram metrics with duplicate keys");
				},
				TreeMap::new));
	}

	private List<String> _createMetricsKeys(
		Interval interval, LocalDateTime localDateTime, TimeRange timeRange) {

		Map<String, Metric> actualMetrics = _createMetrics(
			interval, localDateTime, timeRange);

		return new ArrayList<>(actualMetrics.keySet());
	}

	private List<String> _getMetricValueKeys(
		Collection<Metric> metrics,
		Function<Metric, String> valueKeyMapperFunction) {

		Stream<Metric> stream = metrics.stream();

		return stream.map(
			valueKeyMapperFunction
		).collect(
			Collectors.toList()
		);
	}

	private final MetricHelper _metricHelper = new MetricHelper();

	@Mock
	private MetricType _metricType;

}