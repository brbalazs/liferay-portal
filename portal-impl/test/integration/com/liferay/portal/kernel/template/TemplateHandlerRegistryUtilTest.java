/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template;

import com.liferay.portal.kernel.template.bundle.templatehandlerregistryutil.TestTemplateHandler;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PortalUtil;
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
public class TemplateHandlerRegistryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.templatehandlerregistryutil"));

	@Test
	public void testGetClassNameIds() {
		long classNameId = PortalUtil.getClassNameId(
			TestTemplateHandler.class.getName());

		Assert.assertTrue(
			ArrayUtil.contains(
				TemplateHandlerRegistryUtil.getClassNameIds(), classNameId));
	}

	@Test
	public void testGetTemplateHandlerByClassName() {
		TemplateHandler templateHandler =
			TemplateHandlerRegistryUtil.getTemplateHandler(
				TestTemplateHandler.class.getName());

		Assert.assertEquals(
			TestTemplateHandler.class.getName(),
			templateHandler.getClassName());
	}

	@Test
	public void testGetTemplateHandlerByClassNameId() {
		long classNameId = PortalUtil.getClassNameId(
			TestTemplateHandler.class.getName());

		TemplateHandler templateHandler =
			TemplateHandlerRegistryUtil.getTemplateHandler(classNameId);

		Assert.assertEquals(
			TestTemplateHandler.class.getName(),
			templateHandler.getClassName());
	}

	@Test
	public void testGetTemplateHandlers() {
		boolean exists = false;

		List<TemplateHandler> templateHandlers =
			TemplateHandlerRegistryUtil.getTemplateHandlers();

		for (TemplateHandler templateHandler : templateHandlers) {
			String className = templateHandler.getClassName();

			if (className.equals(TestTemplateHandler.class.getName())) {
				exists = true;

				break;
			}
		}

		Assert.assertTrue(exists);
	}

}