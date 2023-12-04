/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.constants;

import com.liferay.osb.asah.common.date.DateUtil;

/**
 * @author Marcos Martins
 */
public interface PreferenceConstants {

	public static String DATA_RETENTION_PERIOD = String.valueOf(
		13 * DateUtil.MONTH);

	public static String TIME_ZONE_ID = "UTC";

}