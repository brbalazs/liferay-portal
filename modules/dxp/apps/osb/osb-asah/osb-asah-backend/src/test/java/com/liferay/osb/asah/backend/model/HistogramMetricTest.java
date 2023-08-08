/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;

import java.util.Arrays;
import java.util.Collections;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class HistogramMetricTest extends BaseBeanTestCase<HistogramMetric> {

	public HistogramMetricTest() {
		super(null, Arrays.asList("getMetricType", "getTrend"));
	}

	@Override
	@Test
	public void testEqualsAndHashCode() {
		EqualsVerifier<? extends Object> equalsVerifier =
			EqualsVerifier.forClass(HistogramMetric.class);

		Metric metric = new Metric(null);

		metric.setMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(Metric.class, metric, new Metric(null));

		equalsVerifier.suppress(
			Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE);

		equalsVerifier.withRedefinedSuperclass();

		equalsVerifier.verify();
	}

	@Override
	protected HistogramMetric newInstance() {
		return new HistogramMetric(null, new Metric(null));
	}

}