/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.display.template.internal;

import com.liferay.dynamic.data.mapping.kernel.DDMTemplate;
import com.liferay.petra.model.adapter.util.ModelAdapterUtil;
import com.liferay.portal.kernel.portletdisplaytemplate.PortletDisplayTemplateManager;
import com.liferay.portal.kernel.template.TemplateVariableGroup;
import com.liferay.portlet.display.template.PortletDisplayTemplate;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(immediate = true, service = PortletDisplayTemplateManager.class)
public class PortletDisplayTemplateManagerImpl
	implements PortletDisplayTemplateManager {

	@Override
	public DDMTemplate getDDMTemplate(
		long groupId, long classNameId, String displayStyle,
		boolean useDefault) {

		com.liferay.dynamic.data.mapping.model.DDMTemplate ddmTemplate =
			_portletDisplayTemplate.getPortletDisplayTemplateDDMTemplate(
				groupId, classNameId, displayStyle, useDefault);

		if (ddmTemplate == null) {
			return null;
		}

		return ModelAdapterUtil.adapt(DDMTemplate.class, ddmTemplate);
	}

	@Override
	public String getDisplayStyle(String ddmTemplateKey) {
		return _portletDisplayTemplate.getDisplayStyle(ddmTemplateKey);
	}

	@Override
	public Map<String, TemplateVariableGroup> getTemplateVariableGroups(
		String language) {

		return _portletDisplayTemplate.getTemplateVariableGroups(language);
	}

	@Override
	public String renderDDMTemplate(
			HttpServletRequest request, HttpServletResponse response,
			DDMTemplate ddmTemplate, List<?> entries,
			Map<String, Object> contextObjects)
		throws Exception {

		return _portletDisplayTemplate.renderDDMTemplate(
			request, response,
			ModelAdapterUtil.adapt(
				com.liferay.dynamic.data.mapping.model.DDMTemplate.class,
				ddmTemplate),
			entries, contextObjects);
	}

	@Override
	public String renderDDMTemplate(
			HttpServletRequest request, HttpServletResponse response,
			long templateId, List<?> entries,
			Map<String, Object> contextObjects)
		throws Exception {

		return _portletDisplayTemplate.renderDDMTemplate(
			request, response, templateId, entries, contextObjects);
	}

	@Reference(unbind = "-")
	protected void setPortletDisplayTemplate(
		PortletDisplayTemplate portletDisplayTemplate) {

		_portletDisplayTemplate = portletDisplayTemplate;
	}

	private PortletDisplayTemplate _portletDisplayTemplate;

}