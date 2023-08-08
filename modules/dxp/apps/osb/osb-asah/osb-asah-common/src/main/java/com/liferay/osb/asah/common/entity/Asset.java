/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class Asset implements Persistable<Long> {

	public Asset() {
	}

	public Asset(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Asset)) {
			return false;
		}

		Asset asset = (Asset)obj;

		if (Objects.equals(_assetKeywords, asset._assetKeywords) &&
			Objects.equals(_assetType, asset._assetType) &&
			Objects.equals(_canonicalURL, asset._canonicalURL) &&
			Objects.equals(_channelIds, asset._channelIds) &&
			Objects.equals(_dataSourceAssetPK, asset._dataSourceAssetPK) &&
			Objects.equals(_dataSourceId, asset._dataSourceId) &&
			Objects.equals(_description, asset._description) &&
			Objects.equals(_title, asset._title) &&
			Objects.equals(_url, asset._url)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("keywords")
	@MappedCollection(idColumn = "assetid")
	public Set<AssetKeyword> getAssetKeywords() {
		return _assetKeywords;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getAssetType() {
		return _assetType;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("canonicalUrl")
	public String getCanonicalURL() {
		return _canonicalURL;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(contentUsing = ToStringSerializer.class)
	public Set<Long> getChannelIds() {
		return _channelIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDataSourceAssetPK() {
		return _dataSourceAssetPK;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDescription() {
		return _description;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("name")
	public String getTitle() {
		return _title;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getURL() {
		return _url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_assetType, _canonicalURL, _channelIds, _dataSourceAssetPK,
			_dataSourceId, _description, _title, _url);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setAssetKeywords(Set<AssetKeyword> assetKeywords) {
		_assetKeywords = assetKeywords;
	}

	public void setAssetType(String assetType) {
		_assetType = assetType;
	}

	public void setCanonicalURL(String canonicalURL) {
		_canonicalURL = canonicalURL;
	}

	public void setChannelIds(Set<Long> channelIds) {
		_channelIds = channelIds;
	}

	public void setDataSourceAssetPK(String dataSourceAssetPK) {
		_dataSourceAssetPK = dataSourceAssetPK;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setURL(String url) {
		_url = url;
	}

	@Transient
	private Set<AssetKeyword> _assetKeywords;

	@Transient
	private String _assetType;

	@Transient
	private String _canonicalURL;

	@Transient
	private Set<Long> _channelIds;

	@Transient
	private String _dataSourceAssetPK;

	@Transient
	private Long _dataSourceId;

	@Transient
	private String _description;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private String _title;

	@Transient
	private String _url;

}