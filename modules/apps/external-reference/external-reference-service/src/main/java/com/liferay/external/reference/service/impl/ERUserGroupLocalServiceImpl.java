/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.external.reference.service.impl;

import com.liferay.external.reference.service.base.ERUserGroupLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.ServiceContext;

/**
 * @author Dylan Rebelak
 */
public class ERUserGroupLocalServiceImpl
	extends ERUserGroupLocalServiceBaseImpl {

	@Override
	public UserGroup addOrUpdateUserGroup(
			String externalReferenceCode, long userId, long companyId,
			String name, String description, ServiceContext serviceContext)
		throws PortalException {

		UserGroup userGroup =
			userGroupLocalService.fetchUserGroupByReferenceCode(
				companyId, externalReferenceCode);

		if (userGroup == null) {
			userGroup = userGroupLocalService.addUserGroup(
				userId, companyId, name, description, serviceContext);

			userGroup.setExternalReferenceCode(externalReferenceCode);

			userGroup = userGroupLocalService.updateUserGroup(userGroup);
		}
		else {
			userGroupLocalService.updateUserGroup(
				companyId, userGroup.getUserGroupId(), name, description,
				serviceContext);
		}

		return userGroup;
	}

}