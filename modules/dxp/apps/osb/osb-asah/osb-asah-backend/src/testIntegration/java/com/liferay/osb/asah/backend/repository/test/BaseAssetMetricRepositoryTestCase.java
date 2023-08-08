/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.date.dog.util.TimeZoneDogUtil;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.model.Tuple2;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahBQSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestExecutionListeners;

/**
 * @author Marcellus Tavares
 */
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
	value = {
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahBQSQLTestExecutionListener.class
	}
)
public abstract class BaseAssetMetricRepositoryTestCase<T extends AssetMetric>
	implements OSBAsahBackendSpringTestContext {

	@BeforeEach
	public void setUp() {
		Mockito.when(
			_timeZoneDog.getTimeZoneId()
		).thenReturn(
			"UTC"
		);

		Mockito.when(
			_timeZoneDog.getZoneId()
		).thenReturn(
			ZoneOffset.UTC
		);

		TimeZoneDogUtil.setTimeZoneDog(_timeZoneDog);
	}

	@AfterEach
	public void tearDown() {
		TimeZoneDogUtil.setTimeZoneDog(null);
	}

	protected void assertAppearsOnMetric(
		MetricType metricType, TimeRange timeRange) {

		AssetMetricRepository<T> assetMetricRepository =
			getAssetMetricRepository();

		List<T> appearsOnMetrics = assetMetricRepository.getAppearsOnMetrics(
			"e131fabc", null, 1L, metricType, timeRange);

		Stream<T> stream = appearsOnMetrics.stream();

		MatcherAssert.assertThat(
			new String[] {
				"https://www.beryl.com/products/commercial/irrigation",
				"https://www.beryl.com/delivery",
				"https://www.beryl.com/products/commercial/irrigation/FF-2100"
			},
			Matchers.arrayContainingInAnyOrder(
				stream.map(
					AssetMetric::getAssetId
				).toArray()));
	}

	protected void assertAssetMetrics(
		Double[] expectedMetricValues, List<T> metrics,
		Function<T, Metric> mapperFunction) {

		Assertions.assertEquals(
			expectedMetricValues.length, metrics.size(), metrics.toString());

		Stream<T> stream = metrics.stream();

		MatcherAssert.assertThat(
			expectedMetricValues,
			Matchers.arrayContainingInAnyOrder(
				stream.map(
					mapperFunction
				).map(
					Metric::getValue
				).toArray()));
	}

	protected void assertDeviceMetricsOrdering(List<Metric> deviceMetrics) {
		double previousDeviceCount = -1;
		String previousDeviceType = null;

		for (Metric deviceMetric : deviceMetrics) {
			Double deviceCount = deviceMetric.getValue();
			String deviceType = deviceMetric.getValueKey();

			if (previousDeviceType != null) {
				Assertions.assertTrue(deviceCount <= previousDeviceCount);

				if (deviceCount == previousDeviceCount) {
					Assertions.assertTrue(
						deviceType.compareTo(previousDeviceType) >= 0);
				}
			}

			double previousPlatformCount = -1;
			String previousPlatformName = null;

			for (Metric platformMetric : deviceMetric.getMetrics()) {
				Double platformCount = platformMetric.getValue();

				String platformName = platformMetric.getValueKey();

				if (previousPlatformName != null) {
					Assertions.assertTrue(
						platformCount <= previousPlatformCount);

					if (platformCount == previousPlatformCount) {
						Assertions.assertTrue(
							platformName.compareTo(previousPlatformName) >= 0);
					}
				}

				previousPlatformCount = platformCount;
				previousPlatformName = platformName;
			}

			previousDeviceCount = deviceCount;
			previousDeviceType = deviceType;
		}
	}

	protected void assertHistogramMetrics(
		Set<Double> expectedMetricValues,
		List<HistogramMetric> histogramMetrics) {

		Assertions.assertEquals(
			expectedMetricValues.size(), histogramMetrics.size(),
			histogramMetrics.toString());
		Assertions.assertEquals(
			expectedMetricValues,
			SetUtil.map(histogramMetrics, HistogramMetric::getValue));
	}

	protected void assertHistogramMetricsDifferentTimezone(
		String assetId, Long channelId, MetricType metricType,
		int timeZoneDifference, String timeZoneId) {

		AssetMetricRepository<T> assetMetricRepository =
			getAssetMetricRepository();

		List<LocalDateTime> localDateTimes = _getLocalDateTimes(
			assetMetricRepository.getHistogramMetrics(
				assetId, null, channelId, false, Interval.HOUR, metricType,
				TimeRange.LAST_24_HOURS));

		Mockito.when(
			_timeZoneDog.getTimeZoneId()
		).thenReturn(
			timeZoneId
		);

		Mockito.when(
			_timeZoneDog.getZoneId()
		).thenReturn(
			ZoneId.of(timeZoneId)
		);

		List<LocalDateTime> shiftedLocalDateTimes = _getLocalDateTimes(
			assetMetricRepository.getHistogramMetrics(
				assetId, null, channelId, false, Interval.HOUR, metricType,
				TimeRange.LAST_24_HOURS));

		Assertions.assertEquals(
			localDateTimes.size(), shiftedLocalDateTimes.size(),
			shiftedLocalDateTimes.toString());

		for (int i = 0; i < localDateTimes.size(); i++) {

			// Time zone ID to UTC

			Duration duration = Duration.between(
				localDateTimes.get(i), shiftedLocalDateTimes.get(i));

			Assertions.assertEquals(timeZoneDifference, duration.toHours());
		}
	}

	protected void assertMetrics(
		List<Tuple2<String, Double>> expectedMetrics, List<Metric> metrics) {

		Assertions.assertEquals(
			expectedMetrics.size(), metrics.size(), metrics.toString());

		for (int i = 0; i < expectedMetrics.size(); i++) {
			Tuple2<String, Double> expectedMetricTuple2 = expectedMetrics.get(
				i);

			Metric metric = metrics.get(i);

			Assertions.assertEquals(
				expectedMetricTuple2.getT1(), metric.getValueKey());
			Assertions.assertEquals(
				expectedMetricTuple2.getT2(), metric.getValue(), 0);
		}
	}

	protected abstract AssetMetricRepository<T> getAssetMetricRepository();

	private List<LocalDateTime> _getLocalDateTimes(
		List<HistogramMetric> histogramMetrics) {

		Stream<HistogramMetric> stream = histogramMetrics.stream();

		return stream.map(
			HistogramMetric::getKey
		).map(
			LocalDateTime::parse
		).collect(
			Collectors.toList()
		);
	}

	@MockBean
	private TimeZoneDog _timeZoneDog;

}