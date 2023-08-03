/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Matthew Kong
 */
@Table
public final class DataControlTask implements Persistable<Long> {

	public DataControlTask() {
	}

	public DataControlTask(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DataControlTask)) {
			return false;
		}

		DataControlTask dataControlTask = (DataControlTask)obj;

		if (Objects.equals(_batchId, dataControlTask._batchId) &&
			Objects.equals(_completeDate, dataControlTask._completeDate) &&
			Objects.equals(_createDate, dataControlTask._createDate) &&
			Objects.equals(_emailAddress, dataControlTask._emailAddress) &&
			Objects.equals(_id, dataControlTask._id) &&
			Objects.equals(_startDate, dataControlTask._startDate) &&
			Objects.equals(_status, dataControlTask._status) &&
			Objects.equals(_type, dataControlTask._type)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getBatchId() {
		return _batchId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCompleteDate() {
		if (_completeDate == null) {
			return null;
		}

		return new Date(_completeDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getEmailAddress() {
		return _emailAddress;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getOwnerId() {
		return _ownerId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getStartDate() {
		if (_startDate == null) {
			return null;
		}

		return new Date(_startDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getStatus() {
		return _status;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Type getType() {
		return _type;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getUserId() {
		return _userId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getUserName() {
		return _userName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_batchId, _completeDate, _createDate, _emailAddress, _id, _type,
			_status, _userId, _userName);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setBatchId(Long batchId) {
		_batchId = batchId;
	}

	public void setCompleteDate(Date completeDate) {
		if (completeDate != null) {
			_completeDate = new Date(completeDate.getTime());
		}
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setOwnerId(String ownerId) {
		_ownerId = ownerId;
	}

	public void setStartDate(Date startDate) {
		if (startDate != null) {
			_startDate = new Date(startDate.getTime());
		}
	}

	public void setStatus(String status) {
		_status = status;
	}

	public void setType(Type type) {
		_type = type;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public enum Type {

		ACCESS(AuditEvent.Type.USER_ACCESS),
		DELETE(AuditEvent.Type.USER_DELETE),
		SUPPRESS(AuditEvent.Type.USER_SUPPRESS),
		UNSUPPRESS(AuditEvent.Type.USER_UNSUPPRESS);

		public AuditEvent.Type getAuditEventType() {
			return _auditEventType;
		}

		private Type(AuditEvent.Type auditEventType) {
			_auditEventType = auditEventType;
		}

		private final AuditEvent.Type _auditEventType;

	}

	@Transient
	private Long _batchId;

	@Transient
	private Date _completeDate;

	@Transient
	private Date _createDate;

	@Transient
	private String _emailAddress;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _ownerId;

	@Transient
	private Date _startDate;

	@Transient
	private String _status;

	@Transient
	private Type _type;

	@Transient
	private String _userId;

	@Transient
	private String _userName;

}