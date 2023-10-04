/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.RecentSite;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Marcos Martins
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("recent-sites")
public class RecentSiteDTO {

	public RecentSiteDTO() {
	}

	public RecentSiteDTO(List<RecentSite> recentSites) {
		_recentSiteDTOs = SetUtil.map(recentSites, RecentSiteDTO::new);
	}

	public RecentSiteDTO(RecentSite recentSite) {
		_firstVisitDate = recentSite.getFirstVisitDate();
		_groupId = recentSite.getGroupId();
		_lastVisitDate = recentSite.getLastVisitDate();
		_visits = recentSite.getVisits();
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getFirstVisitDate() {
		if (_firstVisitDate == null) {
			return null;
		}

		return new Date(_firstVisitDate.getTime());
	}

	public String getGroupId() {
		return _groupId;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getLastVisitDate() {
		if (_lastVisitDate == null) {
			return null;
		}

		return new Date(_lastVisitDate.getTime());
	}

	@JsonProperty("recent-sites")
	public Set<RecentSiteDTO> getRecentSiteDTOs() {
		return _recentSiteDTOs;
	}

	public Long getVisits() {
		return _visits;
	}

	private Date _firstVisitDate;
	private String _groupId;
	private Date _lastVisitDate;
	private Set<RecentSiteDTO> _recentSiteDTOs;
	private Long _visits;

}