/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventAttributeDefinitionDTO;
import com.liferay.osb.asah.backend.dto.EventDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventAttributeDefinitionDog;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(
	fieldName = "eventAttributeDefinitions", typeName = "EventDefinition"
)
public class EventAttributeDefinitionsDataFetcher
	implements DataFetcher<List<EventAttributeDefinitionDTO>> {

	@Override
	public List<EventAttributeDefinitionDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		EventDefinitionDTO eventDefinitionDTO =
			dataFetchingEnvironment.getSource();

		Long eventDefinitionId = Long.valueOf(eventDefinitionDTO.getId());

		List<EventAttributeDefinition> eventAttributeDefinitions =
			_eventAttributeDefinitionDog.
				getEventAttributeDefinitionsByEventDefinitionId(
					eventDefinitionId);

		Stream<EventAttributeDefinition> stream =
			eventAttributeDefinitions.stream();

		return stream.map(
			eventAttributeDefinition -> new EventAttributeDefinitionDTO(
				eventAttributeDefinition, eventDefinitionId)
		).collect(
			Collectors.toList()
		);
	}

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

}