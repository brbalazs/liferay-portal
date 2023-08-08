/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
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
public class CustomAssetDashboard implements Persistable<String> {

	public CustomAssetDashboard() {
	}

	public CustomAssetDashboard(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public CustomAssetDashboard(String id) {
		_id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CustomAssetDashboard)) {
			return false;
		}

		CustomAssetDashboard customAssetDashboard = (CustomAssetDashboard)obj;

		if (Objects.equals(_assetId, customAssetDashboard._assetId) &&
			Objects.equals(_assetTitle, customAssetDashboard._assetTitle) &&
			Objects.equals(_category, customAssetDashboard._category) &&
			Objects.equals(_channelId, customAssetDashboard._channelId) &&
			Objects.equals(_createDate, customAssetDashboard._createDate) &&
			Objects.equals(_dataSourceId, customAssetDashboard._dataSourceId) &&
			Objects.equals(_definition, customAssetDashboard._definition) &&
			Objects.equals(_id, customAssetDashboard._id) &&
			Objects.equals(
				_modifiedByUserId, customAssetDashboard._modifiedByUserId) &&
			Objects.equals(
				_modifiedByUserName,
				customAssetDashboard._modifiedByUserName) &&
			Objects.equals(_modifiedDate, customAssetDashboard._modifiedDate)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getAssetId() {
		return _assetId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getAssetTitle() {
		return _assetTitle;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getCategory() {
		return _category;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getChannelId() {
		return _channelId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDefinition() {
		return _definition;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public String getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getModifiedByUserId() {
		return _modifiedByUserId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getModifiedByUserName() {
		return _modifiedByUserName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_assetId, _assetTitle, _category, _channelId, _createDate,
			_dataSourceId, _definition, _id, _modifiedByUserId,
			_modifiedByUserName, _modifiedDate);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setAssetId(String assetId) {
		_assetId = assetId;
	}

	public void setAssetTitle(String assetTitle) {
		_assetTitle = assetTitle;
	}

	public void setCategory(String category) {
		_category = category;
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDefinition(String definition) {
		_definition = definition;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setModifiedByUserId(String modifiedByUserId) {
		_modifiedByUserId = modifiedByUserId;
	}

	public void setModifiedByUserName(String modifiedByUserName) {
		_modifiedByUserName = modifiedByUserName;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	@Transient
	private String _assetId;

	@Transient
	private String _assetTitle;

	@Transient
	private String _category;

	@Transient
	private Long _channelId;

	@Transient
	private Date _createDate;

	@Transient
	private Long _dataSourceId;

	@Transient
	private String _definition;

	@Transient
	private String _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _modifiedByUserId;

	@Transient
	private String _modifiedByUserName;

	@Transient
	private Date _modifiedDate;

}