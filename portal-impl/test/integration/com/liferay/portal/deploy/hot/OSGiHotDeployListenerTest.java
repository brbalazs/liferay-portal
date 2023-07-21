/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.deploy.hot;

import com.liferay.portal.kernel.deploy.hot.DependencyManagementThreadLocal;
import com.liferay.portal.kernel.deploy.hot.HotDeployEvent;
import com.liferay.portal.kernel.deploy.hot.HotDeployException;
import com.liferay.portal.kernel.deploy.hot.HotDeployListener;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.util.test.AtomicState;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Adolfo Pérez
 */
public class OSGiHotDeployListenerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.osgihotdeploylistener"));

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();
	}

	@Before
	public void setUp() {
		_dependencyManagerEnabled = DependencyManagementThreadLocal.isEnabled();

		DependencyManagementThreadLocal.setEnabled(false);
	}

	@After
	public void tearDown() {
		DependencyManagementThreadLocal.setEnabled(_dependencyManagerEnabled);
	}

	@Test
	public void testInvokeDeploy() throws HotDeployException {
		_hotDeployListener.invokeDeploy(
			new HotDeployEvent(_servletContext, null));

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testInvokeUndeploy() throws HotDeployException {
		_hotDeployListener.invokeUndeploy(
			new HotDeployEvent(_servletContext, null));

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;

	private boolean _dependencyManagerEnabled;
	private final HotDeployListener _hotDeployListener =
		new OSGiHotDeployListener();
	private final ServletContext _servletContext =
		ProxyFactory.newDummyInstance(ServletContext.class);

}