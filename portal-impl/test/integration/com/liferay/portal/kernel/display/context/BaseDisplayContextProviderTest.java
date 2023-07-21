/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.display.context;

import com.liferay.portal.kernel.display.context.bundle.basedisplaycontextfactory.TestBaseDisplayContextFactoryImpl;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class BaseDisplayContextProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.basedisplaycontextfactory"));

	@BeforeClass
	public static void setUpClass() throws Exception {
		_baseDisplayContextProvider = new BaseDisplayContextProvider<>(
			TestDisplayContextFactory.class);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_baseDisplayContextProvider.destroy();
	}

	@Test
	public void testDisplayContextHasBeenRegistered() throws Exception {
		TestDisplayContextFactory testDisplayContextFactoryExtension = null;

		Iterable<TestDisplayContextFactory> displayContextFactories =
			_baseDisplayContextProvider.getDisplayContextFactories();

		Iterator<TestDisplayContextFactory> iterator =
			displayContextFactories.iterator();

		while (iterator.hasNext()) {
			TestDisplayContextFactory testDisplayContextFactory =
				iterator.next();

			Class<?> clazz = testDisplayContextFactory.getClass();

			String className = clazz.getName();

			if (className.equals(
					TestBaseDisplayContextFactoryImpl.class.getName())) {

				testDisplayContextFactoryExtension = testDisplayContextFactory;
			}
		}

		Assert.assertNotNull(testDisplayContextFactoryExtension);
	}

	private static BaseDisplayContextProvider<TestDisplayContextFactory>
		_baseDisplayContextProvider;

}