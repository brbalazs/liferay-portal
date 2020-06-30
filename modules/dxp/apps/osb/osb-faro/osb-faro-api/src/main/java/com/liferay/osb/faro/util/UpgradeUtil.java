/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.util;

import com.liferay.osb.faro.constants.UpgradeConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Matthew Kong
 */
public class UpgradeUtil {

	public static String getLatestVersion() {
		String version = PrefsPropsUtil.getString(
			PortalUtil.getDefaultCompanyId(), UpgradeConstants.REPOSITORY_SHA);

		if (Validator.isNull(version)) {
			return _REPOSITORY_SHA;
		}
		else if (Validator.isNull(_REPOSITORY_SHA)) {
			return version;
		}

		String[] parts1 = StringUtil.split(version, StringPool.MINUS);
		String[] parts2 = StringUtil.split(_REPOSITORY_SHA, StringPool.MINUS);

		if (parts1[0].compareTo(parts2[0]) > 0) {
			return version;
		}

		return _REPOSITORY_SHA;
	}

	private static final String _REPOSITORY_SHA = System.getenv(
		"FARO_REPOSITORY_SHA");

}