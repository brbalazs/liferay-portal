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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class AuditEvent implements Persistable<Long> {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AuditEvent)) {
			return false;
		}

		AuditEvent auditEvent = (AuditEvent)obj;

		if (Objects.equals(_createDate, auditEvent._createDate) &&
			Objects.equals(_id, auditEvent._id) &&
			Objects.equals(_type, auditEvent._type) &&
			Objects.equals(_userId, auditEvent._userId) &&
			Objects.equals(_userName, auditEvent._userName)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getContext() {
		return _context;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public Long getId() {
		return _id;
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
		return Objects.hash(_createDate, _id, _type, _userId, _userName);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setContext(String context) {
		_context = context;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
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

		CLEAR_CHANNEL, DATA_CONTROL_ACCESS, DATA_CONTROL_DELETE,
		DATA_CONTROL_SUPPRESS, DELETE_CHANNEL, DELETE_DATA_SOURCE

	}

	@Transient
	private String _context;

	@Transient
	private Date _createDate = new Date();

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private Type _type;

	@Transient
	private String _userId;

	@Transient
	private String _userName;

}