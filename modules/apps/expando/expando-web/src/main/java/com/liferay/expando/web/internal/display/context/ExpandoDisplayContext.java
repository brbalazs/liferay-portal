/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.expando.web.internal.display.context;

import com.liferay.expando.web.internal.constants.ExpandoPortletKeys;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pei-Jung Lan
 */
public class ExpandoDisplayContext {

	public ExpandoDisplayContext(HttpServletRequest request) {
		_request = request;
	}

	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						PortletResponse portletResponse =
							(PortletResponse)_request.getAttribute(
								JavaConstants.JAVAX_PORTLET_RESPONSE);

						dropdownItem.setHref(
							StringBundler.concat(
								"javascript:", portletResponse.getNamespace(),
								"deleteCustomFields();"));

						dropdownItem.setIcon("trash");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "delete"));
						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	public CreationMenu getCreationMenu() throws PortalException {
		return new CreationMenu() {
			{
				addDropdownItem(
					dropdownItem -> {
						PortletResponse portletResponse =
							(PortletResponse)_request.getAttribute(
								JavaConstants.JAVAX_PORTLET_RESPONSE);

						LiferayPortletResponse liferayPortletResponse =
							PortalUtil.getLiferayPortletResponse(
								portletResponse);

						String modelResource = ParamUtil.getString(
							_request, "modelResource");

						dropdownItem.setHref(
							liferayPortletResponse.createRenderURL(), "mvcPath",
							"/edit_expando.jsp", "redirect",
							PortalUtil.getCurrentURL(_request), "modelResource",
							modelResource);

						dropdownItem.setLabel(
							LanguageUtil.get(_request, "add-custom-field"));
					});
			}
		};
	}

	public List<NavigationItem> getNavigationItems(final String label) {
		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(true);
						navigationItem.setHref(StringPool.BLANK);
						navigationItem.setLabel(
							LanguageUtil.get(_request, label));
					});
			}
		};
	}

	public boolean showCreationMenu() throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return PortletPermissionUtil.contains(
			themeDisplay.getPermissionChecker(), ExpandoPortletKeys.EXPANDO,
			ActionKeys.ADD_EXPANDO);
	}

	private final HttpServletRequest _request;

}