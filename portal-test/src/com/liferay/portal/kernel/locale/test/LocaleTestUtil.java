/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.locale.test;

import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author André de Oliveira
 */
public class LocaleTestUtil {

	public static Map<Locale, String> getDefaultLocaleMap(String value) {
		Map<Locale, String> map = new HashMap<>();

		map.put(LocaleUtil.getSiteDefault(), value);

		return map;
	}

}