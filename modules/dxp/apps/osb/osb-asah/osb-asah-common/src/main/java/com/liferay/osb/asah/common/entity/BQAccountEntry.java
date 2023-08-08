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
public class BQAccountEntry {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQAccountEntry)) {
			return false;
		}

		BQAccountEntry bqAccountEntry = (BQAccountEntry)obj;

		if (Objects.equals(_accountEntryId, bqAccountEntry._accountEntryId) &&
			Objects.equals(_createDate, bqAccountEntry._createDate) &&
			Objects.equals(
				_defaultCPaymentMethodKey,
				bqAccountEntry._defaultCPaymentMethodKey) &&
			Objects.equals(_description, bqAccountEntry._description) &&
			Objects.equals(_domains, bqAccountEntry._domains) &&
			Objects.equals(_emailAddress, bqAccountEntry._emailAddress) &&
			Objects.equals(_id, bqAccountEntry._id) &&
			Objects.equals(_logoId, bqAccountEntry._logoId) &&
			Objects.equals(_modifiedDate, bqAccountEntry._modifiedDate) &&
			Objects.equals(_name, bqAccountEntry._name) &&
			Objects.equals(
				_parentAccountEntryId, bqAccountEntry._parentAccountEntryId) &&
			Objects.equals(_status, bqAccountEntry._status) &&
			Objects.equals(
				_taxExemptionCode, bqAccountEntry._taxExemptionCode) &&
			Objects.equals(_taxIdNumber, bqAccountEntry._taxIdNumber) &&
			Objects.equals(_type, bqAccountEntry._type)) {

			return true;
		}

		return false;
	}

	public Long getAccountEntryId() {
		return _accountEntryId;
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

	public String getDefaultCPaymentMethodKey() {
		return _defaultCPaymentMethodKey;
	}

	public String getDescription() {
		return _description;
	}

	public String getDomains() {
		return _domains;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public String getId() {
		return _id;
	}

	public Long getLogoId() {
		return _logoId;
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

	public Long getParentAccountEntryId() {
		return _parentAccountEntryId;
	}

	public Integer getStatus() {
		return _status;
	}

	public String getTaxExemptionCode() {
		return _taxExemptionCode;
	}

	public String getTaxIdNumber() {
		return _taxIdNumber;
	}

	public String getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_accountEntryId, _createDate, _defaultCPaymentMethodKey,
			_description, _domains, _emailAddress, _id, _logoId, _modifiedDate,
			_name, _parentAccountEntryId, _status, _taxExemptionCode,
			_taxIdNumber, _type);
	}

	public void setAccountEntryId(Long accountEntryId) {
		_accountEntryId = accountEntryId;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDefaultCPaymentMethodKey(String defaultCPaymentMethodKey) {
		_defaultCPaymentMethodKey = defaultCPaymentMethodKey;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDomains(String domains) {
		_domains = domains;
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setLogoId(Long logoId) {
		_logoId = logoId;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setParentAccountEntryId(Long parentAccountEntryId) {
		_parentAccountEntryId = parentAccountEntryId;
	}

	public void setStatus(Integer status) {
		_status = status;
	}

	public void setTaxExemptionCode(String taxExemptionCode) {
		_taxExemptionCode = taxExemptionCode;
	}

	public void setTaxIdNumber(String taxIdNumber) {
		_taxIdNumber = taxIdNumber;
	}

	public void setType(String type) {
		_type = type;
	}

	private Long _accountEntryId;
	private Date _createDate;
	private Long _dataSourceId;
	private String _defaultCPaymentMethodKey;
	private String _description;
	private String _domains;
	private String _emailAddress;
	private String _id;
	private Long _logoId;
	private Date _modifiedDate;
	private String _name;
	private Long _parentAccountEntryId;
	private Integer _status;
	private String _taxExemptionCode;
	private String _taxIdNumber;
	private String _type;

}