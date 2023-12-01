/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.cookies.CookiesManagerUtil;
import com.liferay.portal.kernel.exception.CookieNotSupportedException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Minhchau Dang
 */
public class CookieKeys {

	public static final String COMPANY_ID = "COMPANY_ID";

	public static final String COOKIE_SUPPORT = "COOKIE_SUPPORT";

	public static final String GUEST_LANGUAGE_ID = "GUEST_LANGUAGE_ID";

	public static final String ID = "ID";

	public static final String JSESSIONID = "JSESSIONID";

	public static final String LOGIN = "LOGIN";

	public static final int MAX_AGE = (int)(Time.YEAR / 1000);

	public static final String PASSWORD = "PASSWORD";

	public static final String REMEMBER_ME = "REMEMBER_ME";

	public static final String REMOTE_PREFERENCE_PREFIX = "REMOTE_PREFERENCE_";

	public static final String SCREEN_NAME = "SCREEN_NAME";

	public static final String USER_UUID = "USER_UUID";

	public static void addCookie(
		HttpServletRequest request, HttpServletResponse response,
		Cookie cookie) {

		addCookie(request, response, cookie, request.isSecure());
	}

	public static void addCookie(
		HttpServletRequest request, HttpServletResponse response, Cookie cookie,
		boolean secure) {

		if (!_SESSION_ENABLE_PERSISTENT_COOKIES) {
			return;
		}

		// LEP-5175

		String name = cookie.getName();

		String originalValue = cookie.getValue();

		String encodedValue = originalValue;

		if (isEncodedCookie(name)) {
			encodedValue = UnicodeFormatter.bytesToHex(
				originalValue.getBytes());

			if (_log.isDebugEnabled()) {
				_log.debug("Add encoded cookie " + name);
				_log.debug("Original value " + originalValue);
				_log.debug("Hex encoded value " + encodedValue);
			}
		}

		cookie.setSecure(secure);
		cookie.setValue(encodedValue);
		cookie.setVersion(0);

		response.addCookie(cookie);

		if (request != null) {
			Map<String, Cookie> cookieMap = _getCookieMap(request);

			cookieMap.put(StringUtil.toUpperCase(name), cookie);
		}
	}

	public static void addSupportCookie(
		HttpServletRequest request, HttpServletResponse response) {

		Cookie cookieSupportCookie = new Cookie(COOKIE_SUPPORT, "true");

		cookieSupportCookie.setPath(StringPool.SLASH);
		cookieSupportCookie.setMaxAge(MAX_AGE);

		addCookie(null, response, cookieSupportCookie, request.isSecure());
	}

	public static void deleteCookies(
		HttpServletRequest request, HttpServletResponse response, String domain,
		String... cookieNames) {

		if (!_SESSION_ENABLE_PERSISTENT_COOKIES) {
			return;
		}

		Map<String, Cookie> cookieMap = _getCookieMap(request);

		for (String cookieName : cookieNames) {
			Cookie cookie = cookieMap.remove(
				StringUtil.toUpperCase(cookieName));

			if (cookie != null) {
				if (domain != null) {
					cookie.setDomain(domain);
				}

				cookie.setMaxAge(0);
				cookie.setPath(StringPool.SLASH);
				cookie.setValue(StringPool.BLANK);

				response.addCookie(cookie);
			}
		}
	}

	public static String getCookie(HttpServletRequest request, String name) {
		return getCookie(request, name, true);
	}

	public static String getCookie(
		HttpServletRequest request, String name, boolean toUpperCase) {

		if (!_SESSION_ENABLE_PERSISTENT_COOKIES) {
			return null;
		}

		String value = _get(request, name, toUpperCase);

		if ((value == null) || !isEncodedCookie(name)) {
			return value;
		}

		try {
			String encodedValue = value;

			String originalValue = new String(
				UnicodeFormatter.hexToBytes(encodedValue));

			if (_log.isDebugEnabled()) {
				_log.debug("Get encoded cookie " + name);
				_log.debug("Hex encoded value " + encodedValue);
				_log.debug("Original value " + originalValue);
			}

			return originalValue;
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e.getMessage());
			}

			return value;
		}
	}

	public static String getDomain(HttpServletRequest request) {
		return CookiesManagerUtil.getDomain(request);
	}

	public static String getDomain(String host) {
		return CookiesManagerUtil.getDomain(host);
	}

	public static boolean hasSessionId(HttpServletRequest request) {
		String jsessionid = getCookie(request, JSESSIONID, false);

		if (jsessionid != null) {
			return true;
		}

		return false;
	}

	public static boolean isEncodedCookie(String name) {
		if (name.equals(ID) || name.equals(LOGIN) || name.equals(PASSWORD) ||
			name.equals(SCREEN_NAME) || name.equals(USER_UUID)) {

			return true;
		}

		return false;
	}

	public static void validateSupportCookie(HttpServletRequest request)
		throws CookieNotSupportedException {

		if (_SESSION_ENABLE_PERSISTENT_COOKIES &&
			_SESSION_TEST_COOKIE_SUPPORT) {

			String cookieSupport = getCookie(request, COOKIE_SUPPORT, false);

			if (Validator.isNull(cookieSupport)) {
				throw new CookieNotSupportedException();
			}
		}
	}

	private static String _get(
		HttpServletRequest request, String name, boolean toUpperCase) {

		Map<String, Cookie> cookieMap = _getCookieMap(request);

		if (toUpperCase) {
			name = StringUtil.toUpperCase(name);
		}

		Cookie cookie = cookieMap.get(name);

		if (cookie == null) {
			return null;
		}

		return cookie.getValue();
	}

	private static Map<String, Cookie> _getCookieMap(
		HttpServletRequest request) {

		Map<String, Cookie> cookieMap =
			(Map<String, Cookie>)request.getAttribute(
				CookieKeys.class.getName());

		if (cookieMap != null) {
			return cookieMap;
		}

		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			cookieMap = new HashMap<>();
		}
		else {
			cookieMap = new HashMap<>(cookies.length * 4 / 3);

			for (Cookie cookie : cookies) {
				String cookieName = GetterUtil.getString(cookie.getName());

				cookieName = StringUtil.toUpperCase(cookieName);

				cookieMap.put(cookieName, cookie);
			}
		}

		request.setAttribute(CookieKeys.class.getName(), cookieMap);

		return cookieMap;
	}

	private static final boolean _SESSION_ENABLE_PERSISTENT_COOKIES =
		GetterUtil.getBoolean(
			PropsUtil.get(PropsKeys.SESSION_ENABLE_PERSISTENT_COOKIES));

	private static final boolean _SESSION_TEST_COOKIE_SUPPORT =
		GetterUtil.getBoolean(
			PropsUtil.get(PropsKeys.SESSION_TEST_COOKIE_SUPPORT));

	private static final Log _log = LogFactoryUtil.getLog(CookieKeys.class);

}