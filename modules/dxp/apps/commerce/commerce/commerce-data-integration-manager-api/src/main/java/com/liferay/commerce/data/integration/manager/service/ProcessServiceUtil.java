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
 * Provides the remote service utility for Process. This utility wraps
 * <code>com.liferay.commerce.data.integration.manager.service.impl.ProcessServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see ProcessService
 * @generated
 */
@ProviderType
public class ProcessServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.data.integration.manager.service.impl.ProcessServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.data.integration.manager.model.Process
			addProcess(
				com.liferay.commerce.data.integration.manager.model.Process
					process,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addProcess(process, serviceContext);
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link ProcessServiceUtil} to access the process remote service.
	 *
	 * @throws PortalException
	 */
	public static com.liferay.commerce.data.integration.manager.model.Process
			addProcess(
				String name, String className, String processType,
				String version, String contextProperties,
				long contextPropertiesFileEntryId, long srcArchiveFileEntryId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addProcess(
			name, className, processType, version, contextProperties,
			contextPropertiesFileEntryId, srcArchiveFileEntryId,
			serviceContext);
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
		create() {

		return getService().create();
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			deleteProcess(
				long userId, long processId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteProcess(userId, processId, serviceContext);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			getProcess(long userId, long processId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getProcess(userId, processId);
	}

	public static java.util.List
		<com.liferay.commerce.data.integration.manager.model.Process>
				getProcessesByGroupId(
					long userId, long groupId, int start, int end)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getProcessesByGroupId(userId, groupId, start, end);
	}

	public static int getProcessesByGroupIdCount(long userId, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getProcessesByGroupIdCount(userId, groupId);
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			updateProcess(
				long processId, String name, String className,
				String processType, String version, String contextProperties,
				long contextPropertiesFileEntryId, long srcArchiveFileEntryId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateProcess(
			processId, name, className, processType, version, contextProperties,
			contextPropertiesFileEntryId, srcArchiveFileEntryId,
			serviceContext);
	}

	public static com.liferay.commerce.data.integration.manager.model.Process
			updateProcess(
				com.liferay.commerce.data.integration.manager.model.Process
					process,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateProcess(process, serviceContext);
	}

	public static ProcessService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ProcessService, ProcessService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ProcessService.class);

		ServiceTracker<ProcessService, ProcessService> serviceTracker =
			new ServiceTracker<ProcessService, ProcessService>(
				bundle.getBundleContext(), ProcessService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}