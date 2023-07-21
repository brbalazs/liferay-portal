/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.search.bar.portlet;

import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.Optional;

/**
 * @author André de Oliveira
 */
public class SearchBarPortletDestinationUtil {

	public static boolean isSameDestination(
		SearchBarPortletPreferences searchBarPortletPreferences,
		ThemeDisplay themeDisplay) {

		Optional<String> optional =
			searchBarPortletPreferences.getDestination();

		if (!optional.isPresent()) {
			return true;
		}

		if (isSameDestination(
				optional.get(),
				themeDisplay.getLayoutFriendlyURL(themeDisplay.getLayout()))) {

			return true;
		}

		return false;
	}

	protected static boolean isSameDestination(
		String destination, String friendlyURL) {

		int offset = 0;

		if (destination.charAt(0) != '/') {
			offset = 1;
		}

		if (destination.regionMatches(
				0, friendlyURL, offset, friendlyURL.length() - offset)) {

			return true;
		}

		return false;
	}

}