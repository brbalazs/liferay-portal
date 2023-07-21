/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.layout.util.GroupControlPanelLayoutUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

/**
 * @author Pavel Savinov
 */
public abstract class BaseAddLayoutMVCActionCommand
	extends BaseMVCActionCommand {

	protected String getContentRedirectURL(
			ActionResponse actionResponse, Layout layout)
		throws PortalException {

		LiferayPortletResponse liferayPortletResponse =
			PortalUtil.getLiferayPortletResponse(actionResponse);

		long groupControlPanelPlid =
			GroupControlPanelLayoutUtil.getGroupControlPanelPlid(
				layout.getGroup());

		PortletURL editLayoutURL = liferayPortletResponse.createRenderURL(
			groupControlPanelPlid);

		editLayoutURL.setParameter("mvcPath", "/edit_content_layout.jsp");

		PortletURL redirectURL = liferayPortletResponse.createRenderURL();

		editLayoutURL.setParameter(
			"redirect",
			HttpUtil.setParameter(
				redirectURL.toString(), "p_p_state",
				WindowState.MAXIMIZED.toString()));

		editLayoutURL.setParameter(
			"groupId", String.valueOf(layout.getGroupId()));
		editLayoutURL.setParameter("selPlid", String.valueOf(layout.getPlid()));

		return editLayoutURL.toString();
	}

	protected String getRedirectURL(
		ActionRequest actionRequest, ActionResponse actionResponse,
		Layout layout) {

		LiferayPortletResponse liferayPortletResponse =
			PortalUtil.getLiferayPortletResponse(actionResponse);

		PortletURL configureLayoutURL =
			liferayPortletResponse.createRenderURL();

		configureLayoutURL.setParameter(
			"mvcRenderCommandName", "/layout/edit_layout");

		String backURL = ParamUtil.getString(actionRequest, "backURL");

		if (Validator.isNull(backURL)) {
			PortletURL redirectURL = liferayPortletResponse.createRenderURL();

			backURL = HttpUtil.setParameter(
				redirectURL.toString(), "p_p_state",
				WindowState.NORMAL.toString());
		}

		configureLayoutURL.setParameter("redirect", backURL);

		String portletResource = ParamUtil.getString(
			actionRequest, "portletResource");

		configureLayoutURL.setParameter("portletResource", portletResource);

		configureLayoutURL.setParameter(
			"groupId", String.valueOf(layout.getGroupId()));
		configureLayoutURL.setParameter(
			"selPlid", String.valueOf(layout.getPlid()));
		configureLayoutURL.setParameter(
			"privateLayout", String.valueOf(layout.isPrivateLayout()));

		return configureLayoutURL.toString();
	}

}