/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Inácio Nery
 */
@Table
public class ChannelDataSource {

	public ChannelDataSource() {
	}

	public ChannelDataSource(
		Set<Long> commerceChannelIds, Long dataSourceId, Set<Long> groupIds) {

		_commerceChannelIds = commerceChannelIds;
		_dataSourceId = dataSourceId;
		_groupIds = groupIds;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ChannelDataSource)) {
			return false;
		}

		ChannelDataSource channelDataSource = (ChannelDataSource)obj;

		if (Objects.equals(_dataSourceId, channelDataSource._dataSourceId) &&
			Objects.equals(_groupIds, channelDataSource._groupIds) &&
			Objects.equals(
				_commerceChannelIds, channelDataSource._commerceChannelIds)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(contentUsing = ToStringSerializer.class)
	public Set<Long> getCommerceChannelIds() {
		return _commerceChannelIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("id")
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(contentUsing = ToStringSerializer.class)
	public Set<Long> getGroupIds() {
		return _groupIds;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_dataSourceId, _groupIds);
	}

	public void setCommerceChannelIds(Set<Long> commerceChannelIds) {
		_commerceChannelIds = commerceChannelIds;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setGroupIds(Set<Long> groupIds) {
		_groupIds = groupIds;
	}

	@Transient
	private Set<Long> _commerceChannelIds;

	@Transient
	private Long _dataSourceId;

	@Transient
	private Set<Long> _groupIds;

}