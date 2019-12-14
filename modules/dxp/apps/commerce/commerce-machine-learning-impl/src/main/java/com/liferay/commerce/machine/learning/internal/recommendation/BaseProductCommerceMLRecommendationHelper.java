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

package com.liferay.commerce.machine.learning.internal.recommendation;

import com.liferay.commerce.machine.learning.internal.recommendation.api.ProductCommerceMLRecommendationHelper;
import com.liferay.commerce.machine.learning.internal.recommendation.constants.CommerceMLRecommendationField;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;

import java.util.Collections;

/**
 * @author Riccardo Ferrari
 */
public abstract class BaseProductCommerceMLRecommendationHelper
	implements ProductCommerceMLRecommendationHelper {

	public SearchSearchRequest getSearchRequest(
			String indexName, long companyId, long entryClassPK)
		throws Exception {

		SearchSearchRequest searchRequest = new SearchSearchRequest();

		searchRequest.setIndexNames(new String[] {indexName});

		searchRequest.setSize(_DEFAULT_FETCH_SIZE);

		TermQuery companyTermQuery = new TermQueryImpl(
			Field.COMPANY_ID, String.valueOf(companyId));

		TermQuery entryClassPKTermQuery = new TermQueryImpl(
			Field.ENTRY_CLASS_PK, String.valueOf(entryClassPK));

		BooleanQuery booleanQuery = new BooleanQueryImpl();

		booleanQuery.add(companyTermQuery, BooleanClauseOccur.MUST);

		booleanQuery.add(entryClassPKTermQuery, BooleanClauseOccur.MUST);

		searchRequest.setQuery(booleanQuery);

		Sort rankSort = SortFactoryUtil.create(
			CommerceMLRecommendationField.RANK, Sort.INT_TYPE, false);

		searchRequest.setSorts(new Sort[] {rankSort});

		searchRequest.setStats(Collections.emptyMap());

		return searchRequest;
	}

	private static final int _DEFAULT_FETCH_SIZE = 10;

}