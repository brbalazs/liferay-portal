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

import com.liferay.commerce.data.integration.manager.constants.ProcessActionKeys;
import com.liferay.commerce.data.integration.manager.model.Process;
import com.liferay.commerce.data.integration.manager.model.ProcessConstants;
import com.liferay.commerce.data.integration.manager.service.base.ProcessServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public class ProcessServiceImpl extends ProcessServiceBaseImpl {

	public Process addProcess(Process process, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ProcessActionKeys.MANAGE_PROCESS);

		return processLocalService.addProcess(process, serviceContext);
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link ProcessServiceUtil} to access the process remote service.
	 * @throws PortalException
	 */
	public Process addProcess(
			String name, String className, String processType, String version,
			String contextProperties, long contextPropertiesFileEntryId,
			long srcArchiveFileEntryId, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ProcessActionKeys.MANAGE_PROCESS);

		return processLocalService.addProcess(
			name, className, processType, version, contextProperties,
			contextPropertiesFileEntryId, srcArchiveFileEntryId,
			serviceContext);
	}

	public Process create() {
		long processId = counterLocalService.increment(Process.class.getName());

		return processLocalService.createProcess(processId);
	}

	public Process deleteProcess(
			long userId, long processId, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ProcessActionKeys.MANAGE_PROCESS);

		return processLocalService.deleteProcess(processId);
	}

	public Process getProcess(long userId, long processId)
		throws PortalException {

		return processLocalService.getProcess(processId);
	}

	public List<Process> getProcessesByGroupId(
			long userId, long groupId, int start, int end)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId, ProcessActionKeys.MANAGE_PROCESS);

		return processLocalService.getProcessesByGroupId(groupId, start, end);
	}

	public int getProcessesByGroupIdCount(long userId, long groupId)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId, ProcessActionKeys.MANAGE_PROCESS);

		return processLocalService.getProcessesByGroupIdCount(groupId);
	}

	public Process updateProcess(
			long processId, String name, String className, String processType,
			String version, String contextProperties,
			long contextPropertiesFileEntryId, long srcArchiveFileEntryId,
			ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ProcessActionKeys.MANAGE_PROCESS);

		return processLocalService.updateProcess(
			processId, name, className, processType, version, contextProperties,
			contextPropertiesFileEntryId, srcArchiveFileEntryId,
			serviceContext);
	}

	public Process updateProcess(Process process, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), serviceContext.getScopeGroupId(),
			ProcessActionKeys.MANAGE_PROCESS);

		return processLocalService.updateProcess(process);
	}

	private static volatile PortletResourcePermission
		_portletResourcePermission =
			PortletResourcePermissionFactory.getInstance(
				ProcessServiceImpl.class, "_portletResourcePermission",
				ProcessConstants.RESOURCE_NAME);

}