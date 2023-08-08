/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Inácio Nery
 */
@Table
public class DataSourceUserGroup {

	public DataSourceUserGroup() {
	}

	public DataSourceUserGroup(
		Boolean enableAllChildren, Long userGroupId, Set<Long> userGroupIds) {

		_enableAllChildren = enableAllChildren;
		_userGroupId = userGroupId;
		_userGroupIds = userGroupIds;
	}

	public DataSourceUserGroup(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DataSourceUserGroup)) {
			return false;
		}

		DataSourceUserGroup dataSourceUserGroup = (DataSourceUserGroup)obj;

		if (Objects.equals(
				_enableAllChildren, dataSourceUserGroup._enableAllChildren) &&
			Objects.equals(_userGroupId, dataSourceUserGroup._userGroupId) &&
			Objects.equals(_userGroupIds, dataSourceUserGroup._userGroupIds)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getEnableAllChildren() {
		return _enableAllChildren;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getUserGroupId() {
		return _userGroupId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<Long> getUserGroupIds() {
		return _userGroupIds;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_enableAllChildren, _userGroupId, _userGroupIds);
	}

	public void setEnableAllChildren(Boolean enableAllChildren) {
		_enableAllChildren = enableAllChildren;
	}

	public void setUserGroupId(Long userGroupId) {
		_userGroupId = userGroupId;
	}

	public void setUserGroupIds(Set<Long> userGroupIds) {
		_userGroupIds = userGroupIds;
	}

	@Transient
	private Boolean _enableAllChildren;

	@Transient
	private Long _userGroupId;

	@Transient
	private Set<Long> _userGroupIds;

}