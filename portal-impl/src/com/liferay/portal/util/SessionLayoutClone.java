/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.servlet.SharedSessionServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * @author Brian Wing Shun Chan
 */
public class SessionLayoutClone implements LayoutClone {

	@Override
	public String get(HttpServletRequest request, long plid) {
		HttpSession session = getPortalSession(request);

		return (String)session.getAttribute(encodeKey(plid));
	}

	@Override
	public void update(
		HttpServletRequest request, long plid, String typeSettings) {

		HttpSession session = getPortalSession(request);

		session.setAttribute(encodeKey(plid), typeSettings);
	}

	protected String encodeKey(long plid) {
		return SessionLayoutClone.class.getName(
		).concat(
			StringPool.POUND
		).concat(
			StringUtil.toHexString(plid)
		);
	}

	protected HttpSession getPortalSession(HttpServletRequest request) {
		HttpServletRequest originalRequest = request;

		while (originalRequest instanceof HttpServletRequestWrapper) {
			if (originalRequest instanceof SharedSessionServletRequest) {
				SharedSessionServletRequest sharedSessionServletRequest =
					(SharedSessionServletRequest)originalRequest;

				return sharedSessionServletRequest.getSharedSession();
			}

			originalRequest =
				(HttpServletRequest)
					((HttpServletRequestWrapper)originalRequest).getRequest();
		}

		return request.getSession();
	}

}