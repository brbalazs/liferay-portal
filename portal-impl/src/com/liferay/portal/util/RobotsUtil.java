/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.util;

import com.liferay.petra.content.ContentUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author David Truong
 * @author Jesse Rao
 */
public class RobotsUtil {

	public static String getDefaultRobots() {
		int portalServerPort = PortalUtil.getPortalServerPort(false);

		return getDefaultRobots(null, false, portalServerPort);
	}

	public static String getDefaultRobots(
		String virtualHost, boolean secure, int port) {

		if (Validator.isNotNull(virtualHost)) {
			return ContentUtil.get(
				RobotsUtil.class.getClassLoader(),
				PropsValues.ROBOTS_TXT_WITH_SITEMAP);
		}

		return ContentUtil.get(
			RobotsUtil.class.getClassLoader(),
			PropsValues.ROBOTS_TXT_WITHOUT_SITEMAP);
	}

	public static String getRobots(LayoutSet layoutSet, boolean secure)
		throws PortalException {

		if (layoutSet == null) {
			return ContentUtil.get(
				RobotsUtil.class.getClassLoader(),
				PropsValues.ROBOTS_TXT_WITHOUT_SITEMAP);
		}

		int portalServerPort = PortalUtil.getPortalServerPort(secure);

		String virtualHostname = PortalUtil.getVirtualHostname(layoutSet);

		String robotsTxt = GetterUtil.getString(
			layoutSet.getSettingsProperty(
				layoutSet.isPrivateLayout() + "-robots.txt"),
			ContentUtil.get(
				RobotsUtil.class.getClassLoader(),
				PropsValues.ROBOTS_TXT_WITH_SITEMAP));

		return _replaceWildcards(
			robotsTxt, virtualHostname, secure, portalServerPort);
	}

	private static String _replaceWildcards(
		String robotsTxt, String virtualHostname, boolean secure, int port) {

		if (Validator.isNotNull(virtualHostname)) {
			robotsTxt = StringUtil.replace(
				robotsTxt, "[$HOST$]", virtualHostname);
		}
		else if (_log.isWarnEnabled()) {
			_log.warn(
				"Placeholder [$HOST$] could not be replaced with the actual " +
					"host");
		}

		robotsTxt = StringUtil.replace(
			robotsTxt, "[$PORT$]", String.valueOf(port));

		if (secure) {
			return StringUtil.replace(robotsTxt, "[$PROTOCOL$]", "https");
		}

		return StringUtil.replace(robotsTxt, "[$PROTOCOL$]", "http");
	}

	private static final Log _log = LogFactoryUtil.getLog(RobotsUtil.class);

}