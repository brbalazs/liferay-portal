/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ivica Cardic
 */
public class BQAsset {

	public BQAsset() {
	}

	public BQAsset(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQAsset)) {
			return false;
		}

		BQAsset bqAsset = (BQAsset)obj;

		if (Objects.equals(_applicationId, bqAsset._applicationId) &&
			Objects.equals(_assetId, bqAsset._assetId) &&
			Objects.equals(_assetTitle, bqAsset._assetTitle) &&
			Objects.equals(_channelId, bqAsset._channelId) &&
			Objects.equals(_dataSourceId, bqAsset._dataSourceId) &&
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

	public Long getChannelId() {
		return _channelId;
	}

	public Long getCount() {
		return _count;
	}

	public Long getDataSourceId() {
		return _dataSourceId;
	}

	public String getId() {
		return _id;
	}

	public Date getModifiedDate() {
		return new Date(_modifiedDate.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_applicationId, _assetId, _assetTitle, _channelId, _dataSourceId,
			_id, _modifiedDate, _count);
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

	public void setId(String id) {
		_id = id;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = new Date(modifiedDate.getTime());
	}

	private String _applicationId;
	private String _assetId;
	private String _assetTitle;
	private Long _channelId;
	private Long _count;
	private Long _dataSourceId;
	private String _id;
	private Date _modifiedDate;

}