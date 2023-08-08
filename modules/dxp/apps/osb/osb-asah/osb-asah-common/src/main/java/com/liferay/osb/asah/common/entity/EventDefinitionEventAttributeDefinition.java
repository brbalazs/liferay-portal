/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Leslie Wong
 */
@Table
public class EventDefinitionEventAttributeDefinition {

	public EventDefinitionEventAttributeDefinition() {
	}

	public EventDefinitionEventAttributeDefinition(
		Long eventDefinitionId, String sampleValue) {

		_eventDefinitionId = eventDefinitionId;
		_sampleValue = sampleValue;
	}

	public EventDefinitionEventAttributeDefinition(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof EventDefinitionEventAttributeDefinition)) {
			return false;
		}

		EventDefinitionEventAttributeDefinition
			eventDefinitionEventAttributeDefinition =
				(EventDefinitionEventAttributeDefinition)obj;

		if (Objects.equals(
				_eventDefinitionId,
				eventDefinitionEventAttributeDefinition._eventDefinitionId) &&
			Objects.equals(
				_sampleValue,
				eventDefinitionEventAttributeDefinition._sampleValue)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getEventDefinitionId() {
		return _eventDefinitionId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getSampleValue() {
		return _sampleValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_eventDefinitionId, _sampleValue);
	}

	public void setEventDefinitionId(Long eventDefinitionId) {
		_eventDefinitionId = eventDefinitionId;
	}

	public void setSampleValue(String sampleValue) {
		_sampleValue = sampleValue;
	}

	@Transient
	private Long _eventDefinitionId;

	@Transient
	private String _sampleValue;

}