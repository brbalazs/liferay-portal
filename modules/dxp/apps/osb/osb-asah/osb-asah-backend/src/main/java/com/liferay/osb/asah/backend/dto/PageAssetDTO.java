/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.entity.Asset;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
@GraphQLType("PageAsset")
public class PageAssetDTO {

	public PageAssetDTO(Asset asset) {
		_asset = asset;
	}

	@JsonProperty("keywords")
	public List<AssetKeywordDTO> getAssetKeywordDTOs() {
		return ListUtil.map(_asset.getAssetKeywords(), AssetKeywordDTO::new);
	}

	@JsonProperty("canonicalUrl")
	public String getCanonicalURL() {
		return _asset.getCanonicalURL();
	}

	public String getDescription() {
		return _asset.getDescription();
	}

	public String getId() {
		return StringUtil.get(_asset.getId(), null);
	}

	@JsonProperty("name")
	public String getTitle() {
		return _asset.getTitle();
	}

	@GraphQLProperty("url")
	public String getURL() {
		return _asset.getURL();
	}

	private final Asset _asset;

}