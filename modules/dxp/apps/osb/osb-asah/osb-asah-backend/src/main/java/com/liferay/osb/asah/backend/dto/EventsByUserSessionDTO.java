/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;

import java.util.List;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@GraphQLType("EventsByUserSession")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("eventsByUserSession")
public class EventsByUserSessionDTO {

	public EventsByUserSessionDTO(
		List<UserSessionDTO> userSessionDTOs, Integer totalEvents) {

		_userSessionDTOs = userSessionDTOs;
		_totalEvents = totalEvents;
	}

	public Integer getTotalEvents() {
		return _totalEvents;
	}

	@GraphQLProperty("userSessions")
	@JsonProperty("userSessions")
	public List<UserSessionDTO> getUserSessionDTOs() {
		return _userSessionDTOs;
	}

	private final Integer _totalEvents;
	private final List<UserSessionDTO> _userSessionDTOs;

}