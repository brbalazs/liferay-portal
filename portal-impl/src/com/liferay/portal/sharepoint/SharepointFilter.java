/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.sharepoint;

import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.servlet.filters.secure.BaseAuthFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Bruno Farache
 * @author Alexander Chow
 */
public class SharepointFilter extends BaseAuthFilter {

	@Override
	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);

		setUsePermissionChecker(true);
	}

	protected boolean isSharepointRequest(String uri) {
		if (uri == null) {
			return false;
		}

		if (uri.endsWith("*.asmx")) {
			return true;
		}

		for (String prefix : _PREFIXES) {
			if (uri.startsWith(prefix)) {
				return true;
			}
		}

		return false;
	}

	protected boolean isWebDAVRequest(String uri) {
		if (uri.startsWith("/webdav")) {
			return true;
		}

		return false;
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		String method = request.getMethod();

		String userAgent = GetterUtil.getString(
			request.getHeader(HttpHeaders.USER_AGENT));

		if ((userAgent.startsWith(
				"Microsoft Data Access Internet Publishing") ||
			 userAgent.startsWith("Microsoft Office Protocol Discovery")) &&
			method.equals(HttpMethods.OPTIONS)) {

			setOptionsHeaders(request, response);

			return;
		}

		if (!isSharepointRequest(request.getRequestURI())) {
			processFilter(
				SharepointFilter.class.getName(), request, response,
				filterChain);

			return;
		}

		if (method.equals(HttpMethods.GET) || method.equals(HttpMethods.HEAD)) {
			setGetHeaders(response);
		}
		else if (method.equals(HttpMethods.POST)) {
			setPostHeaders(response);
		}

		super.processFilter(request, response, filterChain);
	}

	protected void setGetHeaders(HttpServletResponse response) {
		response.setContentType("text/html");

		response.setHeader(
			"Public-Extension", "http://schemas.microsoft.com/repl-2");
		response.setHeader(
			"MicrosoftSharePointTeamServices", SharepointUtil.VERSION);
		response.setHeader("Cache-Control", "no-cache");
	}

	protected void setOptionsHeaders(
		HttpServletRequest request, HttpServletResponse response) {

		if (isWebDAVRequest(request.getRequestURI())) {
			response.setHeader("MS-Author-Via", "DAV,MS-FP/4.0");
		}
		else {
			response.setHeader("MS-Author-Via", "MS-FP/4.0,DAV");
		}

		response.setHeader("MicrosoftOfficeWebServer", "5.0_Collab");
		response.setHeader(
			"MicrosoftSharePointTeamServices", SharepointUtil.VERSION);
		response.setHeader("DAV", "1,2");
		response.setHeader("Accept-Ranges", "none");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader(
			"Allow",
			"COPY, DELETE, GET, GETLIB, HEAD, LOCK, MKCOL, MOVE, OPTIONS, " +
				"POST, PROPFIND, PROPPATCH, PUT, UNLOCK");
	}

	protected void setPostHeaders(HttpServletResponse response) {
		response.setContentType("application/x-vermeer-rpc");

		response.setHeader(
			"MicrosoftSharePointTeamServices", SharepointUtil.VERSION);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Connection", "close");
	}

	private static final String[] _PREFIXES = {
		"/_vti_inf.html", "/_vti_bin", "/sharepoint", "/history", "/resources"
	};

}