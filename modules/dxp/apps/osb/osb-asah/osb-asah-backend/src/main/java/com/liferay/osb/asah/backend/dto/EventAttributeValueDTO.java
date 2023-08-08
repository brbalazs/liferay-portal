/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.BQEventPropertyValue;

import java.util.Date;

/**
 * @author Leslie Wong
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("event-attribute-value")
public class EventAttributeValueDTO {

	public EventAttributeValueDTO() {
	}

	public EventAttributeValueDTO(BQEventPropertyValue bqEventPropertyValue) {
		_lastSeenDate = DateUtil.toUTCString(
			bqEventPropertyValue.getLastSeenDate());
		_value = bqEventPropertyValue.getValue();
	}

	public EventAttributeValueDTO(Date lastSeenDate, String value) {
		_lastSeenDate = DateUtil.toUTCString(lastSeenDate);
		_value = value;
	}

	@JsonProperty("lastSeenDate")
	public String getLastSeenDate() {
		return _lastSeenDate;
	}

	@JsonProperty("value")
	public String getValue() {
		return _value;
	}

	private String _lastSeenDate;
	private String _value;

}