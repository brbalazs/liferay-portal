/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.internal.upgrade.v9_0_0;

import com.liferay.osb.faro.model.impl.FaroProjectModelImpl;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Geyson Silva
 */
public class UpgradeFaroProject extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		alter(
			FaroProjectModelImpl.class,
			new AlterTableAddColumn("timeZoneId VARCHAR(75)"));

		runSQL("update OSBFaro_FaroProject set timeZoneId = 'UTC'");
	}

}