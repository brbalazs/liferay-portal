/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.test.rule.callback;

import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class BaseTestCallback<C, M> implements TestCallback<C, M> {

	@Override
	public void afterClass(Description description, C c) throws Throwable {
	}

	@Override
	public void afterMethod(Description description, M m, Object target)
		throws Throwable {
	}

	@Override
	public C beforeClass(Description description) throws Throwable {
		return null;
	}

	@Override
	public M beforeMethod(Description description, Object target)
		throws Throwable {

		return null;
	}

}