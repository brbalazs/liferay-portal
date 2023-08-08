/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Supplier;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Marcellus Tavares
 */
public class FormMetricTest extends BaseBeanTestCase<FormMetric> {

	public FormMetricTest() {
		super(
			new HashMap<Class<?>, Supplier<?>>() {
				{
					put(Metric.class, () -> new Metric(null));
				}
			},
			Arrays.asList(
				"getAssetType", "getAvailableMetrics", "getDefaultMetric"));
	}

	@Override
	@Test
	public void testEqualsAndHashCode() {
		EqualsVerifier<? extends Object> equalsVerifier =
			EqualsVerifier.forClass(FormMetric.class);

		equalsVerifier.suppress(
			Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE);

		FormMetric formMetric = new FormMetric();

		formMetric.setAssetMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(
			AssetMetric.class, formMetric, new FormMetric());

		Metric metric = new Metric(null);

		metric.setMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(Metric.class, metric, new Metric(null));

		equalsVerifier.withRedefinedSuperclass();

		equalsVerifier.verify();
	}

	@Test
	public void testGetAssetType() {
		FormMetric formMetric = newInstance();

		Assertions.assertEquals(
			AssetType.FORM.getValue(), formMetric.getAssetType());
	}

	@Test
	public void testGetDefaultMetric() {
		FormMetric formMetric = newInstance();

		Assertions.assertEquals(
			new Metric(FormMetricType.SUBMISSIONS),
			formMetric.getDefaultMetric());
	}

	@Override
	protected FormMetric newInstance() {
		return new FormMetric();
	}

}