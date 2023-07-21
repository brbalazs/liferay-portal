/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule.callback;

import com.liferay.portal.kernel.test.rule.callback.TestCallback;

import org.junit.runner.Description;

/**
 * @author Preston Crary
 */
public class InjectTestCallback
	implements TestCallback<InjectTestBag, InjectTestBag> {

	public static final InjectTestCallback INSTANCE = new InjectTestCallback();

	@Override
	public void afterClass(Description description, InjectTestBag injectTestBag)
		throws Exception {

		injectTestBag.resetFields();
	}

	@Override
	public void afterMethod(
			Description description, InjectTestBag injectTestBag, Object target)
		throws Exception {

		injectTestBag.resetFields();
	}

	@Override
	public InjectTestBag beforeClass(Description description) throws Exception {
		InjectTestBag injectTestBag = new InjectTestBag(
			description.getTestClass());

		injectTestBag.injectFields();

		return injectTestBag;
	}

	@Override
	public InjectTestBag beforeMethod(Description description, Object target)
		throws Exception {

		InjectTestBag injectTestBag = new InjectTestBag(
			description.getTestClass(), target);

		injectTestBag.injectFields();

		return injectTestBag;
	}

}