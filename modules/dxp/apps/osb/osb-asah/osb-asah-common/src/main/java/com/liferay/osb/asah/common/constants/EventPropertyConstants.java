/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcos Martins
 */
public final class EventPropertyConstants {

	public static final Map<String, String> globalEventPropertyNames =
		Collections.unmodifiableMap(
			new HashMap<String, String>() {
				{
					put("canonicalUrl", "canonicalUrl");
					put("pageDescription", "description");
					put("pageKeywords", "keywords");
					put("pageTitle", "title");
					put("referrer", "referrer");
					put("url", "url");
				}
			});

}