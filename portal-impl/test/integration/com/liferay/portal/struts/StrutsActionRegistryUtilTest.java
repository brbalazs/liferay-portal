/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.struts;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.util.test.AtomicState;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class StrutsActionRegistryUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.strutsactionregistryutil"));

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();
	}

	@Test
	public void testGetActions() throws Exception {
		Map<String, ?> actions = StrutsActionRegistryUtil.getActions();

		ActionAdapter actionAdapter = (ActionAdapter)actions.get(
			"TestStrutsAction");

		Assert.assertNotNull(actionAdapter);

		_atomicState.reset();

		actionAdapter.execute(null, null, null, null);

		Assert.assertTrue(_atomicState.isSet());

		PortletActionAdapter portletActionAdapter =
			(PortletActionAdapter)actions.get("TestStrutsPortletAction");

		Assert.assertNotNull(portletActionAdapter);

		_atomicState.reset();

		portletActionAdapter.isCheckMethodOnProcessAction();

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testGetGetAction() throws Exception {
		ActionAdapter actionAdapter =
			(ActionAdapter)StrutsActionRegistryUtil.getAction(
				"TestStrutsAction");

		Assert.assertNotNull(actionAdapter);

		_atomicState.reset();

		actionAdapter.execute(null, null, null, null);

		Assert.assertTrue(_atomicState.isSet());

		PortletActionAdapter portletActionAdapter =
			(PortletActionAdapter)StrutsActionRegistryUtil.getAction(
				"TestStrutsPortletAction");

		Assert.assertNotNull(portletActionAdapter);

		_atomicState.reset();

		portletActionAdapter.isCheckMethodOnProcessAction();

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;

}