/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.manager.service.impl;

import com.liferay.commerce.data.integration.manager.constants.ScheduledTaskActionKeys;
import com.liferay.commerce.data.integration.manager.model.ProcessConstants;
import com.liferay.commerce.data.integration.manager.model.ScheduledTask;
import com.liferay.commerce.data.integration.manager.service.base.ScheduledTaskServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class ScheduledTaskServiceImpl extends ScheduledTaskServiceBaseImpl {

	public ScheduledTask addScheduledTask(
			long processId, String frequency, Date startDate, String startHour,
			String name, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ScheduledTaskActionKeys.MANAGE_SCHEDULED_TASK);

		return scheduledTaskLocalService.addScheduledTask(
			processId, frequency, startDate, startHour, name, serviceContext);
	}

	public List<ScheduledTask> getScheduledTaskByGroupId(
			long groupId, int start, int end)
		throws PrincipalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId,
			ScheduledTaskActionKeys.MANAGE_SCHEDULED_TASK);

		return scheduledTaskLocalService.getScheduledTaskByGroupId(
			groupId, start, end);
	}

	public int getScheduledTaskByGroupIdCount(long groupId)
		throws PrincipalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId,
			ScheduledTaskActionKeys.MANAGE_SCHEDULED_TASK);

		return scheduledTaskLocalService.getScheduledTaskByGroupIdCount(
			groupId);
	}

	public ScheduledTask updateScheduledTask(
			long scheduledTaskId, long processId, String frequency,
			Date startDate, String startHour, String name,
			ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ScheduledTaskActionKeys.MANAGE_SCHEDULED_TASK);

		return scheduledTaskLocalService.updateScheduledTask(
			scheduledTaskId, processId, frequency, startDate, startHour, name,
			serviceContext);
	}

	private static volatile PortletResourcePermission
		_portletResourcePermission =
			PortletResourcePermissionFactory.getInstance(
				ProcessServiceImpl.class, "_portletResourcePermission",
				ProcessConstants.RESOURCE_NAME);

}