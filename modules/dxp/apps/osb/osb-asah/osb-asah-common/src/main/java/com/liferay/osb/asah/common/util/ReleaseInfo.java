/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

/**
 * @author Shinn Lok
 */
public class ReleaseInfo {

	public static int getSchemaVersion() {
		return _SCHEMA_VERSION;
	}

	public static String getVersion() {
		return _VERSION;
	}

	private static final int _SCHEMA_VERSION = 14;

	private static final String _VERSION = "4.0.14";

}