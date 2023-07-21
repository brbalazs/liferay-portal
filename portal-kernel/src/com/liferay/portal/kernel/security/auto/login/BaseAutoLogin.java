/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.security.auto.login;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Máté Thurzó
 */
public abstract class BaseAutoLogin implements AutoLogin {

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public String[] handleException(
			HttpServletRequest request, HttpServletResponse response,
			Exception e)
		throws AutoLoginException {

		return doHandleException(request, response, e);
	}

	@Override
	public String[] login(
			HttpServletRequest request, HttpServletResponse response)
		throws AutoLoginException {

		try {
			return doLogin(request, response);
		}
		catch (Exception e) {
			return doHandleException(request, response, e);
		}
	}

	protected void addRedirect(HttpServletRequest request) {
		String redirect = ParamUtil.getString(request, "redirect");

		if (Validator.isNotNull(redirect)) {
			request.setAttribute(
				AUTO_LOGIN_REDIRECT_AND_CONTINUE,
				PortalUtil.escapeRedirect(redirect));
		}
	}

	protected String[] doHandleException(
			HttpServletRequest request, HttpServletResponse response,
			Exception e)
		throws AutoLoginException {

		if (request.getAttribute(AUTO_LOGIN_REDIRECT) == null) {
			throw new AutoLoginException(e);
		}

		_log.error(e, e);

		return null;
	}

	protected abstract String[] doLogin(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception;

	private static final Log _log = LogFactoryUtil.getLog(BaseAutoLogin.class);

}