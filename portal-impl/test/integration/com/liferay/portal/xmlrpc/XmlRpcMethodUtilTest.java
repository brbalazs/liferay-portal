/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.xmlrpc;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.xmlrpc.Method;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.xmlrpc.bundle.xmlrpcmethodutil.TestMethod;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class XmlRpcMethodUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.xmlrpcmethodutil"));

	@Test
	public void testNoReturn() {
		Method method = XmlRpcMethodUtil.getMethod(
			TestMethod.TOKEN, TestMethod.METHOD_NAME);

		Class<?> clazz = method.getClass();

		Assert.assertEquals(TestMethod.class.getName(), clazz.getName());
	}

}