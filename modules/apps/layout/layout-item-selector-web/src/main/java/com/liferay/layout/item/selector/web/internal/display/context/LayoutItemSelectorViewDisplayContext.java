/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.item.selector.web.internal.display.context;

import com.liferay.item.selector.constants.ItemSelectorPortletKeys;
import com.liferay.layout.item.selector.criterion.LayoutItemSelectorCriterion;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Objects;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Roberto Díaz
 */
public class LayoutItemSelectorViewDisplayContext {

	public LayoutItemSelectorViewDisplayContext(
		HttpServletRequest httpServletRequest,
		LayoutItemSelectorCriterion layoutItemSelectorCriterion,
		PortletURL portletURL, String itemSelectedEventName,
		boolean privateLayout) {

		_httpServletRequest = httpServletRequest;
		_layoutItemSelectorCriterion = layoutItemSelectorCriterion;
		_portletURL = portletURL;
		_itemSelectedEventName = itemSelectedEventName;
		_privateLayout = privateLayout;
	}

	public String getItemSelectedEventName() {
		return _itemSelectedEventName;
	}

	public PortletURL getPortletURL() {
		return _portletURL;
	}

	public boolean isCheckDisplayPage() {
		return _layoutItemSelectorCriterion.isCheckDisplayPage();
	}

	public boolean isEnableCurrentPage() {
		return _layoutItemSelectorCriterion.isEnableCurrentPage();
	}

	public boolean isFollowURLOnTitleClick() {
		return _layoutItemSelectorCriterion.isFollowURLOnTitleClick();
	}

	public boolean isMultiSelection() {
		return _layoutItemSelectorCriterion.isMultiSelection();
	}

	public boolean isPrivateLayout() {
		return _privateLayout;
	}

	public boolean isShowBreadcrumb() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		boolean showBreadcrumb = ParamUtil.getBoolean(
			_httpServletRequest, "showBreadcrumb");

		if (Objects.equals(
				ItemSelectorPortletKeys.ITEM_SELECTOR,
				portletDisplay.getPortletName()) ||
			showBreadcrumb) {

			return true;
		}

		return false;
	}

	public boolean isShowHiddenPages() {
		return _layoutItemSelectorCriterion.isShowHiddenPages();
	}

	private final HttpServletRequest _httpServletRequest;
	private final String _itemSelectedEventName;
	private final LayoutItemSelectorCriterion _layoutItemSelectorCriterion;
	private final PortletURL _portletURL;
	private final boolean _privateLayout;

}