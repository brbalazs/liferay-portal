/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.entity.AssetKeyword;

/**
 * @author Marcellus Tavares
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetKeywordDTO {

	public AssetKeywordDTO(AssetKeyword assetKeyword) {
		_assetKeyword = assetKeyword;
	}

	@JsonProperty("type")
	public String getType() {
		return _assetKeyword.getType();
	}

	@JsonProperty("keyword")
	public String getValue() {
		return _assetKeyword.getKeyword();
	}

	private final AssetKeyword _assetKeyword;

}