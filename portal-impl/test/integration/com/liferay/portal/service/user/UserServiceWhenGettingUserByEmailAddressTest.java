/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.user;

import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Brian Wing Shun Chan
 * @author José Manuel Navarro
 * @author Drew Brokke
 */
public class UserServiceWhenGettingUserByEmailAddressTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test(expected = NoSuchUserException.class)
	public void testShouldFailIfUserDeleted() throws Exception {
		User user = UserTestUtil.addUser(true);

		UserServiceUtil.deleteUser(user.getUserId());

		UserServiceUtil.getUserByEmailAddress(
			TestPropsValues.getCompanyId(), user.getEmailAddress());
	}

	@Test
	public void testShouldReturnUserIfPresent() throws Exception {
		User user = UserTestUtil.addUser(true);

		try {
			User retrievedUser = UserServiceUtil.getUserByEmailAddress(
				TestPropsValues.getCompanyId(), user.getEmailAddress());

			Assert.assertEquals(user, retrievedUser);
		}
		finally {
			UserLocalServiceUtil.deleteUser(user);
		}
	}

}