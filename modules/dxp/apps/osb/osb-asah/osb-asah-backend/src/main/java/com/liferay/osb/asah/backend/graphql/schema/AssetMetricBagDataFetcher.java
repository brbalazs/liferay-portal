/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.MetricTypeDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;

import graphql.execution.ExecutionTypeInfo;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
@GraphQLTypeWiring(fieldName = "blogs", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "documents", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "forms", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "journals", typeName = "QueryType")
@GraphQLTypeWiring(fieldName = "pages", typeName = "QueryType")
public class AssetMetricBagDataFetcher extends BaseDataFetcher<ResultBag> {

	@Override
	public ResultBag get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		ResultBag<AssetMetric> resultBag = new ResultBag<>();

		long assetMetricsCount = _metricDog.getAssetMetricsCount(
			searchQueryContext);

		if (assetMetricsCount == 0) {
			return resultBag;
		}

		Map<String, Object> context = dataFetchingEnvironment.getContext();
		Map<String, String> sort = dataFetchingEnvironment.getArgument("sort");
		int size = dataFetchingEnvironment.getArgument("size");
		int start = dataFetchingEnvironment.getArgument("start");

		resultBag.setResults(
			_metricDog.getAssetMetrics(
				start / size, searchQueryContext,
				(Set<String>)context.get("selectedMetrics"), size,
				_createSort(searchQueryContext.getAssetType(), sort)));

		resultBag.setTotal(assetMetricsCount);

		return resultBag;
	}

	@Override
	protected AssetType getAssetType(
		DataFetchingEnvironment dataFetchingEnvironment) {

		ExecutionTypeInfo executionTypeInfo =
			dataFetchingEnvironment.getFieldTypeInfo();

		GraphQLFieldDefinition graphQLFieldDefinition =
			executionTypeInfo.getFieldDefinition();

		String name = graphQLFieldDefinition.getName();

		return AssetType.of(name.substring(0, name.length() - 1));
	}

	private Sort _createSort(AssetType assetType, Map<String, String> sort) {
		MetricType metricType = _metricTypeDog.getMetricType(
			assetType, sort.get("column"));

		return new Sort(metricType.getName(), sort.get("type"));
	}

	@Autowired
	private MetricDog _metricDog;

	@Autowired
	private MetricTypeDog _metricTypeDog;

}