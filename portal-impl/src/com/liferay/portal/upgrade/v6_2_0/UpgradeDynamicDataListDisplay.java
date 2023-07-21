/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.upgrade.RenameUpgradePortletPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class UpgradeDynamicDataListDisplay
	extends RenameUpgradePortletPreferences {

	public UpgradeDynamicDataListDisplay() {
		_preferenceNamesMap.put("detailDDMTemplateId", "formDDMTemplateId");
		_preferenceNamesMap.put("listDDMTemplateId", "displayDDMTemplateId");
	}

	@Override
	protected String[] getPortletIds() {
		return new String[] {"169_INSTANCE_%"};
	}

	@Override
	protected Map<String, String> getPreferenceNamesMap() {
		return _preferenceNamesMap;
	}

	private final Map<String, String> _preferenceNamesMap = new HashMap<>();

}