/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.RecentPage;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Leslie Wong
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("recent-pages")
public class RecentPageDTO {

	public RecentPageDTO() {
	}

	public RecentPageDTO(List<RecentPage> recentPages) {
		_recentPageDTOs = SetUtil.map(recentPages, RecentPageDTO::new);
	}

	public RecentPageDTO(RecentPage recentPage) {
		_counts = recentPage.getCounts();
		_createDate = recentPage.getCreateDate();
		_displayLanguageId = recentPage.getDisplayLanguageId();
		_lastModifiedDate = recentPage.getLastModifiedDate();
		_url = recentPage.getUrl();
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

	@JsonProperty("url")
	public String getKeywords() {
		return _url;
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

	@JsonProperty("recent-pages")
	public Set<RecentPageDTO> getRecentPageDTOs() {
		return _recentPageDTOs;
	}

	private Long _counts;
	private Date _createDate;
	private String _displayLanguageId;
	private Date _lastModifiedDate;
	private Set<RecentPageDTO> _recentPageDTOs;
	private String _url;

}