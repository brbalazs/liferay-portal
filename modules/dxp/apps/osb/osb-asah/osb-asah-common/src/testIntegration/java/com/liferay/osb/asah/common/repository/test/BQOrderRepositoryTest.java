/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQOrderRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Riccardo Ferrari
 */
@Import(JDBCTestConfiguration.class)
public class BQOrderRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Disabled
	@Test
	public void testGetOrderAccountAverageCurrencyValues() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, BigDecimal> orderAccountAverageCurrencyValues =
			_bqOrderRepository.getOrderAccountAverageCurrencyValues(
				123L, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC");

		Assertions.assertNotNull(orderAccountAverageCurrencyValues);
		Assertions.assertNotNull(orderAccountAverageCurrencyValues.get("USD"));

		BigDecimal actualValue = orderAccountAverageCurrencyValues.get("USD");

		BigDecimal expectedValue = new BigDecimal("14.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());

		Assertions.assertNotNull(orderAccountAverageCurrencyValues.get("EUR"));

		actualValue = orderAccountAverageCurrencyValues.get("EUR");
		expectedValue = new BigDecimal("10.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());
	}

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Test
	public void testGetOrderAccountAverageCurrencyValuesWithEmptyDatasourceIds() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Assertions.assertEquals(
			Collections.emptyMap(),
			_bqOrderRepository.getOrderAccountAverageCurrencyValues(
				null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC"));
	}

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Test
	public void testGetOrderAverageCurrencyValues() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, BigDecimal> orderAverageCurrencyValues =
			_bqOrderRepository.getOrderAverageCurrencyValues(
				123L, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC");

		Assertions.assertNotNull(orderAverageCurrencyValues);
		Assertions.assertNotNull(orderAverageCurrencyValues.get("USD"));

		BigDecimal actualValue = orderAverageCurrencyValues.get("USD");

		BigDecimal expectedValue = new BigDecimal("10.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());

		Assertions.assertNotNull(orderAverageCurrencyValues.get("EUR"));

		actualValue = orderAverageCurrencyValues.get("EUR");
		expectedValue = new BigDecimal("10.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());
	}

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Test
	public void testGetOrderAverageCurrencyValuesWithEmptyDatasourceIds() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Assertions.assertEquals(
			Collections.emptyMap(),
			_bqOrderRepository.getOrderAverageCurrencyValues(
				null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC"));
	}

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Test
	public void testGetOrderIncompleteCurrencyValues() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, BigDecimal> orderIncompleteCurrencyValues =
			_bqOrderRepository.getOrderIncompleteCurrencyValues(
				123L, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC");

		Assertions.assertNotNull(orderIncompleteCurrencyValues);
		Assertions.assertNotNull(orderIncompleteCurrencyValues.get("USD"));

		BigDecimal actualValue = orderIncompleteCurrencyValues.get("USD");

		BigDecimal expectedValue = new BigDecimal("20.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());

		Assertions.assertNotNull(orderIncompleteCurrencyValues.get("EUR"));

		actualValue = orderIncompleteCurrencyValues.get("EUR");
		expectedValue = new BigDecimal("10.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());
	}

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Test
	public void testGetOrderIncompleteCurrencyValuesWithEmptyDataSourceIds() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Assertions.assertEquals(
			Collections.emptyMap(),
			_bqOrderRepository.getOrderIncompleteCurrencyValues(
				null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC"));
	}

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Test
	public void testGetOrderTotalCurrencyValues() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, BigDecimal> orderTotalCurrencyValues =
			_bqOrderRepository.getOrderTotalCurrencyValues(
				123L, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC");

		Assertions.assertNotNull(orderTotalCurrencyValues);
		Assertions.assertNotNull(orderTotalCurrencyValues.get("USD"));

		BigDecimal actualValue = orderTotalCurrencyValues.get("USD");

		BigDecimal expectedValue = new BigDecimal("70.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());

		Assertions.assertNotNull(orderTotalCurrencyValues.get("EUR"));

		actualValue = orderTotalCurrencyValues.get("EUR");
		expectedValue = new BigDecimal("40.0");

		Assertions.assertEquals(
			expectedValue.stripTrailingZeros(),
			actualValue.stripTrailingZeros());
	}

	@BQSQLResource(resourcePath = "test_bq_order.sql")
	@Test
	public void testGetOrderTotalCurrencyValuesWithEmptyDataSourceIds() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Assertions.assertEquals(
			Collections.emptyMap(),
			_bqOrderRepository.getOrderTotalCurrencyValues(
				null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), "UTC"));
	}

	@Autowired
	private BQOrderRepository _bqOrderRepository;

}