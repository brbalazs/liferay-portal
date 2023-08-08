/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.date.DateUtil;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;

/**
 * @author Lino Alves
 */
public class DogTestUtil {

	public static void assertMetric(
		double expectedValue, List<Metric> metrics, String... keys) {

		Metric metric = null;

		List<Metric> innerMetrics = metrics;

		for (String key : keys) {
			Optional<Metric> metricOptional = _findMetric(innerMetrics, key);

			if (!metricOptional.isPresent()) {
				if (expectedValue == 0) {
					return;
				}

				Assertions.fail("Invalid keys: " + Arrays.toString(keys));
			}

			metric = metricOptional.get();

			innerMetrics = metric.getMetrics();
		}

		Assertions.assertEquals(expectedValue, metric.getValue(), 0.01);
	}

	public static void assertMetric(double expectedValue, Metric metric) {
		Assertions.assertEquals(expectedValue, metric.getValue(), 0.01);
	}

	public static double[] create90DaysHistogramBuckets() {
		LocalDate localDate = LocalDate.now(Clock.systemUTC());

		LocalDate startLocalDate = localDate.minusDays(90);

		int deltaWeeks = DateUtil.getDeltaWeeks(startLocalDate, localDate);

		double[] expectedValues = new double[deltaWeeks];

		Arrays.fill(expectedValues, 7);

		DayOfWeek dayOfWeek = localDate.getDayOfWeek();

		if (dayOfWeek == DayOfWeek.SUNDAY) {
			expectedValues[deltaWeeks - 1] = 1;
		}
		else {
			expectedValues[deltaWeeks - 1] = dayOfWeek.getValue() + 1;
		}

		DayOfWeek startDayOfWeek = startLocalDate.getDayOfWeek();

		if (startDayOfWeek != DayOfWeek.SUNDAY) {
			expectedValues[0] = 7 - startDayOfWeek.getValue();
		}

		return expectedValues;
	}

	private static Optional<Metric> _findMetric(
		List<Metric> metrics, String valueKey) {

		Stream<Metric> stream = metrics.stream();

		return stream.filter(
			metric -> valueKey.equals(metric.getValueKey())
		).findFirst();
	}

}