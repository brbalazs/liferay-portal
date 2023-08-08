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
public class JournalMetricTypeTest extends BaseEnumTestCase<JournalMetricType> {

	@Test
	public void testViews() {
		JournalMetricType journalMetricType = JournalMetricType.of(
			"viewsMetric");

		Assertions.assertEquals(JournalMetricType.VIEWS, journalMetricType);
	}

	@Test
	public void testViewsFieldName() {
		JournalMetricType journalMetricType = JournalMetricType.VIEWS;

		Assertions.assertEquals("views", journalMetricType.getFieldName());
	}

	@Test
	public void testViewsTrendClassificationOrder() {
		JournalMetricType journalMetricType = JournalMetricType.VIEWS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			journalMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return JournalMetricType.class;
	}

}