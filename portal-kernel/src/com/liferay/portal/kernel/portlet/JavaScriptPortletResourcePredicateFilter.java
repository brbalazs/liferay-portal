/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PredicateFilter;

/**
 * @author Carlos Sierra Andrés
 */
public class JavaScriptPortletResourcePredicateFilter
	implements PredicateFilter<String> {

	public JavaScriptPortletResourcePredicateFilter(ThemeDisplay themeDisplay) {
		_themeDisplay = themeDisplay;
	}

	@Override
	public boolean filter(String resource) {
		return !_themeDisplay.isIncludedJs(resource);
	}

	private final ThemeDisplay _themeDisplay;

}