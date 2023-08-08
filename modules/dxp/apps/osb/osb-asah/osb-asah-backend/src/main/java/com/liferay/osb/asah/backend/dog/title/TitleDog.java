/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.title;

import com.liferay.osb.asah.backend.model.AssetType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lino Alves
 */
@Component
public class TitleDog {

	@Autowired
	public TitleDog() {
	}

	public Map<String, String> getTitle(
		AssetType assetType, Set<String> assetIds) {

		return getTitle(assetIds, assetType, null);
	}

	public Map<String, String> getTitle(
		Set<String> assetIds, AssetType assetType, String keywords) {

		return Collections.emptyMap();
	}

}