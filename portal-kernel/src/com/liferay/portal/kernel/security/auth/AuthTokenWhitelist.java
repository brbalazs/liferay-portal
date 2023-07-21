/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.security.auth;

import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tomas Polesovsky
 * @author Raymond Augé
 */
public interface AuthTokenWhitelist {

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> getOriginCSRFWhitelist();

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> getPortletCSRFWhitelist();

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> getPortletCSRFWhitelistActions();

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> getPortletInvocationWhitelist();

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> getPortletInvocationWhitelistActions();

	public boolean isOriginCSRFWhitelisted(long companyId, String origin);

	public boolean isPortletCSRFWhitelisted(
		HttpServletRequest request, Portlet portlet);

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #isPortletCSRFWhitelisted(HttpServletRequest, Portlet)}
	 */
	@Deprecated
	public boolean isPortletCSRFWhitelisted(
		long companyId, String portletId, String strutsAction);

	public boolean isPortletInvocationWhitelisted(
		HttpServletRequest request, Portlet portlet);

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #isPortletInvocationWhitelisted(HttpServletRequest, Portlet)}
	 */
	@Deprecated
	public boolean isPortletInvocationWhitelisted(
		long companyId, String portletId, String strutsAction);

	public boolean isPortletURLCSRFWhitelisted(
		LiferayPortletURL liferayPortletURL);

	public boolean isPortletURLPortletInvocationWhitelisted(
		LiferayPortletURL liferayPortletURL);

	public boolean isValidSharedSecret(String sharedSecret);

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> resetOriginCSRFWhitelist();

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> resetPortletCSRFWhitelist();

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> resetPortletInvocationWhitelist();

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public Set<String> resetPortletInvocationWhitelistActions();

}