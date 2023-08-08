/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.SiteMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.HeatMapMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(fieldName = "siteVisitorHeatMap", typeName = "QueryType")
public class SiteVisitorHeatMapDataFetcher
	extends BaseDataFetcher<List<HeatMapMetric>> {

	@Override
	public List<HeatMapMetric> get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		return siteMetricDog.getHeatMapMetrics(
			searchQueryContext.getChannelIdAsLong(),
			searchQueryContext.getTimeRange());
	}

	@Autowired
	protected SiteMetricDog siteMetricDog;

}