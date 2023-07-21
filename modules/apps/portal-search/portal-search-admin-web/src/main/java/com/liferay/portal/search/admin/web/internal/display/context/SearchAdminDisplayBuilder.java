/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
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
				navigationItemList, "connections", selectedTab);
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
			!Objects.equals(selectedTab, "connections")) {

			return "index-actions";
		}

		if (Objects.equals(selectedTab, "index-actions")) {
			return "index-actions";
		}

		if (Objects.equals(selectedTab, "connections") &&
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