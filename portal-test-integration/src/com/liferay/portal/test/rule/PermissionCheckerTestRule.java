/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule;

import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.test.rule.callback.PermissionCheckerTestCallback;

/**
 * @author Tom Wang
 */
public class PermissionCheckerTestRule extends BaseTestRule<Void, Void> {

	public static final PermissionCheckerTestRule INSTANCE =
		new PermissionCheckerTestRule();

	private PermissionCheckerTestRule() {
		super(PermissionCheckerTestCallback.INSTANCE);
	}

}