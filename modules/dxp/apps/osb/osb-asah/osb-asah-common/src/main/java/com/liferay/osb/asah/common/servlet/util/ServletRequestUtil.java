/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.servlet.util;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shinn Lok
 */
public class ServletRequestUtil {

	public static String getOriginalURL(HttpServletRequest httpServletRequest) {
		return _getURL(
			_getScheme(httpServletRequest), _getServerName(httpServletRequest),
			_getServerPort(httpServletRequest));
	}

	public static String getURL(HttpServletRequest httpServletRequest) {
		return _getURL(
			httpServletRequest.getScheme(), httpServletRequest.getServerName(),
			httpServletRequest.getServerPort());
	}

	private static String _getScheme(HttpServletRequest httpServletRequest) {
		String forwardedProtocol = httpServletRequest.getHeader(
			"X-Forwarded-Origin-Proto");

		if (forwardedProtocol != null) {
			return forwardedProtocol;
		}

		return httpServletRequest.getScheme();
	}

	private static String _getServerName(
		HttpServletRequest httpServletRequest) {

		String forwardedHost = httpServletRequest.getHeader(
			"X-Forwarded-Origin-Host");

		if (forwardedHost != null) {
			return forwardedHost;
		}

		return httpServletRequest.getServerName();
	}

	private static int _getServerPort(HttpServletRequest httpServletRequest) {
		String forwardedPort = httpServletRequest.getHeader(
			"X-Forwarded-Origin-Port");

		if (forwardedPort != null) {
			return Integer.parseInt(forwardedPort);
		}

		return httpServletRequest.getServerPort();
	}

	private static String _getURL(
		String scheme, String serverName, int serverPort) {

		StringBuilder sb = new StringBuilder();

		try {
			sb.append(URLEncoder.encode(scheme, "UTF-8"));
			sb.append("://");
			sb.append(URLEncoder.encode(serverName, "UTF-8"));
		}
		catch (UnsupportedEncodingException unsupportedEncodingException) {
			throw new RuntimeException(unsupportedEncodingException);
		}

		if ((serverPort > 0) && (serverPort != 80) && (serverPort != 443)) {
			sb.append(":");
			sb.append(serverPort);
		}

		return sb.toString();
	}

}