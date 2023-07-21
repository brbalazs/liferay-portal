/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.bundle.authtokenwhitelistutil;

import com.liferay.portal.kernel.util.PropsKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Tomas Polesovsky
 */
@Component(
	immediate = true,
	property = {
		PropsKeys.PORTLET_ADD_DEFAULT_RESOURCE_CHECK_WHITELIST + "=" + TestPortalAddDefaultResourceCheckWhitelist.TEST_PORTLET_ADD_DEFAULT_RESOURCE_CHECK_WHITELIST_URL,
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = Object.class
)
public class TestPortalAddDefaultResourceCheckWhitelist {

	public static final String
		TEST_PORTLET_ADD_DEFAULT_RESOURCE_CHECK_WHITELIST_URL =
			"TEST_PORTLET_ADD_DEFAULT_RESOURCE_CHECK_WHITELIST_URL";

}