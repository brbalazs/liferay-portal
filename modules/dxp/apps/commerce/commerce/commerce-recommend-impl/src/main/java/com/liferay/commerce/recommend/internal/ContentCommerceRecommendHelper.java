/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.recommend.internal;

import com.liferay.commerce.recommend.internal.api.CommerceRecommendField;
import com.liferay.commerce.recommend.internal.api.CommerceRecommendHelper;
import com.liferay.commerce.recommend.internal.api.CommerceRecommendIndexer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;

import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = CommerceRecommendHelper.class)
public class ContentCommerceRecommendHelper implements CommerceRecommendHelper {

	@Override
	public Hits getRecommendations(
			long companyId, long entryClassPK, Query query)
		throws Exception {

		SearchSearchRequest searchRequest = new SearchSearchRequest();

		searchRequest.setIndexNames(
			new String[] {_commerceRecommendIndexer.getIndexName(companyId)});

		searchRequest.setSize(_DEFAULT_FETCH_SIZE);

		TermQueryImpl companyTermQuery = new TermQueryImpl(
			Field.COMPANY_ID, String.valueOf(companyId));

		TermQuery entryClassPKTermQuery = new TermQueryImpl(
			Field.ENTRY_CLASS_PK, String.valueOf(entryClassPK));

		BooleanQuery booleanQuery = new BooleanQueryImpl();

		booleanQuery.add(companyTermQuery, BooleanClauseOccur.MUST);

		booleanQuery.add(entryClassPKTermQuery, BooleanClauseOccur.MUST);

		if (query != null) {
			booleanQuery.add(query, BooleanClauseOccur.MUST);
		}

		searchRequest.setQuery(booleanQuery);

		Sort rankSort = SortFactoryUtil.create(
			CommerceRecommendField.RANK, Sort.INT_TYPE, false);

		searchRequest.setSorts(new Sort[] {rankSort});

		searchRequest.setStats(Collections.emptyMap());

		SearchSearchResponse searchSearchResponse =
			_searchEngineAdapter.execute(searchRequest);

		if (_log.isTraceEnabled()) {
			_log.trace(searchSearchResponse.getSearchRequestString());
		}

		return searchSearchResponse.getHits();
	}

	private static final int _DEFAULT_FETCH_SIZE = 10;

	private static final Log _log = LogFactoryUtil.getLog(
		ContentCommerceRecommendHelper.class);

	@Reference(
		target = "(component.name=com.liferay.commerce.recommend.internal.search.index.ContentCommerceRecommendIndexer)"
	)
	private CommerceRecommendIndexer _commerceRecommendIndexer;

	@Reference
	private SearchEngineAdapter _searchEngineAdapter;

}