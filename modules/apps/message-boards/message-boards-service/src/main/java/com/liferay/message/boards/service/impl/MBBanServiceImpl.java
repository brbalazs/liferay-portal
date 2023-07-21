/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.service.impl;

import com.liferay.message.boards.constants.MBConstants;
import com.liferay.message.boards.model.MBBan;
import com.liferay.message.boards.service.base.MBBanServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.spring.extender.service.ServiceReference;

/**
 * @author Brian Wing Shun Chan
 */
public class MBBanServiceImpl extends MBBanServiceBaseImpl {

	@Override
	public MBBan addBan(long banUserId, ServiceContext serviceContext)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		_portletResourcePermission.check(
			permissionChecker, serviceContext.getScopeGroupId(),
			ActionKeys.BAN_USER);

		User banUser = _userLocalService.getUser(banUserId);

		boolean groupAdmin = false;

		try {
			groupAdmin = _portal.isGroupAdmin(
				banUser, serviceContext.getScopeGroupId());
		}
		catch (Exception e) {
			throw new SystemException(e);
		}

		if (groupAdmin) {
			throw new PrincipalException();
		}

		return mbBanLocalService.addBan(getUserId(), banUserId, serviceContext);
	}

	@Override
	public void deleteBan(long banUserId, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ActionKeys.BAN_USER);

		mbBanLocalService.deleteBan(banUserId, serviceContext);
	}

	private static volatile PortletResourcePermission
		_portletResourcePermission =
			PortletResourcePermissionFactory.getInstance(
				MBBanServiceImpl.class, "_portletResourcePermission",
				MBConstants.RESOURCE_NAME);

	@ServiceReference(type = Portal.class)
	private Portal _portal;

	@ServiceReference(type = UserLocalService.class)
	private UserLocalService _userLocalService;

}