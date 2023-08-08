/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.EventsByUserSessionDTO;
import com.liferay.osb.asah.backend.dto.UserSessionDTO;
import com.liferay.osb.asah.common.dog.BQEventDog;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Component
@GraphQLTypeWiring(fieldName = "eventsByUserSessions", typeName = "QueryType")
public class EventsByUserSessionsDataFetcher
	extends BaseDataFetcher<EventsByUserSessionDTO> {

	@Override
	public EventsByUserSessionDTO get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		Comparator<UserSessionDTO> comparator = Comparator.comparing(
			UserSessionDTO::getCreateDate);

		Map<BQSession, List<BQEvent>> bqSessions =
			_bqEventDog.searchBQEventsGroupByUserSessionId(
				Long.valueOf(dataFetchingEnvironment.getArgument("channelId")),
				dataFetchingEnvironment.getArgument("entityId"),
				dataFetchingEnvironment.getArgument("keywords"),
				dataFetchingEnvironment.getArgument("page"),
				dataFetchingEnvironment.getArgument("size"),
				searchQueryContext.getTimeRange());

		Set<Map.Entry<BQSession, List<BQEvent>>> entrySet =
			bqSessions.entrySet();

		Stream<Map.Entry<BQSession, List<BQEvent>>> stream = entrySet.stream();

		return new EventsByUserSessionDTO(
			stream.map(
				entry -> new UserSessionDTO(entry.getValue(), entry.getKey())
			).sorted(
				comparator.reversed()
			).collect(
				Collectors.toList()
			),
			_bqEventDog.countBQEvents(
				Long.valueOf(dataFetchingEnvironment.getArgument("channelId")),
				dataFetchingEnvironment.getArgument("entityId"),
				dataFetchingEnvironment.getArgument("keywords"),
				searchQueryContext.getTimeRange()));
	}

	@Autowired
	private BQEventDog _bqEventDog;

}