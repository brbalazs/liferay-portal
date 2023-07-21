/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.announcements.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.SafeConsumer;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Tardín
 */
public class AnnouncementsAdminViewManagementToolbarDisplayContext {

	public AnnouncementsAdminViewManagementToolbarDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		HttpServletRequest request, SearchContainer searchContainer) {

		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_request = request;
		_searchContainer = searchContainer;

		_currentURLObj = PortletURLUtil.getCurrent(
			_liferayPortletRequest, _liferayPortletResponse);
	}

	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData("action", "deleteEntries");
						dropdownItem.setIcon("times");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "delete"));
						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	public CreationMenu getCreationMenu() {
		return new CreationMenu() {
			{
				addDropdownItem(
					dropdownItem -> {
						PortletURL addEntryURL =
							_liferayPortletResponse.createRenderURL();

						addEntryURL.setParameter(
							"mvcRenderCommandName",
							"/announcements/edit_entry");
						addEntryURL.setParameter(
							"redirect", PortalUtil.getCurrentURL(_request));

						String navigation = _getNavigation();

						addEntryURL.setParameter(
							"alert",
							String.valueOf(
								String.valueOf(navigation.equals("alerts"))));

						String distributionScope = ParamUtil.getString(
							_request, "distributionScope");

						addEntryURL.setParameter(
							"distributionScope", distributionScope);

						dropdownItem.setHref(addEntryURL);

						String label = null;

						if (navigation.equals("alerts")) {
							label = "add-alert";
						}
						else {
							label = "add-announcement";
						}

						dropdownItem.setLabel(
							LanguageUtil.get(_request, label));
					});
			}
		};
	}

	public List<DropdownItem> getFilterDropdownItems() {
		return new DropdownItemList() {
			{
				addGroup(
					SafeConsumer.ignore(
						dropdownGroupItem -> {
							dropdownGroupItem.setDropdownItems(
								_getFilterNavigationDropdownItems());
							dropdownGroupItem.setLabel(
								LanguageUtil.get(
									_request, "filter-by-navigation"));
						}));
			}
		};
	}

	public int getTotal() {
		return _searchContainer.getTotal();
	}

	public boolean isDisabled() {
		return false;
	}

	private List<DropdownItem> _getFilterNavigationDropdownItems()
		throws Exception {

		return new DropdownItemList() {
			{
				AnnouncementsAdminViewDisplayContext
					announcementsAdminViewDisplayContext =
						new DefaultAnnouncementsAdminViewDisplayContext(
							_request);

				PortletURL navigationURL = PortletURLUtil.clone(
					_currentURLObj, _liferayPortletResponse);

				String currentDistributionScopeLabel =
					announcementsAdminViewDisplayContext.
						getCurrentDistributionScopeLabel();

				Map<String, String> distributionScopes =
					announcementsAdminViewDisplayContext.
						getDistributionScopes();

				for (Map.Entry<String, String> distributionScopeEntry :
						distributionScopes.entrySet()) {

					add(
						dropdownItem -> {
							dropdownItem.setActive(
								currentDistributionScopeLabel.equals(
									distributionScopeEntry.getKey()));
							dropdownItem.setHref(
								navigationURL, "distributionScope",
								distributionScopeEntry.getValue());
							dropdownItem.setLabel(
								LanguageUtil.get(
									_request, distributionScopeEntry.getKey()));
						});
				}
			}
		};
	}

	private String _getNavigation() {
		return ParamUtil.getString(_request, "navigation", "announcements");
	}

	private final PortletURL _currentURLObj;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final HttpServletRequest _request;
	private final SearchContainer _searchContainer;

}