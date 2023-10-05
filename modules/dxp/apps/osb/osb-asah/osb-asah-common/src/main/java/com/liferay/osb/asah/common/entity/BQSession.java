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
import java.util.Set;

/**
 * @author Leslie Wong
 */
public class BQSession {

	public BQSession() {
	}

	public BQSession(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQSession)) {
			return false;
		}

		BQSession bqSession = (BQSession)obj;

		if (Objects.equals(
				_acquisitionCampaign, bqSession._acquisitionCampaign) &&
			Objects.equals(
				_acquisitionChannel, bqSession._acquisitionChannel) &&
			Objects.equals(
				_acquisitionContent, bqSession._acquisitionContent) &&
			Objects.equals(_acquisitionMedium, bqSession._acquisitionMedium) &&
			Objects.equals(_acquisitionSource, bqSession._acquisitionSource) &&
			Objects.equals(_acquisitionTerm, bqSession._acquisitionTerm) &&
			Objects.equals(_bounce, bqSession._bounce) &&
			Objects.equals(_browserName, bqSession._browserName) &&
			Objects.equals(_channelId, bqSession._channelId) &&
			Objects.equals(_city, bqSession._city) &&
			Objects.equals(_country, bqSession._country) &&
			Objects.equals(_deviceType, bqSession._deviceType) &&
			Objects.equals(_duration, bqSession._duration) &&
			Objects.equals(_id, bqSession._id) &&
			Objects.equals(_platformName, bqSession._platformName) &&
			Objects.equals(_referrers, bqSession._referrers) &&
			Objects.equals(_region, bqSession._region) &&
			Objects.equals(_sessionEnd, bqSession._sessionEnd) &&
			Objects.equals(_sessionStart, bqSession._sessionStart) &&
			Objects.equals(_userId, bqSession._userId)) {

			return true;
		}

		return false;
	}

	@BigQueryColumn
	public String getAcquisitionCampaign() {
		return _acquisitionCampaign;
	}

	@BigQueryColumn
	public String getAcquisitionChannel() {
		return _acquisitionChannel;
	}

	@BigQueryColumn
	public String getAcquisitionContent() {
		return _acquisitionContent;
	}

	@BigQueryColumn
	public String getAcquisitionMedium() {
		return _acquisitionMedium;
	}

	@BigQueryColumn
	public String getAcquisitionSource() {
		return _acquisitionSource;
	}

	@BigQueryColumn
	public String getAcquisitionTerm() {
		return _acquisitionTerm;
	}

	@BigQueryColumn
	public Integer getBounce() {
		return _bounce;
	}

	@BigQueryColumn
	public String getBrowserName() {
		return _browserName;
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
	public String getCountry() {
		return _country;
	}

	@BigQueryColumn
	public String getDeviceType() {
		return _deviceType;
	}

	@BigQueryColumn
	public Long getDuration() {
		return _duration;
	}

	@BigQueryColumn
	public String getId() {
		return _id;
	}

	@BigQueryColumn
	public String getPlatformName() {
		return _platformName;
	}

	@BigQueryColumn
	public Set<String> getReferrers() {
		return _referrers;
	}

	@BigQueryColumn
	public String getRegion() {
		return _region;
	}

	@BigQueryColumn
	public Date getSessionEnd() {
		if (_sessionEnd == null) {
			return null;
		}

		return new Date(_sessionEnd.getTime());
	}

	@BigQueryColumn
	public Date getSessionStart() {
		if (_sessionStart == null) {
			return null;
		}

		return new Date(_sessionStart.getTime());
	}

	@BigQueryColumn
	public Set<String> getURLs() {
		return _urls;
	}

	@BigQueryColumn
	public String getUserId() {
		return _userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_acquisitionCampaign, _acquisitionChannel, _acquisitionContent,
			_acquisitionMedium, _acquisitionSource, _acquisitionTerm, _bounce,
			_browserName, _channelId, _city, _country, _deviceType, _duration,
			_id, _platformName, _referrers, _region, _sessionEnd, _sessionStart,
			_userId);
	}

	public void setAcquisitionCampaign(String acquisitionCampaign) {
		_acquisitionCampaign = acquisitionCampaign;
	}

	public void setAcquisitionChannel(String acquisitionChannel) {
		_acquisitionChannel = acquisitionChannel;
	}

	public void setAcquisitionContent(String acquisitionContent) {
		_acquisitionContent = acquisitionContent;
	}

	public void setAcquisitionMedium(String acquisitionMedium) {
		_acquisitionMedium = acquisitionMedium;
	}

	public void setAcquisitionSource(String acquisitionSource) {
		_acquisitionSource = acquisitionSource;
	}

	public void setAcquisitionTerm(String acquisitionTerm) {
		_acquisitionTerm = acquisitionTerm;
	}

	public void setBounce(Integer bounce) {
		_bounce = bounce;
	}

	public void setBrowserName(String browserName) {
		_browserName = browserName;
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

	public void setDeviceType(String deviceType) {
		_deviceType = deviceType;
	}

	public void setDuration(Long duration) {
		_duration = duration;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setPlatformName(String platformName) {
		_platformName = platformName;
	}

	public void setReferrers(Set<String> referrers) {
		_referrers = referrers;
	}

	public void setRegion(String region) {
		_region = region;
	}

	public void setSessionEnd(Date sessionEnd) {
		if (sessionEnd != null) {
			_sessionEnd = new Date(sessionEnd.getTime());
		}
		else {
			_sessionEnd = null;
		}
	}

	public void setSessionStart(Date sessionStart) {
		if (sessionStart != null) {
			_sessionStart = new Date(sessionStart.getTime());
		}
		else {
			_sessionStart = null;
		}
	}

	public void setUrls(Set<String> urls) {
		_urls = urls;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}

	private String _acquisitionCampaign;
	private String _acquisitionChannel;
	private String _acquisitionContent;
	private String _acquisitionMedium;
	private String _acquisitionSource;
	private String _acquisitionTerm;
	private Integer _bounce;
	private String _browserName;
	private Long _channelId;
	private String _city;
	private String _country;
	private String _deviceType;
	private Long _duration;
	private String _id;
	private String _platformName;
	private Set<String> _referrers;
	private String _region;
	private Date _sessionEnd;
	private Date _sessionStart;
	private Set<String> _urls;
	private String _userId;

}