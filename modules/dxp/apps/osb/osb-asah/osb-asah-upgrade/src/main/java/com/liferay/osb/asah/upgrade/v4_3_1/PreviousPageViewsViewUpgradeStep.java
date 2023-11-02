/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_1;

import com.liferay.osb.asah.common.bigquery.BigQuerySchemaManager;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
public class PreviousPageViewsViewUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) throws Exception {
		_bigQuerySchemaManager.createOrReplaceView(
			ProjectIdThreadLocal.getProjectId(), "previouspageviews");

		if (_log.isInfoEnabled()) {
			_log.info("Previous Page Views view successfully created");
		}
	}

	private static final Log _log = LogFactory.getLog(
		PreviousPageViewsViewUpgradeStep.class);

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

}