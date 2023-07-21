/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template;

import com.liferay.portal.kernel.template.bundle.templateresourceloaderutil.TestTemplateResource;
import com.liferay.portal.kernel.template.bundle.templateresourceloaderutil.TestTemplateResourceLoader;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.Set;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class TemplateResourceLoaderUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.templateresourceloaderutil"));

	@Test
	public void testGetTemplateResource() throws TemplateException {
		TemplateResource templateResource =
			TemplateResourceLoaderUtil.getTemplateResource(
				TestTemplateResourceLoader.TEST_TEMPLATE_RESOURCE_LOADER_NAME,
				TestTemplateResource.TEST_TEMPLATE_RESOURCE_TEMPLATE_ID);

		Class<?> clazz = templateResource.getClass();

		Assert.assertEquals(
			TestTemplateResource.class.getName(), clazz.getName());
	}

	@Test
	public void testGetTemplateResourceLoader() throws TemplateException {
		TemplateResourceLoader templateResourceLoader =
			TemplateResourceLoaderUtil.getTemplateResourceLoader(
				TestTemplateResourceLoader.TEST_TEMPLATE_RESOURCE_LOADER_NAME);

		Class<?> clazz = templateResourceLoader.getClass();

		Assert.assertEquals(
			TestTemplateResourceLoader.class.getName(), clazz.getName());
	}

	@Test
	public void testGetTemplateResourceLoaderNames() {
		Set<String> templateResourceLoaderNames =
			TemplateResourceLoaderUtil.getTemplateResourceLoaderNames();

		Assert.assertTrue(
			templateResourceLoaderNames.toString(),
			templateResourceLoaderNames.contains(
				TestTemplateResourceLoader.TEST_TEMPLATE_RESOURCE_LOADER_NAME));
	}

	@Test
	public void testHasTemplateResource() throws TemplateException {
		Assert.assertTrue(
			TemplateResourceLoaderUtil.hasTemplateResource(
				TestTemplateResourceLoader.TEST_TEMPLATE_RESOURCE_LOADER_NAME,
				TestTemplateResource.TEST_TEMPLATE_RESOURCE_TEMPLATE_ID));
	}

	@Test
	public void testHasTemplateResourceLoader() {
		Assert.assertTrue(
			TemplateResourceLoaderUtil.hasTemplateResourceLoader(
				TestTemplateResourceLoader.TEST_TEMPLATE_RESOURCE_LOADER_NAME));
	}

}