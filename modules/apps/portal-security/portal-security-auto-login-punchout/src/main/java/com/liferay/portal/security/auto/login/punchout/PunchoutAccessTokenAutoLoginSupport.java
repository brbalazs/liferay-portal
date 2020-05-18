/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.security.auto.login.punchout;

import com.liferay.oauth2.provider.punchout.PunchoutAccessTokenProvider;
import com.liferay.oauth2.provider.punchout.model.PunchoutAccessToken;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auto.login.AutoLogin;
import com.liferay.portal.kernel.security.auto.login.BaseAutoLogin;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(
	immediate = true,
	property = {"private.auto.login=true", "type=punchout.access.token"},
	service = AutoLogin.class
)
public class PunchoutAccessTokenAutoLoginSupport extends BaseAutoLogin {

	@Override
	protected String[] doLogin(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String punchoutAccessTokenFromParam = ParamUtil.getString(
			request, getPunchoutAccessTokenParam());

		if (Validator.isNull(punchoutAccessTokenFromParam)) {
			return null;
		}

		PunchoutAccessToken punchoutAccessToken =
			_punchoutAccessTokenProvider.getPunchoutAccessToken(
				punchoutAccessTokenFromParam);

		if (punchoutAccessToken == null) {
			return null;
		}

		String userEmailAddress = punchoutAccessToken.getUserEmailAddress();

		if (Validator.isBlank(userEmailAddress)) {
			return null;
		}

		long companyId = _portal.getCompanyId(request);

		User user = _userLocalService.getUserByEmailAddress(
			companyId, userEmailAddress);

		if (user == null) {
			return null;
		}

		_punchoutAccessTokenProvider.removePunchoutAccessToken(
			punchoutAccessTokenFromParam);

		String[] credentials = new String[3];

		credentials[0] = String.valueOf(user.getUserId());
		credentials[1] = user.getPassword();
		credentials[2] = Boolean.TRUE.toString();

		return credentials;
	}

	protected String getPunchoutAccessTokenParam() {
		return _PUNCHOUT_ACCESS_TOKEN_PARAM;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		_portal = portal;
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final String _PUNCHOUT_ACCESS_TOKEN_PARAM =
		"punchoutAccessToken";

	private Portal _portal;

	@Reference
	private PunchoutAccessTokenProvider _punchoutAccessTokenProvider;

	private UserLocalService _userLocalService;

}