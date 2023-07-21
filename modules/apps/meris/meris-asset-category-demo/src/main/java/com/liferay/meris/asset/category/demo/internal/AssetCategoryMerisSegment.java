/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris.asset.category.demo.internal;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.meris.MerisSegment;

import java.util.Locale;

/**
 * @author Eduardo García
 */
public class AssetCategoryMerisSegment
	implements Comparable<AssetCategoryMerisSegment>, MerisSegment {

	public AssetCategoryMerisSegment(AssetCategory assetCategory) {
		_assetCategory = assetCategory;
	}

	@Override
	public int compareTo(AssetCategoryMerisSegment assetCategoryMerisSegment) {
		String merisSegmentId = getMerisSegmentId();

		return merisSegmentId.compareTo(
			assetCategoryMerisSegment.getMerisSegmentId());
	}

	public long getAssetCategoryId() {
		return _assetCategory.getCategoryId();
	}

	@Override
	public String getDescription(Locale locale) {
		return _assetCategory.getDescription(locale);
	}

	@Override
	public String getMerisSegmentId() {
		return String.valueOf(_assetCategory.getCategoryId());
	}

	@Override
	public String getName(Locale locale) {
		return _assetCategory.getTitle(locale);
	}

	@Override
	public String getScopeId() {
		return String.valueOf(_assetCategory.getGroupId());
	}

	private final AssetCategory _assetCategory;

}