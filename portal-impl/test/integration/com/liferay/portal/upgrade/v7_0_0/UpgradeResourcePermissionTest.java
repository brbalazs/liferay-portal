/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.impl.ResourcePermissionImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Sampsa Sohlman
 */
@Sync(cleanTransaction = true)
public class UpgradeResourcePermissionTest extends UpgradeResourcePermission {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_user = UserTestUtil.addUser();
	}

	@After
	public void tearDown() throws Exception {
		for (long resourcePermissionId : _resourcePermissionIds) {
			ResourcePermissionLocalServiceUtil.deleteResourcePermission(
				resourcePermissionId);
		}
	}

	@Test
	public void testUpgrade() throws Exception {
		String primKey1 = "123";
		long actionIds1 = 4;

		long resourcePermissionId1 = addResourcePermission(
			primKey1, actionIds1);

		String primKey2 = "987_INSTANCE_ABCD";
		long actionIds2 = 3;

		long resourcePermissionId2 = addResourcePermission(
			primKey2, actionIds2);

		upgrade();

		CacheRegistryUtil.clear();

		ResourcePermission resourcePermission1 =
			ResourcePermissionLocalServiceUtil.getResourcePermission(
				resourcePermissionId1);

		Assert.assertEquals(
			(actionIds1 % 2) == 1, resourcePermission1.isViewActionId());
		Assert.assertEquals(
			resourcePermission1.getPrimKeyId(), GetterUtil.getLong(primKey1));

		ResourcePermission resourcePermission2 =
			ResourcePermissionLocalServiceUtil.getResourcePermission(
				resourcePermissionId2);

		Assert.assertEquals(
			(actionIds2 % 2) == 1, resourcePermission2.isViewActionId());
		Assert.assertEquals(0, resourcePermission2.getPrimKeyId());
	}

	protected long addResourcePermission(String primKey, long actionIds) {
		ResourcePermission resourcePermission = new ResourcePermissionImpl();

		long resourcePermissionId = CounterLocalServiceUtil.increment(
			ResourcePermission.class.getName());

		resourcePermission.setResourcePermissionId(resourcePermissionId);

		resourcePermission.setCompanyId(_user.getCompanyId());
		resourcePermission.setName(
			UpgradeResourcePermissionTest.class.getName());
		resourcePermission.setScope(ResourceConstants.SCOPE_INDIVIDUAL);
		resourcePermission.setPrimKey(primKey);
		resourcePermission.setPrimKeyId(-1);
		resourcePermission.setOwnerId(_user.getUserId());
		resourcePermission.setActionIds(actionIds);
		resourcePermission.setViewActionId((actionIds % 2) != 1);

		resourcePermission =
			ResourcePermissionLocalServiceUtil.addResourcePermission(
				resourcePermission);

		_resourcePermissionIds.add(
			resourcePermission.getResourcePermissionId());

		return resourcePermission.getResourcePermissionId();
	}

	private final List<Long> _resourcePermissionIds = new ArrayList<>();

	@DeleteAfterTestRun
	private User _user;

}