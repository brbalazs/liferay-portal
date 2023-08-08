/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade;

import com.liferay.osb.asah.upgrade.v4_1_0.PageReferrersViewUpgradeStep;
import com.liferay.osb.asah.upgrade.v4_1_0.SegmentUpgradeStep;
import com.liferay.osb.asah.upgrade.v4_1_1.BQMembershipUpgradeStep;
import com.liferay.osb.asah.upgrade.v4_1_1.PostgreSQLUpgradeStep;

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
			"4.0.10", "4.0.11", _pageReferrersViewUpgradeStep);
		upgradeProcess.addUpgradeSteps(
			"4.0.11", "4.0.12", _bqMembershipUpgradeStep);
		upgradeProcess.addUpgradeSteps(
			"4.0.12", "4.0.13", _postgreSQLUpgradeStep);
		upgradeProcess.addUpgradeSteps("4.0.13", "4.0.14", _segmentUpgradeStep);

		return upgradeProcess;
	}

	@Autowired
	private BQMembershipUpgradeStep _bqMembershipUpgradeStep;

	@Autowired
	private PageReferrersViewUpgradeStep _pageReferrersViewUpgradeStep;

	@Autowired
	private PostgreSQLUpgradeStep _postgreSQLUpgradeStep;

	@Autowired
	private SegmentUpgradeStep _segmentUpgradeStep;

}