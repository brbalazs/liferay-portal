/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.terms.of.use.internal;

import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.util.TermsOfUseContentProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.journal.model.JournalArticle",
	service = TermsOfUseContentProvider.class
)
public class JournalArticleTermsOfUseContentProvider
	implements TermsOfUseContentProvider {

	@Override
	public String getClassName() {
		return JournalArticle.class.getName();
	}

	@Override
	public void includeConfig(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(_JSP_PATH_CONFIGURATION);

		requestDispatcher.include(request, response);
	}

	@Override
	public void includeView(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(_JSP_PATH_VIEW);

		requestDispatcher.include(request, response);
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.journal.terms.of.use)",
		unbind = "-"
	)
	protected void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private static final String _JSP_PATH_CONFIGURATION = "/configuration.jsp";

	private static final String _JSP_PATH_VIEW = "/view.jsp";

	private ServletContext _servletContext;

}