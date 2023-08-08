/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.BQIndividualMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.IndividualMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
@GraphQLTypeWiring(fieldName = "individualMetric", typeName = "QueryType")
public class IndividualMetricDataFetcher
	extends BaseDataFetcher<IndividualMetric> {

	@Override
	public IndividualMetric get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		Map<String, Object> context = dataFetchingEnvironment.getContext();

		return _bqIndividualMetricDog.getIndividualMetric(
			searchQueryContext, (Set<String>)context.get("selectedMetrics"));
	}

	@Autowired
	private BQIndividualMetricDog _bqIndividualMetricDog;

}