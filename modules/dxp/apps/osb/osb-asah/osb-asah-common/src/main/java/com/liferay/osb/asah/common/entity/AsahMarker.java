/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.json.JSONUtil;

import java.util.Objects;

import org.json.JSONObject;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class AsahMarker implements Persistable<String> {

	public AsahMarker() {
	}

	public AsahMarker(String id) {
		this(id, new JSONObject());
	}

	public AsahMarker(String id, JSONObject contextJSONObject) {
		_id = id;
		_contextJSONObject = contextJSONObject;

		_isNew = Boolean.TRUE;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AsahMarker)) {
			return false;
		}

		AsahMarker asahMarker = (AsahMarker)obj;

		if (Objects.equals(
				JSONUtil.toMap(_contextJSONObject),
				JSONUtil.toMap(asahMarker._contextJSONObject)) &&
			Objects.equals(_id, asahMarker._id)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("context")
	@JsonProperty("context")
	public JSONObject getContextJSONObject() {
		return _contextJSONObject;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public String getId() {
		return _id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_contextJSONObject, _id);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setContextJSONObject(JSONObject contextJSONObject) {
		_contextJSONObject = contextJSONObject;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	@Transient
	private JSONObject _contextJSONObject = new JSONObject();

	@Transient
	private String _id;

	@Transient
	private Boolean _isNew;

}