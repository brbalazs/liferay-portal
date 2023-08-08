/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author André Miranda
 */
@Table
public class BlockedKeyword implements Persistable<Long> {

	public BlockedKeyword() {
	}

	public BlockedKeyword(Date createDate, String keyword) {
		_createDate = new Date(createDate.getTime());
		_keyword = keyword;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BlockedKeyword)) {
			return false;
		}

		BlockedKeyword dataSource = (BlockedKeyword)obj;

		if (Objects.equals(_createDate, dataSource._createDate) &&
			Objects.equals(_id, dataSource._id) &&
			Objects.equals(_keyword, dataSource._keyword)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getKeyword() {
		return _keyword;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_createDate, _id, _keyword);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setKeyword(String keyword) {
		_keyword = keyword;
	}

	@Transient
	private Date _createDate = new Date();

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _keyword;

}