/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
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
 * @author Marcellus Tavares
 */
@Component
public class AssetUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) {
		_bigQuerySchemaManager.createOrReplaceView(
			ProjectIdThreadLocal.getProjectId(), "asset");

		if (_log.isInfoEnabled()) {
			_log.info("Asset view successfully updated");
		}
	}

	private static final Log _log = LogFactory.getLog(AssetUpgradeStep.class);

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

}