/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade;

import com.liferay.osb.asah.upgrade.v4_2_0.BQMembershipUpgradeStep;
import com.liferay.osb.asah.upgrade.v4_2_0.IndividualUpgradeStep;
import com.liferay.osb.asah.upgrade.v4_2_0.PageUpgradeStep;
import com.liferay.osb.asah.upgrade.v4_2_0.PostgreSQLUpgradeStep;
import com.liferay.osb.asah.upgrade.v4_2_0.SuppressionUpgradeStep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcellus Tavares
 */
@Configuration
public class UpgradeProcessConfiguration {

	@Bean
	public UpgradeProcess upgradeProcess() {
		UpgradeProcess upgradeProcess = new UpgradeProcess();

		upgradeProcess.addUpgradeSteps(
			"4.0.12", "4.0.13", _bqMembershipUpgradeStep);
		upgradeProcess.addUpgradeSteps(
			"4.0.13", "4.0.14", _individualUpgradeStep);
		upgradeProcess.addUpgradeSteps("4.0.14", "4.0.15", _pageUpgradeStep);
		upgradeProcess.addUpgradeSteps(
			"4.0.15", "4.0.16", _postgreSQLUpgradeStep);
		upgradeProcess.addUpgradeSteps(
			"4.0.16", "4.0.17", _suppressionUpgradeStep);

		return upgradeProcess;
	}

	@Autowired
	private BQMembershipUpgradeStep _bqMembershipUpgradeStep;

	@Autowired
	private IndividualUpgradeStep _individualUpgradeStep;

	@Autowired
	private PageUpgradeStep _pageUpgradeStep;

	@Autowired
	private PostgreSQLUpgradeStep _postgreSQLUpgradeStep;

	@Autowired
	private SuppressionUpgradeStep _suppressionUpgradeStep;

}