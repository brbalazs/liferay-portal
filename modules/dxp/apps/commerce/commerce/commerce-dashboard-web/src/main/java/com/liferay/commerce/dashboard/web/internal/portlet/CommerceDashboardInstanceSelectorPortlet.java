/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.dashboard.web.internal.portlet;

import com.liferay.commerce.dashboard.web.internal.constants.CommerceDashboardPortletKeys;
import com.liferay.commerce.dashboard.web.internal.display.context.CommerceDashboardInstanceSelectorDisplayContext;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=portlet-commerce-dashboard-instance-selector",
		"com.liferay.portlet.display-category=commerce",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.preferences-unique-per-layout=false",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"javax.portlet.display-name=Commerce Dashboard SKU Selector",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.view-template=/instance_selector/view.jsp",
		"javax.portlet.name=" + CommerceDashboardPortletKeys.COMMERCE_DASHBOARD_INSTANCE_SELECTOR,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = {CommerceDashboardInstanceSelectorPortlet.class, Portlet.class}
)
public class CommerceDashboardInstanceSelectorPortlet extends MVCPortlet {

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		try {
			CommerceDashboardInstanceSelectorDisplayContext
				commerceDashboardInstanceSelectorDisplayContext =
					new CommerceDashboardInstanceSelectorDisplayContext(
						_configurationProvider, _cpInstanceLocalService,
						_cpInstanceService, renderRequest);

			renderRequest.setAttribute(
				WebKeys.PORTLET_DISPLAY_CONTEXT,
				commerceDashboardInstanceSelectorDisplayContext);

			super.render(renderRequest, renderResponse);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private CPInstanceService _cpInstanceService;

}