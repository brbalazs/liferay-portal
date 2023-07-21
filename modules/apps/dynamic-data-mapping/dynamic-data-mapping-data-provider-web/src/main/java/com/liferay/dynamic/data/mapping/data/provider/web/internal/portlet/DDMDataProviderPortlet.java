/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.web.internal.portlet;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderTracker;
import com.liferay.dynamic.data.mapping.data.provider.web.internal.constants.DDMDataProviderPortletKeys;
import com.liferay.dynamic.data.mapping.data.provider.web.internal.display.context.DDMDataProviderDisplayContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.io.DDMFormValuesJSONDeserializer;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.autopropagated-parameters=backURL",
		"com.liferay.portlet.css-class-wrapper=portlet-dynamic-data-mapping-data-provider",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=false",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Dynamic Data Mapping Data Provider Web",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + DDMDataProviderPortletKeys.DYNAMIC_DATA_MAPPING_DATA_PROVIDER,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class DDMDataProviderPortlet extends MVCPortlet {

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		DDMDataProviderDisplayContext ddmDataProviderDisplayContext =
			new DDMDataProviderDisplayContext(
				renderRequest, renderResponse, _ddmDataProviderInstanceService,
				_ddmDataProviderTracker, _ddmFormRenderer,
				_ddmFormValuesJSONDeserializer, _userLocalService);

		renderRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT, ddmDataProviderDisplayContext);

		super.render(renderRequest, renderResponse);
	}

	@Reference(unbind = "-")
	protected void setDDMDataProviderInstanceService(
		DDMDataProviderInstanceService ddmDataProviderInstanceService) {

		_ddmDataProviderInstanceService = ddmDataProviderInstanceService;
	}

	@Reference(unbind = "-")
	protected void setDDMDataProviderTracker(
		DDMDataProviderTracker ddmDataProviderTracker) {

		_ddmDataProviderTracker = ddmDataProviderTracker;
	}

	@Reference(unbind = "-")
	protected void setDDMFormRenderer(DDMFormRenderer ddmFormRenderer) {
		_ddmFormRenderer = ddmFormRenderer;
	}

	@Reference(unbind = "-")
	protected void setDDMFormValuesJSONDeserializer(
		DDMFormValuesJSONDeserializer ddmFormValuesJSONDeserializer) {

		_ddmFormValuesJSONDeserializer = ddmFormValuesJSONDeserializer;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private DDMDataProviderInstanceService _ddmDataProviderInstanceService;
	private DDMDataProviderTracker _ddmDataProviderTracker;
	private DDMFormRenderer _ddmFormRenderer;
	private DDMFormValuesJSONDeserializer _ddmFormValuesJSONDeserializer;
	private UserLocalService _userLocalService;

}