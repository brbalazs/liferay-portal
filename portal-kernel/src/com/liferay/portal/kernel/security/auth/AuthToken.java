/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.security.auth;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Amos Fong
 */
public interface AuthToken {

	public void addCSRFToken(
		HttpServletRequest request, LiferayPortletURL liferayPortletURL);

	public void addPortletInvocationToken(
		HttpServletRequest request, LiferayPortletURL liferayPortletURL);

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #checkCSRFToken(HttpServletRequest, String)}
	 */
	@Deprecated
	public void check(HttpServletRequest request) throws PortalException;

	public void checkCSRFToken(HttpServletRequest request, String origin)
		throws PrincipalException;

	public String getToken(HttpServletRequest request);

	public String getToken(
		HttpServletRequest request, long plid, String portletId);

	public boolean isValidPortletInvocationToken(
		HttpServletRequest request, Layout layout, Portlet portlet);

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #isValidPortletInvocationToken(HttpServletRequest, Layout,
	 *             Portlet)}
	 */
	@Deprecated
	public boolean isValidPortletInvocationToken(
		HttpServletRequest request, long plid, String portletId,
		String strutsAction, String tokenValue);

}