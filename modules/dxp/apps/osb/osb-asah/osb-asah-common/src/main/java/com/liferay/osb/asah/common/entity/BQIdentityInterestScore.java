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
 * @author Marcellus Tavares
 */
public class BQIdentityInterestScore {

	public BQIdentityInterestScore() {
	}

	public BQIdentityInterestScore(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQIdentityInterestScore)) {
			return false;
		}

		BQIdentityInterestScore bqIdentityInterestScore =
			(BQIdentityInterestScore)obj;

		if (Objects.equals(_channelId, bqIdentityInterestScore._channelId) &&
			Objects.equals(_identityId, bqIdentityInterestScore._identityId) &&
			Objects.equals(_interested, bqIdentityInterestScore._interested) &&
			Objects.equals(
				_interestScore, bqIdentityInterestScore._interestScore) &&
			Objects.equals(_keyword, bqIdentityInterestScore._keyword) &&
			Objects.equals(
				_recordedDate, bqIdentityInterestScore._recordedDate)) {

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

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelId, _identityId, _interested, _interestScore, _keyword,
			_recordedDate);
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

	private Long _channelId;
	private String _identityId;
	private Boolean _interested;
	private Double _interestScore;
	private String _keyword;
	private Date _recordedDate;

}