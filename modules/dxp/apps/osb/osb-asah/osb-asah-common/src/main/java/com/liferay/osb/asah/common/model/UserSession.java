/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.liferay.osb.asah.common.date.DateUtil;

import java.io.Serializable;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @author André Miranda
 */
public class UserSession implements Serializable {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof UserSession)) {
			return false;
		}

		UserSession userSession = (UserSession)obj;

		if (Objects.equals(_acquisition, userSession._acquisition) &&
			Objects.equals(_bounced, userSession._bounced) &&
			Objects.equals(_browserName, userSession._browserName) &&
			Objects.equals(_channelId, userSession._channelId) &&
			Objects.equals(_city, userSession._city) &&
			Objects.equals(_completed, userSession._completed) &&
			Objects.equals(_completeDate, userSession._completeDate) &&
			Objects.equals(_completeReason, userSession._completeReason) &&
			Objects.equals(_country, userSession._country) &&
			Objects.equals(_dataSourceId, userSession._dataSourceId) &&
			Objects.equals(_devicePixelRatio, userSession._devicePixelRatio) &&
			Objects.equals(_date, userSession._date) &&
			Objects.equals(_duration, userSession._duration) &&
			Objects.equals(_entryPage, userSession._entryPage) &&
			Objects.equals(_exitPage, userSession._exitPage) &&
			Objects.equals(_finalized, userSession._finalized) &&
			Objects.equals(_firstEventDate, userSession._firstEventDate) &&
			Objects.equals(_id, userSession._id) &&
			Objects.equals(_individualId, userSession._individualId) &&
			Objects.equals(_lastEventDate, userSession._lastEventDate) &&
			Objects.equals(_modifiedDate, userSession._modifiedDate) &&
			Objects.equals(_platformName, userSession._platformName) &&
			Objects.equals(_region, userSession._region) &&
			Objects.equals(_screenHeight, userSession._screenHeight) &&
			Objects.equals(_screenWidth, userSession._screenWidth) &&
			Objects.equals(_userAgent, userSession._userAgent) &&
			Objects.equals(_userId, userSession._userId)) {

			return true;
		}

		return false;
	}

	public Acquisition getAcquisition() {
		return _acquisition;
	}

	public Boolean getBounced() {
		return _bounced;
	}

	public String getBrowserName() {
		return _browserName;
	}

	public Set<String> getCanonicalUrls() {
		if (_canonicalUrls == null) {
			return Collections.emptySet();
		}

		return _canonicalUrls;
	}

	public String getChannelId() {
		return _channelId;
	}

	public String getCity() {
		return _city;
	}

	public Boolean getCompleted() {
		return _completed;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCompleteDate() {
		if (_completeDate == null) {
			return null;
		}

		return new Date(_completeDate.getTime());
	}

	public String getCompleteReason() {
		return _completeReason;
	}

	public String getContentLanguageId() {
		return _contentLanguageId;
	}

	public String getCountry() {
		return _country;
	}

	public String getDataSourceId() {
		return _dataSourceId;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getDate() {
		if (_date == null) {
			return null;
		}

		return new Date(_date.getTime());
	}

	public String getDevicePixelRatio() {
		return _devicePixelRatio;
	}

	public String getDeviceType() {
		return _deviceType;
	}

	public Long getDuration() {
		return _duration;
	}

	public String getEntryPage() {
		return _entryPage;
	}

	public String getExitPage() {
		return _exitPage;
	}

	public Boolean getFinalized() {
		return _finalized;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getFirstEventDate() {
		if (_firstEventDate == null) {
			return null;
		}

		return new Date(_firstEventDate.getTime());
	}

	public String getId() {
		return _id;
	}

	public String getIndividualId() {
		return _individualId;
	}

	public Long getInteractionsCount() {
		return _interactionsCount;
	}

	public String getLanguageId() {
		return _languageId;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getLastEventDate() {
		if (_lastEventDate == null) {
			return null;
		}

		return new Date(_lastEventDate.getTime());
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

	public Long getPageViewsCount() {
		return _pageViewsCount;
	}

	public String getPlatformName() {
		return _platformName;
	}

	public Set<String> getReferrers() {
		if (_referrers == null) {
			return Collections.emptySet();
		}

		return _referrers;
	}

	public String getRegion() {
		return _region;
	}

	public String getScreenHeight() {
		return _screenHeight;
	}

	public String getScreenWidth() {
		return _screenWidth;
	}

	public String getTimezoneOffset() {
		return _timezoneOffset;
	}

	public Set<String> getURLs() {
		if (_urls == null) {
			return Collections.emptySet();
		}

		return _urls;
	}

	public String getUserAgent() {
		return _userAgent;
	}

	public String getUserId() {
		return _userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_acquisition, _bounced, _browserName, _canonicalUrls, _channelId,
			_city, _completed, _completeDate, _completeReason, _country,
			_dataSourceId, _date, _devicePixelRatio, _deviceType, _duration,
			_entryPage, _exitPage, _finalized, _firstEventDate, _id,
			_individualId, _lastEventDate, _modifiedDate, _platformName,
			_referrers, _region, _screenHeight, _screenWidth, _userAgent,
			_userId);
	}

	public void setAcquisition(Acquisition acquisition) {
		_acquisition = acquisition;
	}

	public void setBounced(Boolean bounced) {
		_bounced = bounced;
	}

	public void setBrowserName(String browserName) {
		_browserName = browserName;
	}

	public void setCanonicalUrls(Set<String> canonicalUrls) {
		_canonicalUrls = canonicalUrls;
	}

	public void setChannelId(String channelId) {
		_channelId = channelId;
	}

	public void setCity(String city) {
		_city = city;
	}

	public void setCompleted(Boolean completed) {
		_completed = completed;
	}

	public void setCompleteDate(Date completeDate) {
		if (completeDate != null) {
			_completeDate = new Date(completeDate.getTime());
		}
	}

	public void setCompleteReason(String completeReason) {
		_completeReason = completeReason;
	}

	public void setContentLanguageId(String contentLanguageId) {
		_contentLanguageId = contentLanguageId;
	}

	public void setCountry(String country) {
		_country = country;
	}

	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDate(Date date) {
		if (date != null) {
			_date = new Date(date.getTime());
		}
	}

	public void setDevicePixelRatio(String devicePixelRatio) {
		_devicePixelRatio = devicePixelRatio;
	}

	public void setDeviceType(String deviceType) {
		_deviceType = deviceType;
	}

	public void setDuration(Long duration) {
		_duration = duration;
	}

	public void setEntryPage(String entryPage) {
		_entryPage = entryPage;
	}

	public void setExitPage(String exitPage) {
		_exitPage = exitPage;
	}

	public void setFinalized(Boolean finalized) {
		_finalized = finalized;
	}

	public void setFirstEventDate(Date firstEventDate) {
		if (firstEventDate != null) {
			_firstEventDate = new Date(firstEventDate.getTime());
		}
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIndividualId(String individualId) {
		_individualId = individualId;
	}

	public void setInteractionsCount(Long interactionsCount) {
		_interactionsCount = interactionsCount;
	}

	public void setLanguageId(String languageId) {
		_languageId = languageId;
	}

	public void setLastEventDate(Date lastEventDate) {
		if (lastEventDate != null) {
			_lastEventDate = new Date(lastEventDate.getTime());
		}
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setPageViewsCount(Long pageViewsCount) {
		_pageViewsCount = pageViewsCount;
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

	public void setScreenHeight(String screenHeight) {
		_screenHeight = screenHeight;
	}

	public void setScreenWidth(String screenWidth) {
		_screenWidth = screenWidth;
	}

	public void setTimezoneOffset(String timezoneOffset) {
		_timezoneOffset = timezoneOffset;
	}

	public void setUrls(Set<String> urls) {
		_urls = urls;
	}

	public void setUserAgent(String userAgent) {
		_userAgent = userAgent;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}

	private Acquisition _acquisition;
	private Boolean _bounced;
	private String _browserName;
	private Set<String> _canonicalUrls;
	private String _channelId;
	private String _city;
	private Boolean _completed;
	private Date _completeDate;
	private String _completeReason;
	private String _contentLanguageId;
	private String _country;
	private String _dataSourceId;
	private Date _date;
	private String _devicePixelRatio;
	private String _deviceType;
	private Long _duration;
	private String _entryPage;
	private String _exitPage;
	private Boolean _finalized;
	private Date _firstEventDate;
	private String _id;
	private String _individualId;
	private Long _interactionsCount;
	private String _languageId;
	private Date _lastEventDate;
	private Date _modifiedDate;
	private Long _pageViewsCount;
	private String _platformName;
	private Set<String> _referrers;
	private String _region;
	private String _screenHeight;
	private String _screenWidth;
	private String _timezoneOffset;
	private Set<String> _urls;
	private String _userAgent;
	private String _userId;

}