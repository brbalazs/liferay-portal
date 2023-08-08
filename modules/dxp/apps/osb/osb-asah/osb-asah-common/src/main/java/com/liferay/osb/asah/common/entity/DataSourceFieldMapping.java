/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Rachael Koestartyo
 */
@Table
public class DataSourceFieldMapping {

	public DataSourceFieldMapping() {
	}

	public DataSourceFieldMapping(
		Long dataSourceId, Long fieldMappingFieldName) {

		_dataSourceId = dataSourceId;
		_fieldMappingFieldName = fieldMappingFieldName;
	}

	public DataSourceFieldMapping(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DataSourceFieldMapping)) {
			return false;
		}

		DataSourceFieldMapping dataSourceFieldMapping =
			(DataSourceFieldMapping)obj;

		if (Objects.equals(
				_dataSourceId, dataSourceFieldMapping._dataSourceId) &&
			Objects.equals(
				_fieldMappingFieldName,
				dataSourceFieldMapping._fieldMappingFieldName)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getFieldMappingFieldName() {
		return _fieldMappingFieldName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_dataSourceId, _fieldMappingFieldName);
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setFieldMappingFieldName(Long fieldMappingFieldName) {
		_fieldMappingFieldName = fieldMappingFieldName;
	}

	@Transient
	private Long _dataSourceId;

	@Transient
	private Long _fieldMappingFieldName;

}