/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_1_0;

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
public class PageReferrersViewUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) throws Exception {
		_bigQuerySchemaManager.createOrReplaceView(
			ProjectIdThreadLocal.getProjectId(), "pagereferrers");

		if (_log.isInfoEnabled()) {
			_log.info("Page Referrers view successfully updated");
		}
	}

	private static final Log _log = LogFactory.getLog(
		PageReferrersViewUpgradeStep.class);

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

}