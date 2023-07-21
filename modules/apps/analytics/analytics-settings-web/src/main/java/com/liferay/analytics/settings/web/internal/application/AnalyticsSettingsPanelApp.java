/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.analytics.settings.web.internal.application;

import com.liferay.analytics.settings.web.internal.constants.AnalyticsSettingsPortletKeys;
import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rachael Koestartyo
 */
@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=250",
		"panel.category.key=" + PanelCategoryKeys.CONTROL_PANEL_CONFIGURATION
	},
	service = PanelApp.class
)
public class AnalyticsSettingsPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return AnalyticsSettingsPortletKeys.ANALYTICS_ADMIN_PORTLET;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + AnalyticsSettingsPortletKeys.ANALYTICS_ADMIN_PORTLET + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}