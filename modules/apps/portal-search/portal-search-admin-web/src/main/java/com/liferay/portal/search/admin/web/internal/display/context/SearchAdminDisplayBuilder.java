/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.admin.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.engine.SearchEngineInformation;

import java.util.Objects;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Adam Brandizzi
 */
public class SearchAdminDisplayBuilder {

	public SearchAdminDisplayBuilder(
		Language language, Portal portal, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_language = language;
		_portal = portal;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
	}

	public SearchAdminDisplayContext build() {
		SearchAdminDisplayContext searchAdminDisplayContext =
			new SearchAdminDisplayContext();

		NavigationItemList navigationItemList = new NavigationItemList();
		String selectedTab = getSelectedTab();

		addNavigationItemList(navigationItemList, "index-actions", selectedTab);

		if (isSearchEngineInformationAvailable()) {
			addNavigationItemList(
				navigationItemList, "search-engine", selectedTab);
		}

		searchAdminDisplayContext.setNavigationItemList(navigationItemList);
		searchAdminDisplayContext.setSelectedTab(selectedTab);

		return searchAdminDisplayContext;
	}

	public void setSearchEngineInformation(
		SearchEngineInformation searchEngineInformation) {

		_searchEngineInformation = searchEngineInformation;
	}

	protected void addNavigationItemList(
		NavigationItemList navigationItemList, String label,
		String selectedTab) {

		navigationItemList.add(
			navigationItem -> {
				navigationItem.setActive(selectedTab.equals(label));
				navigationItem.setHref(
					_renderResponse.createRenderURL(), "tabs1", label);
				navigationItem.setLabel(
					_language.get(
						_portal.getHttpServletRequest(_renderRequest), label));
			});
	}

	protected String getSelectedTab() {
		String selectedTab = ParamUtil.getString(
			_renderRequest, "tabs1", "index-actions");

		if (!Objects.equals(selectedTab, "index-actions") &&
			!Objects.equals(selectedTab, "search-engine")) {

			return "index-actions";
		}

		if (Objects.equals(selectedTab, "index-actions")) {
			return "index-actions";
		}

		if (Objects.equals(selectedTab, "search-engine") &&
			!isSearchEngineInformationAvailable()) {

			return "index-actions";
		}

		return selectedTab;
	}

	protected boolean isSearchEngineInformationAvailable() {
		if ((_searchEngineInformation != null) &&
			(_searchEngineInformation.getConnectionInformationList() != null)) {

			return true;
		}

		return false;
	}

	private final Language _language;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private SearchEngineInformation _searchEngineInformation;

}