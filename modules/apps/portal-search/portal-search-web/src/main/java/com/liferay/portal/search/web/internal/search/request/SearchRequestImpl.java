/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.internal.search.request;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.facet.faceted.searcher.FacetedSearcher;
import com.liferay.portal.kernel.search.facet.faceted.searcher.FacetedSearcherManager;
import com.liferay.portal.search.web.search.request.SearchRequest;
import com.liferay.portal.search.web.search.request.SearchSettings;
import com.liferay.portal.search.web.search.request.SearchSettingsContributor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author André de Oliveira
 */
public class SearchRequestImpl implements SearchRequest {

	public SearchRequestImpl(
		SearchContextBuilder searchContextBuilder,
		SearchContainerBuilder searchContainerBuilder,
		FacetedSearcherManager facetedSearcherManager) {

		_searchContextBuilder = searchContextBuilder;
		_searchContainerBuilder = searchContainerBuilder;
		_facetedSearcherManager = facetedSearcherManager;
	}

	@Override
	public void addSearchSettingsContributor(
		SearchSettingsContributor searchSettingsContributor) {

		_searchSettingsContributors.add(searchSettingsContributor);
	}

	@Override
	public void removeSearchSettingsContributor(
		SearchSettingsContributor searchSettingsContributor) {

		_searchSettingsContributors.remove(searchSettingsContributor);
	}

	@Override
	public SearchResponseImpl search() {
		SearchContext searchContext = buildSearchContext();

		SearchSettingsImpl searchSettingsImpl = buildSettings(searchContext);

		SearchContainer<Document> searchContainer = buildSearchContainer(
			searchSettingsImpl);

		searchContext.setEnd(searchContainer.getEnd());
		searchContext.setStart(searchContainer.getStart());

		Hits hits = search(searchContext);

		searchContainer.setResults(hits.toList());

		searchContainer.setSearch(true);

		searchContainer.setTotal(hits.getLength());

		return buildSearchResponse(
			hits, searchContext, searchContainer, searchSettingsImpl);
	}

	protected SearchContainer<Document> buildSearchContainer(
		SearchSettingsImpl searchSettingsImpl) {

		return _searchContainerBuilder.getSearchContainer(searchSettingsImpl);
	}

	protected SearchContext buildSearchContext() {
		SearchContext searchContext = _searchContextBuilder.getSearchContext();

		searchContext.setAttribute("filterExpired", Boolean.TRUE);
		searchContext.setAttribute("paginationType", "more");

		return searchContext;
	}

	protected SearchResponseImpl buildSearchResponse(
		Hits hits, SearchContext searchContext,
		SearchContainer<Document> searchContainer,
		SearchSettings searchSettings) {

		SearchResponseImpl searchResponseImpl = new SearchResponseImpl();

		searchResponseImpl.setDocuments(hits.toList());
		searchResponseImpl.setHits(hits);
		searchResponseImpl.setKeywords(searchContext.getKeywords());
		searchResponseImpl.setPaginationDelta(searchContainer.getDelta());
		searchResponseImpl.setPaginationStart(searchContainer.getCur());
		searchResponseImpl.setQueryString(
			(String)searchContext.getAttribute("queryString"));
		searchResponseImpl.setSearchContainer(searchContainer);
		searchResponseImpl.setSearchContext(searchContext);
		searchResponseImpl.setSearchSettings(searchSettings);
		searchResponseImpl.setTotalHits(hits.getLength());

		return searchResponseImpl;
	}

	protected SearchSettingsImpl buildSettings(SearchContext searchContext) {
		SearchSettingsImpl searchSettingsImpl = new SearchSettingsImpl(
			searchContext);

		_searchSettingsContributors.forEach(
			searchContributor -> searchContributor.contribute(
				searchSettingsImpl));

		return searchSettingsImpl;
	}

	protected Hits search(
		FacetedSearcher facetedSearcher, SearchContext searchContext) {

		try {
			return facetedSearcher.search(searchContext);
		}
		catch (SearchException se) {
			throw new RuntimeException(se);
		}
	}

	protected Hits search(SearchContext searchContext) {
		FacetedSearcher facetedSearcher =
			_facetedSearcherManager.createFacetedSearcher();

		return search(facetedSearcher, searchContext);
	}

	private final FacetedSearcherManager _facetedSearcherManager;
	private final SearchContainerBuilder _searchContainerBuilder;
	private final SearchContextBuilder _searchContextBuilder;
	private final Set<SearchSettingsContributor> _searchSettingsContributors =
		new HashSet<>();

}