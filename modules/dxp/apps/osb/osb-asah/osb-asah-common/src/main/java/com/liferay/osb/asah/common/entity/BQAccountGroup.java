/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import java.util.Date;
import java.util.Objects;

/**
 * @author Marcos Martins
 */
public class BQAccountGroup {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQAccountGroup)) {
			return false;
		}

		BQAccountGroup bqAccountGroup = (BQAccountGroup)obj;

		if (Objects.equals(_accountGroupId, bqAccountGroup._accountGroupId) &&
			Objects.equals(_createDate, bqAccountGroup._createDate) &&
			Objects.equals(
				_defaultAccountGroup, bqAccountGroup._defaultAccountGroup) &&
			Objects.equals(_description, bqAccountGroup._description) &&
			Objects.equals(_id, bqAccountGroup._id) &&
			Objects.equals(_modifiedDate, bqAccountGroup._modifiedDate) &&
			Objects.equals(_name, bqAccountGroup._name) &&
			Objects.equals(_type, bqAccountGroup._type)) {

			return true;
		}

		return false;
	}

	public Long getAccountGroupId() {
		return _accountGroupId;
	}

	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public Long getDataSourceId() {
		return _dataSourceId;
	}

	public Boolean getDefaultAccountGroup() {
		return _defaultAccountGroup;
	}

	public String getDescription() {
		return _description;
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

	public String getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_accountGroupId, _createDate, _defaultAccountGroup, _description,
			_id, _modifiedDate, _name, _type);
	}

	public Boolean isDefaultAccountGroup() {
		return _defaultAccountGroup;
	}

	public void setAccountGroupId(Long accountGroupId) {
		_accountGroupId = accountGroupId;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDefaultAccountGroup(Boolean defaultAccountGroup) {
		_defaultAccountGroup = defaultAccountGroup;
	}

	public void setDescription(String description) {
		_description = description;
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

	public void setType(String type) {
		_type = type;
	}

	private Long _accountGroupId;
	private Date _createDate;
	private Long _dataSourceId;
	private Boolean _defaultAccountGroup;
	private String _description;
	private String _id;
	private Date _modifiedDate;
	private String _name;
	private String _type;

}