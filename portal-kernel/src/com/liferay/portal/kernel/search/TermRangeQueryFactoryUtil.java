/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search;

/**
 * @author     Raymond Augé
 * @author     Michael C. Han
 * @deprecated As of Wilberforce (7.0.x), , replaced by {@link
 *             com.liferay.portal.kernel.search.generic.TermRangeQueryImpl}
 */
@Deprecated
public class TermRangeQueryFactoryUtil {

	public static TermRangeQuery create(
		SearchContext searchContext, String field, String lowerTerm,
		String upperTerm, boolean includesLower, boolean includesUpper) {

		TermRangeQueryFactory termRangeQueryFactory = getTermRangeQueryFactory(
			searchContext);

		return termRangeQueryFactory.create(
			field, lowerTerm, upperTerm, includesLower, includesUpper);
	}

	public static TermRangeQueryFactory getTermRangeQueryFactory(
		SearchContext searchContext) {

		String searchEngineId = searchContext.getSearchEngineId();

		SearchEngine searchEngine = SearchEngineHelperUtil.getSearchEngine(
			searchEngineId);

		return searchEngine.getTermRangeQueryFactory();
	}

}