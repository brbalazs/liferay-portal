/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.security.auth.session;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Tomas Polesovsky
 */
public class AuthenticatedSessionManagerUtil {

	public static AuthenticatedSessionManager getAuthenticatedSessionManager() {
		return _instance._serviceTracker.getService();
	}

	public static long getAuthenticatedUserId(
			HttpServletRequest request, String login, String password,
			String authType)
		throws PortalException {

		return getAuthenticatedSessionManager().getAuthenticatedUserId(
			request, login, password, authType);
	}

	public static void login(
			HttpServletRequest request, HttpServletResponse response,
			String login, String password, boolean rememberMe, String authType)
		throws Exception {

		getAuthenticatedSessionManager().login(
			request, response, login, password, rememberMe, authType);
	}

	public static void logout(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		getAuthenticatedSessionManager().logout(request, response);
	}

	public static HttpSession renewSession(
			HttpServletRequest request, HttpSession session)
		throws Exception {

		return getAuthenticatedSessionManager().renewSession(request, session);
	}

	public static void signOutSimultaneousLogins(long userId) throws Exception {
		getAuthenticatedSessionManager().signOutSimultaneousLogins(userId);
	}

	private AuthenticatedSessionManagerUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			AuthenticatedSessionManager.class);

		_serviceTracker.open();
	}

	private static final AuthenticatedSessionManagerUtil _instance =
		new AuthenticatedSessionManagerUtil();

	private final ServiceTracker<?, AuthenticatedSessionManager>
		_serviceTracker;

}