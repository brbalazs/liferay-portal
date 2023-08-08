/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class DataExportTask implements Persistable<Long> {

	public DataExportTask() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DataExportTask)) {
			return false;
		}

		DataExportTask dataExportTask = (DataExportTask)obj;

		if (Objects.equals(_completedDate, dataExportTask._completedDate) &&
			Objects.equals(_createDate, dataExportTask._createDate) &&
			Objects.equals(_fromDate, dataExportTask._fromDate) &&
			Objects.equals(_id, dataExportTask._id) &&
			Objects.equals(_startedDate, dataExportTask._startedDate) &&
			Objects.equals(_status, dataExportTask._status) &&
			Objects.equals(_toDate, dataExportTask._toDate) &&
			Objects.equals(_type, dataExportTask._type)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getCompletedDate() {
		if (_completedDate == null) {
			return null;
		}

		return new Date(_completedDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonAlias("createdDate")
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getFromDate() {
		if (_fromDate == null) {
			return null;
		}

		return new Date(_fromDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getStartedDate() {
		if (_startedDate == null) {
			return null;
		}

		return new Date(_startedDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Status getStatus() {
		return _status;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getToDate() {
		if (_toDate == null) {
			return null;
		}

		return new Date(_toDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Type getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_completedDate, _createDate, _fromDate, _id, _startedDate, _status,
			_toDate, _type);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setCompletedDate(Date completedDate) {
		if (completedDate != null) {
			_completedDate = new Date(completedDate.getTime());
		}
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setFromDate(Date fromDate) {
		if (fromDate != null) {
			_fromDate = new Date(fromDate.getTime());
		}
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setStartedDate(Date startedDate) {
		if (startedDate != null) {
			_startedDate = new Date(startedDate.getTime());
		}
	}

	public void setStatus(Status status) {
		_status = status;
	}

	public void setToDate(Date toDate) {
		if (toDate != null) {
			_toDate = new Date(toDate.getTime());
		}
	}

	public void setType(Type type) {
		_type = type;
	}

	public static enum Status {

		COMPLETED, ERROR, PENDING, RUNNING

	}

	public static enum Type {

		EVENT, IDENTITY, INDIVIDUAL, MEMBERSHIP, PAGE, SEGMENT

	}

	@Transient
	private Date _completedDate;

	@Transient
	private Date _createDate;

	@Transient
	private Date _fromDate;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private Date _startedDate;

	@Transient
	private Status _status;

	@Transient
	private Date _toDate;

	@Transient
	private Type _type;

}