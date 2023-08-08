/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.date.dog.util;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;

import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author Geyson Silva
 */
public class TimeZoneDogUtil {

	public static String getTimeZoneId() {
		if (_timeZoneDogStatic == null) {
			return "UTC";
		}

		return _timeZoneDogStatic.getTimeZoneId();
	}

	public static ZoneId getZoneId() {
		if (_timeZoneDogStatic == null) {
			return ZoneOffset.UTC;
		}

		return _timeZoneDogStatic.getZoneId();
	}

	public static void setTimeZoneDog(TimeZoneDog timeZoneDog) {
		_timeZoneDogStatic = timeZoneDog;
	}

	private static TimeZoneDog _timeZoneDogStatic;

}