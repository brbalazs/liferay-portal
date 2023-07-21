/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.publisher.web.internal.util;

import com.liferay.asset.kernel.service.persistence.AssetEntryQuery;
import com.liferay.asset.publisher.constants.AssetPublisherPortletKeys;
import com.liferay.asset.util.AssetEntryQueryProcessor;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pavel Savinov
 */
@Component(
	configurationPid = "com.liferay.asset.publisher.web.internal.configuration.AssetPublisherWebConfiguration",
	immediate = true, service = AssetPublisherCustomizer.class
)
public class HighestRatedAssetPublisherCustomizer
	extends DefaultAssetPublisherCustomizer {

	@Override
	public String getPortletId() {
		return AssetPublisherPortletKeys.HIGHEST_RATED_ASSETS;
	}

	@Override
	public boolean isEnablePermissions(HttpServletRequest request) {
		if (!assetPublisherWebConfiguration.permissionCheckingConfigurable()) {
			return true;
		}

		PortletPreferences portletPreferences = getPortletPreferences(request);

		return GetterUtil.getBoolean(
			portletPreferences.getValue("enablePermissions", null), true);
	}

	@Override
	public boolean isOrderingAndGroupingEnabled(HttpServletRequest request) {
		return false;
	}

	@Override
	public boolean isOrderingByTitleEnabled(HttpServletRequest request) {
		return true;
	}

	@Override
	public boolean isSelectionStyleEnabled(HttpServletRequest request) {
		return false;
	}

	@Override
	public boolean isShowAssetEntryQueryProcessor(
		AssetEntryQueryProcessor assetEntryQueryProcessor) {

		return true;
	}

	@Override
	public boolean isShowEnableAddContentButton(HttpServletRequest request) {
		return false;
	}

	@Override
	public boolean isShowEnableRelatedAssets(HttpServletRequest request) {
		return true;
	}

	@Override
	public boolean isShowScopeSelector(HttpServletRequest request) {
		return true;
	}

	@Override
	public boolean isShowSubtypeFieldsFilter(HttpServletRequest request) {
		if (!assetPublisherWebConfiguration.searchWithIndex()) {
			return false;
		}

		return true;
	}

	@Override
	public void setAssetEntryQueryOptions(
		AssetEntryQuery assetEntryQuery, HttpServletRequest request) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletPreferences portletPreferences = getPortletPreferences(request);

		long[] groupIds = assetPublisherHelper.getGroupIds(
			portletPreferences, themeDisplay.getScopeGroupId(),
			themeDisplay.getLayout());

		assetEntryQuery.setGroupIds(groupIds);
	}

}