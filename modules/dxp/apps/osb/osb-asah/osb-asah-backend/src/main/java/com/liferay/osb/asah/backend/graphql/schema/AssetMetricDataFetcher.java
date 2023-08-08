/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
@GraphQLTypeWiring(fieldName = "page", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "blog", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "custom", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "document", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "form", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "journal", typeName = "QueryType")
public class AssetMetricDataFetcher extends BaseDataFetcher<AssetMetric> {

	@Override
	public AssetMetric get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		Map<String, Object> context = dataFetchingEnvironment.getContext();

		Set<String> selectedMetrics = (Set<String>)context.get(
			"selectedMetrics");

		selectedMetrics.remove("accessMetric");

		AssetType assetType = searchQueryContext.getAssetType();

		if (assetType.equals(AssetType.PAGE) &&
			StringUtils.isBlank(searchQueryContext.getAssetId())) {

			searchQueryContext.setAssetId(searchQueryContext.getCanonicalUrl());
		}

		return _metricDog.getAssetMetric(searchQueryContext, selectedMetrics);
	}

	@Autowired
	private MetricDog _metricDog;

}