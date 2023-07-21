/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.BaseManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.SafeConsumer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Drew Brokke
 */
public class UADExportProcessManagementToolbarDisplayContext
	extends BaseManagementToolbarDisplayContext {

	public UADExportProcessManagementToolbarDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		HttpServletRequest request) {

		super(liferayPortletRequest, liferayPortletResponse, request);
	}

	@Override
	public CreationMenu getCreationMenu() {
		CreationMenu creationMenu = new CreationMenu();

		creationMenu.addPrimaryDropdownItem(
			SafeConsumer.ignore(
				dropdownItem -> {
					User selectedUser = PortalUtil.getSelectedUser(request);

					dropdownItem.setHref(
						liferayPortletResponse.createRenderURL(),
						"mvcRenderCommandName", "/add_uad_export_processes",
						"backURL", PortalUtil.getCurrentURL(request), "p_u_i_d",
						String.valueOf(selectedUser.getUserId()));

					dropdownItem.setLabel(
						LanguageUtil.get(request, "add-export-processes"));
				}));

		return creationMenu;
	}

	@Override
	public Boolean isSelectable() {
		return false;
	}

	@Override
	protected String[] getNavigationKeys() {
		return new String[] {"all", "in-progress", "successful", "failed"};
	}

	@Override
	protected String[] getOrderByKeys() {
		return new String[] {"create-date", "name"};
	}

}