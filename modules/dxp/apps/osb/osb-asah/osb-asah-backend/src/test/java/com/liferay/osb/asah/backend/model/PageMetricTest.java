/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;
import com.liferay.osb.asah.common.model.PageMetricType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Supplier;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class PageMetricTest extends BaseBeanTestCase<PageMetric> {

	public PageMetricTest() {
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
			EqualsVerifier.forClass(PageMetric.class);

		equalsVerifier.suppress(
			Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE);

		Metric metric = new Metric(null);

		metric.setMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(Metric.class, metric, new Metric(null));

		PageMetric pageMetric = new PageMetric();

		pageMetric.setAssetMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(
			AssetMetric.class, pageMetric, new PageMetric());

		equalsVerifier.withRedefinedSuperclass();

		equalsVerifier.verify();
	}

	@Test
	public void testGetAssetType() {
		PageMetric pageMetric = newInstance();

		Assertions.assertEquals(
			AssetType.PAGE.getValue(), pageMetric.getAssetType());
	}

	@Test
	public void testGetDefaultMetric() {
		PageMetric pageMetric = newInstance();

		Assertions.assertEquals(
			new Metric(PageMetricType.VIEWS), pageMetric.getDefaultMetric());
	}

	@Override
	protected PageMetric newInstance() {
		return new PageMetric();
	}

}