/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.knowledge.base.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.SafeConsumer;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Tardín
 */
public class KBSuggestionListManagementToolbarDisplayContext {

	public KBSuggestionListManagementToolbarDisplayContext(
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
						dropdownItem.putData("action", "deleteKBComments");
						dropdownItem.setIcon("times");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "delete"));
						dropdownItem.setQuickAction(true);
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

				addGroup(
					SafeConsumer.ignore(
						dropdownGroupItem -> {
							dropdownGroupItem.setDropdownItems(
								_getOrderByDropdownItems());
							dropdownGroupItem.setLabel(
								LanguageUtil.get(_request, "order-by"));
						}));
			}
		};
	}

	public String getOrderByType() {
		return _searchContainer.getOrderByType();
	}

	public SearchContainer getSearchContainer() {
		return _searchContainer;
	}

	public PortletURL getSortingURL() throws PortletException {
		PortletURL sortingURL = _getCurrentSortingURL();

		sortingURL.setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc");

		return sortingURL;
	}

	public int getTotal() {
		return _searchContainer.getTotal();
	}

	public boolean isDisabled() {
		String navigation = _getNavigation();

		if (navigation.equals("all") && !_searchContainer.hasResults()) {
			return true;
		}

		return false;
	}

	private PortletURL _getCurrentSortingURL() throws PortletException {
		PortletURL sortingURL = PortletURLUtil.clone(
			_currentURLObj, _liferayPortletResponse);

		sortingURL.setParameter(
			"storeOrderByPreference", Boolean.TRUE.toString());

		return sortingURL;
	}

	private List<DropdownItem> _getFilterNavigationDropdownItems()
		throws PortletException {

		return new DropdownItemList() {
			{
				String navigation = _getNavigation();
				String[] navigationKeys = {
					"all", "new", "in-progress", "resolved"
				};

				PortletURL navigationURL = PortletURLUtil.clone(
					_currentURLObj, _liferayPortletResponse);

				navigationURL.setParameter(
					"storeOrderByPreference", Boolean.FALSE.toString());

				for (String navigationKey : navigationKeys) {
					add(
						dropdownItem -> {
							dropdownItem.setActive(
								navigation.equals(navigationKey));
							dropdownItem.setHref(
								navigationURL, "navigation", navigationKey);
							dropdownItem.setLabel(
								LanguageUtil.get(_request, navigationKey));
						});
				}
			}
		};
	}

	private String _getNavigation() {
		return ParamUtil.getString(_request, "navigation", "all");
	}

	private String _getOrderByCol() {
		return _searchContainer.getOrderByCol();
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				final Map<String, String> orderColumnsMap = new HashMap<>();

				String navigation = _getNavigation();

				if (navigation.equals("all")) {
					orderColumnsMap.put("status", "status");
				}

				orderColumnsMap.put("modified-date", "modified-date");
				orderColumnsMap.put("user-name", "user-name");

				for (Map.Entry<String, String> orderByColEntry :
						orderColumnsMap.entrySet()) {

					add(
						SafeConsumer.ignore(
							dropdownItem -> {
								String orderByCol = orderByColEntry.getKey();

								dropdownItem.setActive(
									orderByCol.equals(_getOrderByCol()));

								dropdownItem.setHref(
									_getCurrentSortingURL(), "orderByCol",
									orderByColEntry.getValue());
								dropdownItem.setLabel(
									LanguageUtil.get(_request, orderByCol));
							}));
				}
			}
		};
	}

	private final PortletURL _currentURLObj;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final HttpServletRequest _request;
	private final SearchContainer _searchContainer;

}