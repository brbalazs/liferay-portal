/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.pwd;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.pwd.Toolkit;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.security.pwd.bundle.pwdtoolkitutil.TestToolkit;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.util.test.AtomicState;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class PwdToolkitUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.pwdtoolkitutil"));

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();
	}

	@Test
	public void testGenerated() {
		Assert.assertEquals(
			TestToolkit.PASSWORD, PwdToolkitUtil.generate(null));
	}

	@Test
	public void testInstance() {
		Toolkit toolkit = PwdToolkitUtil.getToolkit();

		Class<?> clazz = toolkit.getClass();

		Assert.assertEquals(TestToolkit.class.getName(), clazz.getName());
	}

	@Test
	public void testValidate() throws PortalException {
		_atomicState.reset();

		PwdToolkitUtil.validate(1, 1, "passwd", "passwd", null);

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;

}