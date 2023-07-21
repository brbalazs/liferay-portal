/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.wiki.web.internal.display.context.util;

import com.liferay.portal.kernel.display.context.util.BaseRequestHelper;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.wiki.constants.WikiWebKeys;
import com.liferay.wiki.model.WikiNode;
import com.liferay.wiki.model.WikiPage;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Roberto Díaz
 */
public class WikiPageInfoPanelRequestHelper extends BaseRequestHelper {

	public WikiPageInfoPanelRequestHelper(HttpServletRequest request) {
		super(request);
	}

	public WikiNode getCurrentNode() {
		HttpServletRequest request = getRequest();

		WikiNode node = (WikiNode)request.getAttribute(WikiWebKeys.WIKI_NODE);

		if (node == null) {
			WikiPage page = getPage();

			if (page != null) {
				return page.getNode();
			}
		}

		return node;
	}

	public WikiPage getPage() {
		if (_page != null) {
			return _page;
		}

		HttpServletRequest request = getRequest();

		_page = (WikiPage)request.getAttribute(WikiWebKeys.WIKI_PAGE);

		return _page;
	}

	public List<WikiPage> getPages() {
		if (_pages != null) {
			return _pages;
		}

		HttpServletRequest request = getRequest();

		_pages = (List<WikiPage>)request.getAttribute(WikiWebKeys.WIKI_PAGES);

		if (_pages == null) {
			_pages = Collections.emptyList();
		}

		return _pages;
	}

	public boolean isShowSidebarHeader() {
		HttpServletRequest request = getRequest();

		boolean showSidebarHeader = GetterUtil.getBoolean(
			request.getAttribute(WikiWebKeys.SHOW_SIDEBAR_HEADER));

		return ParamUtil.getBoolean(
			request, "showSidebarHeader", showSidebarHeader);
	}

	private WikiPage _page;
	private List<WikiPage> _pages;

}