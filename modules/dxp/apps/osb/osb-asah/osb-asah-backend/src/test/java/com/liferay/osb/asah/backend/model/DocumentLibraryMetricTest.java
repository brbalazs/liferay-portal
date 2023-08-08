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
public class DocumentLibraryMetricTest
	extends BaseBeanTestCase<DocumentLibraryMetric> {

	public DocumentLibraryMetricTest() {
		super(
			new HashMap<Class<?>, Supplier<?>>() {
				{
					put(Metric.class, () -> new Metric(null));
				}
			},
			Arrays.asList(
				"getAssetType", "getAvailableMetrics", "getDefaultMetric"));
	}

	@Test
	public void testAssetType() {
		DocumentLibraryMetric documentMetric = newInstance();

		Assertions.assertEquals(
			AssetType.DOCUMENT.getValue(), documentMetric.getAssetType());
	}

	@Test
	public void testDefaultMetric() {
		DocumentLibraryMetric documentLibraryMetric = newInstance();

		Assertions.assertEquals(
			new Metric(DocumentLibraryMetricType.DOWNLOADS),
			documentLibraryMetric.getDefaultMetric());
	}

	@Override
	@Test
	public void testEqualsAndHashCode() {
		EqualsVerifier<? extends Object> equalsVerifier =
			EqualsVerifier.forClass(DocumentLibraryMetric.class);

		equalsVerifier.suppress(
			Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE);

		DocumentLibraryMetric documentLibraryMetric =
			new DocumentLibraryMetric();

		documentLibraryMetric.setAssetMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(
			AssetMetric.class, documentLibraryMetric,
			new DocumentLibraryMetric());

		Metric metric = new Metric(null);

		metric.setMetrics(Collections.emptyList());

		equalsVerifier.withPrefabValues(Metric.class, metric, new Metric(null));

		equalsVerifier.withRedefinedSuperclass();

		equalsVerifier.verify();
	}

	@Override
	protected DocumentLibraryMetric newInstance() {
		return new DocumentLibraryMetric();
	}

}