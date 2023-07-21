/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule;

import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.kernel.test.rule.callback.SynchronousDestinationTestCallback.SyncHandler;
import com.liferay.portal.test.rule.callback.SynchronousMailTestCallback;

/**
 * @author Manuel de la Peña
 * @author Roberto Díaz
 * @author Shuyang Zhou
 */
public class SynchronousMailTestRule
	extends BaseTestRule<SyncHandler, SyncHandler> {

	public static final SynchronousMailTestRule INSTANCE =
		new SynchronousMailTestRule();

	private SynchronousMailTestRule() {
		super(SynchronousMailTestCallback.INSTANCE);
	}

}