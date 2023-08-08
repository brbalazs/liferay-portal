/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class BQCSVUser {

	public BQCSVUser() {
	}

	public BQCSVUser(Long dataSourceId) {
		this(dataSourceId, null);
	}

	public BQCSVUser(Long dataSourceId, List<Field> fields) {
		_dataSourceId = dataSourceId;
		_fields = fields;
	}

	public BQCSVUser(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQCSVUser)) {
			return false;
		}

		BQCSVUser bqCSVUser = (BQCSVUser)obj;

		if (Objects.equals(_dataSourceId, bqCSVUser._dataSourceId) &&
			Objects.equals(_dataSourceUserPK, bqCSVUser._dataSourceUserPK) &&
			Objects.equals(_emailAddress, bqCSVUser._emailAddress) &&
			Objects.equals(_fields, bqCSVUser._fields) &&
			Objects.equals(_id, bqCSVUser._id)) {

			return true;
		}

		return false;
	}

	@BigQueryColumn
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@BigQueryColumn
	@JsonAlias("dataSourceUserPK")
	@JsonProperty("dataSourceIndividualPK")
	public String getDataSourceUserPK() {
		return _dataSourceUserPK;
	}

	@BigQueryColumn
	public String getEmailAddress() {
		return _emailAddress;
	}

	@BigQueryColumn
	@JsonProperty("fields")
	public List<Field> getFields() {
		return _fields;
	}

	@BigQueryColumn
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getId() {
		return _id;
	}

	@BigQueryColumn
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_dataSourceId, _dataSourceUserPK, _emailAddress, _fields, _id);
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceUserPK(String dataSourceUserPK) {
		_dataSourceUserPK = dataSourceUserPK;
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	public void setFields(List<Field> fields) {
		_fields = fields;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public static class Field {

		public Field() {
		}

		public Field(String name, String value) {
			_name = name;
			_value = value;
		}

		@BigQueryColumn
		public String getName() {
			return _name;
		}

		@BigQueryColumn
		public String getValue() {
			return _value;
		}

		public void setName(String name) {
			_name = name;
		}

		public void setValue(String value) {
			_value = value;
		}

		private String _name;
		private String _value;

	}

	private Long _dataSourceId;
	private String _dataSourceUserPK;
	private String _emailAddress;
	private List<Field> _fields;
	private Long _id;
	private Date _modifiedDate;

}