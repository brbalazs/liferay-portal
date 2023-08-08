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
 * @author André Miranda
 */
@Table
public class Project implements Persistable<String> {

	public Project() {
	}

	public Project(String id) {
		_id = id;
	}

	public Project(String id, String version) {
		_id = id;
		_version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof Project)) {
			return false;
		}

		Project project = (Project)obj;

		if (Objects.equals(_id, project._id)) {
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
	public String getVersion() {
		return _version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_id, _version);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return true;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setVersion(String version) {
		_version = version;
	}

	@Transient
	private String _id;

	@Transient
	private String _version;

}