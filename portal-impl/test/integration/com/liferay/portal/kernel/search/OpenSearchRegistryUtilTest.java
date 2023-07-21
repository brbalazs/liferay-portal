/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.search.bundle.opensearchregistryutil.TestOpenSearch;
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
public class OpenSearchRegistryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.opensearchregistryutil"));

	@Test
	public void testGetOpenSearch() {
		OpenSearch openSearch = OpenSearchRegistryUtil.getOpenSearch(
			TestOpenSearch.class);

		Assert.assertEquals(
			TestOpenSearch.class.getName(), openSearch.getClassName());
	}

	@Test
	public void testGetOpenSearchInstances() {
		boolean exists = false;

		List<OpenSearch> openSearches =
			OpenSearchRegistryUtil.getOpenSearchInstances();

		for (OpenSearch openSearch : openSearches) {
			String openSearchClassName = openSearch.getClassName();

			if (openSearchClassName.equals(TestOpenSearch.class.getName())) {
				exists = true;

				break;
			}
		}

		Assert.assertTrue(exists);
	}

}