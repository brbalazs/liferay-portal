/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.PagePathDog;
import com.liferay.osb.asah.backend.model.AdjacentPageViewsMetric;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcellus Tavares
 */
public class PagePathDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		Preference preference = _preferenceDog.getPreference("time-zone-id");

		if (!StringUtils.equals(preference.getValue(), "UTC")) {
			_preferenceDog.savePreference("time-zone-id", "UTC");
		}
	}

	@BQSQLResource(resourcePath = "page_path_events.sql")
	@Test
	public void testGetAdjacentPagesViewsMetric() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"http://google.com", Boolean.TRUE, "http://google.com",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"http://liferay.com/b", Boolean.FALSE, "B - Liferay DXP",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"http://liferay.com/c", Boolean.TRUE, "C - Liferay DXP",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"http://liferay.com/d", Boolean.FALSE, "D - Liferay DXP",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"http://liferay.com/e", Boolean.TRUE, "E - Liferay DXP",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", BigDecimal.ONE)),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"http://liferay.com/a", 1L, null, TimeRange.LAST_30_DAYS,
				"A - Liferay DXP"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_custom_date_range.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricCustomDateRange() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.TRUE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(2))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(resourcePath = "test_get_adjacent_pages_views_metric.sql")
	@Test
	public void testGetAdjacentPagesViewsMetricLast7Days() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.TRUE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.FALSE,
					"Try Our Online Calculator", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(3))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.LAST_7_DAYS, "From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_last_24_hours.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricLast24Hours() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.TRUE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.FALSE,
					"Try Our Online Calculator", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(3))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.LAST_24_HOURS,
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(resourcePath = "test_get_adjacent_pages_views_metric.sql")
	@Test
	public void testGetAdjacentPagesViewsMetricLast28Days() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.TRUE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.FALSE,
					"Try Our Online Calculator", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(3))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.LAST_28_DAYS, "From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(resourcePath = "test_get_adjacent_pages_views_metric.sql")
	@Test
	public void testGetAdjacentPagesViewsMetricLast30Days() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.TRUE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(3))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.LAST_30_DAYS, "From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(resourcePath = "test_get_adjacent_pages_views_metric.sql")
	@Test
	public void testGetAdjacentPagesViewsMetricLast90Days() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.TRUE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(5)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(4))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.LAST_90_DAYS, "From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(resourcePath = "test_get_adjacent_pages_views_metric.sql")
	@Test
	public void testGetAdjacentPagesViewsMetricLast180Days() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/files", Boolean.TRUE,
					"Computer File Systems", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.TRUE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(6)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/gui", Boolean.FALSE,
					"Graphical User Interface", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(5))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.LAST_180_DAYS,
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_no_direct_access.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricNoDirectAccess() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.TRUE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(2))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-03"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_vews_metric_no_next_page_views.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricNoNextPageViews() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.TRUE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", BigDecimal.ONE)),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_view_metric_no_previous_page_views.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricNoPreviousPageViews() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(6)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(2))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_one_next_page_view.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricOneNextPageView() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.TRUE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(5))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_one_previous_page_view.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricOnePreviousPageView() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", new BigDecimal(6)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(2))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_three_next_page_views.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricThreeNextPageViews() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.TRUE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/files", Boolean.FALSE,
					"Computer File Systems", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/gui", Boolean.FALSE,
					"Graphical User Interface", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE)),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_three_previous_page_views.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricThreePreviousPageViews() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.TRUE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(2))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-03"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_with_segment_id.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricWithSegmentId() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/gui", Boolean.TRUE,
					"Graphical User Interface", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/files", Boolean.FALSE,
					"Computer File Systems", BigDecimal.ONE)),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, 24680L,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_with_time_zone.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricWithTimeZone() {
		_preferenceDog.savePreference("time-zone-id", "America/Los_Angeles");

		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.TRUE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/encoding", Boolean.FALSE,
					"How Do Computers Talk?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(2))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.of(
					LocalDate.parse("2023-11-05"),
					LocalDate.parse("2023-10-25")),
				"From Abacus to Modern Day Computers"));
	}

	@BQSQLResource(
		resourcePath = "test_get_adjacent_pages_views_metric_yesterday.sql"
	)
	@Test
	public void testGetAdjacentPagesViewsMetricYesterday() {
		Assertions.assertEquals(
			SetUtil.of(
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.TRUE,
					"How Binary Encoding is Used in a Computer",
					new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.TRUE,
					"Are Calculators Computers?", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.TRUE,
					"Encoding Decimal to Binary", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.TRUE, "other", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"direct", Boolean.TRUE, "direct", new BigDecimal(3)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/decimal", Boolean.FALSE,
					"Encoding Decimal to Binary", new BigDecimal(2)),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/binary", Boolean.FALSE,
					"How Binary Encoding is Used in a Computer",
					BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"https://www.computer.com/calculator", Boolean.FALSE,
					"Try Our Online Calculator", BigDecimal.ONE),
				new AdjacentPageViewsMetric(
					"other", Boolean.FALSE, "other", new BigDecimal(3))),
			_pagePathDog.getAdjacentPagesViewsMetric(
				"https://www.computer.com/abacus", 12345L, null,
				TimeRange.YESTERDAY, "From Abacus to Modern Day Computers"));
	}

	@Autowired
	private PagePathDog _pagePathDog;

	@Autowired
	private PreferenceDog _preferenceDog;

}