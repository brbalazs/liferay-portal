/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris.asset.category.demo.internal;

import com.liferay.meris.MerisProfile;
import com.liferay.portal.kernel.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eduardo García
 */
public class AssetCategoryMerisProfile
	implements Comparable<AssetCategoryMerisProfile>, MerisProfile {

	public AssetCategoryMerisProfile(User user, long[] assetCategoryIds) {
		_user = user;

		_attributes.put("assetCategoryIds", assetCategoryIds);
	}

	@Override
	public int compareTo(AssetCategoryMerisProfile assetCategoryMerisProfile) {
		String merisProfileId = getMerisProfileId();

		return merisProfileId.compareTo(
			assetCategoryMerisProfile.getMerisProfileId());
	}

	@Override
	public Object getAttribute(String key) {
		return _attributes.get(key);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return _attributes;
	}

	@Override
	public String getMerisProfileId() {
		return String.valueOf(_user.getUserId());
	}

	private final Map<String, Object> _attributes = new HashMap();
	private final User _user;

}