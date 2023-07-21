/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v7_0_6;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Michael Bowerman
 */
public class UpgradeThemeId extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		for (String[] themeIds : _THEME_IDS) {
			String oldThemeId = themeIds[0];
			String newThemeId = themeIds[1];

			for (String tableName : _TABLE_NAMES) {
				runSQL(
					StringBundler.concat(
						"update ", tableName, " set themeId = '", newThemeId,
						"' where themeId = '", oldThemeId, "'"));
			}
		}
	}

	private static final String[] _TABLE_NAMES = {
		"Layout", "LayoutRevision", "LayoutSet", "LayoutSetBranch"
	};

	private static final String[][] _THEME_IDS = {
		{"classic", "classic_WAR_classictheme"},
		{"controlpanel", "admin_WAR_admintheme"}
	};

}