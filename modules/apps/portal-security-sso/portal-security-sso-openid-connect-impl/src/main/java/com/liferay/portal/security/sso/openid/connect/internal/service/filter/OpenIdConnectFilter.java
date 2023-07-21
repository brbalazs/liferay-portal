/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect.internal.service.filter;

import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnect;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectFlowState;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceException;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceHandler;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectSession;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectConstants;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectWebKeys;
import com.liferay.portal.security.sso.openid.connect.internal.exception.StrangersNotAllowedException;
import com.liferay.portal.security.sso.openid.connect.provider.OpenIdConnectSessionProvider;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Edward C. Han
 */
@Component(
	configurationPid = "com.liferay.portal.security.sso.openid.connect.configuration.OpenIdConnectConfiguration",
	immediate = true,
	property = {
		"before-filter=Auto Login Filter", "servlet-context-name=",
		"servlet-filter-name=SSO OpenId Connect Filter",
		"url-pattern=" + OpenIdConnectConstants.REDIRECT_URL_PATTERN
	},
	service = {Filter.class, OpenIdConnectFilter.class}
)
public class OpenIdConnectFilter extends BaseFilter {

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		long companyId = _portal.getCompanyId(request);

		return _openIdConnect.isEnabled(companyId);
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	protected void processAuthenticationResponse(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		HttpSession httpSession = httpServletRequest.getSession(false);

		if (httpSession == null) {
			return;
		}

		try {
			OpenIdConnectSession openIdConnectSession =
				_openIdConnectSessionProvider.getOpenIdConnectSession(
					httpSession);

			if (openIdConnectSession == null) {
				return;
			}

			OpenIdConnectFlowState openIdConnectFlowState =
				openIdConnectSession.getOpenIdConnectFlowState();

			if (OpenIdConnectFlowState.INITIALIZED.equals(
					openIdConnectFlowState)) {

				throw new OpenIdConnectServiceException.AuthenticationException(
					"OpenId Connect authentication flow not started");
			}
			else if (OpenIdConnectFlowState.AUTH_COMPLETE.equals(
						openIdConnectFlowState) ||
					 OpenIdConnectFlowState.PORTAL_AUTH_COMPLETE.equals(
						 openIdConnectFlowState)) {

				if (_log.isDebugEnabled()) {
					_log.debug("User has already been logged in");
				}
			}
			else {
				_openIdConnectServiceHandler.processAuthenticationResponse(
					httpServletRequest, httpServletResponse);

				String actionURL = (String)httpSession.getAttribute(
					OpenIdConnectWebKeys.OPEN_ID_CONNECT_ACTION_URL);

				if (actionURL != null) {
					httpServletResponse.sendRedirect(actionURL);
				}
			}
		}
		catch (StrangersNotAllowedException |
			   UserEmailAddressException.MustNotUseCompanyMx e) {

			Class<?> clazz = e.getClass();

			httpSession.removeAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION);

			sendError(
				clazz.getSimpleName(), httpServletRequest, httpServletResponse);
		}
		catch (Exception e) {
			_log.error(
				"Unable to process OpenID Connect authentication response: " +
					e.getMessage(),
				e);

			httpSession.removeAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION);

			_portal.sendError(e, httpServletRequest, httpServletResponse);
		}
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		processAuthenticationResponse(request, response);

		processFilter(
			OpenIdConnectFilter.class.getName(), request, response,
			filterChain);
	}

	protected void sendError(
			String error, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}

		String actionURL = (String)session.getAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_ACTION_URL);

		if (actionURL == null) {
			return;
		}

		actionURL = _http.addParameter(actionURL, "error", error);

		response.sendRedirect(actionURL);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenIdConnectFilter.class);

	@Reference
	private Http _http;

	@Reference
	private OpenIdConnect _openIdConnect;

	@Reference
	private OpenIdConnectServiceHandler _openIdConnectServiceHandler;

	@Reference
	private OpenIdConnectSessionProvider _openIdConnectSessionProvider;

	@Reference
	private Portal _portal;

}