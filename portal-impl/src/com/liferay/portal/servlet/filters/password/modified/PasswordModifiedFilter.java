/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.servlet.filters.password.modified;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManagerUtil;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.servlet.filters.BasePortalFilter;

import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Marta Medio
 */
public class PasswordModifiedFilter extends BasePortalFilter {

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		if (_isPasswordModified(request)) {
			AuthenticatedSessionManagerUtil.logout(request, response);

			if (StringUtil.equals(request.getMethod(), HttpMethods.GET)) {
				response.sendRedirect(
					PortalUtil.getCurrentCompleteURL(request));
			}
			else {
				response.sendRedirect(PortalUtil.getPortalURL(request));
			}
		}
		else {
			filterChain.doFilter(request, response);
		}
	}

	private boolean _isPasswordModified(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return false;
		}

		if (!request.isRequestedSessionIdValid()) {
			return false;
		}

		try {
			User user = PortalUtil.getUser(request);

			if ((user == null) || user.isDefaultUser()) {
				return false;
			}

			if (!_isValidRealUserId(session, user)) {
				return false;
			}

			Date passwordModifiedDate = user.getPasswordModifiedDate();

			if (passwordModifiedDate == null) {
				return false;
			}

			if (!request.isRequestedSessionIdValid() ||
				(session.getCreationTime() < passwordModifiedDate.getTime())) {

				return true;
			}

			return false;
		}
		catch (PortalException pe) {
			_log.error(pe, pe);

			return false;
		}
	}

	private boolean _isValidRealUserId(HttpSession session, User user) {
		Long realUserId = (Long)session.getAttribute(WebKeys.USER_ID);

		if ((realUserId == null) || (user.getUserId() != realUserId)) {
			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PasswordModifiedFilter.class);

}