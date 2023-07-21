/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.engine.client.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @author André Miranda
 */
public class TokenConstants {

	public static final String OSB_ASAH_SECURITY_TOKEN;

	static {
		if (StringUtils.isNotBlank(System.getenv("OSB_ASAH_SECURITY_TOKEN"))) {
			OSB_ASAH_SECURITY_TOKEN = System.getenv("OSB_ASAH_SECURITY_TOKEN");
		}
		else {
			OSB_ASAH_SECURITY_TOKEN = System.getenv("OSB_ASAH_TOKEN");
		}
	}

}