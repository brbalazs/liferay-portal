/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventAnalysisDTO;
import com.liferay.osb.asah.common.dog.EventAnalysisDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.AnalysisType;
import com.liferay.osb.asah.common.model.EventAnalysisBreakdown;
import com.liferay.osb.asah.common.model.EventAnalysisFilter;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
@GraphQLTypeWiring(fieldName = "createEventAnalysis", typeName = "MutationType")
public class CreateEventAnalysisMutationDataFetcher
	implements DataFetcher<EventAnalysisDTO> {

	@Override
	public EventAnalysisDTO get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		List<EventAnalysisBreakdown> eventAnalysisBreakdowns = ListUtil.map(
			dataFetchingEnvironment.getArgument("eventAnalysisBreakdowns"),
			eventAnalysisBreakdown -> new EventAnalysisBreakdown(
				(Map<String, Object>)eventAnalysisBreakdown));
		List<EventAnalysisFilter> eventAnalysisFilters = ListUtil.map(
			dataFetchingEnvironment.getArgument("eventAnalysisFilters"),
			eventAnalysisFilter -> new EventAnalysisFilter(
				(Map<String, Object>)eventAnalysisFilter));

		LocalDate rangeEndLocalDate = null;
		LocalDate rangeStartLocalDate = null;

		if ((dataFetchingEnvironment.getArgument("rangeEnd") != null) &&
			(dataFetchingEnvironment.getArgument("rangeStart") != null)) {

			rangeEndLocalDate = LocalDate.parse(
				dataFetchingEnvironment.getArgument("rangeEnd"));
			rangeStartLocalDate = LocalDate.parse(
				dataFetchingEnvironment.getArgument("rangeStart"));
		}

		return new EventAnalysisDTO(
			_eventAnalysisDog.addEventAnalysis(
				AnalysisType.valueOf(
					dataFetchingEnvironment.getArgument("analysisType")),
				Long.valueOf(dataFetchingEnvironment.getArgument("channelId")),
				dataFetchingEnvironment.getArgument("compareToPrevious"),
				eventAnalysisBreakdowns, eventAnalysisFilters,
				Long.valueOf(
					dataFetchingEnvironment.getArgument("eventDefinitionId")),
				dataFetchingEnvironment.getArgument("name"), rangeEndLocalDate,
				dataFetchingEnvironment.getArgument("rangeKey"),
				rangeStartLocalDate,
				Long.valueOf(dataFetchingEnvironment.getArgument("userId")),
				dataFetchingEnvironment.getArgument("userName")));
	}

	@Autowired
	private EventAnalysisDog _eventAnalysisDog;

}