/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.BQDXPEntity;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author Marcos Martins
 */
public class BQUserGroup implements BQDXPEntity {

	public BQUserGroup() {
	}

	public BQUserGroup(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	public String getDataSourceName() {
		return _dataSourceName;
	}

	public String getDXPEntityType() {
		return DXPEntity.Type.USER_GROUP.name();
	}

	@JsonProperty("fields")
	public JSONObject getFieldsJSONObject() {
		return JSONUtil.put(
			"name", _name
		).put(
			"userGroupId", _userGroupId
		);
	}

	@Override
	public String getId() {
		return _id;
	}

	public String getIdFieldName() {
		return "userGroupId";
	}

	public Long getIdFieldValue() {
		return _userGroupId;
	}

	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	public String getName() {
		return _name;
	}

	public Long getUserGroupId() {
		return _userGroupId;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceName(String dataSourceName) {
		_dataSourceName = dataSourceName;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setUserGroupId(Long userGroupId) {
		_userGroupId = userGroupId;
	}

	private Long _dataSourceId;
	private String _dataSourceName;
	private String _id;
	private Date _modifiedDate;
	private String _name;
	private Long _userGroupId;

}