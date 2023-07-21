/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class SearchFormTag<R> extends IncludeTag {

	public void setSearchContainer(SearchContainer<?> searchContainer) {
		_searchContainer = searchContainer;
	}

	public void setShowAddButton(boolean showAddButton) {
		_showAddButton = showAddButton;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_searchContainer = null;
		_showAddButton = false;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		SearchContainerTag<R> searchContainerTag =
			(SearchContainerTag<R>)findAncestorWithClass(
				this, SearchContainerTag.class);

		if (searchContainerTag != null) {
			_searchContainer = searchContainerTag.getSearchContainer();

			request.setAttribute(
				"liferay-ui:search:compactEmptyResultsMessage",
				String.valueOf(
					searchContainerTag.isCompactEmptyResultsMessage()));
		}

		request.setAttribute(
			"liferay-ui:search:searchContainer", _searchContainer);
		request.setAttribute(
			"liferay-ui:search:showAddButton", String.valueOf(_showAddButton));
	}

	private SearchContainer<?> _searchContainer;
	private boolean _showAddButton;

}