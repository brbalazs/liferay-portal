/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventAttributeDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventAttributeDefinitionDog;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(
	fieldName = "eventAttributeDefinition", typeName = "QueryType"
)
public class EventAttributeDefinitionDataFetcher
	implements DataFetcher<EventAttributeDefinitionDTO> {

	@Override
	public EventAttributeDefinitionDTO get(
		DataFetchingEnvironment environment) {

		String displayName = environment.getArgument("displayName");

		if (StringUtils.isNotEmpty(displayName)) {
			return _toEventAttributeDefinitionDTO(
				_eventAttributeDefinitionDog.
					fetchEventAttributeDefinitionByDisplayName(displayName));
		}

		String id = environment.getArgument("id");

		if (StringUtils.isNotEmpty(id)) {
			return _toEventAttributeDefinitionDTO(
				_eventAttributeDefinitionDog.getEventAttributeDefinition(
					Long.valueOf(id)));
		}

		throw new OSBAsahException(
			HttpStatus.BAD_REQUEST,
			"Request was made without display name or ID");
	}

	private EventAttributeDefinitionDTO _toEventAttributeDefinitionDTO(
		EventAttributeDefinition eventAttributeDefinition) {

		if (eventAttributeDefinition == null) {
			return null;
		}

		return new EventAttributeDefinitionDTO(eventAttributeDefinition);
	}

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

}