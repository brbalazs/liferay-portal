/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.entity.EventDefinition;

/**
 * @author Leslie Wong
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("event-definition")
public class EventDefinitionDTO {

	public EventDefinitionDTO() {
	}

	public EventDefinitionDTO(EventDefinition eventDefinition) {
		_description = eventDefinition.getDescription();
		_displayName = eventDefinition.getDisplayName();
		_hidden = eventDefinition.isHidden();
		_id = String.valueOf(eventDefinition.getId());
		_name = eventDefinition.getName();
		_type = eventDefinition.getType();
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

	@JsonProperty("type")
	public EventDefinition.Type getType() {
		return _type;
	}

	@JsonProperty("hidden")
	public boolean isHidden() {
		return _hidden;
	}

	private String _description;
	private String _displayName;
	private boolean _hidden;
	private String _id;
	private String _name;
	private EventDefinition.Type _type;

}