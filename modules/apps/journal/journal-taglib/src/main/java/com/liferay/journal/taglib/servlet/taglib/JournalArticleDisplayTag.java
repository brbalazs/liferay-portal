/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.taglib.servlet.taglib;

import com.liferay.journal.model.JournalArticleDisplay;
import com.liferay.journal.taglib.internal.servlet.ServletContextUtil;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Alejandro Tardín
 */
public class JournalArticleDisplayTag extends IncludeTag {

	public void setArticleDisplay(JournalArticleDisplay articleDisplay) {
		_articleDisplay = articleDisplay;
	}

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);

		servletContext = ServletContextUtil.getServletContext();
	}

	public void setShowTitle(boolean showTitle) {
		_showTitle = showTitle;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_articleDisplay = null;
		_showTitle = false;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute(
			"liferay-journal:journal-article:articleDisplay", _articleDisplay);
		request.setAttribute(
			"liferay-journal:journal-article:showTitle",
			String.valueOf(_showTitle));
	}

	private static final String _PAGE = "/journal_article/page.jsp";

	private JournalArticleDisplay _articleDisplay;
	private boolean _showTitle;

}