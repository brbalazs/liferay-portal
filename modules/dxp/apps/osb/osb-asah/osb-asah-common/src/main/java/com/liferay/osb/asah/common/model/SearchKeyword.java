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
 * @author Rachael Koestartyo
 */
public class SearchKeyword {

	public SearchKeyword() {
	}

	public SearchKeyword(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SearchKeyword)) {
			return false;
		}

		SearchKeyword searchKeyword = (SearchKeyword)obj;

		if (Objects.equals(_counts, searchKeyword._counts) &&
			Objects.equals(_createDate, searchKeyword._createDate) &&
			Objects.equals(_dataSourceId, searchKeyword._dataSourceId) &&
			Objects.equals(
				_displayLanguageId, searchKeyword._displayLanguageId) &&
			Objects.equals(_groupId, searchKeyword._groupId) &&
			Objects.equals(_keywords, searchKeyword._keywords) &&
			Objects.equals(
				_lastModifiedDate, searchKeyword._lastModifiedDate)) {

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

	public Long getDataSourceId() {
		return _dataSourceId;
	}

	public String getDisplayLanguageId() {
		return _displayLanguageId;
	}

	public String getGroupId() {
		return _groupId;
	}

	public String getKeywords() {
		return _keywords;
	}

	public Date getLastModifiedDate() {
		if (_lastModifiedDate == null) {
			return null;
		}

		return new Date(_lastModifiedDate.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_counts, _createDate, _dataSourceId, _displayLanguageId, _groupId,
			_keywords, _lastModifiedDate);
	}

	public void setCounts(Long counts) {
		_counts = counts;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDisplayLanguageId(String displayLanguageId) {
		_displayLanguageId = displayLanguageId;
	}

	public void setGroupId(String groupId) {
		_groupId = groupId;
	}

	public void setKeywords(String keywords) {
		_keywords = keywords;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		if (lastModifiedDate != null) {
			_lastModifiedDate = new Date(lastModifiedDate.getTime());
		}
	}

	private Long _counts;
	private Date _createDate;
	private Long _dataSourceId;
	private String _displayLanguageId;
	private String _groupId;
	private String _keywords;
	private Date _lastModifiedDate;

}