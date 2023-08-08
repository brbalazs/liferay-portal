/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Leslie Wong
 */
@Table
public class EventAttributeDefinition implements Persistable<Long> {

	public EventAttributeDefinition() {
	}

	public EventAttributeDefinition(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public void addEventDefinitionEventAttributeDefinition(
		EventDefinitionEventAttributeDefinition
			eventDefinitionEventAttributeDefinition) {

		_eventDefinitionEventAttributeDefinitions.add(
			eventDefinitionEventAttributeDefinition);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof EventAttributeDefinition)) {
			return false;
		}

		EventAttributeDefinition eventDefinition =
			(EventAttributeDefinition)obj;

		if (Objects.equals(_dataType, eventDefinition._dataType) &&
			Objects.equals(_description, eventDefinition._description) &&
			Objects.equals(_displayName, eventDefinition._displayName) &&
			Objects.equals(
				_eventDefinitionEventAttributeDefinitions,
				eventDefinition._eventDefinitionEventAttributeDefinitions) &&
			Objects.equals(_id, eventDefinition._id) &&
			Objects.equals(_name, eventDefinition._name)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public DataType getDataType() {
		return _dataType;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDescription() {
		return _description;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDisplayName() {
		return _displayName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@MappedCollection(idColumn = "eventattributedefinitionid")
	public Set<EventDefinitionEventAttributeDefinition>
		getEventDefinitionEventAttributeDefinitions() {

		return _eventDefinitionEventAttributeDefinitions;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getName() {
		return _name;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Type getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_dataType, _description, _displayName,
			_eventDefinitionEventAttributeDefinitions, _id, _name);
	}

	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setDataType(DataType dataType) {
		_dataType = dataType;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDisplayName(String displayName) {
		_displayName = displayName;
	}

	public void setEventDefinitionEventAttributeDefinitions(
		Set<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinition) {

		_eventDefinitionEventAttributeDefinitions =
			eventDefinitionEventAttributeDefinition;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(boolean isNew) {
		_isNew = isNew;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setType(Type type) {
		_type = type;
	}

	public static enum DataType {

		BOOLEAN, DATE, DURATION, NUMBER, STRING

	}

	public static enum Type {

		ALL, GLOBAL, LOCAL

	}

	@Transient
	private DataType _dataType;

	@Transient
	private String _description;

	@Transient
	private String _displayName;

	@Transient
	private Set<EventDefinitionEventAttributeDefinition>
		_eventDefinitionEventAttributeDefinitions = new HashSet<>();

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _name;

	@Transient
	private Type _type;

}