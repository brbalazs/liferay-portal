/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * See http://www.zytrax.com/tech/web/browser_ids.htm for examples.
 *
 * @author Brian Wing Shun Chan
 * @author Eduardo Lundgren
 */
public class BrowserSnifferUtil {

	public static boolean acceptsGzip(HttpServletRequest request) {
		return getBrowserSniffer().acceptsGzip(request);
	}

	public static String getBrowserId(HttpServletRequest request) {
		return getBrowserSniffer().getBrowserId(request);
	}

	public static BrowserMetadata getBrowserMetadata(
		HttpServletRequest request) {

		return getBrowserSniffer().getBrowserMetadata(request);
	}

	public static BrowserSniffer getBrowserSniffer() {
		return _browserSniffer;
	}

	public static float getMajorVersion(HttpServletRequest request) {
		return getBrowserSniffer().getMajorVersion(request);
	}

	public static String getRevision(HttpServletRequest request) {
		return getBrowserSniffer().getRevision(request);
	}

	public static String getVersion(HttpServletRequest request) {
		return getBrowserSniffer().getVersion(request);
	}

	public static boolean isAir(HttpServletRequest request) {
		return getBrowserSniffer().isAir(request);
	}

	public static boolean isAndroid(HttpServletRequest request) {
		return getBrowserSniffer().isAndroid(request);
	}

	public static boolean isChrome(HttpServletRequest request) {
		return getBrowserSniffer().isChrome(request);
	}

	public static boolean isEdge(HttpServletRequest request) {
		return getBrowserSniffer().isEdge(request);
	}

	public static boolean isFirefox(HttpServletRequest request) {
		return getBrowserSniffer().isFirefox(request);
	}

	public static boolean isGecko(HttpServletRequest request) {
		return getBrowserSniffer().isGecko(request);
	}

	public static boolean isIe(HttpServletRequest request) {
		return getBrowserSniffer().isIe(request);
	}

	public static boolean isIeOnWin32(HttpServletRequest request) {
		return getBrowserSniffer().isIeOnWin32(request);
	}

	public static boolean isIeOnWin64(HttpServletRequest request) {
		return getBrowserSniffer().isIeOnWin64(request);
	}

	public static boolean isIphone(HttpServletRequest request) {
		return getBrowserSniffer().isIphone(request);
	}

	public static boolean isLinux(HttpServletRequest request) {
		return getBrowserSniffer().isLinux(request);
	}

	public static boolean isMac(HttpServletRequest request) {
		return getBrowserSniffer().isMac(request);
	}

	public static boolean isMobile(HttpServletRequest request) {
		return getBrowserSniffer().isMobile(request);
	}

	public static boolean isMozilla(HttpServletRequest request) {
		return getBrowserSniffer().isMozilla(request);
	}

	public static boolean isOpera(HttpServletRequest request) {
		return getBrowserSniffer().isOpera(request);
	}

	public static boolean isRtf(HttpServletRequest request) {
		return getBrowserSniffer().isRtf(request);
	}

	public static boolean isSafari(HttpServletRequest request) {
		return getBrowserSniffer().isSafari(request);
	}

	public static boolean isSun(HttpServletRequest request) {
		return getBrowserSniffer().isSun(request);
	}

	public static boolean isWebKit(HttpServletRequest request) {
		return getBrowserSniffer().isWebKit(request);
	}

	public static boolean isWindows(HttpServletRequest request) {
		return getBrowserSniffer().isWindows(request);
	}

	public void setBrowserSniffer(BrowserSniffer browserSniffer) {
		_browserSniffer = browserSniffer;
	}

	private static BrowserSniffer _browserSniffer;

}