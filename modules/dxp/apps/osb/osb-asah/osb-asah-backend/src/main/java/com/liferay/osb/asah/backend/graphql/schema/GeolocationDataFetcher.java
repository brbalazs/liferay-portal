/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.MetricTypeDog;
import com.liferay.osb.asah.backend.dog.SiteMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.MetricType;

import graphql.execution.ExecutionTypeInfo;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
@GraphQLTypeWiring(fieldName = "geolocation", typeName = "Metric")
public class GeolocationDataFetcher extends BaseDataFetcher<List<Metric>> {

	@Override
	public List<Metric> get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		ExecutionTypeInfo fieldExecutionTypeInfo =
			dataFetchingEnvironment.getFieldTypeInfo();

		ExecutionTypeInfo parentExecutionTypeInfo =
			fieldExecutionTypeInfo.getParentTypeInfo();

		GraphQLFieldDefinition graphQLFieldDefinition =
			parentExecutionTypeInfo.getFieldDefinition();

		MetricType metricType = _metricTypeDog.getMetricType(
			searchQueryContext.getAssetType(),
			graphQLFieldDefinition.getName());

		if (searchQueryContext.getAssetType() == AssetType.SITE) {
			return _siteMetricDog.getGeolocationMetrics(
				metricType, searchQueryContext);
		}

		return _metricDog.getGeolocationMetrics(metricType, searchQueryContext);
	}

	@Autowired
	private MetricDog _metricDog;

	@Autowired
	private MetricTypeDog _metricTypeDog;

	@Autowired
	private SiteMetricDog _siteMetricDog;

}