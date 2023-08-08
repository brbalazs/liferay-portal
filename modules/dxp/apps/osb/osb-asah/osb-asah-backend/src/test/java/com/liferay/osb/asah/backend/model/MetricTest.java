/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TrendClassification;
import com.liferay.petra.string.StringPool;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Supplier;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class MetricTest extends BaseBeanTestCase<Metric> {

	public MetricTest() {
		super(
			new HashMap<Class<?>, Supplier<?>>() {
				{
					put(MetricType.class, MockMetricType::new);
				}
			},
			Arrays.asList("getTrend"));
	}

	@Test
	public void testCompare1() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(1000D);
		metric.setValue(1000D);

		Assertions.assertEquals(0, metric.compare());
	}

	@Test
	public void testCompare2() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(1005D);
		metric.setValue(1000D);

		Assertions.assertEquals(-1, metric.compare());
	}

	@Test
	public void testCompare3() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(1000D);
		metric.setValue(1005D);

		Assertions.assertEquals(1, metric.compare());
	}

	@Test
	public void testCompare4() {
		Metric metric = new Metric(_metricType);

		metric.setValue(1005D);

		Assertions.assertEquals(0, metric.compare());
	}

	@Override
	@Test
	public void testEqualsAndHashCode() {
		EqualsVerifier<? extends Object> equalsVerifier =
			EqualsVerifier.forClass(Metric.class);

		equalsVerifier.suppress(
			Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE);

		Metric metric = new Metric(null);

		metric.setMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(Metric.class, metric, new Metric(null));

		equalsVerifier.verify();
	}

	@Test
	public void testGetPercentage1() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(50D);
		metric.setValue(100D);

		Assertions.assertEquals(
			BigDecimal.valueOf(100.0D), metric.getPercentage());
	}

	@Test
	public void testGetPercentage2() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(0D);
		metric.setValue(100D);

		Assertions.assertNull(metric.getPercentage());
	}

	@Test
	public void testGetPercentage3() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(0.126170);
		metric.setValue(0.115810);

		Assertions.assertEquals(
			BigDecimal.valueOf(-7.7), metric.getPercentage());
	}

	@Test
	public void testGetPreviousValue() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(100D);

		Assertions.assertEquals(100D, metric.getPreviousValue(), 0D);
	}

	@Test
	public void testGetTrend() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(100D);
		metric.setValue(50D);

		Trend expectedTrend = new Trend(
			TrendClassification.NEGATIVE, BigDecimal.valueOf(-50D));

		Assertions.assertEquals(expectedTrend, metric.getTrend());
	}

	@Test
	public void testGetValue1() {
		Metric metric = new Metric(_metricType);

		metric.setValue(100D);

		Assertions.assertEquals(100D, metric.getValue(), 0D);
	}

	@Test
	public void testGetValue2() {
		Metric metric1 = new Metric(_metricType);

		metric1.setValue(100D);

		Metric metric2 = new Metric(_metricType);

		metric2.setValue(50D);

		Assertions.assertNotEquals(metric1.getValue(), metric2.getValue(), 0.0);
	}

	@Test
	public void testNeutralTrendClassification() {
		Metric metric = new Metric(_metricType);

		Assertions.assertEquals(
			TrendClassification.NEUTRAL, metric.getTrendClassification());
	}

	@Test
	public void testTrendClassification() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(50D);
		metric.setValue(100D);

		Assertions.assertEquals(
			TrendClassification.POSITIVE, metric.getTrendClassification());
	}

	@Test
	public void testTrendClassification1Decimal() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(1000D);
		metric.setValue(1001D);

		Assertions.assertEquals(
			TrendClassification.POSITIVE, metric.getTrendClassification());
	}

	@Test
	public void testTrendClassification2Decimals() {
		Metric metric = new Metric(_metricType);

		metric.setPreviousValue(10000D);
		metric.setValue(10001D);

		Assertions.assertEquals(
			TrendClassification.NEUTRAL, metric.getTrendClassification());
	}

	@Override
	protected Metric newInstance() {
		return new Metric(_metricType);
	}

	private final MetricType _metricType = new MockMetricType();

	private static class MockMetricType implements MetricType {

		@Override
		public String getAggregationName() {
			return StringPool.BLANK;
		}

		@Override
		public String getFieldName() {
			return StringPool.BLANK;
		}

		@Override
		public String getName() {
			return StringPool.BLANK;
		}

		@Override
		public TrendClassification.Order getTrendClassificationOrder() {
			return TrendClassification.Order.ASC;
		}

	}

}