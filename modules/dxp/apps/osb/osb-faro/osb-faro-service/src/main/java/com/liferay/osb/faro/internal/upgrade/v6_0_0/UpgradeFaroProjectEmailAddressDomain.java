/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.internal.upgrade.v6_0_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Geyson Silva
 */
public class UpgradeFaroProjectEmailAddressDomain extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		runSQL(
			StringBundler.concat(
				"create table OSBFaro_FaroProjectEmailAddressDomain ",
				"(faroProjectEmailAddressDomainId LONG not null primary key, ",
				"groupId LONG, faroProjectId LONG, emailAddressDomain ",
				"VARCHAR(255) null)"));
	}

}