/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade;

import com.liferay.osb.asah.common.util.ReleaseInfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Marcellus Tavares
 */
public class UpgradeProcessTest {

	@Test
	public void testMaxVersionString1() {
		UpgradeProcess upgradeProcess = new UpgradeProcess();

		upgradeProcess.addUpgradeSteps("0", "1");
		upgradeProcess.addUpgradeSteps("1", "2.1");

		Assertions.assertEquals("2.1", upgradeProcess.getMaxVersionString());
	}

	@Test
	public void testMaxVersionString2() {
		UpgradeProcess upgradeProcess = new UpgradeProcess();

		upgradeProcess.addUpgradeSteps("0", "1");
		upgradeProcess.addUpgradeSteps("1", "2.1.3");
		upgradeProcess.addUpgradeSteps("2.1.0", "2.1.1");
		upgradeProcess.addUpgradeSteps("2.1.1", "2.1.2");

		Assertions.assertEquals("2.1.3", upgradeProcess.getMaxVersionString());
	}

	@Test
	public void testMaxVersionString3() {
		UpgradeProcess upgradeProcess = new UpgradeProcess();

		String version = ReleaseInfo.getVersion();

		version = version.substring(0, version.length() - 1);

		version += "99";

		upgradeProcess.addUpgradeSteps("0", "1");
		upgradeProcess.addUpgradeSteps("1", version);

		Assertions.assertEquals("1", upgradeProcess.getMaxVersionString());
	}

}