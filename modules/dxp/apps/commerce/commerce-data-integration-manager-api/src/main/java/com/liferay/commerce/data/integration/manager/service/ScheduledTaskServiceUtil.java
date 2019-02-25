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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for ScheduledTask. This utility wraps
 * {@link com.liferay.commerce.data.integration.manager.service.impl.ScheduledTaskServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see ScheduledTaskService
 * @see com.liferay.commerce.data.integration.manager.service.base.ScheduledTaskServiceBaseImpl
 * @see com.liferay.commerce.data.integration.manager.service.impl.ScheduledTaskServiceImpl
 * @generated
 */
@ProviderType
public class ScheduledTaskServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.commerce.data.integration.manager.service.impl.ScheduledTaskServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this class directly. Always use {@link ScheduledTaskServiceUtil} to access the scheduled task remote service.
	*/
	public static com.liferay.commerce.data.integration.manager.model.ScheduledTask addScheduledTask(
		long processId, String frequency, java.util.Date startDate,
		String startHour, String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addScheduledTask(processId, frequency, startDate,
			startHour, name, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static java.util.List<com.liferay.commerce.data.integration.manager.model.ScheduledTask> getScheduledTaskByGroupId(
		long groupId, int start, int end)
		throws com.liferay.portal.kernel.security.auth.PrincipalException {
		return getService().getScheduledTaskByGroupId(groupId, start, end);
	}

	public static int getScheduledTaskByGroupIdCount(long groupId)
		throws com.liferay.portal.kernel.security.auth.PrincipalException {
		return getService().getScheduledTaskByGroupIdCount(groupId);
	}

	public static com.liferay.commerce.data.integration.manager.model.ScheduledTask updateScheduledTask(
		long scheduledTaskId, long processId, String frequency,
		java.util.Date startDate, String startHour, String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateScheduledTask(scheduledTaskId, processId, frequency,
			startDate, startHour, name, serviceContext);
	}

	public static ScheduledTaskService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ScheduledTaskService, ScheduledTaskService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ScheduledTaskService.class);

		ServiceTracker<ScheduledTaskService, ScheduledTaskService> serviceTracker =
			new ServiceTracker<ScheduledTaskService, ScheduledTaskService>(bundle.getBundleContext(),
				ScheduledTaskService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}