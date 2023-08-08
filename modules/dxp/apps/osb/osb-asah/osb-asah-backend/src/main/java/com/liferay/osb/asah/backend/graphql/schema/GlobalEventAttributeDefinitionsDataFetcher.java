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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alejo Ceballos
 */
@Component
@GraphQLTypeWiring(
	fieldName = "globalEventAttributeDefinitions", typeName = "QueryType"
)
public class GlobalEventAttributeDefinitionsDataFetcher
	implements DataFetcher<List<EventAttributeDefinitionDTO>> {

	@Override
	public List<EventAttributeDefinitionDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		List<EventAttributeDefinition> eventAttributeDefinitions =
			_eventAttributeDefinitionDog.getEventAttributeDefinitionsByType(
				EventAttributeDefinition.Type.GLOBAL);

		Stream<EventAttributeDefinition> stream =
			eventAttributeDefinitions.stream();

		return stream.filter(
			eventAttributeDefinition -> _globalEventAttributeNames.contains(
				eventAttributeDefinition.getName())
		).map(
			EventAttributeDefinitionDTO::new
		).collect(
			Collectors.toList()
		);
	}

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

	private final List<String> _globalEventAttributeNames = Arrays.asList(
		"canonicalUrl", "pageTitle", "referrer", "url");

}