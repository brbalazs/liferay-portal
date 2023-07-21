/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import java.text.DateFormat;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author     Brian Wing Shun Chan
 * @deprecated As of Bunyan (6.0.x), replaced by {@link DateFormatFactoryUtil}
 *             or {@link FastDateFormatFactoryUtil}
 */
@Deprecated
public class DateFormats {

	public static DateFormat getDate(Locale locale) {
		return DateFormatFactoryUtil.getDate(locale);
	}

	public static DateFormat getDate(Locale locale, TimeZone timeZone) {
		return DateFormatFactoryUtil.getDate(locale, timeZone);
	}

	public static DateFormat getDateTime(Locale locale) {
		return DateFormatFactoryUtil.getDateTime(locale);
	}

	public static DateFormat getDateTime(Locale locale, TimeZone timeZone) {
		return DateFormatFactoryUtil.getDateTime(locale, timeZone);
	}

	public static DateFormat getTime(Locale locale) {
		return DateFormatFactoryUtil.getTime(locale);
	}

	public static DateFormat getTime(Locale locale, TimeZone timeZone) {
		return DateFormatFactoryUtil.getTime(locale, timeZone);
	}

}