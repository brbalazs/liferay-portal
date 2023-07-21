/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.internal.servlet;

import com.liferay.frontend.js.loader.modules.extender.internal.Details;
import com.liferay.frontend.js.loader.modules.extender.internal.resolution.BrowserModulesResolution;
import com.liferay.frontend.js.loader.modules.extender.internal.resolution.BrowserModulesResolver;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.URLDecoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rodolfo Roza Miranda
 */
@Component(
	configurationPid = "com.liferay.frontend.js.loader.modules.extender.internal.Details",
	immediate = true,
	property = {
		"osgi.http.whiteboard.servlet.name=com.liferay.frontend.js.loader.modules.extender.internal.servlet.JSResolveModulesServlet",
		"osgi.http.whiteboard.servlet.pattern=/js_resolve_modules",
		"service.ranking:Integer=" + Details.MAX_VALUE_LESS_1K
	},
	service = {JSResolveModulesServlet.class, Servlet.class}
)
public class JSResolveModulesServlet extends HttpServlet {

	@Override
	protected void service(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		response.setCharacterEncoding(StringPool.UTF8);
		response.setContentType(ContentTypes.APPLICATION_JSON);

		PrintWriter printWriter = new PrintWriter(
			response.getOutputStream(), true);

		List<String> moduleNames = _getModuleNames(request);

		BrowserModulesResolution browserModulesResolution =
			_browserModulesResolver.resolve(moduleNames, request);

		printWriter.write(browserModulesResolution.toJSON());

		printWriter.close();
	}

	private List<String> _getModuleNames(HttpServletRequest request)
		throws IOException {

		String[] moduleNames = null;

		String method = request.getMethod();

		if (method.equals("GET")) {
			moduleNames = ParamUtil.getStringValues(request, "modules");
		}
		else {
			String body = StringUtil.read(request.getInputStream());

			body = URLDecoder.decode(body, request.getCharacterEncoding());

			body = body.substring(8);

			moduleNames = body.split(StringPool.COMMA);
		}

		if (moduleNames != null) {
			return Arrays.asList(moduleNames);
		}

		return Collections.emptyList();
	}

	@Reference
	private BrowserModulesResolver _browserModulesResolver;

}