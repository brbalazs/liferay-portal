/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.analytics.settings.web.internal.portlet.action;

import com.liferay.analytics.settings.web.internal.constants.AnalyticsSettingsPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rachael Koestartyo
 */
@Component(
	property = {
		"javax.portlet.name=" + AnalyticsSettingsPortletKeys.ANALYTICS_ADMIN_PORTLET,
		"mvc.command.name=/analytics_settings/view"
	},
	service = MVCRenderCommand.class
)
public class ViewMVCRenderCommand extends BaseAnalyticsMVCRenderCommand {

	@Override
	protected String getJspPath() {
		return "/view.jsp";
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.analytics.settings.web)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		super.servletContext = servletContext;
	}

}