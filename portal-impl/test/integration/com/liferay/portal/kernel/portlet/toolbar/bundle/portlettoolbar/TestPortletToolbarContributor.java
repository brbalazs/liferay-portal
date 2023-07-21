/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet.toolbar.bundle.portlettoolbar;

import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributor;
import com.liferay.portal.kernel.servlet.taglib.ui.Menu;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * @author Philip Jones
 */
public class TestPortletToolbarContributor
	implements PortletToolbarContributor {

	public static final String LABEL = "LABEL";

	@Override
	public List<Menu> getPortletTitleMenus(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		List<Menu> portletTitleMenus = new ArrayList<>();

		Menu menu = new Menu();

		menu.setLabel(LABEL);

		portletTitleMenus.add(menu);

		return portletTitleMenus;
	}

}