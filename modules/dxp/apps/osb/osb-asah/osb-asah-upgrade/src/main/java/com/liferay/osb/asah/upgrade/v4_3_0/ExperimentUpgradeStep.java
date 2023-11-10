/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_0;

import com.liferay.osb.asah.upgrade.UpgradeStep;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class ExperimentUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) {
		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(
				new ClassPathResource("v4_3_0/experiment.sql")),
			_dataSource);

		if (_log.isInfoEnabled()) {
			_log.info("Experiment has successfully upgraded");
		}
	}

	private static final Log _log = LogFactory.getLog(
		ExperimentUpgradeStep.class);

	@Autowired
	private DataSource _dataSource;

}