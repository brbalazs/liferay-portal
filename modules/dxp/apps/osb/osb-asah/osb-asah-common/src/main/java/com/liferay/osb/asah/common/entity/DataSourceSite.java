/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Inácio Nery
 */
@Table
public class DataSourceSite {

	public DataSourceSite() {
	}

	public DataSourceSite(Boolean enableAllChildren, Long siteId) {
		_enableAllChildren = enableAllChildren;
		_siteId = siteId;
	}

	public DataSourceSite(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DataSourceSite)) {
			return false;
		}

		DataSourceSite dataSourceSite = (DataSourceSite)obj;

		if (Objects.equals(
				_enableAllChildren, dataSourceSite._enableAllChildren) &&
			Objects.equals(_siteId, dataSourceSite._siteId)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getEnableAllChildren() {
		return _enableAllChildren;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("id")
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getSiteId() {
		return _siteId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_enableAllChildren, _siteId);
	}

	public void setEnableAllChildren(Boolean enableAllChildren) {
		_enableAllChildren = enableAllChildren;
	}

	public void setSiteId(Long siteId) {
		_siteId = siteId;
	}

	@Transient
	private Boolean _enableAllChildren;

	@Transient
	private Long _siteId;

}