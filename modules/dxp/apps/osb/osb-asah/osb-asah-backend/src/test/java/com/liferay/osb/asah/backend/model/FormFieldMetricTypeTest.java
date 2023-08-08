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
public class FormFieldMetricTypeTest
	extends BaseEnumTestCase<FormFieldMetricType> {

	@Test
	public void testFieldAbandonments() {
		FormFieldMetricType formFieldMetricType = FormFieldMetricType.of(
			"fieldAbandonmentsMetric");

		Assertions.assertEquals(
			FormFieldMetricType.FIELD_ABANDONMENTS, formFieldMetricType);
	}

	@Test
	public void testFieldAbandonmentsFieldName() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_ABANDONMENTS;

		Assertions.assertEquals(
			"abandonments", formFieldMetricType.getFieldName());
	}

	@Test
	public void testFieldAbandonmentsTrendClassificationOrder() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_ABANDONMENTS;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			formFieldMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testFieldInteraction() {
		FormFieldMetricType formFieldMetricType = FormFieldMetricType.of(
			"fieldInteractionsMetric");

		Assertions.assertEquals(
			FormFieldMetricType.FIELD_INTERACTIONS, formFieldMetricType);
	}

	@Test
	public void testFieldInteractionDuration() {
		FormFieldMetricType formFieldMetricType = FormFieldMetricType.of(
			"fieldInteractionsDurationMetric");

		Assertions.assertEquals(
			FormFieldMetricType.FIELD_INTERACTION_DURATION,
			formFieldMetricType);
	}

	@Test
	public void testFieldRefilled() {
		FormFieldMetricType formFieldMetricType = FormFieldMetricType.of(
			"fieldRefilledMetric");

		Assertions.assertEquals(
			FormFieldMetricType.FIELD_REFILLED, formFieldMetricType);
	}

	@Test
	public void testInteractionDurationFieldName() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_INTERACTION_DURATION;

		Assertions.assertEquals(
			"interactionsDuration", formFieldMetricType.getFieldName());
	}

	@Test
	public void testInteractionDurationTrendClassificationOrder() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_INTERACTION_DURATION;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			formFieldMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testInteractionFieldName() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_INTERACTIONS;

		Assertions.assertEquals(
			"interactions", formFieldMetricType.getFieldName());
	}

	@Test
	public void testInteractionTrendClassificationOrder() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_INTERACTIONS;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			formFieldMetricType.getTrendClassificationOrder());
	}

	@Test
	public void testRefilledFieldName() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_REFILLED;

		Assertions.assertEquals("refilled", formFieldMetricType.getFieldName());
	}

	@Test
	public void testRefilledTrendClassificationOrder() {
		FormFieldMetricType formFieldMetricType =
			FormFieldMetricType.FIELD_REFILLED;

		Assertions.assertEquals(
			TrendClassification.Order.DESC,
			formFieldMetricType.getTrendClassificationOrder());
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return FormMetricType.class;
	}

}