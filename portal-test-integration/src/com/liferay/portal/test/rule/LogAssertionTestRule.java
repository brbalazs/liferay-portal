/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule;

import com.liferay.portal.kernel.test.rule.BaseTestRule;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.rule.callback.LogAssertionTestCallback;

import java.util.List;

/**
 * @author Shuyang Zhou
 */
public class LogAssertionTestRule
	extends BaseTestRule<List<CaptureAppender>, List<CaptureAppender>> {

	public static final LogAssertionTestRule INSTANCE =
		new LogAssertionTestRule();

	private LogAssertionTestRule() {
		super(LogAssertionTestCallback.INSTANCE);
	}

}