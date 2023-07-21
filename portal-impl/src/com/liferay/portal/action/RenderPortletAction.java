/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.action;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletContainerUtil;
import com.liferay.portal.kernel.portlet.WindowStateFactory;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Brian Wing Shun Chan
 */
public class RenderPortletAction extends Action {

	@Override
	public ActionForward execute(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		themeDisplay.setAjax(true);

		String ajaxId = request.getParameter("ajax_id");

		long companyId = PortalUtil.getCompanyId(request);
		User user = PortalUtil.getUser(request);
		Layout layout = (Layout)request.getAttribute(WebKeys.LAYOUT);
		String portletId = ParamUtil.getString(request, "p_p_id");

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			companyId, portletId);

		String columnId = ParamUtil.getString(request, "p_p_col_id");
		int columnPos = ParamUtil.getInteger(request, "p_p_col_pos");
		int columnCount = ParamUtil.getInteger(request, "p_p_col_count");

		Boolean boundary = null;

		String boundaryParam = ParamUtil.getString(
			request, "p_p_boundary", null);

		if (boundaryParam != null) {
			boundary = GetterUtil.getBoolean(boundaryParam);
		}

		Boolean decorate = null;

		String decorateParam = ParamUtil.getString(
			request, "p_p_decorate", null);

		if (decorateParam != null) {
			decorate = GetterUtil.getBoolean(decorateParam);
		}

		boolean staticPortlet = ParamUtil.getBoolean(request, "p_p_static");

		if (staticPortlet) {
			portlet = (Portlet)portlet.clone();

			portlet.setStatic(true);

			boolean staticStartPortlet = ParamUtil.getBoolean(
				request, "p_p_static_start");

			portlet.setStaticStart(staticStartPortlet);
		}

		if (ajaxId != null) {
			response.setHeader("Ajax-ID", ajaxId);
		}

		WindowState windowState = WindowStateFactory.getWindowState(
			ParamUtil.getString(request, "p_p_state"));

		PortalUtil.updateWindowState(
			portletId, user, layout, windowState, request);

		request = PortletContainerUtil.setupOptionalRenderParameters(
			request, null, columnId, columnPos, columnCount, boundary,
			decorate);

		PortletContainerUtil.processPublicRenderParameters(
			request, themeDisplay.getLayout());

		PortletContainerUtil.render(request, response, portlet);

		return null;
	}

}