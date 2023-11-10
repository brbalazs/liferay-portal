/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.RecentVisit;

import java.util.Date;

/**
 * @author Leslie Wong
 */
public abstract class RecentVisitDTO {

	public RecentVisitDTO() {
	}

	public RecentVisitDTO(RecentVisit recentVisit) {
		_firstVisitDate = recentVisit.getFirstVisitDate();
		_groupId = recentVisit.getGroupId();
		_lastVisitDate = recentVisit.getLastVisitDate();
		_visits = recentVisit.getVisits();
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("firstVisitDate")
	public Date getFirstVisitDate() {
		if (_firstVisitDate == null) {
			return null;
		}

		return new Date(_firstVisitDate.getTime());
	}

	@JsonProperty("groupId")
	public String getGroupId() {
		return _groupId;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("lastVisitDate")
	public Date getLastVisitDate() {
		if (_lastVisitDate == null) {
			return null;
		}

		return new Date(_lastVisitDate.getTime());
	}

	@JsonProperty("visits")
	public Long getVisits() {
		return _visits;
	}

	private Date _firstVisitDate;
	private String _groupId;
	private Date _lastVisitDate;
	private Long _visits;

}