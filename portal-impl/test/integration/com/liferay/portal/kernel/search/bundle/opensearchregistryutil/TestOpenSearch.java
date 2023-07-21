/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search.bundle.opensearchregistryutil;

import com.liferay.portal.kernel.search.OpenSearch;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = OpenSearch.class
)
public class TestOpenSearch implements OpenSearch {

	@Override
	public String getClassName() {
		return TestOpenSearch.class.getName();
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public String search(
		HttpServletRequest request, long groupId, long userId, String keywords,
		int startPage, int itemsPerPage, String format) {

		return groupId + ":" + userId;
	}

	@Override
	public String search(
		HttpServletRequest request, long userId, String keywords, int startPage,
		int itemsPerPage, String format) {

		return userId + ":" + startPage;
	}

	@Override
	public String search(HttpServletRequest request, String url) {
		return url;
	}

}