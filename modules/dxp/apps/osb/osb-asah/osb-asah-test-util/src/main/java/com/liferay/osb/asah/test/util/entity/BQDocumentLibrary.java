/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
 * @author Robson Pastor
 */
@Table
public class BQDocumentLibrary implements Persistable<Long> {

	public BQDocumentLibrary() {
	}

	public BQDocumentLibrary(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof BQDocumentLibrary)) {
			return false;
		}

		BQDocumentLibrary documentLibrary = (BQDocumentLibrary)obj;

		if (Objects.equals(_id, documentLibrary._id) &&
			Objects.equals(_assetId, documentLibrary._assetId) &&
			Objects.equals(
				_assetPrimaryKey, documentLibrary._assetPrimaryKey) &&
			Objects.equals(_browserName, documentLibrary._browserName) &&
			Objects.equals(_canonicalUrl, documentLibrary._canonicalUrl) &&
			Objects.equals(_channelId, documentLibrary._channelId) &&
			Objects.equals(_city, documentLibrary._city) &&
			Objects.equals(_comments, documentLibrary._comments) &&
			Objects.equals(_country, documentLibrary._country) &&
			Objects.equals(_dataSourceId, documentLibrary._dataSourceId) &&
			Objects.equals(_deviceType, documentLibrary._deviceType) &&
			Objects.equals(_downloads, documentLibrary._downloads) &&
			Objects.equals(_eventDate, documentLibrary._eventDate) &&
			Objects.equals(_individualId, documentLibrary._individualId) &&
			Objects.equals(
				_knownIndividual, documentLibrary._knownIndividual) &&
			Objects.equals(_platformName, documentLibrary._platformName) &&
			Objects.equals(_previews, documentLibrary._previews) &&
			Objects.equals(_ratings, documentLibrary._ratings) &&
			Objects.equals(_ratingsScore, documentLibrary._ratingsScore) &&
			Objects.equals(_region, documentLibrary._region) &&
			Objects.equals(_segmentNames, documentLibrary._segmentNames) &&
			Objects.equals(_sessionId, documentLibrary._sessionId) &&
			Objects.equals(_title, documentLibrary._title) &&
			Objects.equals(_userId, documentLibrary._userId) &&
			Objects.equals(_variantId, documentLibrary._variantId)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getAssetId() {
		return _assetId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getAssetPrimaryKey() {
		return _assetPrimaryKey;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getBrowserName() {
		return _browserName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("canonicalUrls")
	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getChannelId() {
		return _channelId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getCity() {
		return _city;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getComments() {
		return _comments;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getCountry() {
		return _country;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDeviceType() {
		return _deviceType;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getDownloads() {
		return _downloads;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getEventDate() {
		if (_eventDate == null) {
			return null;
		}

		return new Date(_eventDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getIndividualId() {
		return _individualId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getKnownIndividual() {
		return _knownIndividual;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getPreviews() {
		return _previews;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getRatings() {
		return _ratings;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getRatingsScore() {
		return _ratingsScore;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getRegion() {
		return _region;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getSegmentNames() {
		return _segmentNames;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getSessionId() {
		return _sessionId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getTitle() {
		return _title;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getUserId() {
		return _userId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getVariantId() {
		return _variantId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_id, _assetId, _assetPrimaryKey, _browserName, _canonicalUrl,
			_channelId, _city, _comments, _country, _dataSourceId, _deviceType,
			_downloads, _eventDate, _individualId, _knownIndividual,
			_platformName, _previews, _ratings, _ratingsScore, _region,
			_segmentNames, _sessionId, _title, _userId, _variantId);
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

	public void setAssetPrimaryKey(String assetPrimaryKey) {
		_assetPrimaryKey = assetPrimaryKey;
	}

	public void setBrowserName(String browserName) {
		_browserName = browserName;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setCity(String city) {
		_city = city;
	}

	public void setComments(Long comments) {
		_comments = comments;
	}

	public void setCountry(String country) {
		_country = country;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDeviceType(String deviceType) {
		_deviceType = deviceType;
	}

	public void setDownloads(Long downloads) {
		_downloads = downloads;
	}

	public void setEventDate(Date eventDate) {
		if (eventDate != null) {
			_eventDate = new Date(eventDate.getTime());
		}
		else {
			_eventDate = null;
		}
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIndividualId(Long individualId) {
		_individualId = individualId;
	}

	public void setIsNew(boolean isNew) {
		_isNew = isNew;
	}

	public void setKnownIndividual(Boolean knownIndividual) {
		_knownIndividual = knownIndividual;
	}

	public void setPlatformName(String platformName) {
		_platformName = platformName;
	}

	public void setPreviews(Long previews) {
		_previews = previews;
	}

	public void setRatings(Long ratings) {
		_ratings = ratings;
	}

	public void setRatingsScore(Double ratingsScore) {
		_ratingsScore = ratingsScore;
	}

	public void setRegion(String region) {
		_region = region;
	}

	public void setSegmentNames(String segmentNames) {
		_segmentNames = segmentNames;
	}

	public void setSessionId(String sessionId) {
		_sessionId = sessionId;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}

	public void setVariantId(String variantId) {
		_variantId = variantId;
	}

	@Transient
	private String _assetId;

	@Transient
	private String _assetPrimaryKey;

	@Transient
	private String _browserName;

	@Transient
	private String _canonicalUrl;

	@Transient
	private Long _channelId;

	@Transient
	private String _city;

	@Transient
	private Long _comments;

	@Transient
	private String _country;

	@Transient
	private Long _dataSourceId;

	@Transient
	private String _deviceType;

	@Transient
	private Long _downloads;

	@Transient
	private Date _eventDate;

	@Transient
	private Long _id;

	@Transient
	private Long _individualId;

	@Transient
	private Boolean _isNew;

	@Transient
	private Boolean _knownIndividual;

	@Transient
	private String _platformName;

	@Transient
	private Long _previews;

	@Transient
	private Long _ratings;

	@Transient
	private Double _ratingsScore;

	@Transient
	private String _region;

	@Transient
	private String _segmentNames;

	@Transient
	private String _sessionId;

	@Transient
	private String _title;

	@Transient
	private String _userId;

	@Transient
	private String _variantId;

}