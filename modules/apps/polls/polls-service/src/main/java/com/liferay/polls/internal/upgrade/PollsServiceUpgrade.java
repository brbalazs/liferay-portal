/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.polls.internal.upgrade;

import com.liferay.polls.internal.upgrade.v1_0_0.UpgradeKernelPackage;
import com.liferay.polls.internal.upgrade.v1_0_0.UpgradeLastPublishDate;
import com.liferay.polls.internal.upgrade.v1_0_0.UpgradePortletId;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Miguel Pastor
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class PollsServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"0.0.1", "0.0.2", new UpgradeKernelPackage(),
			new UpgradePortletId());

		registry.register("0.0.2", "1.0.0", new UpgradeLastPublishDate());

		registry.register("1.0.0", "1.0.4", new DummyUpgradeStep());

		registry.register(
			"1.0.3", "1.0.4",
			new com.liferay.polls.internal.upgrade.v1_0_4.UpgradePortletId());
	}

}