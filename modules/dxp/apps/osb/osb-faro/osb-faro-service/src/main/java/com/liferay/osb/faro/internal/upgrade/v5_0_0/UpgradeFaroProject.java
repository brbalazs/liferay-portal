/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.internal.upgrade.v5_0_0;

import com.liferay.osb.faro.model.impl.FaroProjectModelImpl;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author André Miranda
 */
public class UpgradeFaroProject extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		alter(
			FaroProjectModelImpl.class,
			new AlterTableAddColumn("ipAddresses STRING"));
	}

}