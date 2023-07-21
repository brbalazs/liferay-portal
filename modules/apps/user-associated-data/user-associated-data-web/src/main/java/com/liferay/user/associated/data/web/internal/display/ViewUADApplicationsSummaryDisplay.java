/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.web.internal.display;

import com.liferay.portal.kernel.dao.search.SearchContainer;

/**
 * @author Drew Brokke
 */
public class ViewUADApplicationsSummaryDisplay {

	public SearchContainer<UADApplicationSummaryDisplay> getSearchContainer() {
		return _searchContainer;
	}

	public int getTotalCount() {
		return _totalCount;
	}

	public void setSearchContainer(
		SearchContainer<UADApplicationSummaryDisplay> searchContainer) {

		_searchContainer = searchContainer;
	}

	public void setTotalCount(int totalCount) {
		_totalCount = totalCount;
	}

	private SearchContainer<UADApplicationSummaryDisplay> _searchContainer;
	private int _totalCount;

}