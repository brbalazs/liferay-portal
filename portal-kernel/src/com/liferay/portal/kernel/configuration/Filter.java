/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.configuration;

import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class Filter {

	public Filter(String selector1) {
		this(new String[] {selector1}, null);
	}

	public Filter(String selector1, Map<String, String> variables) {
		this(new String[] {selector1}, variables);
	}

	public Filter(String selector1, String selector2) {
		this(new String[] {selector1, selector2}, null);
	}

	public Filter(
		String selector1, String selector2, Map<String, String> variables) {

		this(new String[] {selector1, selector2}, variables);
	}

	public Filter(String selector1, String selector2, String selector3) {
		this(new String[] {selector1, selector2, selector3}, null);
	}

	public Filter(
		String selector1, String selector2, String selector3,
		Map<String, String> variables) {

		this(new String[] {selector1, selector2, selector3}, variables);
	}

	public Filter(String[] selectors, Map<String, String> variables) {
		_selectors = selectors;
		_variables = variables;
	}

	public String[] getSelectors() {
		return _selectors;
	}

	public Map<String, String> getVariables() {
		return _variables;
	}

	private final String[] _selectors;
	private final Map<String, String> _variables;

}