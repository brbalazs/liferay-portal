/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.EventAnalysisDTO;
import com.liferay.osb.asah.common.dog.EventAnalysisDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
@GraphQLTypeWiring(fieldName = "eventAnalysis", typeName = "QueryType")
public class EventAnalysisDataFetcher
	extends BaseDataFetcher<EventAnalysisDTO> {

	@Override
	public EventAnalysisDTO get(
		DataFetchingEnvironment environment,
		SearchQueryContext searchQueryContext) {

		return new EventAnalysisDTO(
			_eventAnalysisDog.getEventAnalysis(
				Long.valueOf(environment.getArgument("eventAnalysisId"))));
	}

	@Autowired
	private EventAnalysisDog _eventAnalysisDog;

}