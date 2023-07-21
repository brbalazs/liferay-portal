/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.verify;

import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.verify.model.VerifiableResourcedModel;
import com.liferay.portal.model.impl.ResourcePermissionImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.verify.model.GroupVerifiableResourcedModel;
import com.liferay.portal.verify.model.RoleVerifiableModel;
import com.liferay.portal.verify.test.BaseVerifyProcessTestCase;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 * @author Preston Crary
 */
public class VerifyResourcePermissionsTest extends BaseVerifyProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testVerifyGroupResourcedModel() throws Exception {
		_group = GroupTestUtil.addGroup();

		_testVerifyResourcedModel(
			_group.getCompanyId(), _group.getGroupId(),
			_group.getCreatorUserId(), new GroupVerifiableResourcedModel());
	}

	@Test
	public void testVerifyRoleResourcedModel() throws Exception {
		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		_testVerifyResourcedModel(
			_role.getCompanyId(), _role.getRoleId(), _role.getUserId(),
			new RoleVerifiableModel());
	}

	@Override
	protected VerifyProcess getVerifyProcess() {
		return new VerifyResourcePermissions();
	}

	private void _testVerifyResourcedModel(
			long companyId, long pk, long userId,
			VerifiableResourcedModel verifiableResourcedModel)
		throws Exception {

		Role ownerRole = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.OWNER);

		ResourcePermission resourcePermission =
			ResourcePermissionLocalServiceUtil.fetchResourcePermission(
				companyId, verifiableResourcedModel.getModelName(),
				ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(pk),
				ownerRole.getRoleId());

		Assert.assertNotNull(resourcePermission);

		ResourcePermissionLocalServiceUtil.deleteResourcePermission(
			resourcePermission);

		VerifyResourcePermissions verifyResourcePermissions =
			new VerifyResourcePermissions();

		verifyResourcePermissions.verify(verifiableResourcedModel);

		EntityCacheUtil.clearCache(ResourcePermissionImpl.class);
		FinderCacheUtil.clearCache(ResourcePermissionImpl.class.getName());

		resourcePermission =
			ResourcePermissionLocalServiceUtil.fetchResourcePermission(
				companyId, verifiableResourcedModel.getModelName(),
				ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(pk),
				ownerRole.getRoleId());

		Assert.assertNotNull(resourcePermission);

		Assert.assertEquals(
			String.valueOf(pk), resourcePermission.getPrimKey());

		Assert.assertEquals(userId, resourcePermission.getOwnerId());
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private Role _role;

}