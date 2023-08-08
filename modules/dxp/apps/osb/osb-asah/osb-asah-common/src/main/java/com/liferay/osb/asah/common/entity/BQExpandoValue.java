/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author Marcos Martins
 */
public class BQExpandoValue {

	public BQExpandoValue() {
	}

	public BQExpandoValue(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public String getClassPK() {
		return _classPK;
	}

	public String getClassType() {
		return _classType;
	}

	public String getColumnId() {
		return _columnId;
	}

	public Long getDataSourceId() {
		return _dataSourceId;
	}

	public String getFieldName() {
		return _fieldName;
	}

	public String getId() {
		return _id;
	}

	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	public String getValue() {
		return _value;
	}

	public void setClassPK(String classPK) {
		_classPK = classPK;
	}

	public void setClassType(String classType) {
		_classType = classType;
	}

	public void setColumnId(String columnId) {
		_columnId = columnId;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setFieldName(String fieldName) {
		_fieldName = fieldName;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setValue(String value) {
		_value = value;
	}

	private String _classPK;
	private String _classType;
	private String _columnId;
	private Long _dataSourceId;
	private String _fieldName;
	private String _id;
	private Date _modifiedDate;
	private String _value;

}