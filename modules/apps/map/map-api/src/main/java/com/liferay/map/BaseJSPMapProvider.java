/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.map;

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
 * @author Jürgen Kappler
 */
public abstract class BaseJSPMapProvider implements MapProvider {

	public abstract String getConfigurationJspPath();

	public abstract String getJspPath();

	@Override
	public boolean include(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		return includeJSP(request, response, getJspPath());
	}

	@Override
	public boolean includeConfiguration(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		return includeJSP(request, response, getConfigurationJspPath());
	}

	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	protected boolean includeJSP(
			HttpServletRequest request, HttpServletResponse response,
			String jspPath)
		throws IOException {

		if (Validator.isNull(jspPath)) {
			return false;
		}

		RequestDispatcher requestDispatcher =
			_servletContext.getRequestDispatcher(jspPath);

		prepareRequest(request);

		try {
			requestDispatcher.include(request, response);
		}
		catch (ServletException se) {
			_log.error("Unable to include " + jspPath, se);

			return false;
		}

		return true;
	}

	protected abstract void prepareRequest(HttpServletRequest request);

	private static final Log _log = LogFactoryUtil.getLog(
		BaseJSPMapProvider.class);

	private ServletContext _servletContext;

}