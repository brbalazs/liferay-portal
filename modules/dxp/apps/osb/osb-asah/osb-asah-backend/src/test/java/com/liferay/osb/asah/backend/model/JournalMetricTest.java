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
 * @author Inácio Nery
 */
public class JournalMetricTest extends BaseBeanTestCase<JournalMetric> {

	public JournalMetricTest() {
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
			EqualsVerifier.forClass(JournalMetric.class);

		equalsVerifier.suppress(
			Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE);

		Metric metric = new Metric(null);

		metric.setMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(Metric.class, metric, new Metric(null));

		JournalMetric journalMetric = new JournalMetric();

		journalMetric.setAssetMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(
			AssetMetric.class, journalMetric, new JournalMetric());

		equalsVerifier.withRedefinedSuperclass();

		equalsVerifier.verify();
	}

	@Test
	public void testGetAssetType() {
		JournalMetric journalMetric = newInstance();

		Assertions.assertEquals(
			AssetType.JOURNAL.getValue(), journalMetric.getAssetType());
	}

	@Test
	public void testGetDefaultMetric() {
		JournalMetric journalMetric = newInstance();

		Assertions.assertEquals(
			new Metric(JournalMetricType.VIEWS),
			journalMetric.getDefaultMetric());
	}

	@Override
	protected JournalMetric newInstance() {
		return new JournalMetric();
	}

}