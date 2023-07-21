/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Jesse Rao
 */
@ProviderType
public interface OpenIdConnectSession {

	public String getAccessTokenValue();

	public long getLoginTime();

	public long getLoginUserId();

	public String getNonceValue();

	public OpenIdConnectFlowState getOpenIdConnectFlowState();

	public String getOpenIdProviderName();

	public String getRefreshTokenValue();

	public String getStateValue();

	public void setOpenIdConnectFlowState(
		OpenIdConnectFlowState openIdConnectFlowState);

}