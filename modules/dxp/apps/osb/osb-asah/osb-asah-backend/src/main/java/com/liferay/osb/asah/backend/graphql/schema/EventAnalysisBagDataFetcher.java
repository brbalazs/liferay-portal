/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventAnalysisDTO;
import com.liferay.osb.asah.common.dog.EventAnalysisDog;
import com.liferay.osb.asah.common.entity.EventAnalysis;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
@GraphQLTypeWiring(fieldName = "eventAnalyses", typeName = "QueryType")
public class EventAnalysisBagDataFetcher
	implements DataFetcher<ResultBag<EventAnalysisDTO>> {

	@Override
	public ResultBag<EventAnalysisDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		Page<EventAnalysis> eventAnalysisPage =
			_eventAnalysisDog.getEventAnalysisPage(
				Long.valueOf(dataFetchingEnvironment.getArgument("channelId")),
				dataFetchingEnvironment.getArgument("keywords"),
				dataFetchingEnvironment.getArgument("page"),
				dataFetchingEnvironment.getArgument("size"),
				Sort.of(dataFetchingEnvironment.getArgument("sort")));

		Stream<EventAnalysis> stream = eventAnalysisPage.stream();

		List<EventAnalysisDTO> eventAnalysisDTOs = stream.map(
			EventAnalysisDTO::new
		).collect(
			Collectors.toList()
		);

		return new ResultBag<>(
			eventAnalysisDTOs, eventAnalysisPage.getTotalElements());
	}

	@Autowired
	private EventAnalysisDog _eventAnalysisDog;

}