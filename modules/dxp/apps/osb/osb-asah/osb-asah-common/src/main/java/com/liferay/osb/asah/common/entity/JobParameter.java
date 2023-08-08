/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class JobParameter {

	public static JobParameter of(Map<String, String> jobParameter) {
		return new JobParameter(
			jobParameter.get("name"), jobParameter.get("value"));
	}

	public JobParameter() {
	}

	public JobParameter(String name, String value) {
		_name = name;
		_value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof JobParameter)) {
			return false;
		}

		JobParameter jobParameter = (JobParameter)obj;

		if (Objects.equals(_name, jobParameter._name) &&
			Objects.equals(_value, jobParameter._value)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getName() {
		return _name;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getValue() {
		return _value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_name, _value);
	}

	public void setName(String name) {
		_name = name;
	}

	public void setValue(String value) {
		_value = value;
	}

	@Transient
	private String _name;

	@Transient
	private String _value;

}