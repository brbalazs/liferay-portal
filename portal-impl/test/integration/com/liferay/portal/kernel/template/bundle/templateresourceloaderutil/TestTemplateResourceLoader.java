/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template.bundle.templateresourceloaderutil;

import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoader;

import org.osgi.service.component.annotations.Component;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = TemplateResourceLoader.class
)
public class TestTemplateResourceLoader implements TemplateResourceLoader {

	public static final String TEST_TEMPLATE_RESOURCE_LOADER_NAME =
		"TEST_TEMPLATE_RESOURCE_LOADER_NAME";

	@Override
	public void clearCache() {
	}

	@Override
	public void clearCache(String templateId) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String getName() {
		return TEST_TEMPLATE_RESOURCE_LOADER_NAME;
	}

	@Override
	public TemplateResource getTemplateResource(String templateId) {
		if (templateId.equals(
				TestTemplateResource.TEST_TEMPLATE_RESOURCE_TEMPLATE_ID)) {

			return new TestTemplateResource();
		}

		return null;
	}

	@Override
	public boolean hasTemplateResource(String templateId) {
		if (templateId.equals(
				TestTemplateResource.TEST_TEMPLATE_RESOURCE_TEMPLATE_ID)) {

			return true;
		}

		return false;
	}

}