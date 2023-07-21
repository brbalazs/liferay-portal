/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.web.internal.portlet.action;

import com.liferay.dynamic.data.mapping.constants.DDMPortletKeys;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderer;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRendererRegistry;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DDMPortletKeys.DYNAMIC_DATA_MAPPING,
		"mvc.command.name=renderStructureField"
	},
	service = MVCResourceCommand.class
)
public class RenderStructureFieldMVCRenderCommand
	extends BaseMVCResourceCommand {

	protected DDMFormFieldRenderingContext createDDMFormFieldRenderingContext(
		HttpServletRequest request, HttpServletResponse response) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		String mode = ParamUtil.getString(request, "mode");
		String namespace = ParamUtil.getString(request, "namespace");
		String portletNamespace = ParamUtil.getString(
			request, "portletNamespace");
		boolean readOnly = ParamUtil.getBoolean(request, "readOnly");

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			new DDMFormFieldRenderingContext();

		request.setAttribute("aui:form:portletNamespace", portletNamespace);

		ddmFormFieldRenderingContext.setHttpServletRequest(
			_portal.getOriginalServletRequest(request));
		ddmFormFieldRenderingContext.setHttpServletResponse(response);
		ddmFormFieldRenderingContext.setLocale(themeDisplay.getLocale());
		ddmFormFieldRenderingContext.setMode(mode);
		ddmFormFieldRenderingContext.setNamespace(namespace);
		ddmFormFieldRenderingContext.setPortletNamespace(portletNamespace);
		ddmFormFieldRenderingContext.setReadOnly(readOnly);

		return ddmFormFieldRenderingContext;
	}

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		HttpServletResponse httpServletResponse =
			_portal.getHttpServletResponse(resourceResponse);

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			resourceRequest);

		DDMFormField ddmFormField = getDDMFormField(httpServletRequest);

		DDMFormFieldRenderer ddmFormFieldRenderer =
			_ddmFormFieldRendererRegistry.getDDMFormFieldRenderer(
				ddmFormField.getType());

		DDMFormFieldRenderingContext ddmFormFieldRenderingContext =
			createDDMFormFieldRenderingContext(
				httpServletRequest, httpServletResponse);

		String ddmFormFieldHTML = ddmFormFieldRenderer.render(
			ddmFormField, ddmFormFieldRenderingContext);

		httpServletResponse.setContentType(ContentTypes.TEXT_HTML);

		ServletResponseUtil.write(httpServletResponse, ddmFormFieldHTML);
	}

	protected DDMFormField getDDMFormField(HttpServletRequest request)
		throws PortalException {

		String definition = ParamUtil.getString(request, "definition");
		String fieldName = ParamUtil.getString(request, "fieldName");

		DDMForm ddmForm = _ddmFormJSONDeserializer.deserialize(definition);

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(true);

		return ddmFormFieldsMap.get(fieldName);
	}

	@Reference(unbind = "-")
	protected void setDDMFormFieldRendererRegistry(
		DDMFormFieldRendererRegistry ddmFormFieldRendererRegistry) {

		_ddmFormFieldRendererRegistry = ddmFormFieldRendererRegistry;
	}

	@Reference(unbind = "-")
	protected void setDDMFormJSONDeserializer(
		DDMFormJSONDeserializer ddmFormJSONDeserializer) {

		_ddmFormJSONDeserializer = ddmFormJSONDeserializer;
	}

	private DDMFormFieldRendererRegistry _ddmFormFieldRendererRegistry;
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;

	@Reference
	private Portal _portal;

}