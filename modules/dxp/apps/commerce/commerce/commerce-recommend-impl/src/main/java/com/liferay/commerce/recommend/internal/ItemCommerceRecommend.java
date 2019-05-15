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

import com.liferay.commerce.recommend.internal.api.CommerceRecommend;
import com.liferay.commerce.recommend.internal.api.CommerceRecommendIndexer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = CommerceRecommend.class)
public class ItemCommerceRecommend extends BaseContentCommerceRecommend {

	@Override
	public Hits getRecommendations(long companyId, long entryClassPK)
		throws Exception {

		SearchSearchRequest searchRequest = getSearchRequest(
			_commerceRecommendIndexer.getIndexName(companyId), companyId,
			entryClassPK);

		SearchSearchResponse searchSearchResponse =
			_searchEngineAdapter.execute(searchRequest);

		if (_log.isTraceEnabled()) {
			_log.trace(searchSearchResponse.getSearchRequestString());
		}

		return searchSearchResponse.getHits();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ItemCommerceRecommend.class);

	@Reference(
		target = "(component.name=com.liferay.commerce.recommend.internal.search.index.ItemCommerceRecommendIndexer)"
	)
	private CommerceRecommendIndexer _commerceRecommendIndexer;

	@Reference
	private SearchEngineAdapter _searchEngineAdapter;

}