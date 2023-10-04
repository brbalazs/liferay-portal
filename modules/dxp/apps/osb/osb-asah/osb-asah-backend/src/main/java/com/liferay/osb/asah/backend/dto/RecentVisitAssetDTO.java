/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.model.RecentVisitAsset;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.List;
import java.util.Set;

/**
 * @author Leslie Wong
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("recent-assets")
public class RecentVisitAssetDTO extends RecentVisitDTO {

	public RecentVisitAssetDTO() {
	}

	public RecentVisitAssetDTO(List<RecentVisitAsset> recentVisitAssets) {
		_recentVisitAssetDTOS = SetUtil.map(
			recentVisitAssets, RecentVisitAssetDTO::new);
	}

	public RecentVisitAssetDTO(RecentVisitAsset recentVisitAsset) {
		super(recentVisitAsset);

		_assetId = recentVisitAsset.getAssetId();
		_assetTitle = recentVisitAsset.getAssetTitle();

		RecentVisitAsset.ContentType contentType =
			recentVisitAsset.getContentType();

		_contentType = contentType.getValue();

		_url = recentVisitAsset.getUrl();
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

	@JsonProperty("recent-assets")
	public Set<RecentVisitAssetDTO> getRecentVisitAssetDTOs() {
		return _recentVisitAssetDTOS;
	}

	@JsonProperty("url")
	public String getUrl() {
		return _url;
	}

	private String _assetId;
	private String _assetTitle;
	private String _contentType;
	private Set<RecentVisitAssetDTO> _recentVisitAssetDTOS;
	private String _url;

}