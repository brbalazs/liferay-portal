/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.saml.runtime.profile;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.saml.persistence.model.SamlSpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Mika Koivisto
 * @deprecated As of Judson (7.1.x), replaced by {@link
 * com.liferay.saml.runtime.servlet.profile.WebSsoProfile}
 */
@Deprecated
@ProviderType
public interface WebSsoProfile {

	public SamlSpSession getSamlSpSession(
		HttpServletRequest httpServletRequest);

	public void processAuthnRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws PortalException;

	public void processResponse(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws PortalException;

	public void sendAuthnRequest(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String relayState)
		throws PortalException;

	public void updateSamlSpSession(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse);

}