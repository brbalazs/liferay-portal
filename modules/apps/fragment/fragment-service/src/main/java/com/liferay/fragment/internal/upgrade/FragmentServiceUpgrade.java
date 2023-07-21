/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.internal.upgrade;

import com.liferay.fragment.internal.upgrade.v1_0_1.UpgradeFragmentEntry;
import com.liferay.fragment.internal.upgrade.v1_0_2.UpgradePortletPreferences;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

/**
 * @author José Ángel Jiménez
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class FragmentServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("1.0.0", "1.0.1", new UpgradeFragmentEntry());

		registry.register("1.0.1", "1.0.2", new UpgradePortletPreferences());
	}

}