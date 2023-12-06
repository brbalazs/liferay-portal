/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author Leslie Wong
 */
public class RecentVisitPage extends RecentVisit {

	public RecentVisitPage() {
	}

	public RecentVisitPage(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RecentVisitPage)) {
			return false;
		}

		RecentVisitPage recentVisitPage = (RecentVisitPage)obj;

		if (Objects.equals(dataSourceId, recentVisitPage.dataSourceId) &&
			Objects.equals(firstVisitDate, recentVisitPage.firstVisitDate) &&
			Objects.equals(groupId, recentVisitPage.groupId) &&
			Objects.equals(lastVisitDate, recentVisitPage.lastVisitDate) &&
			Objects.equals(visits, recentVisitPage.visits) &&
			Objects.equals(
				_displayLanguageId, recentVisitPage._displayLanguageId) &&
			Objects.equals(_title, recentVisitPage._title) &&
			Objects.equals(_url, recentVisitPage._url)) {

			return true;
		}

		return false;
	}

	public String getDisplayLanguageId() {
		return _displayLanguageId;
	}

	public String getTitle() {
		return _title;
	}

	public String getURL() {
		return _url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			dataSourceId, firstVisitDate, groupId, lastVisitDate, visits,
			_displayLanguageId, _title, _url);
	}

	public void setDisplayLanguageId(String displayLanguageId) {
		_displayLanguageId = displayLanguageId;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setUrl(String url) {
		_url = url;
	}

	private String _displayLanguageId;
	private String _title;
	private String _url;

}