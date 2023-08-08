/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.entity.EventDefinition;
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
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(fieldName = "eventDefinitions", typeName = "QueryType")
public class EventDefinitionBagDataFetcher
	implements DataFetcher<ResultBag<EventDefinitionDTO>> {

	@Override
	public ResultBag<EventDefinitionDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		EventDefinition.Type type = EventDefinition.Type.valueOf(
			dataFetchingEnvironment.getArgument("eventType"));
		String keyword = dataFetchingEnvironment.getArgument("keyword");

		Page<EventDefinition> eventDefinitionPage =
			_eventDefinitionDog.getEventDefinitionPage(
				false, dataFetchingEnvironment.getArgument("hidden"), keyword,
				dataFetchingEnvironment.getArgument("page"),
				dataFetchingEnvironment.getArgument("size"),
				Sort.of(dataFetchingEnvironment.getArgument("sort")), type);

		Stream<EventDefinition> stream = eventDefinitionPage.stream();

		List<EventDefinitionDTO> eventDefinitionDTOs = stream.map(
			EventDefinitionDTO::new
		).collect(
			Collectors.toList()
		);

		return new ResultBag<>(
			eventDefinitionDTOs, eventDefinitionPage.getTotalElements());
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}