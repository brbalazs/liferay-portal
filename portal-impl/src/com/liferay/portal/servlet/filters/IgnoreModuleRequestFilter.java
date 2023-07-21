/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.servlet.filters;

import com.liferay.portal.kernel.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Raymond Augé
 */
public abstract class IgnoreModuleRequestFilter extends BasePortalFilter {

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		if (isModuleRequest(request)) {
			return false;
		}

		return super.isFilterEnabled(request, response);
	}

	protected boolean isModuleRequest(HttpServletRequest request) {
		String contextPath = request.getContextPath();

		String requestURI = request.getRequestURI();

		String resourcePath = requestURI;

		int index = requestURI.indexOf(contextPath);

		if (index == 0) {
			resourcePath = resourcePath.substring(contextPath.length());
		}

		if (resourcePath.startsWith(_MODULE_REQUEST_PREFIX)) {
			return true;
		}

		return false;
	}

	private static final String _MODULE_REQUEST_PREFIX =
		PortalUtil.getPathModule() + "/";

}