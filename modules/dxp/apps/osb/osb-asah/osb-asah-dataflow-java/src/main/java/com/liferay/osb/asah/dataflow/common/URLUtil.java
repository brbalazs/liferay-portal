/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.common;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Leslie Wong
 */
public class URLUtil {

	public static URI createURI(String url) throws URISyntaxException {
		if (StringUtils.isBlank(url)) {
			throw new NullPointerException(
				"Unable to parse URL from empty string");
		}

		String scheme = null;

		int schemeIndex = url.indexOf(_URI_SCHEME_STRING);

		if (schemeIndex != -1) {
			scheme = url.substring(0, schemeIndex);

			schemeIndex += _URI_SCHEME_STRING.length();
		}

		if (StringUtils.isBlank(scheme)) {
			throw new URISyntaxException(url, "Unable to find scheme in URL");
		}

		int authorityIndex = url.indexOf(
			_URI_PATH_SEPARATOR_STRING, Math.max(schemeIndex, 0));

		int fragmentIndex = url.indexOf(_URI_FRAGMENT_STRING, schemeIndex);
		int queryIndex = url.indexOf(_URI_QUERY_STRING, schemeIndex);

		if ((authorityIndex == -1) && (fragmentIndex == -1) &&
			(queryIndex == -1)) {

			authorityIndex = url.length();
		}
		else {
			int[] indices = {authorityIndex, fragmentIndex, queryIndex};

			IntStream stream = Arrays.stream(indices);

			authorityIndex = stream.filter(
				num -> num >= 0
			).min(
			).getAsInt();
		}

		String authority = url.substring(schemeIndex, authorityIndex);

		int pathEndIndex;

		if ((fragmentIndex == -1) && (queryIndex == -1)) {
			pathEndIndex = url.length();
		}
		else {
			int[] indices = {fragmentIndex, queryIndex};

			IntStream stream = Arrays.stream(indices);

			pathEndIndex = stream.filter(
				num -> num >= 0
			).min(
			).getAsInt();
		}

		String path = url.substring(authorityIndex, pathEndIndex);

		if (path.equals("") || path.equals("/")) {
			path = null;
		}

		String query = null;

		if (queryIndex != -1) {
			if (fragmentIndex != -1) {
				query = url.substring(queryIndex + 1, fragmentIndex);
			}
			else {
				query = url.substring(queryIndex + 1);
			}
		}

		String fragment = null;

		if ((fragmentIndex != -1) && (fragmentIndex != (url.length() - 1))) {
			fragment = url.substring(fragmentIndex + 1);
		}

		return new URI(scheme, authority, path, query, fragment);
	}

	private static final String _URI_FRAGMENT_STRING = "#";

	private static final String _URI_PATH_SEPARATOR_STRING = "/";

	private static final String _URI_QUERY_STRING = "?";

	private static final String _URI_SCHEME_STRING = "://";

}