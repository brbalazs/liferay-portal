/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.my.account.web.internal.application.list;

import com.liferay.application.list.PanelApp;
import com.liferay.application.list.UserMenuPanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.my.account.web.internal.constants.MyAccountPortletKeys;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=100",
		"panel.category.key=" + PanelCategoryKeys.USER_MY_ACCOUNT
	},
	service = PanelApp.class
)
public class MyAccountPanelApp extends UserMenuPanelApp {

	@Override
	public String getPortletId() {
		return MyAccountPortletKeys.MY_ACCOUNT;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + MyAccountPortletKeys.MY_ACCOUNT + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}