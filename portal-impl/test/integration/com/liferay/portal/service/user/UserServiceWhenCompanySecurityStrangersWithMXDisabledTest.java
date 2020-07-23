/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service.user;

import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsUtil;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Brian Wing Shun Chan
 * @author José Manuel Navarro
 * @author Drew Brokke
 */
public class UserServiceWhenCompanySecurityStrangersWithMXDisabledTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test(expected = UserEmailAddressException.MustNotUseCompanyMx.class)
	public void testShouldNotAddUser() throws Exception {
		String name = PrincipalThreadLocal.getName();

		try {
			PropsUtil.set(
				PropsKeys.COMPANY_SECURITY_STRANGERS_WITH_MX,
				Boolean.FALSE.toString());

			PrincipalThreadLocal.setName(0);

			UserTestUtil.addUser(true);
		}
		finally {
			PrincipalThreadLocal.setName(name);
		}
	}

	@Test(expected = UserEmailAddressException.MustNotUseCompanyMx.class)
	public void testShouldNotUpdateEmailAddress() throws Exception {
		String name = PrincipalThreadLocal.getName();

		try {
			PropsUtil.set(
				PropsKeys.COMPANY_SECURITY_STRANGERS_WITH_MX,
				Boolean.FALSE.toString());

			_updateEmailAddress();
		}
		finally {
			PrincipalThreadLocal.setName(name);
		}
	}

	@Test(expected = UserEmailAddressException.MustNotUseCompanyMx.class)
	public void testShouldNotUpdateUser() throws Exception {
		String name = PrincipalThreadLocal.getName();

		User user = UserTestUtil.addUser(false);

		try {
			PropsUtil.set(
				PropsKeys.COMPANY_SECURITY_STRANGERS_WITH_MX,
				Boolean.FALSE.toString());

			_updateUser();
		}
		finally {
			PrincipalThreadLocal.setName(name);

			UserLocalServiceUtil.deleteUser(user);
		}
	}

	@Test
	public void testShouldUpdateEmailAddress() throws Exception {
		String name = PrincipalThreadLocal.getName();

		String companySecurityStrangers = PropsUtil.get(
			PropsKeys.COMPANY_SECURITY_STRANGERS);

		PropsUtil.set(
			PropsKeys.COMPANY_SECURITY_STRANGERS, Boolean.FALSE.toString());

		try {
			_updateEmailAddress();
		}
		finally {
			PrincipalThreadLocal.setName(name);

			PropsUtil.set(
				PropsKeys.COMPANY_SECURITY_STRANGERS, companySecurityStrangers);
		}
	}

	@Test
	public void testShouldUpdateUser() throws Exception {
		String name = PrincipalThreadLocal.getName();

		String companySecurityStrangers = PropsUtil.get(
			PropsKeys.COMPANY_SECURITY_STRANGERS);

		PropsUtil.set(
			PropsKeys.COMPANY_SECURITY_STRANGERS, Boolean.FALSE.toString());

		try {
			_updateUser();
		}
		finally {
			PrincipalThreadLocal.setName(name);

			PropsUtil.set(
				PropsKeys.COMPANY_SECURITY_STRANGERS, companySecurityStrangers);
		}
	}

	private void _updateEmailAddress() throws Exception {
		User user = UserTestUtil.addUser(false);

		PrincipalThreadLocal.setName(user.getUserId());

		String emailAddress =
			"UserServiceTest." + RandomTestUtil.nextLong() + "@liferay.com";

		UserServiceUtil.updateEmailAddress(
			user.getUserId(), user.getPassword(), emailAddress, emailAddress,
			new ServiceContext());
	}

	private void _updateUser() throws Exception {
		User user = UserTestUtil.addUser(false);

		PrincipalThreadLocal.setName(user.getUserId());

		UserTestUtil.updateUser(user);
	}

}