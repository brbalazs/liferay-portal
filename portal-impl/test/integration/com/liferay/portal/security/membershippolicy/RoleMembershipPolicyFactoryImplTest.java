/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.membershippolicy;

import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyFactory;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyFactoryUtil;
import com.liferay.portal.kernel.security.membershippolicy.RoleMembershipPolicyUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.model.impl.RoleImpl;
import com.liferay.portal.security.membershippolicy.bundle.rolemembershippolicyfactoryimpl.TestRoleMembershipPolicy;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.util.test.AtomicState;

import java.io.Serializable;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class RoleMembershipPolicyFactoryImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.rolemembershippolicyfactoryimpl"));

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();
	}

	@Test
	public void testCheckRoles() throws Exception {
		_atomicState.reset();

		long[] array = {1, 2, 3};

		RoleMembershipPolicyUtil.checkRoles(array, array, array);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testGetRoleMembershipPolicy() {
		RoleMembershipPolicy roleMembershipPolicy =
			RoleMembershipPolicyFactoryUtil.getRoleMembershipPolicy();

		Class<?> clazz = roleMembershipPolicy.getClass();

		Assert.assertEquals(
			TestRoleMembershipPolicy.class.getName(), clazz.getName());
	}

	@Test
	public void testGetRoleMembershipPolicyFactory() {
		RoleMembershipPolicyFactory roleMembershipPolicyFactory =
			RoleMembershipPolicyFactoryUtil.getRoleMembershipPolicyFactory();

		RoleMembershipPolicy roleMembershipPolicy =
			roleMembershipPolicyFactory.getRoleMembershipPolicy();

		Class<?> clazz = roleMembershipPolicy.getClass();

		Assert.assertEquals(
			TestRoleMembershipPolicy.class.getName(), clazz.getName());
	}

	@Test
	public void testIsRoleAllowed() throws Exception {
		Assert.assertTrue(RoleMembershipPolicyUtil.isRoleAllowed(1, 1));
		Assert.assertFalse(RoleMembershipPolicyUtil.isRoleAllowed(2, 2));
	}

	@Test
	public void testIsRoleRequired() throws Exception {
		Assert.assertTrue(RoleMembershipPolicyUtil.isRoleRequired(1, 1));
		Assert.assertFalse(RoleMembershipPolicyUtil.isRoleRequired(2, 2));
	}

	@Test
	public void testPropagateRoles() throws Exception {
		_atomicState.reset();

		long[] array = {1, 2, 3};

		RoleMembershipPolicyUtil.propagateRoles(array, array, array);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testVerifyPolicy1() throws Exception {
		_atomicState.reset();

		RoleMembershipPolicyUtil.verifyPolicy();

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testVerifyPolicy2() throws Exception {
		_atomicState.reset();

		RoleMembershipPolicyUtil.verifyPolicy(new RoleImpl());

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testVerifyPolicy3() throws Exception {
		_atomicState.reset();

		RoleMembershipPolicyUtil.verifyPolicy(
			new RoleImpl(), new RoleImpl(),
			new HashMap<String, Serializable>());

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;

}