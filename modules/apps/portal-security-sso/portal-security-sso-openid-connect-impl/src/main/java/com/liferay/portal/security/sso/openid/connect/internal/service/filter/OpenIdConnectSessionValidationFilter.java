/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect.internal.service.filter;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnect;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectFlowState;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceHandler;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectSession;
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
	immediate = true,
	property = {
		"servlet-context-name=",
		"servlet-filter-name=OpenId Connect Session Validation Filter",
		"url-pattern=/*"
	},
	service = Filter.class
)
public class OpenIdConnectSessionValidationFilter extends BaseFilter {

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		long companyId = _portal.getCompanyId(request);

		return _openIdConnect.isEnabled(companyId);
	}

	protected boolean checkEndSession(HttpSession httpSession)
		throws Exception {

		boolean endSession = false;

		OpenIdConnectSession openIdConnectSession =
			_openIdConnectSessionProvider.getOpenIdConnectSession(httpSession);

		if (openIdConnectSession == null) {
			return endSession;
		}

		OpenIdConnectFlowState openIdConnectFlowState =
			openIdConnectSession.getOpenIdConnectFlowState();

		if (!OpenIdConnectFlowState.AUTH_COMPLETE.equals(
				openIdConnectFlowState) &&
			!OpenIdConnectFlowState.PORTAL_AUTH_COMPLETE.equals(
				openIdConnectFlowState)) {

			return endSession;
		}

		try {
			if (!_openIdConnectServiceHandler.hasValidOpenIdConnectSession(
					httpSession)) {

				endSession = true;
			}
		}
		catch (PortalException pe) {
			_log.error(
				"Unable to validate OpenId Connect session: " + pe.getMessage(),
				pe);

			endSession = true;
		}

		return endSession;
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		HttpSession httpSession = request.getSession(false);

		if ((httpSession != null) && checkEndSession(httpSession)) {
			httpSession.invalidate();

			response.sendRedirect(_portal.getHomeURL(request));

			return;
		}

		processFilter(
			OpenIdConnectSessionValidationFilter.class.getName(), request,
			response, filterChain);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenIdConnectSessionValidationFilter.class);

	@Reference
	private OpenIdConnect _openIdConnect;

	@Reference
	private OpenIdConnectServiceHandler _openIdConnectServiceHandler;

	@Reference
	private OpenIdConnectSessionProvider _openIdConnectSessionProvider;

	@Reference
	private Portal _portal;

}