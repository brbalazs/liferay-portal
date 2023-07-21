/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authtokenutil;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.security.auth.AuthToken;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Manuel de la Peña
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = AuthToken.class
)
public class TestAuthToken implements AuthToken {

	@Override
	public void addCSRFToken(
		HttpServletRequest request, LiferayPortletURL liferayPortletURL) {

		liferayPortletURL.setParameter("p_auth", "TEST_TOKEN");
	}

	@Override
	public void addPortletInvocationToken(
		HttpServletRequest request, LiferayPortletURL liferayPortletURL) {

		liferayPortletURL.setParameter(
			"p_p_auth", "TEST_TOKEN_BY_PLID_AND_PORTLET_ID");
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public void check(HttpServletRequest request) {
	}

	@Override
	public void checkCSRFToken(HttpServletRequest request, String origin) {
	}

	@Override
	public String getToken(HttpServletRequest request) {
		return "TEST_TOKEN";
	}

	@Override
	public String getToken(
		HttpServletRequest request, long plid, String portletId) {

		return "TEST_TOKEN_BY_PLID_AND_PORTLET_ID";
	}

	@Override
	public boolean isValidPortletInvocationToken(
		HttpServletRequest request, Layout layout, Portlet portlet) {

		String tokenValue = request.getParameter("p_p_auth");

		return "VALID_PORTLET_INVOCATION_TOKEN".equals(tokenValue);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public boolean isValidPortletInvocationToken(
		HttpServletRequest request, long plid, String portletId,
		String strutsAction, String tokenValue) {

		return "VALID_PORTLET_INVOCATION_TOKEN".equals(tokenValue);
	}

}