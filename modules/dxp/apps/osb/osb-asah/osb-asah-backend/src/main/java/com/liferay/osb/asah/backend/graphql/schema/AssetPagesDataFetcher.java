/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.MetricTypeDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
@GraphQLTypeWiring(fieldName = "assetPages", typeName = "QueryType")
public class AssetPagesDataFetcher extends BaseDataFetcher<List<AssetMetric>> {

	@Override
	public List<AssetMetric> get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		return _metricDog.getAppearsOnMetrics(
			_metricTypeDog.getDefaultMetricType(
				searchQueryContext.getAssetType()),
			searchQueryContext);
	}

	@Autowired
	private MetricDog _metricDog;

	@Autowired
	private MetricTypeDog _metricTypeDog;

}