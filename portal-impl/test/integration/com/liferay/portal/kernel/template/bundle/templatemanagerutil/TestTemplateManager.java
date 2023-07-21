/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template.bundle.templatemanagerutil;

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateResource;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"language.type=English", "service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = TemplateManager.class
)
public class TestTemplateManager implements TemplateManager {

	public static final String TEST_TEMPLATE_MANAGER_NAME =
		"TEST_TEMPLATE_MANAGER_NAME";

	@Override
	public void addContextObjects(
		Map<String, Object> contextObjects,
		Map<String, Object> newContextObjects) {
	}

	@Override
	public void addStaticClassSupport(
		Map<String, Object> contextObjects, String variableName,
		Class<?> variableClass) {
	}

	@Override
	public void addTaglibApplication(
		Map<String, Object> contextObjects, String applicationName,
		ServletContext servletContext) {
	}

	@Override
	public void addTaglibFactory(
		Map<String, Object> contextObjects, String taglibLiferayHash,
		ServletContext servletContext) {
	}

	@Override
	public void addTaglibRequest(
		Map<String, Object> contextObjects, String applicationName,
		HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void addTaglibSupport(
		Map<String, Object> contextObjects, HttpServletRequest request,
		HttpServletResponse response) {
	}

	@Override
	public void addTaglibTheme(
		Map<String, Object> contextObjects, String string,
		HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void destroy(ClassLoader classLoader) {
	}

	@Override
	public String getName() {
		return TEST_TEMPLATE_MANAGER_NAME;
	}

	@Override
	public String[] getRestrictedVariables() {
		return null;
	}

	@Override
	public Template getTemplate(
		List<TemplateResource> templateResources, boolean restricted) {

		return getTemplate(templateResources.get(0), restricted);
	}

	@Override
	public Template getTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted) {

		return getTemplate(templateResources, restricted);
	}

	@Override
	public Template getTemplate(
		TemplateResource templateResource, boolean restricted) {

		String templateId = templateResource.getTemplateId();

		if (templateId.equals(
				TestTemplateResource.TEST_TEMPLATE_RESOURCE_TEMPLATE_ID)) {

			return new TestTemplate();
		}

		return null;
	}

	@Override
	public Template getTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted) {

		return getTemplate(templateResource, restricted);
	}

	@Override
	public void init() {
	}

}