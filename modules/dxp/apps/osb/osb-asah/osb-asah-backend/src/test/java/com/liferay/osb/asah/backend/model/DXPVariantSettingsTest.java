/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;
import com.liferay.osb.asah.common.model.DXPVariantSettings;

/**
 * @author André Miranda
 */
public class DXPVariantSettingsTest
	extends BaseBeanTestCase<DXPVariantSettings> {

	@Override
	protected DXPVariantSettings newInstance() {
		return new DXPVariantSettings();
	}

}