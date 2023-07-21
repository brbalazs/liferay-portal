/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.deploy.hot;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.registry.ServiceReference;

import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class CustomJspBagRegistryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.customjspbagregistryutil"));

	@Test
	public void testGetCustomJspBags() {
		Assert.assertTrue(
			"TestCustomJspBag not found",
			_findCustomJspBag("TestCustomJspBag"));
	}

	@Test
	public void testGetGlobalCustomJspBags() {
		Assert.assertTrue(
			"TestGlobalCustomJspBag not found",
			_findCustomJspBag("TestGlobalCustomJspBag"));
	}

	private boolean _findCustomJspBag(String targetContextId) {
		Map<ServiceReference<CustomJspBag>, CustomJspBag> customJspBags =
			CustomJspBagRegistryUtil.getCustomJspBags();

		for (ServiceReference<CustomJspBag> serviceReference :
				customJspBags.keySet()) {

			String contextId = GetterUtil.getString(
				serviceReference.getProperty("context.id"));

			if (contextId.equals(targetContextId)) {
				return true;
			}
		}

		return false;
	}

}