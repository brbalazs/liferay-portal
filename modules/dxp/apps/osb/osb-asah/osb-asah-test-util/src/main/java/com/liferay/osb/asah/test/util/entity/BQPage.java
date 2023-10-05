/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class BQPage implements Persistable<Long> {

	public BQPage() {
	}

	public BQPage(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof BQPage)) {
			return false;
		}

		BQPage bqPage = (BQPage)obj;

		if (Objects.equals(_bounce, bqPage._bounce) &&
			Objects.equals(_browserName, bqPage._browserName) &&
			Objects.equals(_canonicalUrl, bqPage._canonicalUrl) &&
			Objects.equals(_channelId, bqPage._channelId) &&
			Objects.equals(_city, bqPage._city) &&
			Objects.equals(_contentLanguageId, bqPage._contentLanguageId) &&
			Objects.equals(_country, bqPage._country) &&
			Objects.equals(_ctaClicks, bqPage._ctaClicks) &&
			Objects.equals(_dataSourceId, bqPage._dataSourceId) &&
			Objects.equals(_deviceType, bqPage._deviceType) &&
			Objects.equals(_directAccess, bqPage._directAccess) &&
			Objects.equals(_directAccessDates, bqPage._directAccessDates) &&
			Objects.equals(_engagementScore, bqPage._engagementScore) &&
			Objects.equals(_entrances, bqPage._entrances) &&
			Objects.equals(_eventDate, bqPage._eventDate) &&
			Objects.equals(_exits, bqPage._exits) &&
			Objects.equals(_experienceId, bqPage._experienceId) &&
			Objects.equals(_experimentId, bqPage._experimentId) &&
			Objects.equals(_firstEventDate, bqPage._firstEventDate) &&
			Objects.equals(_formSubmissions, bqPage._formSubmissions) &&
			Objects.equals(_indirectAccess, bqPage._indirectAccess) &&
			Objects.equals(_indirectAccessDates, bqPage._indirectAccessDates) &&
			Objects.equals(_individualId, bqPage._individualId) &&
			Objects.equals(_interactionDates, bqPage._interactionDates) &&
			Objects.equals(_knownIndividual, bqPage._knownIndividual) &&
			Objects.equals(_lastEventDate, bqPage._lastEventDate) &&
			Objects.equals(_modifiedDate, bqPage._modifiedDate) &&
			Objects.equals(_platformName, bqPage._platformName) &&
			Objects.equals(_primaryKey, bqPage._primaryKey) &&
			Objects.equals(_reads, bqPage._reads) &&
			Objects.equals(_region, bqPage._region) &&
			Objects.equals(_segmentNames, bqPage._segmentNames) &&
			Objects.equals(_sessionId, bqPage._sessionId) &&
			Objects.equals(_timeOnPage, bqPage._timeOnPage) &&
			Objects.equals(_title, bqPage._title) &&
			Objects.equals(_url, bqPage._url) &&
			Objects.equals(_userId, bqPage._userId) &&
			Objects.equals(_variantId, bqPage._variantId) &&
			Objects.equals(_views, bqPage._views)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getBounce() {
		return _bounce;
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
	public String getChannelId() {
		return _channelId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getCity() {
		return _city;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getContentLanguageId() {
		return _contentLanguageId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getCountry() {
		return _country;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getCtaClicks() {
		return _ctaClicks;
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
	public Long getDirectAccess() {
		return _directAccess;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getDirectAccessDates() {
		if (_directAccessDates == null) {
			return null;
		}

		return new Date(_directAccessDates.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getEngagementScore() {
		return _engagementScore;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getEntrances() {
		return _entrances;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getEventDate() {
		if (_eventDate == null) {
			return null;
		}

		return new Date(_eventDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getExits() {
		return _exits;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getExperienceId() {
		return _experienceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getExperimentId() {
		return _experimentId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getFirstEventDate() {
		if (_firstEventDate == null) {
			return null;
		}

		return new Date(_firstEventDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getFormSubmissions() {
		return _formSubmissions;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getIndirectAccess() {
		return _indirectAccess;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getIndirectAccessDates() {
		if (_indirectAccessDates == null) {
			return null;
		}

		return new Date(_indirectAccessDates.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getIndividualId() {
		return _individualId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getInteractionDates() {
		if (_interactionDates == null) {
			return null;
		}

		return new Date(_interactionDates.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getKnownIndividual() {
		return _knownIndividual;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getLastEventDate() {
		if (_lastEventDate == null) {
			return null;
		}

		return new Date(_lastEventDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
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
	public Long getReads() {
		return _reads;
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
	public Long getTimeOnPage() {
		return _timeOnPage;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getTitle() {
		return _title;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getURL() {
		return _url;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getUserId() {
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

	public int hashCode() {
		return Objects.hash(
			_id, _bounce, _browserName, _canonicalUrl, _channelId, _city,
			_contentLanguageId, _country, _ctaClicks, _dataSourceId,
			_deviceType, _directAccess, _directAccessDates, _engagementScore,
			_entrances, _eventDate, _exits, _experienceId, _experimentId,
			_firstEventDate, _formSubmissions, _indirectAccess,
			_indirectAccessDates, _individualId, _interactionDates,
			_knownIndividual, _lastEventDate, _modifiedDate, _platformName,
			_primaryKey, _reads, _region, _segmentNames, _sessionId,
			_timeOnPage, _title, _url, _userId, _variantId, _views);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setBounce(Long bounce) {
		_bounce = bounce;
	}

	public void setBrowserName(String browserName) {
		_browserName = browserName;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setChannelId(String channelId) {
		_channelId = channelId;
	}

	public void setCity(String city) {
		_city = city;
	}

	public void setContentLanguageId(String contentLanguageId) {
		_contentLanguageId = contentLanguageId;
	}

	public void setCountry(String country) {
		_country = country;
	}

	public void setCtaClicks(Long ctaClicks) {
		_ctaClicks = ctaClicks;
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDeviceType(String deviceType) {
		_deviceType = deviceType;
	}

	public void setDirectAccess(Long directAccess) {
		_directAccess = directAccess;
	}

	public void setDirectAccessDates(Date directAccessDates) {
		if (directAccessDates != null) {
			_directAccessDates = new Date(directAccessDates.getTime());
		}
		else {
			_directAccessDates = null;
		}
	}

	public void setEngagementScore(Double engagementScore) {
		_engagementScore = engagementScore;
	}

	public void setEntrances(Long entrances) {
		_entrances = entrances;
	}

	public void setEventDate(Date eventDate) {
		if (eventDate != null) {
			_eventDate = new Date(eventDate.getTime());
		}
		else {
			_eventDate = null;
		}
	}

	public void setExits(Long exits) {
		_exits = exits;
	}

	public void setExperienceId(String experienceId) {
		_experienceId = experienceId;
	}

	public void setExperimentId(String experimentId) {
		_experimentId = experimentId;
	}

	public void setFirstEventDate(Date firstEventDate) {
		if (firstEventDate != null) {
			_firstEventDate = new Date(firstEventDate.getTime());
		}
		else {
			_firstEventDate = null;
		}
	}

	public void setFormSubmissions(Long formSubmissions) {
		_formSubmissions = formSubmissions;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIndirectAccess(Long indirectAccess) {
		_indirectAccess = indirectAccess;
	}

	public void setIndirectAccessDates(Date indirectAccessDates) {
		if (indirectAccessDates != null) {
			_indirectAccessDates = new Date(indirectAccessDates.getTime());
		}
		else {
			_indirectAccessDates = null;
		}
	}

	public void setIndividualId(Long individualId) {
		_individualId = individualId;
	}

	public void setInteractionDates(Date interactionDates) {
		if (interactionDates != null) {
			_interactionDates = new Date(interactionDates.getTime());
		}
		else {
			_interactionDates = null;
		}
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setKnownIndividual(Boolean knownIndividual) {
		_knownIndividual = knownIndividual;
	}

	public void setLastEventDate(Date lastEventDate) {
		if (lastEventDate != null) {
			_lastEventDate = new Date(lastEventDate.getTime());
		}
		else {
			_lastEventDate = null;
		}
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
		else {
			_modifiedDate = null;
		}
	}

	public void setPlatformName(String platformName) {
		_platformName = platformName;
	}

	public void setPrimaryKey(String primaryKey) {
		_primaryKey = primaryKey;
	}

	public void setReads(Long reads) {
		_reads = reads;
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

	public void setTimeOnPage(Long timeOnPage) {
		_timeOnPage = timeOnPage;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setUserId(Long userId) {
		_userId = userId;
	}

	public void setVariantId(String variantId) {
		_variantId = variantId;
	}

	public void setViews(Long views) {
		_views = views;
	}

	@Transient
	private Long _bounce;

	@Transient
	private String _browserName;

	@Transient
	private String _canonicalUrl;

	@Transient
	private String _channelId;

	@Transient
	private String _city;

	@Transient
	private String _contentLanguageId;

	@Transient
	private String _country;

	@Transient
	private Long _ctaClicks;

	@Transient
	private Long _dataSourceId;

	@Transient
	private String _deviceType;

	@Transient
	private Long _directAccess;

	@Transient
	private Date _directAccessDates;

	@Transient
	private Double _engagementScore;

	@Transient
	private Long _entrances;

	@Transient
	private Date _eventDate;

	@Transient
	private Long _exits;

	@Transient
	private String _experienceId;

	@Transient
	private String _experimentId;

	@Transient
	private Date _firstEventDate;

	@Transient
	private Long _formSubmissions;

	@Transient
	private Long _id;

	@Transient
	private Long _indirectAccess;

	@Transient
	private Date _indirectAccessDates;

	@Transient
	private Long _individualId;

	@Transient
	private Date _interactionDates;

	@Transient
	private Boolean _isNew;

	@Transient
	private Boolean _knownIndividual;

	@Transient
	private Date _lastEventDate;

	@Transient
	private Date _modifiedDate;

	@Transient
	private String _platformName;

	@Transient
	private String _primaryKey;

	@Transient
	private Long _reads;

	@Transient
	private String _region;

	@Transient
	private String _segmentNames;

	@Transient
	private String _sessionId;

	@Transient
	private Long _timeOnPage;

	@Transient
	private String _title;

	@Transient
	private String _url;

	@Transient
	private Long _userId;

	@Transient
	private String _variantId;

	@Transient
	private Long _views;

}