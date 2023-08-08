/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.util.ListUtil;

import java.util.List;
import java.util.Objects;

/**
 * @author Rachael Koestartyo
 */
@GraphQLType("EventAnalysisReferencedObject")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("referencedObjects")
public class EventAnalysisReferencedObjectDTO {

	public EventAnalysisReferencedObjectDTO(
		EventDefinition eventDefinition,
		List<EventAttributeDefinition> eventAttributeDefinitions) {

		_eventAttributeDefinitionDTOs = ListUtil.map(
			eventAttributeDefinitions, EventAttributeDefinitionDTO::new);
		_eventDefinitionDTO = new EventDefinitionDTO(eventDefinition);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof EventAnalysisReferencedObjectDTO)) {
			return false;
		}

		EventAnalysisReferencedObjectDTO eventAnalysisReferencedObjectDTO =
			(EventAnalysisReferencedObjectDTO)obj;

		if (Objects.equals(
				_eventAttributeDefinitionDTOs,
				eventAnalysisReferencedObjectDTO.
					_eventAttributeDefinitionDTOs) &&
			Objects.equals(
				_eventDefinitionDTO,
				eventAnalysisReferencedObjectDTO._eventDefinitionDTO)) {

			return true;
		}

		return false;
	}

	@GraphQLProperty("eventAttributeDefinitions")
	@JsonProperty("eventAttributeDefinitions")
	public List<EventAttributeDefinitionDTO> getEventAttributeDefinitionDTOs() {
		return _eventAttributeDefinitionDTOs;
	}

	@GraphQLProperty("eventDefinition")
	@JsonProperty("eventDefinition")
	public EventDefinitionDTO getEventDefinitionDTO() {
		return _eventDefinitionDTO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_eventAttributeDefinitionDTOs, _eventDefinitionDTO);
	}

	private final List<EventAttributeDefinitionDTO>
		_eventAttributeDefinitionDTOs;
	private final EventDefinitionDTO _eventDefinitionDTO;

}