/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.http;

import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.URLCodec;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Manuel de la Peña
 */
public class HttpPrincipalTestUtil {

	public static HttpPrincipal getHttpPrincipal() throws Exception {
		return getHttpPrincipal(getLogin());
	}

	public static HttpPrincipal getHttpPrincipal(String login) {
		return getHttpPrincipal(login, true);
	}

	public static HttpPrincipal getHttpPrincipal(
		String login, boolean authenticated) {

		HttpPrincipal httpPrincipal = null;

		if (authenticated) {
			httpPrincipal = new HttpPrincipal(
				TestPropsValues.PORTAL_URL, login,
				TestPropsValues.USER_PASSWORD);
		}
		else {
			httpPrincipal = new HttpPrincipal(TestPropsValues.PORTAL_URL);
		}

		return httpPrincipal;
	}

	public static String getLogin() throws Exception {
		return getLogin(true);
	}

	public static String getLogin(boolean encodeLogin) throws Exception {
		String login = null;

		String authType = GetterUtil.getString(
			PropsUtil.get(PropsKeys.COMPANY_SECURITY_AUTH_TYPE));

		User user = TestPropsValues.getUser();

		if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
			login = user.getEmailAddress();

			if (encodeLogin) {
				login = URLCodec.encodeURL(login);
			}
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
			login = user.getScreenName();
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
			login = String.valueOf(TestPropsValues.getUserId());
		}

		return login;
	}

	public static URL getSoapURL(String serviceName) throws Exception {
		return getSoapURL(getLogin(), serviceName);
	}

	public static URL getSoapURL(
			String login, boolean authenticated, String serviceName)
		throws MalformedURLException {

		String url = TestPropsValues.PORTAL_URL;

		if (authenticated) {
			String password = TestPropsValues.USER_PASSWORD;

			int pos = url.indexOf("://");

			String protocol = url.substring(0, pos + 3);
			String host = url.substring(pos + 3);

			url = StringBundler.concat(
				protocol, login, ":", password, "@", host, "/api/axis/",
				serviceName);
		}
		else {
			url += "/api/axis/" + serviceName;
		}

		return new URL(url);
	}

	public static URL getSoapURL(String login, String serviceName)
		throws MalformedURLException {

		return getSoapURL(login, true, serviceName);
	}

}