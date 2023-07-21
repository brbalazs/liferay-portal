/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.search.request;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.web.search.request.SearchSettings;

import java.util.Map;
import java.util.Optional;

/**
 * @author André de Oliveira
 */
public class SearchSettingsImpl implements SearchSettings {

	public SearchSettingsImpl(SearchContext searchContext) {
		_searchContext = searchContext;
	}

	@Override
	public void addCondition(BooleanClause<Query> booleanClause) {
		BooleanClause<Query>[] booleanClauses =
			_searchContext.getBooleanClauses();

		if (booleanClauses == null) {
			booleanClauses = new BooleanClause[] {booleanClause};
		}
		else {
			booleanClauses = ArrayUtil.append(booleanClauses, booleanClause);
		}

		_searchContext.setBooleanClauses(booleanClauses);
	}

	@Override
	public void addFacet(Facet facet) {
		Map<String, Facet> facets = _searchContext.getFacets();

		facets.put(getAggregationName(facet), facet);
	}

	@Override
	public Optional<String> getKeywordsParameterName() {
		return Optional.ofNullable(_keywordsParameterName);
	}

	@Override
	public Optional<Integer> getPaginationDelta() {
		return Optional.ofNullable(_paginationDelta);
	}

	@Override
	public Optional<String> getPaginationDeltaParameterName() {
		return Optional.ofNullable(_paginationDeltaParameterName);
	}

	@Override
	public Optional<Integer> getPaginationStart() {
		return Optional.ofNullable(_paginationStart);
	}

	@Override
	public Optional<String> getPaginationStartParameterName() {
		return Optional.ofNullable(_paginationStartParameterName);
	}

	@Override
	public QueryConfig getQueryConfig() {
		return _searchContext.getQueryConfig();
	}

	@Override
	public SearchContext getSearchContext() {
		return _searchContext;
	}

	@Override
	public void setKeywords(String keywords) {
		_searchContext.setKeywords(keywords);
	}

	@Override
	public void setKeywordsParameterName(String keywordsParameterName) {
		_keywordsParameterName = keywordsParameterName;
	}

	@Override
	public void setPaginationDelta(int paginationDelta) {
		_paginationDelta = paginationDelta;
	}

	@Override
	public void setPaginationDeltaParameterName(
		String paginationDeltaParameterName) {

		_paginationDeltaParameterName = paginationDeltaParameterName;
	}

	@Override
	public void setPaginationStart(int paginationStart) {
		_paginationStart = paginationStart;
	}

	@Override
	public void setPaginationStartParameterName(
		String paginationStartParameterName) {

		_paginationStartParameterName = paginationStartParameterName;
	}

	protected String getAggregationName(Facet facet) {
		if (facet instanceof com.liferay.portal.search.facet.Facet) {
			com.liferay.portal.search.facet.Facet osgiFacet =
				(com.liferay.portal.search.facet.Facet)facet;

			return osgiFacet.getAggregationName();
		}

		return facet.getFieldName();
	}

	private String _keywordsParameterName;
	private Integer _paginationDelta;
	private String _paginationDeltaParameterName;
	private Integer _paginationStart;
	private String _paginationStartParameterName;
	private final SearchContext _searchContext;

}