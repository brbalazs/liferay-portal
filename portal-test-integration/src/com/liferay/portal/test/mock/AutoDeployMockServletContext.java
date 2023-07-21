/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.mock;

import java.io.File;

import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Cristina González
 */
public class AutoDeployMockServletContext extends MockServletContext {

	public AutoDeployMockServletContext(ResourceLoader resourceLoader) {
		super(getResourceBasePath(), resourceLoader);
	}

	protected static String getResourceBasePath() {
		File file = new File("portal-web/docroot");

		return "file:" + file.getAbsolutePath();
	}

	/**
	 * @see com.liferay.portal.server.capabilities.TomcatServerCapabilities
	 */
	protected Boolean autoDeploy = Boolean.TRUE;

}