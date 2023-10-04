/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Leslie Wong
 */
public class RecentPage {

	public RecentPage() {
	}

	public RecentPage(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RecentPage)) {
			return false;
		}

		RecentPage recentPage = (RecentPage)obj;

		if (Objects.equals(_firstVisitDate, recentPage._firstVisitDate) &&
			Objects.equals(_displayLanguageId, recentPage._displayLanguageId) &&
			Objects.equals(_lastVisitDate, recentPage._lastVisitDate) &&
			Objects.equals(_url, recentPage._url) &&
			Objects.equals(_visits, recentPage._visits)) {

			return true;
		}

		return false;
	}

	public String getDisplayLanguageId() {
		return _displayLanguageId;
	}

	public Date getFirstVisitDate() {
		if (_firstVisitDate == null) {
			return null;
		}

		return new Date(_firstVisitDate.getTime());
	}

	public Date getLastVisitDate() {
		if (_lastVisitDate == null) {
			return null;
		}

		return new Date(_lastVisitDate.getTime());
	}

	public String getUrl() {
		return _url;
	}

	public Long getVisits() {
		return _visits;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_firstVisitDate, _displayLanguageId, _lastVisitDate, _url, _visits);
	}

	public void setDisplayLanguageId(String displayLanguageId) {
		_displayLanguageId = displayLanguageId;
	}

	public void setFirstVisitDate(Date firstVisitDate) {
		if (firstVisitDate != null) {
			_firstVisitDate = new Date(firstVisitDate.getTime());
		}
	}

	public void setLastVisitDate(Date lastVisitDate) {
		if (lastVisitDate != null) {
			_lastVisitDate = new Date(lastVisitDate.getTime());
		}
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setVisits(Long visits) {
		_visits = visits;
	}

	private String _displayLanguageId;
	private Date _firstVisitDate;
	private Date _lastVisitDate;
	private String _url;
	private Long _visits;

}