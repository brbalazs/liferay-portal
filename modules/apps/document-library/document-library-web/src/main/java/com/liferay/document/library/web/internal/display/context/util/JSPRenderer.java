/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.display.context.util;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Iván Zaera
 */
public class JSPRenderer {

	public JSPRenderer(String jspPath) {
		_jspPath = jspPath;
	}

	public void render(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		Map<String, Object> savedAttributes = new HashMap<>();

		for (Map.Entry<String, Object> entry : _attributes.entrySet()) {
			String key = entry.getKey();

			savedAttributes.put(key, request.getAttribute(key));

			request.setAttribute(key, entry.getValue());
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(
			_jspPath);

		requestDispatcher.include(request, response);

		for (Map.Entry<String, Object> entry : savedAttributes.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
	}

	public void setAttribute(String key, Object value) {
		_attributes.put(key, value);
	}

	private final Map<String, Object> _attributes = new HashMap<>();
	private final String _jspPath;

}