/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.velocity;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.PortalWebResourceConstants;
import com.liferay.portal.kernel.servlet.PortalWebResourcesUtil;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.template.URLResourceParser;

import java.io.IOException;

import java.net.URL;

import javax.servlet.ServletContext;

/**
 * @author Alexander Chow
 * @author Raymond Augé
 */
public class VelocityServletResourceParser extends URLResourceParser {

	@Override
	public URL getURL(String source) throws IOException {
		int pos = source.indexOf(TemplateConstants.SERVLET_SEPARATOR);

		if (pos == -1) {
			return null;
		}

		String servletContextName = source.substring(0, pos);

		if (servletContextName.equals(PortalUtil.getPathContext())) {
			servletContextName = PortalUtil.getServletContextName();
		}

		ServletContext servletContext = ServletContextPool.get(
			servletContextName);

		if (servletContext == null) {
			_log.error(
				StringBundler.concat(
					source, " is not valid because ", servletContextName,
					" does not map to a servlet context"));

			return null;
		}

		String name = source.substring(
			pos + TemplateConstants.SERVLET_SEPARATOR.length());

		if (_log.isDebugEnabled()) {
			_log.debug(
				StringBundler.concat(
					name, " is associated with the servlet context ",
					servletContextName, " ", String.valueOf(servletContext)));
		}

		URL url = servletContext.getResource(name);

		if ((url == null) && name.endsWith("/init_custom.vm")) {
			if (_log.isWarnEnabled()) {
				_log.warn("The template " + name + " should be created");
			}

			ServletContext themeClassicServletContext =
				PortalWebResourcesUtil.getServletContext(
					PortalWebResourceConstants.RESOURCE_TYPE_THEME_CLASSIC);

			url = themeClassicServletContext.getResource(
				"/classic/templates/init_custom.vm");
		}

		return url;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		VelocityServletResourceParser.class);

}