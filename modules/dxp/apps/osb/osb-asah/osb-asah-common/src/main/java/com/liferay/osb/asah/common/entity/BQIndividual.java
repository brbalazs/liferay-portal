/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Rachael Koestartyo
 */
public class BQIndividual {

	public BQIndividual() {
	}

	public BQIndividual(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQIndividual)) {
			return false;
		}

		BQIndividual bqIndividual = (BQIndividual)obj;

		if (Objects.equals(_emailAddress, bqIndividual._emailAddress)) {
			return true;
		}

		return false;
	}

	@BigQueryColumn
	public Date getBirthday() {
		if (_birthday == null) {
			return null;
		}

		return new Date(_birthday.getTime());
	}

	@BigQueryColumn
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@BigQueryColumn
	public String getEmailAddress() {
		return _emailAddress;
	}

	@BigQueryColumn
	public List<Field> getFields() {
		return _fields;
	}

	@BigQueryColumn
	public String getFirstName() {
		return _firstName;
	}

	@BigQueryColumn
	public String getId() {
		return _id;
	}

	@BigQueryColumn
	public String getJobTitle() {
		return _jobTitle;
	}

	@BigQueryColumn
	public String getLanguageId() {
		return _languageId;
	}

	@BigQueryColumn
	public String getLastName() {
		return _lastName;
	}

	@BigQueryColumn
	public List<Membership> getMemberships() {
		return _memberships;
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

	@BigQueryColumn
	public String getScreenName() {
		return _screenName;
	}

	@BigQueryColumn
	public Boolean getSuppressed() {
		return _suppressed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_emailAddress);
	}

	public void setBirthday(Date birthday) {
		if (birthday != null) {
			_birthday = new Date(birthday.getTime());
		}
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
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

	public void setJobTitle(String jobTitle) {
		_jobTitle = jobTitle;
	}

	public void setLanguageId(String languageId) {
		_languageId = languageId;
	}

	public void setLastName(String lastName) {
		_lastName = lastName;
	}

	public void setMemberships(List<Membership> memberships) {
		_memberships = memberships;
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

	public void setSuppressed(boolean suppressed) {
		_suppressed = suppressed;
	}

	public static class Field {

		public Field() {
		}

		public Field(Long dataSourceId, String name, String value) {
			_dataSourceId = dataSourceId;
			_name = name;
			_value = value;
		}

		@BigQueryColumn
		public Long getDataSourceId() {
			return _dataSourceId;
		}

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
		public String getValue() {
			return _value;
		}

		public void setDataSourceId(Long dataSourceId) {
			_dataSourceId = dataSourceId;
		}

		public void setModifiedDate(Date modifiedDate) {
			if (modifiedDate != null) {
				_modifiedDate = new Date(modifiedDate.getTime());
			}
		}

		public void setName(String name) {
			_name = name;
		}

		public void setValue(String value) {
			_value = value;
		}

		private Long _dataSourceId;
		private Date _modifiedDate;
		private String _name;
		private String _value;

	}

	public static class Membership {

		public Membership() {
		}

		public Membership(List<String> ids, String name) {
			_ids = ids;
			_name = name;
		}

		@BigQueryColumn
		public List<String> getIds() {
			return _ids;
		}

		@BigQueryColumn
		public String getName() {
			return _name;
		}

		public void setIds(List<String> ids) {
			_ids = ids;
		}

		public void setName(String name) {
			_name = name;
		}

		private List<String> _ids;
		private String _name;

	}

	private Date _birthday;
	private Date _createDate;
	private String _emailAddress;
	private List<Field> _fields;
	private String _firstName;
	private String _id;
	private String _jobTitle;
	private String _languageId;
	private String _lastName;
	private List<Membership> _memberships;
	private String _middleName;
	private Date _modifiedDate;
	private String _screenName;
	private Boolean _suppressed;

}