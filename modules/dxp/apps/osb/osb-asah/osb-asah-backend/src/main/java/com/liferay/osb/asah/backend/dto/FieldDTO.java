/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.ExpandoField;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Rachael Koestartyo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("fields")
public class FieldDTO {

	public FieldDTO() {
	}

	public FieldDTO(ExpandoField expandoField) {
		_dataSourceId = StringUtil.get(expandoField.getDataSourceId(), null);
		_dataSourceName = expandoField.getDataSourceName();
		_fieldType = expandoField.getDataType();
		_id = StringUtil.get(expandoField.getId(), null);
		_modifiedDate = expandoField.getModifiedDate();
		_name = expandoField.getName();
		_ownerId = expandoField.getClassPK();
		_ownerType = expandoField.getClassType();
		_sourceName = expandoField.getColumnId();
		_value = expandoField.getValue();
	}

	public FieldDTO(Field field) {
		_context = field.getContext();
		_dataSourceId = StringUtil.get(field.getDataSourceId(), null);
		_dataSourceName = field.getDataSourceName();
		_fieldType = field.getFieldType();
		_id = StringUtil.get(field.getId(), null);
		_modifiedDate = field.getModifiedDate();
		_name = field.getName();
		_ownerId = StringUtil.get(field.getOwnerId(), null);
		_ownerType = field.getOwnerType();
		_sourceName = field.getSourceName();
		_value = field.getValue();
	}

	public FieldDTO(List<Field> fields) {
		_fieldDTOs = SetUtil.map(fields, FieldDTO::new);
	}

	@JsonProperty("context")
	public String getContext() {
		return _context;
	}

	@JsonProperty("dataSourceId")
	public String getDataSourceId() {
		return _dataSourceId;
	}

	@JsonProperty("dataSourceName")
	public String getDataSourceName() {
		return _dataSourceName;
	}

	@JsonProperty("fields")
	public Set<FieldDTO> getFieldDTOs() {
		return _fieldDTOs;
	}

	@JsonProperty("fieldType")
	public String getFieldType() {
		return _fieldType;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonAlias("modifiedDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateModified")
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@JsonProperty("name")
	public String getName() {
		return _name;
	}

	@JsonProperty("ownerId")
	public String getOwnerId() {
		return _ownerId;
	}

	@JsonProperty("ownerType")
	public String getOwnerType() {
		return _ownerType;
	}

	@JsonProperty("sourceName")
	public String getSourceName() {
		return _sourceName;
	}

	@JsonProperty("value")
	public Object getValue() {
		return _value;
	}

	private String _context;
	private String _dataSourceId;
	private String _dataSourceName;
	private Set<FieldDTO> _fieldDTOs;
	private String _fieldType;
	private String _id;
	private Date _modifiedDate;
	private String _name;
	private String _ownerId;
	private String _ownerType;
	private String _sourceName;
	private Object _value;

}