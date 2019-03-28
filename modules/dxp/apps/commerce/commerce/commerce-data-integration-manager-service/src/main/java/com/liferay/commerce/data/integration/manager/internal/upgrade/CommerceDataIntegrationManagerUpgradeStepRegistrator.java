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

package com.liferay.commerce.data.integration.manager.internal.upgrade;

import com.liferay.commerce.data.integration.manager.internal.upgrade.v1_1_0.ProcessUpgradeProcess;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Riccardo Ferrari
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class CommerceDataIntegrationManagerUpgradeStepRegistrator
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		if (_log.isInfoEnabled()) {
			_log.info(
				"COMMERCE DATA INTEGRATION MANAGER UPGRADE STEP REGISTRATOR " +
					"STARTED");
		}

		registry.register(
			_SCHEMA_VERSION_1_0_0, _SCHEMA_VERSION_1_1_0,
			new ProcessUpgradeProcess());

		if (_log.isInfoEnabled()) {
			_log.info(
				"COMMERCE DATA INTEGRATION MANAGER UPGRADE STEP REGISTRATOR " +
					"FINISHED");
		}
	}

	private static final String _SCHEMA_VERSION_1_0_0 = "1.0.0";

	private static final String _SCHEMA_VERSION_1_1_0 = "1.1.0";

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceDataIntegrationManagerUpgradeStepRegistrator.class);

}