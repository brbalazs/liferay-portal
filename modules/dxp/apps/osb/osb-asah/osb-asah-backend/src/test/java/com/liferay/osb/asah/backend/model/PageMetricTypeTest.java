/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseEnumTestCase;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TrendClassification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class PageMetricTypeTest extends BaseEnumTestCase<PageMetricType> {

	@Test
	public void testAvgTimeOnPage() {
		PageMetricType pageMetricType = PageMetricType.of(
			"avgTimeOnPageMetric");

		Assertions.assertEquals(
			PageMetricType.AVG_TIME_ON_PAGE, pageMetricType);
	}

	@Test
	public void testAvgTimeOnPageFieldName() {
		PageMetricType pageMetricType = PageMetricType.AVG_TIME_ON_PAGE;

		Assertions.assertEquals("timeOnPage", pageMetricType.getFieldName());
	}

	@Test
	public void testAvgTimeOnPageTrendClassificationOrder() {
		PageMetricType pageMetricType = PageMetricType.AVG_TIME_ON_PAGE;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			pageMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testBounceRate() {
		PageMetricType pageMetricType = PageMetricType.of("bounceRateMetric");

		Assertions.assertEquals(PageMetricType.BOUNCE_RATE, pageMetricType);
	}

	@Test
	public void testBounceRateFieldName() {
		PageMetricType pageMetricType = PageMetricType.BOUNCE_RATE;

		Assertions.assertEquals("bounce", pageMetricType.getFieldName());
	}

	@Test
	public void testBounceRateTrendClassificationOrder() {
		PageMetricType pageMetricType = PageMetricType.BOUNCE_RATE;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			pageMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testDirectAccess() {
		PageMetricType pageMetricType = PageMetricType.of("directAccessMetric");

		Assertions.assertEquals(PageMetricType.DIRECT_ACCESS, pageMetricType);
	}

	@Test
	public void testDirectAccessFieldName() {
		PageMetricType pageMetricType = PageMetricType.DIRECT_ACCESS;

		Assertions.assertEquals("directAccess", pageMetricType.getFieldName());
	}

	@Test
	public void testDirectAccessTrendClassificationOrder() {
		PageMetricType pageMetricType = PageMetricType.DIRECT_ACCESS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			pageMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testUsers() {
		PageMetricType pageMetricType = PageMetricType.of("visitorsMetric");

		Assertions.assertEquals(PageMetricType.VISITORS, pageMetricType);
	}

	@Test
	public void testUsersFieldName() {
		PageMetricType pageMetricType = PageMetricType.VISITORS;

		Assertions.assertEquals("visitors", pageMetricType.getFieldName());
	}

	@Test
	public void testUsersTrendClassificationOrder() {
		PageMetricType pageMetricType = PageMetricType.VISITORS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			pageMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testViews() {
		PageMetricType pageMetricType = PageMetricType.of("viewsMetric");

		Assertions.assertEquals(PageMetricType.VIEWS, pageMetricType);
	}

	@Test
	public void testViewsFieldName() {
		PageMetricType pageMetricType = PageMetricType.VIEWS;

		Assertions.assertEquals("views", pageMetricType.getFieldName());
	}

	@Test
	public void testViewsTrendClassificationOrder() {
		PageMetricType pageMetricType = PageMetricType.VIEWS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			pageMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return PageMetricType.class;
	}

}