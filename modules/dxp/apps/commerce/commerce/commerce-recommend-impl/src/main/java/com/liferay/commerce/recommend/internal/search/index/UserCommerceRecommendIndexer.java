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

package com.liferay.commerce.recommend.internal.search.index;

import com.liferay.commerce.recommend.internal.api.CommerceRecommendIndexer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Ferrari
 */
@Component(service = CommerceRecommendIndexer.class)
public class UserCommerceRecommendIndexer implements CommerceRecommendIndexer {

	@Override
	public void createIndex(long companyId) {
		_commerceRecommendSearchEngineHelper.createIndex(
			getIndexName(companyId), _INDEX_MAPPING_FILE_NAME);
	}

	@Override
	public void dropIndex(long companyId) {
		_commerceRecommendSearchEngineHelper.dropIndex(getIndexName(companyId));
	}

	@Override
	public String getIndexName(long companyId) {
		return String.format(_INDEX_NAME_PREFIX, companyId);
	}

	private static final String _INDEX_MAPPING_FILE_NAME =
		"/META-INF/search/commerce-user-recommend-document-type.json";

	private static final String _INDEX_NAME_PREFIX =
		"commerce-user-recommend-%s";

	@Reference
	private CommerceRecommendSearchEngineHelper
		_commerceRecommendSearchEngineHelper;

}