/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service;

import com.liferay.portal.kernel.exception.NoSuchResourceException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.security.permission.DoAsUserThread;
import com.liferay.portal.service.test.ServiceTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Brian Wing Shun Chan
 */
public class ResourceLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_users = new User[ServiceTestUtil.THREAD_COUNT];

		for (int i = 0; i < ServiceTestUtil.THREAD_COUNT; i++) {
			User user = UserTestUtil.addUser(
				"ResourceLocalServiceTest" + (i + 1), _group.getGroupId());

			_users[i] = user;
		}
	}

	@Test
	public void testAddResourcesConcurrently() throws Exception {
		DoAsUserThread[] doAsUserThreads =
			new DoAsUserThread[ServiceTestUtil.THREAD_COUNT];

		for (int i = 0; i < doAsUserThreads.length; i++) {
			doAsUserThreads[i] = new AddResources(_users[i].getUserId());
		}

		for (DoAsUserThread doAsUserThread : doAsUserThreads) {
			doAsUserThread.start();
		}

		for (DoAsUserThread doAsUserThread : doAsUserThreads) {
			doAsUserThread.join();
		}

		int successCount = 0;

		for (DoAsUserThread doAsUserThread : doAsUserThreads) {
			if (doAsUserThread.isSuccess()) {
				successCount++;
			}
		}

		Assert.assertTrue(
			StringBundler.concat(
				"Only ", String.valueOf(successCount), " out of ",
				String.valueOf(ServiceTestUtil.THREAD_COUNT),
				" threads added resources successfully"),
			successCount == ServiceTestUtil.THREAD_COUNT);
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private User[] _users;

	private class AddResources extends DoAsUserThread {

		public AddResources(long userId) {
			super(userId);
		}

		@Override
		protected void doRun() throws Exception {
			try {
				ResourceLocalServiceUtil.getResource(
					TestPropsValues.getCompanyId(), Layout.class.getName(),
					ResourceConstants.SCOPE_INDIVIDUAL,
					String.valueOf(
						TestPropsValues.getPlid(_group.getGroupId())));
			}
			catch (NoSuchResourceException nsre) {
				boolean addGroupPermission = true;
				boolean addGuestPermission = true;

				ResourceLocalServiceUtil.addResources(
					TestPropsValues.getCompanyId(), _group.getGroupId(), 0,
					Layout.class.getName(),
					TestPropsValues.getPlid(_group.getGroupId()), false,
					addGroupPermission, addGuestPermission);
			}
		}

	}

}