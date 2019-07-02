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

package com.liferay.commerce.machine.learning.internal.recommend.search.index;

import com.liferay.commerce.machine.learning.internal.search.api.CommerceIndexer;
import com.liferay.commerce.machine.learning.internal.search.index.CommerceSearchEngineHelper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = CommerceIndexer.class)
public class ItemRecommendCommerceIndexer implements CommerceIndexer {

	@Override
	public void createIndex(long companyId) {
		_commerceSearchEngineHelper.createIndex(
			getIndexName(companyId), _INDEX_MAPPING_FILE_NAME);
	}

	@Override
	public void dropIndex(long companyId) {
		_commerceSearchEngineHelper.dropIndex(getIndexName(companyId));
	}

	@Override
	public String getIndexName(long companyId) {
		return String.format(_INDEX_NAME_PREFIX, companyId);
	}

	private static final String _INDEX_MAPPING_FILE_NAME =
		"/META-INF/search/commerce-item-recommend-document-type.json";

	private static final String _INDEX_NAME_PREFIX =
		"commerce-item-recommend-%s";

	@Reference
	private CommerceSearchEngineHelper _commerceSearchEngineHelper;

}