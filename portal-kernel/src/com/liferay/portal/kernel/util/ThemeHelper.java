/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;

import java.net.URL;

import java.util.Objects;

import javax.servlet.ServletContext;

/**
 * @author Raymond Augé
 */
public class ThemeHelper {

	public static final String TEMPLATE_EXTENSION_FTL = "ftl";

	public static final String TEMPLATE_EXTENSION_JSP = "jsp";

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public static final String TEMPLATE_EXTENSION_VM = "vm";

	public static String getResourcePath(
		ServletContext servletContext, Theme theme, String portletId,
		String path) {

		StringBundler sb = new StringBundler(11);

		String themeContextName = GetterUtil.getString(
			theme.getServletContextName());

		sb.append(themeContextName);

		String servletContextName = StringPool.BLANK;

		String contextPath = servletContext.getContextPath();

		if (!Objects.equals(
				PortalUtil.getPathContext(contextPath),
				PortalUtil.getPathContext())) {

			servletContextName = GetterUtil.getString(
				servletContext.getServletContextName());
		}

		String extension = theme.getTemplateExtension();

		if (extension.equals(TEMPLATE_EXTENSION_FTL)) {
			sb.append(theme.getFreeMarkerTemplateLoader());
			sb.append(theme.getTemplatesPath());

			if (Validator.isNotNull(servletContextName) &&
				!path.startsWith(StringPool.SLASH.concat(servletContextName))) {

				sb.append(StringPool.SLASH);
				sb.append(servletContextName);
			}

			sb.append(StringPool.SLASH);

			int start = 0;

			if (path.startsWith(StringPool.SLASH)) {
				start = 1;
			}

			int end = path.lastIndexOf(CharPool.PERIOD);

			sb.append(path.substring(start, end));

			sb.append(StringPool.PERIOD);

			if (Validator.isNotNull(portletId)) {
				sb.append(portletId);
				sb.append(StringPool.PERIOD);
			}

			sb.append(TEMPLATE_EXTENSION_FTL);

			return sb.toString();
		}

		return path;
	}

	public static boolean resourceExists(
			ServletContext servletContext, Theme theme, String portletId,
			String path)
		throws Exception {

		Boolean exists = null;

		if (Validator.isNotNull(portletId)) {
			exists = _resourceExists(servletContext, theme, portletId, path);

			if (!exists && PortletIdCodec.hasInstanceId(portletId)) {
				String rootPortletId = PortletIdCodec.decodePortletName(
					portletId);

				exists = _resourceExists(
					servletContext, theme, rootPortletId, path);
			}

			if (!exists) {
				exists = _resourceExists(servletContext, theme, null, path);
			}
		}

		if (exists == null) {
			exists = _resourceExists(servletContext, theme, portletId, path);
		}

		return exists;
	}

	private static boolean _resourceExists(
			ServletContext servletContext, Theme theme, String portletId,
			String path)
		throws Exception {

		if (Validator.isNull(path)) {
			return false;
		}

		String resourcePath = getResourcePath(
			servletContext, theme, portletId, path);

		String extension = theme.getTemplateExtension();

		if (extension.equals(TEMPLATE_EXTENSION_FTL)) {
			return TemplateResourceLoaderUtil.hasTemplateResource(
				TemplateConstants.LANG_TYPE_FTL, resourcePath);
		}

		URL url = null;

		if (theme.isWARFile()) {
			ServletContext themeServletContext = servletContext.getContext(
				theme.getContextPath());

			url = themeServletContext.getResource(resourcePath);
		}
		else {
			url = servletContext.getResource(resourcePath);
		}

		if (url == null) {
			return false;
		}

		return true;
	}

}