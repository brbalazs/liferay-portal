/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.VisitorHistogramDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQPageRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author André Miranda
 */
@Disabled
public class VisitorHistogramDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_histogram_page_last_7_days_info.json"
	)
	@Test
	public void testVisitorHistogramMetricsLast7Days() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(
			7, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {0, 0, 1, 0, 0, 0, 1};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_histogram_page_last_24_hours_info.json"
	)
	@Test
	public void testVisitorHistogramMetricsLast24Hours() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(
			24, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 1, 0, 0, 0, 0, 0, 0,
			0
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_histogram_page_last_28_days_info.json"
	)
	@Test
	public void testVisitorHistogramMetricsLast28Days() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(
			28, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = new double[28];

		Arrays.fill(expectedValues, 1);

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_histogram_page_last_90_days_info.json"
	)
	@Test
	public void testVisitorHistogramMetricsLast90Days() {
		double[] expectedValues = DogTestUtil.create90DaysHistogramBuckets();

		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.WEEK, TimeRange.LAST_90_DAYS);

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);

		HistogramMetric histogramMetric = histogramMetrics.get(0);

		LocalDateTime localDateTime = LocalDateTime.now();

		localDateTime = localDateTime.minusDays(179);

		if (localDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
			histogramMetric = histogramMetrics.get(1);
		}

		Assertions.assertEquals(1D, histogramMetric.getPreviousValue(), 0);
	}

	private double[] _getActualValues(List<HistogramMetric> histogramMetrics) {
		double[] actualValues = new double[histogramMetrics.size()];

		for (int i = 0; i < histogramMetrics.size(); i++) {
			HistogramMetric histogramMetric = histogramMetrics.get(i);

			actualValues[i] = histogramMetric.getValue();
		}

		return actualValues;
	}

	private List<HistogramMetric> _getHistogramMetrics(
		Interval interval, TimeRange timeRange) {

		HistogramMetricBag histogramMetricBag =
			_visitorHistogramDog.getHistogramMetricBag(
				false, PageMetricType.VISITORS,
				new SearchQueryContext(AssetType.PAGE) {
					{
						setInterval(interval.getKey());
						setTimeRange(timeRange);
					}
				});

		return histogramMetricBag.getMetrics();
	}

	@Autowired
	private VisitorHistogramDog _visitorHistogramDog;

}