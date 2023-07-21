/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search.generic;

import com.liferay.portal.kernel.search.TermRangeQuery;
import com.liferay.portal.kernel.search.TermRangeQueryFactory;

/**
 * @author     Raymond Augé
 * @deprecated As of Wilberforce (7.0.x)
 */
@Deprecated
public class TermRangeQueryFactoryImpl implements TermRangeQueryFactory {

	@Override
	public TermRangeQuery create(
		String field, String lowerTerm, String upperTerm, boolean includesLower,
		boolean includesUpper) {

		return new TermRangeQueryImpl(
			field, lowerTerm, upperTerm, includesLower, includesUpper);
	}

}