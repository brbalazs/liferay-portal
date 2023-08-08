/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.EventMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.EventMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Component
@GraphQLTypeWiring(fieldName = "eventMetric", typeName = "QueryType")
public class EventMetricDataFetcher extends BaseDataFetcher<EventMetric> {

	@Override
	public EventMetric get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		return _eventMetricDog.getEventMetric(searchQueryContext);
	}

	@Autowired
	private EventMetricDog _eventMetricDog;

}