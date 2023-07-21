/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authtokenwhitelistutil;

import com.liferay.portal.kernel.util.PropsKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Cristina González
 */
@Component(
	immediate = true,
	property = {
		PropsKeys.AUTH_TOKEN_IGNORE_PORTLETS + "=" + TestAuthTokenIgnorePortlets.TEST_AUTH_TOKEN_IGNORE_PORTLETS_URL,
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = Object.class
)
public class TestAuthTokenIgnorePortlets {

	public static final String TEST_AUTH_TOKEN_IGNORE_PORTLETS_URL =
		"TEST_AUTH_TOKEN_IGNORE_PORTLETS_URL";

}