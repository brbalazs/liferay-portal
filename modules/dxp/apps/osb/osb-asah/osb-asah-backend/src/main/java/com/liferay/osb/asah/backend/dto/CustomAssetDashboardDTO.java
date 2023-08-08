/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.CustomAssetDashboard;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.Date;

/**
 * @author André Miranda
 */
public class CustomAssetDashboardDTO {

	public CustomAssetDashboardDTO() {
	}

	public CustomAssetDashboardDTO(CustomAssetDashboard customAssetDashboard) {
		_assetId = customAssetDashboard.getAssetId();
		_assetTitle = customAssetDashboard.getAssetTitle();
		_category = customAssetDashboard.getCategory();
		_createDate = customAssetDashboard.getCreateDate();
		_definition = customAssetDashboard.getDefinition();
		_id = String.valueOf(customAssetDashboard.getId());
		_modifiedByUserId = customAssetDashboard.getModifiedByUserId();
		_modifiedByUserName = customAssetDashboard.getModifiedByUserName();
		_modifiedDate = customAssetDashboard.getModifiedDate();
	}

	public String getAssetId() {
		return _assetId;
	}

	public String getAssetTitle() {
		return _assetTitle;
	}

	public String getCategory() {
		return _category;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty
	public LocalDateTime getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return DateUtil.toLocalDateTime(_createDate, ZoneOffset.UTC);
	}

	public String getDefinition() {
		return _definition;
	}

	public String getId() {
		return _id;
	}

	public String getModifiedByUserId() {
		return _modifiedByUserId;
	}

	public String getModifiedByUserName() {
		return _modifiedByUserName;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty
	public LocalDateTime getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return DateUtil.toLocalDateTime(_modifiedDate, ZoneOffset.UTC);
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

	public void setCreateDate(LocalDateTime createDate) {
		if (createDate != null) {
			_createDate = DateUtil.toUTCDate(createDate);
		}
	}

	public void setDefinition(String definition) {
		_definition = definition;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setModifiedByUserId(String modifiedByUserId) {
		_modifiedByUserId = modifiedByUserId;
	}

	public void setModifiedByUserName(String modifiedByUserName) {
		_modifiedByUserName = modifiedByUserName;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = DateUtil.toUTCDate(modifiedDate);
		}
	}

	private String _assetId;
	private String _assetTitle;
	private String _category;
	private Date _createDate;
	private String _definition;
	private String _id;
	private String _modifiedByUserId;
	private String _modifiedByUserName;
	private Date _modifiedDate;

}