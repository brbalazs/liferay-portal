/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

/**
 * @author Sampsa Sohlman
 */
public class PrefixPredicateFilter implements PredicateFilter<String> {

	public PrefixPredicateFilter(String prefix) {
		this(prefix, false);
	}

	public PrefixPredicateFilter(String prefix, boolean include) {
		_prefix = prefix;
		_include = include;
	}

	@Override
	public boolean filter(String string) {
		if (_include) {
			return string.startsWith(_prefix);
		}

		return !string.startsWith(_prefix);
	}

	private final boolean _include;
	private final String _prefix;

}