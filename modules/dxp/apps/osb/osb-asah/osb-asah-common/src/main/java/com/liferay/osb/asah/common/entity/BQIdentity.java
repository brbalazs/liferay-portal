/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcos Martins
 */
public class BQIdentity {

	public BQIdentity() {
	}

	public BQIdentity(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQIdentity)) {
			return false;
		}

		BQIdentity bqIdentity = (BQIdentity)obj;

		if (Objects.equals(_id, bqIdentity._id)) {
			return true;
		}

		return false;
	}

	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public String getId() {
		return _id;
	}

	@JsonAlias("emailAddressHashed")
	public String getIndividualId() {
		return _individualId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_id);
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIndividualId(String individualId) {
		_individualId = individualId;
	}

	private Date _createDate;
	private String _id;
	private String _individualId;

}