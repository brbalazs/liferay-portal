/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.MetricTypeDog;
import com.liferay.osb.asah.backend.dog.SegmentMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;

import graphql.execution.ExecutionTypeInfo;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "segment", typeName = "Metric")
public class SegmentDataFetcher extends BaseDataFetcher<ResultBag<Metric>> {

	@Override
	public ResultBag<Metric> get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		ExecutionTypeInfo fieldExecutionTypeInfo =
			dataFetchingEnvironment.getFieldTypeInfo();

		ExecutionTypeInfo parentExecutionTypeInfo =
			fieldExecutionTypeInfo.getParentTypeInfo();

		GraphQLFieldDefinition graphQLFieldDefinition =
			parentExecutionTypeInfo.getFieldDefinition();

		return _segmentDog.getSegmentMetricResultBag(
			_metricTypeDog.getMetricType(
				searchQueryContext.getAssetType(),
				graphQLFieldDefinition.getName()),
			searchQueryContext);
	}

	@Autowired
	private MetricTypeDog _metricTypeDog;

	@Autowired
	private SegmentMetricDog _segmentDog;

}