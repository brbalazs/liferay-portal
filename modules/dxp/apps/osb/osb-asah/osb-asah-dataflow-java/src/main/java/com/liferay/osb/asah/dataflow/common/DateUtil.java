/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Marcellus Tavares
 */
public class DateUtil {

	public static long getDeltaMilliseconds(
		String dateString1, String dateString2) {

		ZonedDateTime zonedDateTime1 = toUTCZonedDateTime(dateString1);
		ZonedDateTime zonedDateTime2 = toUTCZonedDateTime(dateString2);

		return ChronoUnit.MILLIS.between(zonedDateTime1, zonedDateTime2);
	}

	public static ZonedDateTime toUTCZonedDateTime(String dateString) {
		LocalDateTime localDateTime = LocalDateTime.parse(
			dateString, _dateTimeFormatter);

		return localDateTime.atZone(ZoneOffset.UTC);
	}

	private static final DateTimeFormatter _dateTimeFormatter =
		DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

}