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
import com.liferay.osb.asah.common.model.RecentAsset;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Leslie Wong
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("recent-assets")
public class RecentAssetDTO {

	public RecentAssetDTO() {
	}

	public RecentAssetDTO(List<RecentAsset> recentAssets) {
		_recentAssetDTOs = SetUtil.map(recentAssets, RecentAssetDTO::new);
	}

	public RecentAssetDTO(RecentAsset recentAsset) {
		_assetId = recentAsset.getAssetId();
		_assetTitle = recentAsset.getAssetTitle();

		RecentAsset.ContentType contentType = recentAsset.getContentType();

		_contentType = contentType.getValue();

		_firstVisitDate = recentAsset.getFirstVisitDate();
		_lastVisitDate = recentAsset.getLastVisitDate();
		_url = recentAsset.getUrl();
		_visits = recentAsset.getVisits();
	}

	@JsonProperty("assetId")
	public String getAssetId() {
		return _assetId;
	}

	@JsonProperty("assetTitle")
	public String getAssetTitle() {
		return _assetTitle;
	}

	@JsonProperty("contentType")
	public String getContentType() {
		return _contentType;
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

	@JsonProperty("recent-assets")
	public Set<RecentAssetDTO> getRecentAssetDTOs() {
		return _recentAssetDTOs;
	}

	@JsonProperty("url")
	public String getUrl() {
		return _url;
	}

	@JsonProperty("visits")
	public Long getVisits() {
		return _visits;
	}

	private String _assetId;
	private String _assetTitle;
	private String _contentType;
	private Date _firstVisitDate;
	private Date _lastVisitDate;
	private Set<RecentAssetDTO> _recentAssetDTOs;
	private String _url;
	private Long _visits;

}