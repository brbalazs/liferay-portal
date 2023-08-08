/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinitionEventAttributeDefinition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Leslie Wong
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("event-attribute-definition")
public class EventAttributeDefinitionDTO {

	public EventAttributeDefinitionDTO() {
	}

	public EventAttributeDefinitionDTO(
		EventAttributeDefinition eventAttributeDefinition) {

		_dataType = eventAttributeDefinition.getDataType();
		_description = eventAttributeDefinition.getDescription();
		_displayName = eventAttributeDefinition.getDisplayName();
		_id = String.valueOf(eventAttributeDefinition.getId());
		_name = eventAttributeDefinition.getName();

		List<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions = new ArrayList<>(
				eventAttributeDefinition.
					getEventDefinitionEventAttributeDefinitions());

		Stream<EventDefinitionEventAttributeDefinition> stream =
			eventDefinitionEventAttributeDefinitions.stream();

		_sampleValue = stream.filter(
			eventDefinitionEventAttributeDefinition ->
				eventDefinitionEventAttributeDefinition.getSampleValue() != null
		).sorted(
			Comparator.comparing(
				EventDefinitionEventAttributeDefinition::getEventDefinitionId)
		).findFirst(
		).map(
			EventDefinitionEventAttributeDefinition::getSampleValue
		).orElse(
			null
		);

		_type = eventAttributeDefinition.getType();
	}

	public EventAttributeDefinitionDTO(
		EventAttributeDefinition eventAttributeDefinition,
		Long eventDefinitionId) {

		this(eventAttributeDefinition);

		Set<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions =
				eventAttributeDefinition.
					getEventDefinitionEventAttributeDefinitions();

		for (EventDefinitionEventAttributeDefinition
				eventDefinitionEventAttributeDefinition :
					eventDefinitionEventAttributeDefinitions) {

			if (Objects.equals(
					eventDefinitionId,
					eventDefinitionEventAttributeDefinition.
						getEventDefinitionId())) {

				_sampleValue =
					eventDefinitionEventAttributeDefinition.getSampleValue();

				break;
			}
		}
	}

	@JsonProperty("dataType")
	public EventAttributeDefinition.DataType getDataType() {
		return _dataType;
	}

	@JsonProperty("description")
	public String getDescription() {
		return _description;
	}

	@JsonProperty("displayName")
	public String getDisplayName() {
		return _displayName;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonProperty("name")
	public String getName() {
		return _name;
	}

	@JsonProperty("sampleValue")
	public String getSampleValue() {
		return _sampleValue;
	}

	@JsonProperty("type")
	public EventAttributeDefinition.Type getType() {
		return _type;
	}

	private EventAttributeDefinition.DataType _dataType;
	private String _description;
	private String _displayName;
	private String _id;
	private String _name;
	private String _sampleValue;
	private EventAttributeDefinition.Type _type;

}