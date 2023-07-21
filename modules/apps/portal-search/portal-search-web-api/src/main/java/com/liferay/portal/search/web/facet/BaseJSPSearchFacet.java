/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.facet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eudaldo Alonso
 */
public abstract class BaseJSPSearchFacet extends BaseSearchFacet {

	public abstract String getConfigurationJspPath();

	public abstract String getDisplayJspPath();

	@Override
	public void includeConfiguration(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		if (Validator.isNull(getConfigurationJspPath())) {
			return;
		}

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(getConfigurationJspPath());

		try {
			requestDispatcher.include(request, response);
		}
		catch (ServletException se) {
			_log.error("Unable to include JSP " + getDisplayJspPath(), se);

			throw new IOException(
				"Unable to include " + getDisplayJspPath(), se);
		}
	}

	@Override
	public void includeView(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		if (Validator.isNull(getDisplayJspPath())) {
			return;
		}

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(getDisplayJspPath());

		try {
			requestDispatcher.include(request, response);
		}
		catch (ServletException se) {
			_log.error("Unable to include JSP", se);

			throw new IOException(
				"Unable to include " + getDisplayJspPath(), se);
		}
	}

	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseJSPSearchFacet.class);

	private ServletContext _servletContext;

}