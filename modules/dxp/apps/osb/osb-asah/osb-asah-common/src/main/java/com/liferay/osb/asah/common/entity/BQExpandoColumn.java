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
public class BQExpandoColumn implements BQDXPEntity {

	public BQExpandoColumn() {
	}

	public BQExpandoColumn(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public String getClassName() {
		return _className;
	}

	public String getColumnId() {
		return _columnId;
	}

	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	public String getDataSourceName() {
		return _dataSourceName;
	}

	public String getDataType() {
		return _dataType;
	}

	public String getDisplayType() {
		return _displayType;
	}

	public String getDXPEntityType() {
		return DXPEntity.Type.EXPANDO_COLUMN.name();
	}

	@JsonProperty("fields")
	public JSONObject getFieldsJSONObject() {
		return JSONUtil.put(
			"columnId", _columnId
		).put(
			"dataType", _dataType
		).put(
			"name", _name
		);
	}

	@Override
	public String getId() {
		return _id;
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

	public void setClassName(String className) {
		_className = className;
	}

	public void setColumnId(String columnId) {
		_columnId = columnId;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceName(String dataSourceName) {
		_dataSourceName = dataSourceName;
	}

	public void setDataType(String dataType) {
		_dataType = dataType;
	}

	public void setDisplayType(String displayType) {
		_displayType = displayType;
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

	private String _className;
	private String _columnId;
	private Long _dataSourceId;
	private String _dataSourceName;
	private String _dataType;
	private String _displayType;
	private String _id;
	private Date _modifiedDate;
	private String _name;

}