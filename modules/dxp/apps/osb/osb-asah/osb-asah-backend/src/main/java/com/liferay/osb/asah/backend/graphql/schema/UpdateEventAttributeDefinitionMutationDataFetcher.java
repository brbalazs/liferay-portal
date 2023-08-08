/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventAttributeDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventAttributeDefinitionDog;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
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
	fieldName = "updateEventAttributeDefinition", typeName = "MutationType"
)
public class UpdateEventAttributeDefinitionMutationDataFetcher
	implements DataFetcher<EventAttributeDefinitionDTO> {

	@Override
	public EventAttributeDefinitionDTO get(
		DataFetchingEnvironment environment) {

		return new EventAttributeDefinitionDTO(
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				EventAttributeDefinition.DataType.valueOf(
					environment.getArgument("dataType")),
				environment.getArgument("description"),
				environment.getArgument("displayName"),
				Long.valueOf(
					environment.getArgument("eventAttributeDefinitionId")),
				null, null));
	}

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

}