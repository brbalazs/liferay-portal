/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.publisher.web.internal.util;

import com.liferay.asset.kernel.service.persistence.AssetEntryQuery;
import com.liferay.asset.util.AssetEntryQueryProcessor;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pavel Savinov
 */
public interface AssetPublisherCustomizer {

	public Integer getDelta(HttpServletRequest request);

	public String getPortletId();

	public boolean isEnablePermissions(HttpServletRequest request);

	public boolean isOrderingAndGroupingEnabled(HttpServletRequest request);

	public boolean isOrderingByTitleEnabled(HttpServletRequest request);

	public boolean isSelectionStyleEnabled(HttpServletRequest request);

	public boolean isShowAssetEntryQueryProcessor(
		AssetEntryQueryProcessor assetEntryQueryProcessor);

	public boolean isShowEnableAddContentButton(HttpServletRequest request);

	public boolean isShowEnableRelatedAssets(HttpServletRequest request);

	public boolean isShowScopeSelector(HttpServletRequest request);

	public boolean isShowSubtypeFieldsFilter(HttpServletRequest request);

	public void setAssetEntryQueryOptions(
		AssetEntryQuery assetEntryQuery, HttpServletRequest request);

}