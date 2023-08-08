/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

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
 * @author Leslie Wong
 */
@Table
public class EventDefinition implements Persistable<Long> {

	public EventDefinition() {
	}

	public EventDefinition(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof EventDefinition)) {
			return false;
		}

		EventDefinition eventDefinition = (EventDefinition)obj;

		if (Objects.equals(_applicationId, eventDefinition._applicationId) &&
			Objects.equals(_blocked, eventDefinition._blocked) &&
			Objects.equals(
				_blockedLastSeenDate, eventDefinition._blockedLastSeenDate) &&
			Objects.equals(
				_blockedLastSeenURL, eventDefinition._blockedLastSeenURL) &&
			Objects.equals(
				_blockedReasonType, eventDefinition._blockedReasonType) &&
			Objects.equals(_description, eventDefinition._description) &&
			Objects.equals(_displayName, eventDefinition._displayName) &&
			Objects.equals(_hidden, eventDefinition._hidden) &&
			Objects.equals(_id, eventDefinition._id) &&
			Objects.equals(_name, eventDefinition._name) &&
			Objects.equals(_type, eventDefinition._type)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getApplicationId() {
		return _applicationId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getBlockedLastSeenDate() {
		if (_blockedLastSeenDate == null) {
			return null;
		}

		return new Date(_blockedLastSeenDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getBlockedLastSeenURL() {
		return _blockedLastSeenURL;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public BlockedReasonType getBlockedReasonType() {
		return _blockedReasonType;
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
			_applicationId, _blocked, _blockedLastSeenDate, _blockedLastSeenURL,
			_blockedReasonType, _description, _displayName, _type, _hidden, _id,
			_name);
	}

	@AccessType(AccessType.Type.PROPERTY)
	public boolean isBlocked() {
		return _blocked;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public boolean isHidden() {
		return _hidden;
	}

	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}

	public void setBlocked(boolean blocked) {
		_blocked = blocked;
	}

	public void setBlockedLastSeenDate(Date blockedLastSeenDate) {
		if (blockedLastSeenDate != null) {
			_blockedLastSeenDate = new Date(blockedLastSeenDate.getTime());
		}
		else {
			_blockedLastSeenDate = null;
		}
	}

	public void setBlockedLastSeenURL(String blockedLastSeenURL) {
		_blockedLastSeenURL = blockedLastSeenURL;
	}

	public void setBlockedReasonType(BlockedReasonType blockedReasonType) {
		_blockedReasonType = blockedReasonType;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDisplayName(String displayName) {
		_displayName = displayName;
	}

	public void setHidden(boolean hidden) {
		_hidden = hidden;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setType(Type type) {
		_type = type;
	}

	public enum BlockedReasonType {

		BLOCKED_BY_USER, THRESHOLD_OVERFLOW

	}

	public enum Type {

		ALL, CUSTOM, DEFAULT

	}

	@Transient
	private String _applicationId;

	@Transient
	private boolean _blocked;

	@Transient
	private Date _blockedLastSeenDate;

	@Transient
	private String _blockedLastSeenURL;

	@Transient
	private BlockedReasonType _blockedReasonType;

	@Transient
	private String _description;

	@Transient
	private String _displayName;

	@Transient
	private boolean _hidden;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _name;

	@Transient
	private Type _type;

}