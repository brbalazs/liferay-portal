/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.util.ParallelUpgradeSchemaUtil;

/**
 * @author Raymond Augé
 */
public class UpgradeSchema extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		ParallelUpgradeSchemaUtil.execute(
			"update-6.1.1-6.2.0.sql", "update-6.1.1-6.2.0-dl.sql",
			"update-6.1.1-6.2.0-expando.sql", "update-6.1.1-6.2.0-group.sql",
			"update-6.1.1-6.2.0-journal.sql", "update-6.1.1-6.2.0-user.sql",
			"update-6.1.1-6.2.0-wiki.sql");

		upgrade(new UpgradeMVCCVersion());
	}

}