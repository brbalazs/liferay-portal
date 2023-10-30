/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_4_0;

import com.liferay.osb.asah.common.bigquery.BigQuerySchemaManager;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class AssetEntitySchemaUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) throws Exception {
		_bigQuerySchemaManager.createTable(
			ProjectIdThreadLocal.getProjectId(), "assetentity");
		_bigQuerySchemaManager.createTable(
			ProjectIdThreadLocal.getProjectId(), "assetentity_raw");

		if (_log.isInfoEnabled()) {
			_log.info("Asset Entity tables successfully created");
		}
	}

	private static final Log _log = LogFactory.getLog(
		AssetEntitySchemaUpgradeStep.class);

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

}