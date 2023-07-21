/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.webdav.test;

import com.liferay.portal.kernel.test.rule.BaseTestRule;

/**
 * @author Miguel Pastor
 * @author Shuyang Zhou
 */
public class WebDAVEnvironmentConfigTestRule
	extends BaseTestRule<Object, Object> {

	public static final WebDAVEnvironmentConfigTestRule INSTANCE =
		new WebDAVEnvironmentConfigTestRule();

	private WebDAVEnvironmentConfigTestRule() {
		super(WebDAVEnvironmentConfigTestCallback.INSTANCE);
	}

}