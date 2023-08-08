/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Matthew Kong
 */
@Table
public class Preference implements Persistable<String> {

	public Preference() {
	}

	public Preference(String id, String value) {
		_id = id;
		_value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof Preference)) {
			return false;
		}

		Preference preference = (Preference)obj;

		if (Objects.equals(_id, preference._id) &&
			Objects.equals(_value, preference._value)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public String getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getValue() {
		return _value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_id, _value);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setValue(String value) {
		_value = value;
	}

	@Transient
	private String _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _value;

}