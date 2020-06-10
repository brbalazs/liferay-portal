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

package com.liferay.commerce.punchout.service.impl;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.model.CommerceAccountUserRel;
import com.liferay.commerce.account.service.CommerceAccountUserRelLocalService;
import com.liferay.commerce.punchout.service.PunchoutAccountRoleHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.RoleLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jaclyn Ong
 */
@Component(immediate = true, service = PunchoutAccountRoleHelper.class)
public class PunchoutAccountRoleHelperImpl
	implements PunchoutAccountRoleHelper {

	@Override
	public boolean hasPunchoutRole(
			long companyId, long userId, long commerceAccountId)
		throws PortalException {

		List<CommerceAccountUserRel> commerceAccountUserRels =
			_commerceAccountUserRelLocalService.getCommerceAccountUserRels(
				commerceAccountId);

		if (commerceAccountUserRels.isEmpty()) {
			return false;
		}

		Role punchoutRole = _roleLocalService.fetchRole(
			companyId, CommerceAccountConstants.ROLE_NAME_ACCOUNT_PUNCHOUT);

		if (punchoutRole == null) {
			return false;
		}

		for (CommerceAccountUserRel commerceAccountUserRel :
				commerceAccountUserRels) {

			List<UserGroupRole> userGroupRoles =
				commerceAccountUserRel.getUserGroupRoles();

			for (UserGroupRole userGroupRole : userGroupRoles) {
				Role role = userGroupRole.getRole();

				if ((userGroupRole.getUserId() == userId) &&
					(role.getRoleId() == punchoutRole.getRoleId())) {

					return true;
				}
			}
		}

		return false;
	}

	@Reference
	private CommerceAccountUserRelLocalService
		_commerceAccountUserRelLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

}