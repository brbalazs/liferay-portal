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
public class BQGroup implements BQDXPEntity {

	public BQGroup() {
	}

	public BQGroup(Map<String, Object> source) {
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
		return DXPEntity.Type.GROUP.name();
	}

	@JsonProperty("fields")
	public JSONObject getFieldsJSONObject() {
		return JSONUtil.put(
			"groupId", _groupId
		).put(
			"name", _name
		);
	}

	public Long getGroupId() {
		return _groupId;
	}

	@Override
	public String getId() {
		return _id;
	}

	public String getIdFieldName() {
		return "groupId";
	}

	public Long getIdFieldValue() {
		return _groupId;
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

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceName(String dataSourceName) {
		_dataSourceName = dataSourceName;
	}

	public void setGroupId(Long groupId) {
		_groupId = groupId;
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

	private Long _dataSourceId;
	private String _dataSourceName;
	private Long _groupId;
	private String _id;
	private Date _modifiedDate;
	private String _name;

}