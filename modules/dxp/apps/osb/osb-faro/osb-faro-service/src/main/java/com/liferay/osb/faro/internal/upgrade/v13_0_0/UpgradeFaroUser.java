/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.internal.upgrade.v13_0_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Matthew Kong
 */
public class UpgradeFaroUser extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		runSQL(
			"create index IX_1B6F355D on OSBFaro_FaroUser (groupId, roleId)");
	}

}