/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.punchout.portal.security.auto.login;

import com.liferay.commerce.punchout.oauth2.provider.PunchoutAccessTokenProvider;
import com.liferay.commerce.punchout.oauth2.provider.model.PunchoutAccessToken;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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
			request, _PUNCHOUT_ACCESS_TOKEN_PARAM);

		if (Validator.isNull(punchoutAccessTokenFromParam)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					_PUNCHOUT_ACCESS_TOKEN_PARAM + "  parameter not found in " +
						"request");
			}

			return null;
		}

		PunchoutAccessToken punchoutAccessToken =
			_punchoutAccessTokenProvider.getPunchoutAccessToken(
				punchoutAccessTokenFromParam);

		if (punchoutAccessToken == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Punchout access token not found");
			}

			return null;
		}

		String userEmailAddress = punchoutAccessToken.getUserEmailAddress();

		if (Validator.isBlank(userEmailAddress)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Blank punchout user email address in punchout access " +
						"token");
			}

			return null;
		}

		long companyId = _portal.getCompanyId(request);

		User punchoutUser = _userLocalService.getUserByEmailAddress(
			companyId, userEmailAddress);

		if (punchoutUser == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Punchout user not found in punchout access token");
			}

			return null;
		}

		request.setAttribute("punchoutAccessToken", punchoutAccessToken);
		request.setAttribute("punchoutUserId", punchoutUser.getUserId());

		_punchoutAccessTokenProvider.removePunchoutAccessToken(
			punchoutAccessTokenFromParam);

		String[] credentials = new String[3];

		credentials[0] = String.valueOf(punchoutUser.getUserId());
		credentials[1] = punchoutUser.getPassword();
		credentials[2] = Boolean.TRUE.toString();

		return credentials;
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

	private static final Log _log = LogFactoryUtil.getLog(
		PunchoutAccessTokenAutoLoginSupport.class);

	private Portal _portal;

	@Reference
	private PunchoutAccessTokenProvider _punchoutAccessTokenProvider;

	private UserLocalService _userLocalService;

}