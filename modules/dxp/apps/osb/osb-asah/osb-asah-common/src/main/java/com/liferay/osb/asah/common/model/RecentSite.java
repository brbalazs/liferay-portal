/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcos Martins
 */
public class RecentSite {

	public RecentSite() {
	}

	public RecentSite(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RecentSite)) {
			return false;
		}

		RecentSite recentSite = (RecentSite)obj;

		if (Objects.equals(_firstVisitDate, recentSite._firstVisitDate) &&
			Objects.equals(_groupId, recentSite._groupId) &&
			Objects.equals(_lastVisitDate, recentSite._lastVisitDate) &&
			Objects.equals(_visits, recentSite._visits)) {

			return true;
		}

		return false;
	}

	public Date getFirstVisitDate() {
		if (_firstVisitDate == null) {
			return null;
		}

		return new Date(_firstVisitDate.getTime());
	}

	public String getGroupId() {
		return _groupId;
	}

	public Date getLastVisitDate() {
		if (_lastVisitDate == null) {
			return null;
		}

		return new Date(_lastVisitDate.getTime());
	}

	public Long getVisits() {
		return _visits;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_firstVisitDate, _groupId, _lastVisitDate, _visits);
	}

	public void setFirstVisitDate(Date firstVisitDate) {
		if (firstVisitDate != null) {
			_firstVisitDate = new Date(firstVisitDate.getTime());
		}
	}

	public void setGroupId(String groupId) {
		_groupId = groupId;
	}

	public void setLastVisitDate(Date lastVisitDate) {
		if (lastVisitDate != null) {
			_lastVisitDate = new Date(lastVisitDate.getTime());
		}
	}

	public void setVisits(Long visits) {
		_visits = visits;
	}

	private Date _firstVisitDate;
	private String _groupId;
	private Date _lastVisitDate;
	private Long _visits;

}