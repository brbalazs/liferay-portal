/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.charset.StandardCharsets;

import org.springframework.web.util.UriUtils;

/**
 * @author André Miranda
 */
public class URLUtil {

	public static String decode(String url) {
		return UriUtils.decode(url, StandardCharsets.UTF_8);
	}

	public static URI toURI(String url) throws URISyntaxException {
		return new URI(UriUtils.encodePath(url, StandardCharsets.UTF_8));
	}

}