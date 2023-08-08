/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.PageReferrerDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.PageReferrerMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leonardo Barros
 */
@Component
@GraphQLTypeWiring(fieldName = "pageReferrerMetrics", typeName = "PageMetric")
public class PageReferrerMetricsDataFetcher
	extends BaseDataFetcher<List<PageReferrerMetric>> {

	@Override
	public List<PageReferrerMetric> get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		return _pageReferrerDog.getPageReferrerMetrics(searchQueryContext);
	}

	@Override
	protected AssetType getAssetType(
		DataFetchingEnvironment dataFetchingEnvironment) {

		return AssetType.PAGE;
	}

	@Autowired
	private PageReferrerDog _pageReferrerDog;

}