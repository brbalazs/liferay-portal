/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.test.rule;

import com.liferay.portal.kernel.test.rule.callback.SynchronousDestinationTestCallback;
import com.liferay.portal.kernel.test.rule.callback.SynchronousDestinationTestCallback.SyncHandler;

/**
 * @author Miguel Pastor
 * @author Shuyang Zhou
 */
public class SynchronousDestinationTestRule
	extends BaseTestRule<SyncHandler, SyncHandler> {

	public static final SynchronousDestinationTestRule INSTANCE =
		new SynchronousDestinationTestRule();

	private SynchronousDestinationTestRule() {
		super(SynchronousDestinationTestCallback.INSTANCE);
	}

}