/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.web.internal.util;

import com.liferay.configuration.admin.display.ConfigurationFormRenderer;
import com.liferay.configuration.admin.web.internal.constants.ConfigurationAdminWebKeys;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jorge Ferrer
 */
public class DefaultConfigurationFormRenderer
	implements ConfigurationFormRenderer {

	@Override
	public String getPid() {
		return "DEFAULT";
	}

	@Override
	public Map<String, Object> getRequestParameters(
		HttpServletRequest request) {

		return null;
	}

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		PrintWriter printWriter = response.getWriter();

		String html = (String)request.getAttribute(
			ConfigurationAdminWebKeys.CONFIGURATION_MODEL_FORM_HTML);

		printWriter.print(html);
	}

}