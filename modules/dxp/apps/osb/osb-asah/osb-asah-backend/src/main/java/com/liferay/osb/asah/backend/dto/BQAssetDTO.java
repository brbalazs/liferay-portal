/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQAsset;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Thiago Buarque
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("activities")
public class BQAssetDTO {

	public BQAssetDTO() {
	}

	public BQAssetDTO(BQAsset bqAsset) {
		_applicationId = bqAsset.getApplicationId();
		_assetId = bqAsset.getAssetId();
		_assetTitle = bqAsset.getAssetTitle();
		_channelId = bqAsset.getChannelId();
		_count = bqAsset.getCount();
		_dataSourceId = bqAsset.getDataSourceId();
		_id = bqAsset.getId();
		_modifiedDate = bqAsset.getModifiedDate();
	}

	public BQAssetDTO(Collection<BQAssetDTO> bqAssetDTOs) {
		_bqAssetDTOs = new LinkedHashSet<>(bqAssetDTOs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQAssetDTO)) {
			return false;
		}

		BQAssetDTO bqAsset = (BQAssetDTO)obj;

		if (Objects.equals(_applicationId, bqAsset._applicationId) &&
			Objects.equals(_assetId, bqAsset._assetId) &&
			Objects.equals(_assetTitle, bqAsset._assetTitle) &&
			Objects.equals(_channelId, bqAsset._channelId) &&
			Objects.equals(_dataSourceId, bqAsset._dataSourceId) &&
			Objects.equals(_dataSourceName, bqAsset._dataSourceName) &&
			Objects.equals(_id, bqAsset._id) &&
			Objects.equals(_modifiedDate, bqAsset._modifiedDate) &&
			Objects.equals(_count, bqAsset._count)) {

			return true;
		}

		return false;
	}

	public String getApplicationId() {
		return _applicationId;
	}

	@JsonProperty("dataSourceAssetPK")
	public String getAssetId() {
		return _assetId;
	}

	@JsonProperty("name")
	public String getAssetTitle() {
		return _assetTitle;
	}

	@JsonProperty("activities")
	public Set<BQAssetDTO> getBQAssetDTOs() {
		return _bqAssetDTOs;
	}

	public Long getChannelId() {
		return _channelId;
	}

	public Long getCount() {
		return _count;
	}

	public Long getDataSourceId() {
		return _dataSourceId;
	}

	public String getDataSourceName() {
		return _dataSourceName;
	}

	public String getId() {
		return _id;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_applicationId, _assetId, _assetTitle, _channelId, _dataSourceId,
			_dataSourceName, _id, _modifiedDate, _count);
	}

	public void setAssetId(String assetId) {
		_assetId = assetId;
	}

	public void setAssetTitle(String assetTitle) {
		_assetTitle = assetTitle;
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setCount(Long count) {
		_count = count;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceName(String dataSourceName) {
		_dataSourceName = dataSourceName;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	private String _applicationId;
	private String _assetId;
	private String _assetTitle;
	private Set<BQAssetDTO> _bqAssetDTOs;
	private Long _channelId;
	private Long _count;
	private Long _dataSourceId;
	private String _dataSourceName;
	private String _id;
	private Date _modifiedDate;

}