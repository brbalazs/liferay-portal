/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.entity.BQExpandoColumn;
import com.liferay.osb.asah.common.entity.BQExpandoValue;

import java.util.Date;

/**
 * @author Marcos Martins
 */
public class ExpandoField {

	public ExpandoField(
		BQExpandoColumn bqExpandoColumn, BQExpandoValue bqExpandoValue) {

		_classPK = bqExpandoValue.getClassPK();
		_classType = bqExpandoValue.getClassType();
		_columnId = bqExpandoColumn.getColumnId();
		_dataSourceId = bqExpandoColumn.getDataSourceId();
		_dataSourceName = bqExpandoColumn.getDataSourceName();
		_dataType = bqExpandoColumn.getDataType();
		_id = bqExpandoColumn.getId();
		_modifiedDate = bqExpandoColumn.getModifiedDate();
		_name = bqExpandoColumn.getName();
		_value = bqExpandoValue.getValue();
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

	public String getDataSourceName() {
		return _dataSourceName;
	}

	public String getDataType() {
		return _dataType;
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

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}

	private final String _classPK;
	private final String _classType;
	private final String _columnId;
	private final Long _dataSourceId;
	private final String _dataSourceName;
	private final String _dataType;
	private final String _id;
	private final Date _modifiedDate;
	private final String _name;
	private final String _value;

}