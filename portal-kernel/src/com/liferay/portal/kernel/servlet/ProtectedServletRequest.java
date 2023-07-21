/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.servlet;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class ProtectedServletRequest
	extends PersistentHttpServletRequestWrapper {

	public ProtectedServletRequest(
		HttpServletRequest request, String remoteUser) {

		this(request, remoteUser, null);
	}

	public ProtectedServletRequest(
		HttpServletRequest request, String remoteUser, String authType) {

		super(request);

		if (remoteUser == null) {
			throw new NullPointerException("Remote user is null");
		}

		if (request instanceof ProtectedServletRequest) {
			ProtectedServletRequest parentRequest =
				(ProtectedServletRequest)request;

			setRequest(parentRequest.getRequest());
		}

		_remoteUser = remoteUser;
		_authType = authType;

		_userPrincipal = new ProtectedPrincipal(remoteUser);
	}

	@Override
	public String getAuthType() {
		if (_authType == null) {
			return super.getAuthType();
		}

		return _authType;
	}

	@Override
	public String getRemoteUser() {
		return _remoteUser;
	}

	@Override
	public Principal getUserPrincipal() {
		return _userPrincipal;
	}

	private final String _authType;
	private final String _remoteUser;
	private final Principal _userPrincipal;

}