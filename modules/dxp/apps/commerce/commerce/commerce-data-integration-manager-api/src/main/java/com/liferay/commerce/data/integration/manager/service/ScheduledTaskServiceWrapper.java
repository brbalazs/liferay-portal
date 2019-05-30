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

package com.liferay.commerce.data.integration.manager.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ScheduledTaskService}.
 *
 * @author Marco Leo
 * @see ScheduledTaskService
 * @generated
 */
@ProviderType
public class ScheduledTaskServiceWrapper
	implements ScheduledTaskService, ServiceWrapper<ScheduledTaskService> {

	public ScheduledTaskServiceWrapper(
		ScheduledTaskService scheduledTaskService) {

		_scheduledTaskService = scheduledTaskService;
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link ScheduledTaskServiceUtil} to access the scheduled task remote service.
	 */
	@Override
	public com.liferay.commerce.data.integration.manager.model.ScheduledTask
			addScheduledTask(
				long processId, String frequency, java.util.Date startDate,
				String startHour, String name,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _scheduledTaskService.addScheduledTask(
			processId, frequency, startDate, startHour, name, serviceContext);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _scheduledTaskService.getOSGiServiceIdentifier();
	}

	@Override
	public java.util.List
		<com.liferay.commerce.data.integration.manager.model.ScheduledTask>
				getScheduledTaskByGroupId(long groupId, int start, int end)
			throws com.liferay.portal.kernel.security.auth.PrincipalException {

		return _scheduledTaskService.getScheduledTaskByGroupId(
			groupId, start, end);
	}

	@Override
	public int getScheduledTaskByGroupIdCount(long groupId)
		throws com.liferay.portal.kernel.security.auth.PrincipalException {

		return _scheduledTaskService.getScheduledTaskByGroupIdCount(groupId);
	}

	@Override
	public com.liferay.commerce.data.integration.manager.model.ScheduledTask
			updateScheduledTask(
				long scheduledTaskId, long processId, String frequency,
				java.util.Date startDate, String startHour, String name,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _scheduledTaskService.updateScheduledTask(
			scheduledTaskId, processId, frequency, startDate, startHour, name,
			serviceContext);
	}

	@Override
	public ScheduledTaskService getWrappedService() {
		return _scheduledTaskService;
	}

	@Override
	public void setWrappedService(ScheduledTaskService scheduledTaskService) {
		_scheduledTaskService = scheduledTaskService;
	}

	private ScheduledTaskService _scheduledTaskService;

}