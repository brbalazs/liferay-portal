/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.type.controller.asset.display.internal.display.context;

import com.liferay.asset.display.contributor.AssetDisplayContributor;
import com.liferay.asset.display.contributor.constants.AssetDisplayWebKeys;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class AssetDisplayLayoutTypeControllerDisplayContext {

	public AssetDisplayLayoutTypeControllerDisplayContext(
		HttpServletRequest request) {

		_request = request;
	}

	public Map<String, Object> getAssetDisplayFieldsValues()
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		AssetDisplayContributor assetDisplayContributor =
			(AssetDisplayContributor)_request.getAttribute(
				AssetDisplayWebKeys.ASSET_DISPLAY_CONTRIBUTOR);

		AssetEntry assetEntry = (AssetEntry)_request.getAttribute(
			AssetDisplayWebKeys.ASSET_ENTRY);

		return assetDisplayContributor.getAssetDisplayFieldsValues(
			assetEntry, themeDisplay.getLocale());
	}

	private final HttpServletRequest _request;

}