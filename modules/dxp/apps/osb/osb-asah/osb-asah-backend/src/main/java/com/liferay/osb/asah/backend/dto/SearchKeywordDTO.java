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
import com.liferay.osb.asah.common.model.SearchKeyword;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Rachael Koestartyo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("search-keywords")
public class SearchKeywordDTO {

	public SearchKeywordDTO() {
	}

	public SearchKeywordDTO(List<SearchKeyword> searchKeywords) {
		_searchKeywordDTOs = SetUtil.map(searchKeywords, SearchKeywordDTO::new);
	}

	public SearchKeywordDTO(SearchKeyword searchKeyword) {
		_counts = searchKeyword.getCounts();
		_createDate = searchKeyword.getCreateDate();
		_displayLanguageId = searchKeyword.getDisplayLanguageId();
		_groupId = searchKeyword.getGroupId();
		_keywords = searchKeyword.getKeywords();
		_lastModifiedDate = searchKeyword.getLastModifiedDate();
	}

	@JsonProperty("counts")
	public Long getCounts() {
		return _counts;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("createDate")
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@JsonProperty("displayLanguageId")
	public String getDisplayLanguageId() {
		return _displayLanguageId;
	}

	@JsonProperty("groupId")
	public String getGroupId() {
		return _groupId;
	}

	@JsonProperty("keywords")
	public String getKeywords() {
		return _keywords;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("lastModifiedDate")
	public Date getLastModifiedDate() {
		if (_lastModifiedDate == null) {
			return null;
		}

		return new Date(_lastModifiedDate.getTime());
	}

	@JsonProperty("search-keywords")
	public Set<SearchKeywordDTO> getSearchKeywordDTOs() {
		return _searchKeywordDTOs;
	}

	private Long _counts;
	private Date _createDate;
	private String _displayLanguageId;
	private String _groupId;
	private String _keywords;
	private Date _lastModifiedDate;
	private Set<SearchKeywordDTO> _searchKeywordDTOs;

}