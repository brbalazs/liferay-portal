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

package com.liferay.commerce.organization.web.internal.portlet;

import com.liferay.commerce.organization.constants.CommerceOrganizationPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.BasePortletProvider;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.ViewPortletProvider;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.portal.kernel.model.Organization",
	service = ViewPortletProvider.class
)
public class CommerceOrganizationViewPortletProvider
	extends BasePortletProvider implements ViewPortletProvider {

	@Override
	public String getPortletName() {
		return CommerceOrganizationPortletKeys.COMMERCE_ORGANIZATION;
	}

	@Override
	public PortletURL getPortletURL(HttpServletRequest request, Group group)
		throws PortalException {

		if (group == null) {
			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			group = themeDisplay.getScopeGroup();
		}

		long plid = _portal.getPlidFromPortletId(
			group.getGroupId(), getPortletName());

		PortletURL portletURL = PortletURLFactoryUtil.create(
			request, getPortletName(), plid, PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"mvcRenderCommandName", "viewCommerceOrganization");

		return portletURL;
	}

	@Reference
	private Portal _portal;

}