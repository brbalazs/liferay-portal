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
public class FormMetricTypeTest extends BaseEnumTestCase<FormMetricType> {

	@Test
	public void testAbandonments() {
		FormMetricType formMetricType = FormMetricType.of("abandonmentsMetric");

		Assertions.assertEquals(FormMetricType.ABANDONMENTS, formMetricType);
	}

	@Test
	public void testAbandonmentsFieldName() {
		FormMetricType formMetricType = FormMetricType.ABANDONMENTS;

		Assertions.assertEquals("abandonments", formMetricType.getFieldName());
	}

	@Test
	public void testAbandonmentsTrendClassificationOrder() {
		FormMetricType formMetricType = FormMetricType.ABANDONMENTS;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			formMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testCompletionTime() {
		FormMetricType formMetricType = FormMetricType.of(
			"completionTimeMetric");

		Assertions.assertEquals(FormMetricType.COMPLETION_TIME, formMetricType);
	}

	@Test
	public void testCompletionTimeFieldName() {
		FormMetricType formMetricType = FormMetricType.COMPLETION_TIME;

		Assertions.assertEquals(
			"submissionsTime", formMetricType.getFieldName());
	}

	@Test
	public void testCompletionTimeTrendClassificationOrder() {
		FormMetricType completionTime = FormMetricType.COMPLETION_TIME;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			completionTime.getTrendClassificationOrder());
	}

	@Test
	public void testSubmissions() {
		FormMetricType formMetricType = FormMetricType.of("submissionsMetric");

		Assertions.assertEquals(FormMetricType.SUBMISSIONS, formMetricType);
	}

	@Test
	public void testSubmissionsFieldName() {
		FormMetricType formMetricType = FormMetricType.SUBMISSIONS;

		Assertions.assertEquals("submissions", formMetricType.getFieldName());
	}

	@Test
	public void testSubmissionsTrendClassificationOrder() {
		FormMetricType formMetricType = FormMetricType.SUBMISSIONS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			formMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testViews() {
		FormMetricType formMetricType = FormMetricType.of("viewsMetric");

		Assertions.assertEquals(FormMetricType.VIEWS, formMetricType);
	}

	@Test
	public void testViewsFieldName() {
		FormMetricType formMetricType = FormMetricType.VIEWS;

		Assertions.assertEquals("views", formMetricType.getFieldName());
	}

	@Test
	public void testViewsTrendClassificationOrder() {
		FormMetricType formMetricType = FormMetricType.VIEWS;

		Assertions.assertEquals(
			TrendClassification.Order.ASC,
			formMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return FormMetricType.class;
	}

}