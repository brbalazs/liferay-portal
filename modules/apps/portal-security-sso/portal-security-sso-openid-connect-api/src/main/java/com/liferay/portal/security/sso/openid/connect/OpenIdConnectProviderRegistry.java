/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso.openid.connect;

import aQute.bnd.annotation.ProviderType;

import java.util.Collection;

/**
 * @author Thuong Dinh
 */
@ProviderType
public interface OpenIdConnectProviderRegistry<S, T> {

	public OpenIdConnectProvider<S, T> findOpenIdConnectProvider(String name)
		throws OpenIdConnectServiceException.ProviderException;

	public OpenIdConnectProvider<S, T> getOpenIdConnectProvider(String name);

	public Collection<String> getOpenIdConnectProviderNames();

}