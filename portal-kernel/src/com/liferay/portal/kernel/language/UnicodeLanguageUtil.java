/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.language;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class UnicodeLanguageUtil {

	public static String format(
		HttpServletRequest request, String pattern, LanguageWrapper argument) {

		return getUnicodeLanguage().format(request, pattern, argument);
	}

	public static String format(
		HttpServletRequest request, String pattern, LanguageWrapper argument,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			request, pattern, argument, translateArguments);
	}

	public static String format(
		HttpServletRequest request, String pattern,
		LanguageWrapper[] arguments) {

		return getUnicodeLanguage().format(request, pattern, arguments);
	}

	public static String format(
		HttpServletRequest request, String pattern, LanguageWrapper[] arguments,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			request, pattern, arguments, translateArguments);
	}

	public static String format(
		HttpServletRequest request, String pattern, Object argument) {

		return getUnicodeLanguage().format(request, pattern, argument);
	}

	public static String format(
		HttpServletRequest request, String pattern, Object argument,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			request, pattern, argument, translateArguments);
	}

	public static String format(
		HttpServletRequest request, String pattern, Object[] arguments) {

		return getUnicodeLanguage().format(request, pattern, arguments);
	}

	public static String format(
		HttpServletRequest request, String pattern, Object[] arguments,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			request, pattern, arguments, translateArguments);
	}

	public static String format(
		Locale locale, String pattern, Object argument) {

		return getUnicodeLanguage().format(locale, pattern, argument);
	}

	public static String format(
		Locale locale, String pattern, Object argument,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			locale, pattern, argument, translateArguments);
	}

	public static String format(
		Locale locale, String pattern, Object[] arguments) {

		return getUnicodeLanguage().format(locale, pattern, arguments);
	}

	public static String format(
		Locale locale, String pattern, Object[] arguments,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			locale, pattern, arguments, translateArguments);
	}

	public static String format(
		ResourceBundle resourceBundle, String pattern, Object argument) {

		return getUnicodeLanguage().format(resourceBundle, pattern, argument);
	}

	public static String format(
		ResourceBundle resourceBundle, String pattern, Object argument,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			resourceBundle, pattern, argument, translateArguments);
	}

	public static String format(
		ResourceBundle resourceBundle, String pattern, Object[] arguments) {

		return getUnicodeLanguage().format(resourceBundle, pattern, arguments);
	}

	public static String format(
		ResourceBundle resourceBundle, String pattern, Object[] arguments,
		boolean translateArguments) {

		return getUnicodeLanguage().format(
			resourceBundle, pattern, arguments, translateArguments);
	}

	public static String get(HttpServletRequest request, String key) {
		return getUnicodeLanguage().get(request, key);
	}

	public static String get(
		HttpServletRequest request, String key, String defaultValue) {

		return getUnicodeLanguage().get(request, key, defaultValue);
	}

	public static String get(Locale locale, String key) {
		return getUnicodeLanguage().get(locale, key);
	}

	public static String get(Locale locale, String key, String defaultValue) {
		return getUnicodeLanguage().get(locale, key, defaultValue);
	}

	public static String get(ResourceBundle resourceBundle, String key) {
		return getUnicodeLanguage().get(resourceBundle, key);
	}

	public static String get(
		ResourceBundle resourceBundle, String key, String defaultValue) {

		return getUnicodeLanguage().get(resourceBundle, key, defaultValue);
	}

	public static String getTimeDescription(
		HttpServletRequest request, long milliseconds) {

		return getUnicodeLanguage().getTimeDescription(request, milliseconds);
	}

	public static String getTimeDescription(
		HttpServletRequest request, Long milliseconds) {

		return getUnicodeLanguage().getTimeDescription(request, milliseconds);
	}

	public static UnicodeLanguage getUnicodeLanguage() {
		return _unicodeLanguage;
	}

	public void setUnicodeLanguage(UnicodeLanguage unicodeLanguage) {
		_unicodeLanguage = unicodeLanguage;
	}

	private static UnicodeLanguage _unicodeLanguage;

}