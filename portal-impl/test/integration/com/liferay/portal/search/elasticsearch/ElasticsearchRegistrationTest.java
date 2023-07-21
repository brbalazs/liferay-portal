/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch;

import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.Collection;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Cristina González
 */
public class ElasticsearchRegistrationTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetSearchEngineService() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		Collection<SearchEngine> searchEngines = registry.getServices(
			SearchEngine.class, "(search.engine.id=SYSTEM_ENGINE)");

		Assert.assertEquals(searchEngines.toString(), 1, searchEngines.size());

		for (SearchEngine searchEngine : searchEngines) {
			Class<? extends SearchEngine> searchEngineClass =
				searchEngine.getClass();

			String searchEngineClassName = searchEngineClass.getName();

			Assert.assertTrue(
				"The registered search engine is " + searchEngineClassName,
				searchEngineClassName.endsWith("ElasticsearchSearchEngine"));
		}
	}

}