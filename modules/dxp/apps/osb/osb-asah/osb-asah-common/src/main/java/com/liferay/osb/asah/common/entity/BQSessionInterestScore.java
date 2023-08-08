/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Robson Pastor
 */
public class BQSessionInterestScore {

	public BQSessionInterestScore() {
	}

	public BQSessionInterestScore(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQSessionInterestScore)) {
			return false;
		}

		BQSessionInterestScore bqSessionInterestScore =
			(BQSessionInterestScore)obj;

		if (Objects.equals(_channelId, bqSessionInterestScore._channelId) &&
			Objects.equals(_identityId, bqSessionInterestScore._identityId) &&
			Objects.equals(_interested, bqSessionInterestScore._interested) &&
			Objects.equals(
				_interestScore, bqSessionInterestScore._interestScore) &&
			Objects.equals(_keyword, bqSessionInterestScore._keyword) &&
			Objects.equals(
				_recordedDate, bqSessionInterestScore._recordedDate) &&
			Objects.equals(_sessionId, bqSessionInterestScore._sessionId)) {

			return true;
		}

		return false;
	}

	public Long getChannelId() {
		return _channelId;
	}

	public String getIdentityId() {
		return _identityId;
	}

	public Boolean getInterested() {
		return _interested;
	}

	public Double getInterestScore() {
		return _interestScore;
	}

	public String getKeyword() {
		return _keyword;
	}

	@JsonAlias("recordedDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateRecorded")
	public Date getRecordedDate() {
		if (_recordedDate == null) {
			return null;
		}

		return new Date(_recordedDate.getTime());
	}

	public String getSessionId() {
		return _sessionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelId, _identityId, _interested, _interestScore, _keyword,
			_recordedDate, _sessionId);
	}

	public Boolean isInterested() {
		return _interested;
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setIdentityId(String identityId) {
		_identityId = identityId;
	}

	public void setInterested(Boolean interested) {
		_interested = interested;
	}

	public void setInterestScore(Double interestScore) {
		_interestScore = interestScore;
	}

	public void setKeyword(String keyword) {
		_keyword = keyword;
	}

	public void setRecordedDate(Date recordedDate) {
		if (recordedDate != null) {
			_recordedDate = new Date(recordedDate.getTime());
		}
	}

	public void setSessionId(String sessionId) {
		_sessionId = sessionId;
	}

	private Long _channelId;
	private String _identityId;
	private Boolean _interested;
	private Double _interestScore;
	private String _keyword;
	private Date _recordedDate;
	private String _sessionId;

}