/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;

/**
 * @author Inácio Nery
 */
public class AssetIdTest extends BaseBeanTestCase<AssetId> {

	@Override
	protected AssetId newInstance() {
		return AssetId.of(null, null);
	}

}