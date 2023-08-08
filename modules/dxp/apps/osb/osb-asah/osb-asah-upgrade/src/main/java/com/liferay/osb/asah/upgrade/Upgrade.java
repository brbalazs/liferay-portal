/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@Profile("!test")
public class Upgrade implements CommandLineRunner {

	@Override
	public void run(String... args) {
		if (_upgradeProcessRunner != null) {
			_upgradeProcessRunner.run();
		}

		if (_verifyProcessRunner != null) {
			_verifyProcessRunner.run();
		}
	}

	@Autowired(required = false)
	private UpgradeProcessRunner _upgradeProcessRunner;

	@Autowired(required = false)
	private VerifyProcessRunner _verifyProcessRunner;

}