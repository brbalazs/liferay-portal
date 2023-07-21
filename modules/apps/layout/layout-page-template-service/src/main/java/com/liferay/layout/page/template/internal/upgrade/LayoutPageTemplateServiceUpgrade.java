/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.internal.upgrade;

import com.liferay.layout.page.template.internal.upgrade.v1_1_0.UpgradeLayoutPrototype;
import com.liferay.layout.page.template.internal.upgrade.v1_1_1.UpgradeLayoutPageTemplateEntry;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutPrototypeLocalService;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class LayoutPageTemplateServiceUpgrade
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.1", "1.0.0", new DummyUpgradeStep());

		registry.register(
			"1.0.0", "1.1.0",
			new UpgradeLayoutPrototype(
				_companyLocalService, _layoutPrototypeLocalService));

		registry.register(
			"1.1.0", "1.1.1",
			new UpgradeLayoutPageTemplateEntry(_companyLocalService));
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LayoutPrototypeLocalService _layoutPrototypeLocalService;

}