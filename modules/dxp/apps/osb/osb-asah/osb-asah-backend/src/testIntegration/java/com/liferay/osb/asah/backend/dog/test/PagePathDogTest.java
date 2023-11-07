/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.PagePathDog;
import com.liferay.osb.asah.backend.model.AdjacentPageViewsMetric;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcellus Tavares
 */
public class PagePathDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "page_path_events.sql")
	@Test
	public void testPagePathNode() {
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

	@Autowired
	private PagePathDog _pagePathDog;

}