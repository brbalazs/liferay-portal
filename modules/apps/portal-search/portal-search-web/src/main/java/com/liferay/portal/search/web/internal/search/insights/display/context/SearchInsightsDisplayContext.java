/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.search.insights.display.context;

import java.io.Serializable;

/**
 * @author Bryan Engler
 */
public class SearchInsightsDisplayContext implements Serializable {

	public String getQueryString() {
		return _queryString;
	}

	public void setQueryString(String queryString) {
		_queryString = queryString;
	}

	private String _queryString;

}