/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.model.RecentVisitPage;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.List;
import java.util.Set;

/**
 * @author Leslie Wong
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("recent-pages")
public class RecentVisitPageDTO extends RecentVisitDTO {

	public RecentVisitPageDTO() {
	}

	public RecentVisitPageDTO(List<RecentVisitPage> recentVisitPages) {
		_recentVisitPageDTOS = SetUtil.map(
			recentVisitPages, RecentVisitPageDTO::new);
	}

	public RecentVisitPageDTO(RecentVisitPage recentVisitPage) {
		super(recentVisitPage);

		_displayLanguageId = recentVisitPage.getDisplayLanguageId();
		_title = recentVisitPage.getTitle();
		_url = recentVisitPage.getURL();
	}

	@JsonProperty("displayLanguageId")
	public String getDisplayLanguageId() {
		return _displayLanguageId;
	}

	@JsonProperty("recent-pages")
	public Set<RecentVisitPageDTO> getRecentVisitPageDTOs() {
		return _recentVisitPageDTOS;
	}

	@JsonProperty("title")
	public String getTitle() {
		return _title;
	}

	@JsonProperty("url")
	public String getURL() {
		return _url;
	}

	private String _displayLanguageId;
	private Set<RecentVisitPageDTO> _recentVisitPageDTOS;
	private String _title;
	private String _url;

}