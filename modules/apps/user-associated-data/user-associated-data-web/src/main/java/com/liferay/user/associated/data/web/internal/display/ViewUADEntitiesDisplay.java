/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.display;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.portal.kernel.dao.search.SearchContainer;

import java.util.List;

/**
 * @author Drew Brokke
 */
public class ViewUADEntitiesDisplay {

	public String getApplicationKey() {
		return _applicationKey;
	}

	public List<NavigationItem> getNavigationItems() {
		return _navigationItems;
	}

	public SearchContainer<UADEntity> getSearchContainer() {
		return _searchContainer;
	}

	public String getTypeName() {
		return _typeName;
	}

	public String getUADRegistryKey() {
		return _uadRegistryKey;
	}

	public void setApplicationKey(String applicationKey) {
		_applicationKey = applicationKey;
	}

	public void setNavigationItems(List<NavigationItem> navigationItems) {
		_navigationItems = navigationItems;
	}

	public void setSearchContainer(SearchContainer<UADEntity> searchContainer) {
		_searchContainer = searchContainer;
	}

	public void setTypeName(String typeName) {
		_typeName = typeName;
	}

	public void setUADRegistryKey(String uadRegistryKey) {
		_uadRegistryKey = uadRegistryKey;
	}

	private String _applicationKey;
	private List<NavigationItem> _navigationItems;
	private SearchContainer<UADEntity> _searchContainer;
	private String _typeName;
	private String _uadRegistryKey;

}