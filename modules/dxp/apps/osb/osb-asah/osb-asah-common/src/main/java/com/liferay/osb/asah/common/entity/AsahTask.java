/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Objects;

import org.json.JSONObject;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author André Miranda
 */
@Table
public class AsahTask implements Persistable<Long> {

	public AsahTask() {
	}

	public AsahTask(Long id) {
		_id = id;
	}

	public AsahTask(
		String className, JSONObject contextJSONObject, String projectId) {

		_className = className;
		_contextJSONObject = contextJSONObject;
		_projectId = projectId;
	}

	public AsahTask(
		String className, JSONObject contextJSONObject, String cronExpression,
		String projectId) {

		_className = className;
		_contextJSONObject = contextJSONObject;
		_cronExpression = cronExpression;
		_projectId = projectId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AsahTask)) {
			return false;
		}

		AsahTask dataSource = (AsahTask)obj;

		if (Objects.equals(_className, dataSource._className) &&
			Objects.equals(_contextJSONObject, dataSource._contextJSONObject) &&
			Objects.equals(_cronExpression, dataSource._cronExpression) &&
			Objects.equals(_id, dataSource._id) &&
			Objects.equals(_projectId, dataSource._projectId)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getClassName() {
		return _className;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("context")
	@JsonProperty("context")
	public JSONObject getContextJSONObject() {
		return _contextJSONObject;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getCronExpression() {
		return _cronExpression;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getProjectId() {
		return _projectId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_className, _contextJSONObject, _cronExpression, _id, _projectId);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setContextJSONObject(JSONObject contextJSONObject) {
		_contextJSONObject = contextJSONObject;
	}

	public void setCronExpression(String cronExpression) {
		_cronExpression = cronExpression;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	@Transient
	private String _className;

	@Transient
	private JSONObject _contextJSONObject;

	@Transient
	private String _cronExpression;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _projectId;

}