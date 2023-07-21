/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.publisher.web.util;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;

/**
 * Provides utility methods to be used from Asset Publisher display templates.
 * This class is injected in the context of Asset Publisher display templates.
 *
 * @author     Juan Fernández
 * @deprecated As of Judson (7.1.x), replaced by {@link
 *             com.liferay.asset.publisher.util.AssetPublisherHelper}
 */
@Deprecated
public class AssetPublisherHelper {

	public static String getAssetViewURL(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, AssetEntry assetEntry) {

		return _assetPublisherHelper.getAssetViewURL(
			liferayPortletRequest, liferayPortletResponse, assetEntry, false);
	}

	public static String getAssetViewURL(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, AssetEntry assetEntry,
		boolean viewInContext) {

		return _assetPublisherHelper.getAssetViewURL(
			liferayPortletRequest, liferayPortletResponse, assetEntry,
			viewInContext);
	}

	public static String getAssetViewURL(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		AssetRenderer<?> assetRenderer, AssetEntry assetEntry,
		boolean viewInContext) {

		return _assetPublisherHelper.getAssetViewURL(
			liferayPortletRequest, liferayPortletResponse, assetRenderer,
			assetEntry, viewInContext);
	}

	private static final com.liferay.asset.publisher.util.AssetPublisherHelper
		_assetPublisherHelper =
			AssetPublisherHelperUtil.getAssetPublisherHelper();

}