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

package com.liferay.commerce.data.integration.manager.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.data.integration.manager.service.ScheduledTaskServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * {@link ScheduledTaskServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap}.
 * If the method in the service utility returns a
 * {@link com.liferay.commerce.data.integration.manager.model.ScheduledTask}, that is translated to a
 * {@link com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap}. Methods that SOAP cannot
 * safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ScheduledTaskServiceHttp
 * @see com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap
 * @see ScheduledTaskServiceUtil
 * @generated
 */
@ProviderType
public class ScheduledTaskServiceSoap {
	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this class directly. Always use {@link ScheduledTaskServiceUtil} to access the scheduled task remote service.
	*/
	public static com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap addScheduledTask(
		long processId, String frequency, java.util.Date startDate,
		String startHour, String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			com.liferay.commerce.data.integration.manager.model.ScheduledTask returnValue =
				ScheduledTaskServiceUtil.addScheduledTask(processId, frequency,
					startDate, startHour, name, serviceContext);

			return com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap[] getScheduledTaskByGroupId(
		long groupId, int start, int end) throws RemoteException {
		try {
			java.util.List<com.liferay.commerce.data.integration.manager.model.ScheduledTask> returnValue =
				ScheduledTaskServiceUtil.getScheduledTaskByGroupId(groupId,
					start, end);

			return com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap.toSoapModels(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static int getScheduledTaskByGroupIdCount(long groupId)
		throws RemoteException {
		try {
			int returnValue = ScheduledTaskServiceUtil.getScheduledTaskByGroupIdCount(groupId);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap updateScheduledTask(
		long scheduledTaskId, long processId, String frequency,
		java.util.Date startDate, String startHour, String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			com.liferay.commerce.data.integration.manager.model.ScheduledTask returnValue =
				ScheduledTaskServiceUtil.updateScheduledTask(scheduledTaskId,
					processId, frequency, startDate, startHour, name,
					serviceContext);

			return com.liferay.commerce.data.integration.manager.model.ScheduledTaskSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(ScheduledTaskServiceSoap.class);
}