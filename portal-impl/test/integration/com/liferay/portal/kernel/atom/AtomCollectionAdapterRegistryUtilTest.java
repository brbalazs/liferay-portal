/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.atom;

import com.liferay.portal.kernel.atom.bundle.atomcollectionadapterregistryutil.TestAtomCollectionAdapter;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class AtomCollectionAdapterRegistryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule(
				"bundle.atomcollectionadapterregistryutil"));

	@Test
	public void testGetAtomCollectionAdapter() {
		AtomCollectionAdapter<?> atomCollectionAdapter =
			AtomCollectionAdapterRegistryUtil.getAtomCollectionAdapter(
				TestAtomCollectionAdapter.COLLECTION_NAME);

		Class<?> clazz = atomCollectionAdapter.getClass();

		Assert.assertEquals(
			TestAtomCollectionAdapter.class.getName(), clazz.getName());
	}

	@Test
	public void testGetAtomCollectionAdapters() {
		List<AtomCollectionAdapter<?>> atomCollectionAdapters =
			AtomCollectionAdapterRegistryUtil.getAtomCollectionAdapters();

		Assert.assertTrue(
			TestAtomCollectionAdapter.COLLECTION_NAME + " not found in " +
				atomCollectionAdapters,
			atomCollectionAdapters.removeIf(
				atomCollectionAdapter ->
					TestAtomCollectionAdapter.COLLECTION_NAME.equals(
						atomCollectionAdapter.getCollectionName())));
	}

}