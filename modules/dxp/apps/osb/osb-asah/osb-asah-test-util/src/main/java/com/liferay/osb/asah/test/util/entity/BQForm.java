/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.google.common.base.Objects;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Robson Pastor
 */
@Table
public class BQForm implements Persistable<Long> {

	public BQForm() {
	}

	public BQForm(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof BQForm)) {
			return false;
		}

		BQForm bqForm = (BQForm)obj;

		if (Objects.equal(_abandonments, bqForm._abandonments) &&
			Objects.equal(_assetId, bqForm._assetId) &&
			Objects.equal(_assetPrimaryKey, bqForm._assetPrimaryKey) &&
			Objects.equal(_browserName, bqForm._browserName) &&
			Objects.equal(_canonicalUrl, bqForm._canonicalUrl) &&
			Objects.equal(_channelId, bqForm._channelId) &&
			Objects.equal(_city, bqForm._city) &&
			Objects.equal(_country, bqForm._country) &&
			Objects.equal(_dataSourceId, bqForm._dataSourceId) &&
			Objects.equal(_deviceType, bqForm._deviceType) &&
			Objects.equal(_eventDate, bqForm._eventDate) &&
			Objects.equal(_individualId, bqForm._individualId) &&
			Objects.equal(_knownIndividual, bqForm._knownIndividual) &&
			Objects.equal(_platformName, bqForm._platformName) &&
			Objects.equal(_primaryKey, bqForm._primaryKey) &&
			Objects.equal(_region, bqForm._region) &&
			Objects.equal(_segmentNames, bqForm._segmentNames) &&
			Objects.equal(_sessionId, bqForm._sessionId) &&
			Objects.equal(_submissions, bqForm._submissions) &&
			Objects.equal(_submissionsTime, bqForm._submissionsTime) &&
			Objects.equal(_title, bqForm._title) &&
			Objects.equal(_userId, bqForm._userId) &&
			Objects.equal(_variantId, bqForm._variantId) &&
			Objects.equal(_views, bqForm._views)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getAbandonments() {
		return _abandonments;
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
	public String getPlatformName() {
		return _platformName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getPrimaryKey() {
		return _primaryKey;
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
	public Long getSubmissions() {
		return _submissions;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getSubmissionsTime() {
		return _submissionsTime;
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

	@AccessType(AccessType.Type.PROPERTY)
	public Long getViews() {
		return _views;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(
			_abandonments, _assetId, _assetPrimaryKey, _browserName,
			_canonicalUrl, _channelId, _city, _country, _dataSourceId,
			_deviceType, _eventDate, _id, _individualId, _isNew,
			_knownIndividual, _platformName, _primaryKey, _region,
			_segmentNames, _sessionId, _submissions, _submissionsTime, _title,
			_userId, _variantId, _views);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setAbandonments(Long abandonments) {
		_abandonments = abandonments;
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

	public void setCountry(String country) {
		_country = country;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDeviceType(String deviceType) {
		_deviceType = deviceType;
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

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setKnownIndividual(Boolean knownIndividual) {
		_knownIndividual = knownIndividual;
	}

	public void setPlatformName(String platformName) {
		_platformName = platformName;
	}

	public void setPrimaryKey(String primaryKey) {
		_primaryKey = primaryKey;
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

	public void setSubmissions(Long submissions) {
		_submissions = submissions;
	}

	public void setSubmissionsTime(Long submissionsTime) {
		_submissionsTime = submissionsTime;
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

	public void setViews(Long views) {
		_views = views;
	}

	@Transient
	private Long _abandonments;

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
	private String _country;

	@Transient
	private Long _dataSourceId;

	@Transient
	private String _deviceType;

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
	private String _primaryKey;

	@Transient
	private String _region;

	@Transient
	private String _segmentNames;

	@Transient
	private String _sessionId;

	@Transient
	private Long _submissions;

	@Transient
	private Long _submissionsTime;

	@Transient
	private String _title;

	@Transient
	private String _userId;

	@Transient
	private String _variantId;

	@Transient
	private Long _views;

}