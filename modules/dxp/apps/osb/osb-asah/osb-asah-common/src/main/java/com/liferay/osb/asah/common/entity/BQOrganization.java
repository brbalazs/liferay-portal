/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.BQDXPEntity;
import com.liferay.osb.asah.common.model.ExpandoField;
import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author Marcos Martins
 */
public class BQOrganization implements BQDXPEntity {

	public BQOrganization() {
	}

	public BQOrganization(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@BigQueryColumn
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@BigQueryColumn
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	public String getDataSourceName() {
		return _dataSourceName;
	}

	public String getDXPEntityType() {
		return DXPEntity.Type.ORGANIZATION.name();
	}

	public List<ExpandoField> getExpandoFields() {
		return _expandoFields;
	}

	@JsonProperty("fields")
	public JSONObject getFieldsJSONObject() {
		return JSONUtil.put(
			"name", _name
		).put(
			"organizationId", _organizationId
		).put(
			"parentOrganizationId", _parentOrganizationId
		).put(
			"parentOrganizationName", _parentOrganizationName
		).put(
			"treePath", _treePath
		).put(
			"type", _type
		);
	}

	@BigQueryColumn
	@Override
	public String getId() {
		return _id;
	}

	public String getIdFieldName() {
		return "organizationId";
	}

	public Long getIdFieldValue() {
		return _organizationId;
	}

	@BigQueryColumn
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@BigQueryColumn
	public String getName() {
		return _name;
	}

	@BigQueryColumn
	public Long getOrganizationId() {
		return _organizationId;
	}

	@BigQueryColumn
	public Long getParentOrganizationId() {
		return _parentOrganizationId;
	}

	public String getParentOrganizationName() {
		return _parentOrganizationName;
	}

	@BigQueryColumn
	public String getTreePath() {
		return _treePath;
	}

	@BigQueryColumn
	public String getType() {
		return _type;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceName(String dataSourceName) {
		_dataSourceName = dataSourceName;
	}

	public void setExpandoFields(List<ExpandoField> expandoFields) {
		if (expandoFields != null) {
			_expandoFields = new ArrayList<>(expandoFields);
		}
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

	public void setOrganizationId(Long organizationId) {
		_organizationId = organizationId;
	}

	public void setParentOrganizationId(Long parentOrganizationId) {
		_parentOrganizationId = parentOrganizationId;
	}

	public void setParentOrganizationName(String parentOrganizationName) {
		_parentOrganizationName = parentOrganizationName;
	}

	public void setTreePath(String treePath) {
		_treePath = treePath;
	}

	public void setType(String type) {
		_type = type;
	}

	private Date _createDate;
	private Long _dataSourceId;
	private String _dataSourceName;
	private List<ExpandoField> _expandoFields;
	private String _id;
	private Date _modifiedDate;
	private String _name;
	private Long _organizationId;
	private Long _parentOrganizationId;
	private String _parentOrganizationName;
	private String _treePath;
	private String _type;

}