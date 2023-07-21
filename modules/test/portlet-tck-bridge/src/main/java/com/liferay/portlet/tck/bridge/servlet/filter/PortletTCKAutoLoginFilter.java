/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.tck.bridge.servlet.filter;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.servlet.filters.BasePortalFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
@Component(
	immediate = true,
	property = {
		"servlet-context-name=", "servlet-filter-name=TCK Auto Login Filter",
		"url-pattern=/*"
	},
	service = Filter.class
)
public class PortletTCKAutoLoginFilter extends BasePortalFilter {

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		// The portlet TCK has two tests named GetRemoteUserNullTestPortlet. One
		// tests an action request and the other tests a render request. Those
		// two tests assume that the current user is not authenticated. This
		// filter skips automatic authentication as a workaround for those two
		// tests.

		HttpSession httpSession = request.getSession();

		if (httpSession.getAttribute(_TCK_SKIP_LOGIN) == Boolean.TRUE) {
			processFilter(
				PortletTCKAutoLoginFilter.class.getName(), request, response,
				filterChain);

			return;
		}

		String[] portletIds = request.getParameterValues("portletName");

		if (portletIds != null) {
			for (String portlet : portletIds) {
				if (portlet.endsWith("GetRemoteUserNullTestPortlet")) {
					httpSession.setAttribute(_TCK_SKIP_LOGIN, Boolean.TRUE);

					processFilter(
						PortletTCKAutoLoginFilter.class.getName(), request,
						response, filterChain);

					return;
				}
			}
		}

		User tckUser = _userLocalService.fetchUserByEmailAddress(
			_portal.getCompanyId(request), "tck@liferay.com");

		if (tckUser != null) {
			request.setAttribute(WebKeys.USER_ID, tckUser.getUserId());
		}

		processFilter(
			PortletTCKAutoLoginFilter.class.getName(), request, response,
			filterChain);
	}

	@Reference(unbind = "-")
	protected void setUserLocalService(UserLocalService userLocalService) {
		_userLocalService = userLocalService;
	}

	private static final String _TCK_SKIP_LOGIN = "TCK_SKIP_LOGIN";

	@Reference
	private Portal _portal;

	private UserLocalService _userLocalService;

}