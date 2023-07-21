/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.display.internal.contributor;

import com.liferay.asset.display.contributor.AssetDisplayContributorField;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(
	property = "model.class.name=com.liferay.asset.kernel.model.AssetEntry",
	service = AssetDisplayContributorField.class
)
public class AssetEntrySummaryAssetDisplayContributorField
	implements AssetDisplayContributorField<AssetEntry> {

	@Override
	public String getKey() {
		return "summary";
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			locale, getClass());

		return LanguageUtil.get(resourceBundle, "summary");
	}

	@Override
	public String getType() {
		return "text";
	}

	@Override
	public String getValue(AssetEntry assetEntry, Locale locale) {
		return assetEntry.getSummary(locale);
	}

}