/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.web.internal.constants;

import com.liferay.osb.faro.constants.FaroUserConstants;
import com.liferay.portal.kernel.model.RoleConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class UserConstants {

	public static Map<String, String> getRoleNames() {
		return _roleNames;
	}

	public static Map<String, Integer> getStatuses() {
		return _statuses;
	}

	private static final Map<String, String> _roleNames =
		new HashMap<String, String>() {
			{
				put("administrator", RoleConstants.SITE_ADMINISTRATOR);
				put("member", RoleConstants.SITE_MEMBER);
				put("owner", RoleConstants.SITE_OWNER);
			}
		};
	private static final Map<String, Integer> _statuses =
		new HashMap<String, Integer>() {
			{
				put("approved", FaroUserConstants.STATUS_APPROVED);
				put("pending", FaroUserConstants.STATUS_PENDING);
			}
		};

}