/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(
	fieldName = "updateEventDefinition", typeName = "MutationType"
)
public class UpdateEventDefinitionMutationDataFetcher
	implements DataFetcher<EventDefinitionDTO> {

	@Override
	public EventDefinitionDTO get(DataFetchingEnvironment environment) {
		return new EventDefinitionDTO(
			_eventDefinitionDog.updateEventDefinition(
				null, null, environment.getArgument("description"),
				environment.getArgument("displayName"),
				Long.valueOf(environment.getArgument("eventDefinitionId"))));
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}