/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts.bundle.authpublicpathregistry;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true,
	property = {
		"auth.public.path=testAuthPublicPath",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = Object.class
)
public class TestAuthPublicPath {
}