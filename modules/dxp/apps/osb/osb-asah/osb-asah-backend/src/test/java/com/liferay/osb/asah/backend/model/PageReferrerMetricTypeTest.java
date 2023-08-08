/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseEnumTestCase;
import com.liferay.osb.asah.common.model.TrendClassification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class PageReferrerMetricTypeTest
	extends BaseEnumTestCase<PageReferrerMetricType> {

	@Test
	public void testAccess() {
		PageReferrerMetricType pageReferrerMetricType =
			PageReferrerMetricType.of("accessMetric");

		Assertions.assertEquals(
			PageReferrerMetricType.ACCESS, pageReferrerMetricType);
	}

	@Test
	public void testAccessFieldName() {
		PageReferrerMetricType pageReferrerMetricType =
			PageReferrerMetricType.ACCESS;

		Assertions.assertEquals(
			"access", pageReferrerMetricType.getFieldName());
	}

	@Test
	public void testAccessTrendClassificationOrder() {
		PageReferrerMetricType pageReferrerMetricType =
			PageReferrerMetricType.ACCESS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			pageReferrerMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return PageReferrerMetricType.class;
	}

}