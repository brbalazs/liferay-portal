/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Matthew Kong
 */
public class BQEvent {

	public BQEvent() {
	}

	public BQEvent(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public BQEvent(
		String applicationId, String browserName, String canonicalUrl,
		Long channelId, String city, String contentLanguageId, String context,
		String country, Date createDate, Long dataSourceId, String description,
		String deviceType, String emailAddressHashed, Date eventDate,
		String eventId, String eventProperties, String experienceId, String id,
		String keywords, String languageId, String platformName,
		String projectTimeZoneId, String referrer, String region,
		String sessionId, String timezoneOffset, String title, String url,
		String userId, String variantId) {

		_applicationId = applicationId;
		_browserName = browserName;
		_canonicalUrl = canonicalUrl;
		_channelId = channelId;
		_city = city;
		_contentLanguageId = contentLanguageId;
		_context = context;
		_country = country;

		setCreateDate(createDate);

		_dataSourceId = dataSourceId;
		_description = description;
		_deviceType = deviceType;
		_emailAddressHashed = emailAddressHashed;

		setEventDate(eventDate);

		_eventId = eventId;
		_eventProperties = eventProperties;
		_experienceId = experienceId;
		_id = id;
		_keywords = keywords;
		_languageId = languageId;
		_platformName = platformName;
		_projectTimeZoneId = projectTimeZoneId;
		_referrer = referrer;
		_region = region;
		_sessionId = sessionId;
		_timezoneOffset = timezoneOffset;
		_title = title;
		_url = url;
		_userId = userId;
		_variantId = variantId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQEvent)) {
			return false;
		}

		BQEvent bqEvent = (BQEvent)obj;

		if (Objects.equals(_applicationId, bqEvent._applicationId) &&
			Objects.equals(_assetId, bqEvent._assetId) &&
			Objects.equals(_assetTitle, bqEvent._assetTitle) &&
			Objects.equals(_browserName, bqEvent._browserName) &&
			Objects.equals(_canonicalUrl, bqEvent._canonicalUrl) &&
			Objects.equals(_channelId, bqEvent._channelId) &&
			Objects.equals(_city, bqEvent._city) &&
			Objects.equals(_contentLanguageId, bqEvent._contentLanguageId) &&
			Objects.equals(_context, bqEvent._context) &&
			Objects.equals(_country, bqEvent._country) &&
			Objects.equals(_createDate, bqEvent._createDate) &&
			Objects.equals(_dataSourceId, bqEvent._dataSourceId) &&
			Objects.equals(_description, bqEvent._description) &&
			Objects.equals(_deviceType, bqEvent._deviceType) &&
			Objects.equals(_emailAddressHashed, bqEvent._emailAddressHashed) &&
			Objects.equals(_eventDate, bqEvent._eventDate) &&
			Objects.equals(_eventId, bqEvent._eventId) &&
			Objects.equals(_eventProperties, bqEvent._eventProperties) &&
			Objects.equals(_experienceId, bqEvent._experienceId) &&
			Objects.equals(_id, bqEvent._id) &&
			Objects.equals(_keywords, bqEvent._keywords) &&
			Objects.equals(_languageId, bqEvent._languageId) &&
			Objects.equals(_platformName, bqEvent._platformName) &&
			Objects.equals(_projectTimeZoneId, bqEvent._projectTimeZoneId) &&
			Objects.equals(_referrer, bqEvent._referrer) &&
			Objects.equals(_region, bqEvent._region) &&
			Objects.equals(_sessionId, bqEvent._sessionId) &&
			Objects.equals(_timezoneOffset, bqEvent._timezoneOffset) &&
			Objects.equals(_title, bqEvent._title) &&
			Objects.equals(_url, bqEvent._url) &&
			Objects.equals(_userId, bqEvent._userId) &&
			Objects.equals(_variantId, bqEvent._variantId)) {

			return true;
		}

		return false;
	}

	@BigQueryColumn
	public String getApplicationId() {
		return _applicationId;
	}

	@BigQueryColumn
	public String getAssetId() {
		return _assetId;
	}

	@BigQueryColumn
	public String getAssetTitle() {
		return _assetTitle;
	}

	@BigQueryColumn
	public String getBrowserName() {
		return _browserName;
	}

	@BigQueryColumn
	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	@BigQueryColumn
	public Long getChannelId() {
		return _channelId;
	}

	@BigQueryColumn
	public String getCity() {
		return _city;
	}

	@BigQueryColumn
	public String getContentLanguageId() {
		return _contentLanguageId;
	}

	@BigQueryColumn
	public String getContext() {
		return _context;
	}

	@BigQueryColumn
	public String getCountry() {
		return _country;
	}

	@BigQueryColumn
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@BigQueryColumn
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@BigQueryColumn
	public String getDescription() {
		return _description;
	}

	@BigQueryColumn
	public String getDeviceType() {
		return _deviceType;
	}

	@BigQueryColumn
	public String getEmailAddressHashed() {
		return _emailAddressHashed;
	}

	@BigQueryColumn
	public Date getEventDate() {
		if (_eventDate == null) {
			return null;
		}

		return new Date(_eventDate.getTime());
	}

	@BigQueryColumn
	public String getEventId() {
		return _eventId;
	}

	@BigQueryColumn
	public String getEventProperties() {
		return _eventProperties;
	}

	@BigQueryColumn
	public String getExperienceId() {
		return _experienceId;
	}

	@BigQueryColumn
	public Long getExperimentId() {
		return _experimentId;
	}

	@BigQueryColumn
	public String getId() {
		return _id;
	}

	@BigQueryColumn
	public String getKeywords() {
		return _keywords;
	}

	@BigQueryColumn
	public String getLanguageId() {
		return _languageId;
	}

	@BigQueryColumn
	public String getPlatformName() {
		return _platformName;
	}

	@BigQueryColumn
	public String getProjectTimeZoneId() {
		return _projectTimeZoneId;
	}

	@BigQueryColumn
	public String getReferrer() {
		return _referrer;
	}

	@BigQueryColumn
	public String getRegion() {
		return _region;
	}

	@BigQueryColumn
	public String getSessionId() {
		return _sessionId;
	}

	@BigQueryColumn
	public String getTimezoneOffset() {
		return _timezoneOffset;
	}

	@BigQueryColumn
	public String getTitle() {
		return _title;
	}

	@BigQueryColumn
	public String getURL() {
		return _url;
	}

	@BigQueryColumn
	public String getUserId() {
		return _userId;
	}

	@BigQueryColumn
	public String getVariantId() {
		return _variantId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_applicationId, _assetId, _assetTitle, _browserName, _canonicalUrl,
			_channelId, _city, _contentLanguageId, _context, _country,
			_createDate, _dataSourceId, _description, _deviceType,
			_emailAddressHashed, _eventDate, _eventId, _eventProperties,
			_experienceId, _experimentId, _id, _keywords, _languageId,
			_platformName, _projectTimeZoneId, _referrer, _region, _sessionId,
			_timezoneOffset, _title, _url, _userId, _variantId);
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}

	public void setAssetId(String assetId) {
		_assetId = assetId;
	}

	public void setAssetTitle(String assetTitle) {
		_assetTitle = assetTitle;
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

	public void setContentLanguageId(String contentLanguageId) {
		_contentLanguageId = contentLanguageId;
	}

	public void setContext(String context) {
		_context = context;
	}

	public void setCountry(String country) {
		_country = country;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
		else {
			_createDate = null;
		}
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDeviceType(String deviceType) {
		_deviceType = deviceType;
	}

	public void setEmailAddressHashed(String emailAddressHashed) {
		_emailAddressHashed = emailAddressHashed;
	}

	public void setEventDate(Date eventDate) {
		if (eventDate != null) {
			_eventDate = new Date(eventDate.getTime());
		}
		else {
			_eventDate = null;
		}
	}

	public void setEventId(String eventId) {
		_eventId = eventId;
	}

	public void setEventProperties(String eventProperties) {
		_eventProperties = eventProperties;
	}

	public void setExperienceId(String experienceId) {
		_experienceId = experienceId;
	}

	public void setExperimentId(Long experimentId) {
		_experimentId = experimentId;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setKeywords(String keywords) {
		_keywords = keywords;
	}

	public void setLanguageId(String languageId) {
		_languageId = languageId;
	}

	public void setPlatformName(String platformName) {
		_platformName = platformName;
	}

	public void setProjectTimeZoneId(String projectTimeZoneId) {
		_projectTimeZoneId = projectTimeZoneId;
	}

	public void setReferrer(String referrer) {
		_referrer = referrer;
	}

	public void setRegion(String region) {
		_region = region;
	}

	public void setSessionId(String sessionId) {
		_sessionId = sessionId;
	}

	public void setTimezoneOffset(String timezoneOffset) {
		_timezoneOffset = timezoneOffset;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}

	public void setVariantId(String variantId) {
		_variantId = variantId;
	}

	private String _applicationId;
	private String _assetId;
	private String _assetTitle;
	private String _browserName;
	private String _canonicalUrl;
	private Long _channelId;
	private String _city;
	private String _contentLanguageId;
	private String _context;
	private String _country;
	private Date _createDate;
	private Long _dataSourceId;
	private String _description;
	private String _deviceType;
	private String _emailAddressHashed;
	private Date _eventDate;
	private String _eventId;
	private String _eventProperties;
	private String _experienceId;
	private Long _experimentId;
	private String _id;
	private String _keywords;
	private String _languageId;
	private String _platformName;
	private String _projectTimeZoneId;
	private String _referrer;
	private String _region;
	private String _sessionId;
	private String _timezoneOffset;
	private String _title;
	private String _url;
	private String _userId;
	private String _variantId;

}