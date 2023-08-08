/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.model.BQDXPEntity;
import com.liferay.osb.asah.common.model.ExpandoField;
import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Marcos Martins
 */
public class BQUser implements BQDXPEntity {

	public BQUser() {
	}

	public BQUser(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
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
		return DXPEntity.Type.USER.name();
	}

	@BigQueryColumn("dxpUserId")
	@JsonProperty("userId")
	public Long getDXPUserId() {
		return _dxpUserId;
	}

	@BigQueryColumn
	public String getEmailAddress() {
		return _emailAddress;
	}

	public List<ExpandoField> getExpandoFields() {
		return _expandoFields;
	}

	@BigQueryColumn
	@JsonProperty("fields")
	public List<Field> getFields() {
		return _fields;
	}

	@BigQueryColumn
	public String getFirstName() {
		return _firstName;
	}

	@BigQueryColumn
	@Override
	public String getId() {
		return _id;
	}

	public String getIdFieldName() {
		return "userId";
	}

	public Long getIdFieldValue() {
		return _dxpUserId;
	}

	@BigQueryColumn
	public String getIndividualId() {
		return _individualId;
	}

	@BigQueryColumn
	public String getJobTitle() {
		return _jobTitle;
	}

	@BigQueryColumn
	public String getLastName() {
		return _lastName;
	}

	@BigQueryColumn
	public String getMiddleName() {
		return _middleName;
	}

	@BigQueryColumn
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@Override
	public String getName() {
		return _firstName + " " + _lastName;
	}

	@BigQueryColumn
	public String getScreenName() {
		return _screenName;
	}

	@BigQueryColumn
	public String getUserName() {
		return _userName;
	}

	@BigQueryColumn
	public String getUuid() {
		return _uuid;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceName(String dataSourceName) {
		_dataSourceName = dataSourceName;
	}

	public void setDXPUserId(Long dxpUserId) {
		_dxpUserId = dxpUserId;
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	public void setExpandoFields(List<ExpandoField> expandoFields) {
		_expandoFields = expandoFields;
	}

	public void setFields(List<Field> fields) {
		_fields = fields;
	}

	public void setFirstName(String firstName) {
		_firstName = firstName;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIndividualId(String individualId) {
		_individualId = individualId;
	}

	public void setJobTitle(String jobTitle) {
		_jobTitle = jobTitle;
	}

	public void setLastName(String lastName) {
		_lastName = lastName;
	}

	public void setMiddleName(String middleName) {
		_middleName = middleName;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setScreenName(String screenName) {
		_screenName = screenName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
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
	private String _dataSourceName;
	private Long _dxpUserId;
	private String _emailAddress;
	private List<ExpandoField> _expandoFields;
	private List<Field> _fields;
	private String _firstName;
	private String _id;
	private String _individualId;
	private String _jobTitle;
	private String _lastName;
	private String _middleName;
	private Date _modifiedDate;
	private String _screenName;
	private String _userName;
	private String _uuid;

}