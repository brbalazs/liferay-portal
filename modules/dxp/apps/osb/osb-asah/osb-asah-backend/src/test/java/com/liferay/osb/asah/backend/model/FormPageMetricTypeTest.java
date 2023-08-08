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
 * @author André Miranda
 */
public class FormPageMetricTypeTest
	extends BaseEnumTestCase<FormPageMetricType> {

	@Test
	public void testPageAbandonments() {
		FormPageMetricType formPageMetricType = FormPageMetricType.of(
			"pageAbandonmentsMetric");

		Assertions.assertEquals(
			FormPageMetricType.PAGE_ABANDONMENTS, formPageMetricType);
	}

	@Test
	public void testPageAbandonmentsFieldName() {
		FormPageMetricType formPageMetricType =
			FormPageMetricType.PAGE_ABANDONMENTS;

		Assertions.assertEquals(
			"abandonments", formPageMetricType.getFieldName());
	}

	@Test
	public void testPageAbandonmentsTrendClassificationOrder() {
		FormPageMetricType formPageMetricType =
			FormPageMetricType.PAGE_ABANDONMENTS;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			formPageMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testPageViews() {
		FormPageMetricType formPageMetricType = FormPageMetricType.of(
			"pageViewsMetric");

		Assertions.assertEquals(
			FormPageMetricType.PAGE_VIEWS, formPageMetricType);
	}

	@Test
	public void testPageViewsFieldName() {
		FormPageMetricType formPageMetricType = FormPageMetricType.PAGE_VIEWS;

		Assertions.assertEquals("views", formPageMetricType.getFieldName());
	}

	@Test
	public void testPageViewsTrendClassificationOrder() {
		FormPageMetricType formPageMetricType = FormPageMetricType.PAGE_VIEWS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			formPageMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return FormMetricType.class;
	}

}