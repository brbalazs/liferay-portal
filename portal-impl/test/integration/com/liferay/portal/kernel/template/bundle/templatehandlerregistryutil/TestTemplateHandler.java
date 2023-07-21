/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template.bundle.templatehandlerregistryutil;

import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.TemplateVariableGroup;
import com.liferay.portal.kernel.xml.Element;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = TemplateHandler.class
)
public class TestTemplateHandler implements TemplateHandler {

	@Override
	public String getClassName() {
		return TestTemplateHandler.class.getName();
	}

	@Override
	public Map<String, Object> getCustomContextObjects() {
		return Collections.emptyMap();
	}

	@Override
	public List<Element> getDefaultTemplateElements() {
		return Collections.emptyList();
	}

	@Override
	public String getDefaultTemplateKey() {
		return null;
	}

	@Override
	public String getName(Locale locale) {
		return null;
	}

	@Override
	public String getResourceName() {
		return null;
	}

	@Override
	public String[] getRestrictedVariables(String language) {
		return null;
	}

	@Override
	public String getTemplatesHelpContent(String language) {
		return null;
	}

	@Override
	public String getTemplatesHelpPath(String language) {
		return null;
	}

	@Override
	public String getTemplatesHelpPropertyKey() {
		return null;
	}

	@Override
	public Map<String, TemplateVariableGroup> getTemplateVariableGroups(
		long classPK, String language, Locale locale) {

		return null;
	}

	@Override
	public boolean isDisplayTemplateHandler() {
		return false;
	}

}