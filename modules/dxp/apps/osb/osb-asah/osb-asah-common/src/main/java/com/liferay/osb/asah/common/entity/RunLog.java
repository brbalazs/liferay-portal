/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class RunLog implements Persistable<Long> {

	public RunLog() {
	}

	public RunLog(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RunLog)) {
			return false;
		}

		RunLog runLog = (RunLog)obj;

		if (Objects.equals(
				JSONUtil.toMap(_contextJSONObject),
				JSONUtil.toMap(runLog._contextJSONObject)) &&
			Objects.equals(_dataSourceId, runLog._dataSourceId) &&
			Objects.equals(_dateLogged, runLog._dateLogged) &&
			Objects.equals(_id, runLog._id) &&
			Objects.equals(_naniteClassName, runLog._naniteClassName) &&
			Objects.equals(_status, runLog._status)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("context")
	@JsonProperty("context")
	public JSONObject getContextJSONObject() {
		return _contextJSONObject;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getDateLogged() {
		if (_dateLogged == null) {
			return null;
		}

		return new Date(_dateLogged.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getNaniteClassName() {
		return _naniteClassName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getStatus() {
		return _status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_contextJSONObject, _dataSourceId, _dateLogged, _id,
			_naniteClassName, _status);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setContextJSONObject(JSONObject contextJSONObject) {
		_contextJSONObject = contextJSONObject;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDateLogged(Date dateLogged) {
		if (dateLogged != null) {
			_dateLogged = new Date(dateLogged.getTime());
		}
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setNaniteClassName(String naniteClassName) {
		_naniteClassName = naniteClassName;
	}

	public void setStatus(String status) {
		_status = status;
	}

	@Transient
	private JSONObject _contextJSONObject;

	@Transient
	private Long _dataSourceId;

	@Transient
	private Date _dateLogged = new Date();

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _naniteClassName;

	@Transient
	private String _status;

}