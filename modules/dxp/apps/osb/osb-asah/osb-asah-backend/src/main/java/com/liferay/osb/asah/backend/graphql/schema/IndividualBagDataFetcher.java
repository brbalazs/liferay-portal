/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.MetricTypeDog;
import com.liferay.osb.asah.backend.dog.ReportIndividualDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.Individual;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.ResultBag;

import graphql.execution.ExecutionTypeInfo;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
@GraphQLTypeWiring(fieldName = "individuals", typeName = "Metric")
public class IndividualBagDataFetcher
	extends BaseDataFetcher<ResultBag<Individual>> {

	@Override
	public ResultBag<Individual> get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		String keywords = dataFetchingEnvironment.getArgument("keywords");

		ExecutionTypeInfo fieldExecutionTypeInfo =
			dataFetchingEnvironment.getFieldTypeInfo();

		ExecutionTypeInfo parentExecutionTypeInfo =
			fieldExecutionTypeInfo.getParentTypeInfo();

		GraphQLFieldDefinition graphQLFieldDefinition =
			parentExecutionTypeInfo.getFieldDefinition();

		MetricType metricType = _metricTypeDog.getMetricType(
			searchQueryContext.getAssetType(),
			graphQLFieldDefinition.getName());

		int size = dataFetchingEnvironment.getArgument("size");
		int start = dataFetchingEnvironment.getArgument("start");

		return _reportIndividualDog.getIndividualResultBag(
			keywords, metricType, searchQueryContext, size, start);
	}

	@Autowired
	private MetricTypeDog _metricTypeDog;

	@Autowired
	private ReportIndividualDog _reportIndividualDog;

}