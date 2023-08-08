/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.common.dog.EventAnalysisDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.AnalysisType;
import com.liferay.osb.asah.common.model.EventAnalysisBreakdown;
import com.liferay.osb.asah.common.model.EventAnalysisFilter;
import com.liferay.osb.asah.common.model.EventAnalysisResult;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
@GraphQLTypeWiring(fieldName = "eventAnalysisResult", typeName = "QueryType")
public class EventAnalysisResultDataFetcher
	extends BaseDataFetcher<EventAnalysisResult> {

	@Override
	public EventAnalysisResult get(
		DataFetchingEnvironment environment,
		SearchQueryContext searchQueryContext) {

		List<EventAnalysisBreakdown> eventAnalysisBreakdowns = ListUtil.map(
			environment.getArgument("eventAnalysisBreakdowns"),
			eventAnalysisBreakdown -> new EventAnalysisBreakdown(
				(Map<String, Object>)eventAnalysisBreakdown));
		List<EventAnalysisFilter> eventAnalysisFilters = ListUtil.map(
			environment.getArgument("eventAnalysisFilters"),
			eventAnalysisFilter -> new EventAnalysisFilter(
				(Map<String, Object>)eventAnalysisFilter));

		return _eventAnalysisDog.getEventAnalysisResult(
			AnalysisType.valueOf(environment.getArgument("analysisType")),
			Long.valueOf(environment.getArgument("channelId")),
			environment.getArgument("compareToPrevious"),
			eventAnalysisBreakdowns, eventAnalysisFilters,
			Long.valueOf(environment.getArgument("eventDefinitionId")),
			environment.getArgument("page"), environment.getArgument("size"),
			searchQueryContext.getTimeRange());
	}

	@Autowired
	private EventAnalysisDog _eventAnalysisDog;

}