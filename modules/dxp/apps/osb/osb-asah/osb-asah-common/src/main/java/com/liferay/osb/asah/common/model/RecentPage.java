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

		if (Objects.equals(_counts, recentPage._counts) &&
			Objects.equals(_createDate, recentPage._createDate) &&
			Objects.equals(_displayLanguageId, recentPage._displayLanguageId) &&
			Objects.equals(_lastModifiedDate, recentPage._lastModifiedDate) &&
			Objects.equals(_url, recentPage._url)) {

			return true;
		}

		return false;
	}

	public Long getCounts() {
		return _counts;
	}

	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public String getDisplayLanguageId() {
		return _displayLanguageId;
	}

	public Date getLastModifiedDate() {
		if (_lastModifiedDate == null) {
			return null;
		}

		return new Date(_lastModifiedDate.getTime());
	}

	public String getUrl() {
		return _url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_counts, _createDate, _displayLanguageId, _lastModifiedDate, _url);
	}

	public void setCounts(Long counts) {
		_counts = counts;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDisplayLanguageId(String displayLanguageId) {
		_displayLanguageId = displayLanguageId;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		if (lastModifiedDate != null) {
			_lastModifiedDate = new Date(lastModifiedDate.getTime());
		}
	}

	public void setUrl(String url) {
		_url = url;
	}

	private Long _counts;
	private Date _createDate;
	private String _displayLanguageId;
	private Date _lastModifiedDate;
	private String _url;

}