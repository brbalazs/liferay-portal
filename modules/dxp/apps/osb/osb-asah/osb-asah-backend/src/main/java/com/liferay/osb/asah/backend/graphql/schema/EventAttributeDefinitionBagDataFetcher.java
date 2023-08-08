/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventAttributeDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventAttributeDefinitionDog;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(
	fieldName = "eventAttributeDefinitions", typeName = "QueryType"
)
public class EventAttributeDefinitionBagDataFetcher
	implements DataFetcher<ResultBag<EventAttributeDefinitionDTO>> {

	@Override
	public ResultBag<EventAttributeDefinitionDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		Long eventDefinitionId = null;

		String eventDefinitionIdString = dataFetchingEnvironment.getArgument(
			"eventDefinitionId");

		if (StringUtils.isNotBlank(eventDefinitionIdString)) {
			eventDefinitionId = Long.valueOf(
				dataFetchingEnvironment.getArgument("eventDefinitionId"));
		}

		Page<EventAttributeDefinition> eventAttributeDefinitionPage =
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				eventDefinitionId,
				dataFetchingEnvironment.getArgument("keyword"),
				dataFetchingEnvironment.getArgument("page"),
				dataFetchingEnvironment.getArgument("size"),
				Sort.of(dataFetchingEnvironment.getArgument("sort")),
				EventAttributeDefinition.Type.valueOf(
					dataFetchingEnvironment.getArgument("type")));

		Stream<EventAttributeDefinition> stream =
			eventAttributeDefinitionPage.stream();

		List<EventAttributeDefinitionDTO> eventAttributeDefinitionDTOs =
			stream.map(
				EventAttributeDefinitionDTO::new
			).collect(
				Collectors.toList()
			);

		return new ResultBag<>(
			eventAttributeDefinitionDTOs,
			eventAttributeDefinitionPage.getTotalElements());
	}

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

}