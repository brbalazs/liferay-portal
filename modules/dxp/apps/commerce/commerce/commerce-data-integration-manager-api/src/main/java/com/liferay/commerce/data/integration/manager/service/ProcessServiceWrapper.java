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
 * Provides a wrapper for {@link ProcessService}.
 *
 * @author Brian Wing Shun Chan
 * @see ProcessService
 * @generated
 */
@ProviderType
public class ProcessServiceWrapper implements ProcessService,
	ServiceWrapper<ProcessService> {
	public ProcessServiceWrapper(ProcessService processService) {
		_processService = processService;
	}

	@Override
	public com.liferay.commerce.data.integration.manager.model.Process addProcess(
		com.liferay.commerce.data.integration.manager.model.Process process,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.addProcess(process, serviceContext);
	}

	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this class directly. Always use {@link ProcessServiceUtil} to access the process remote service.
	*
	* @throws PortalException
	*/
	@Override
	public com.liferay.commerce.data.integration.manager.model.Process addProcess(
		String name, String className, String processType, String version,
		String contextProperties, long contextPropertiesFileEntryId,
		long srcArchiveFileEntryId,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.addProcess(name, className, processType,
			version, contextProperties, contextPropertiesFileEntryId,
			srcArchiveFileEntryId, serviceContext);
	}

	@Override
	public com.liferay.commerce.data.integration.manager.model.Process create() {
		return _processService.create();
	}

	@Override
	public com.liferay.commerce.data.integration.manager.model.Process deleteProcess(
		long userId, long processId,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.deleteProcess(userId, processId, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _processService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.commerce.data.integration.manager.model.Process getProcess(
		long userId, long processId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.getProcess(userId, processId);
	}

	@Override
	public java.util.List<com.liferay.commerce.data.integration.manager.model.Process> getProcessesByGroupId(
		long userId, long groupId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.getProcessesByGroupId(userId, groupId, start, end);
	}

	@Override
	public int getProcessesByGroupIdCount(long userId, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.getProcessesByGroupIdCount(userId, groupId);
	}

	@Override
	public com.liferay.commerce.data.integration.manager.model.Process updateProcess(
		long processId, String name, String className, String processType,
		String version, String contextProperties,
		long contextPropertiesFileEntryId, long srcArchiveFileEntryId,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.updateProcess(processId, name, className,
			processType, version, contextProperties,
			contextPropertiesFileEntryId, srcArchiveFileEntryId, serviceContext);
	}

	@Override
	public com.liferay.commerce.data.integration.manager.model.Process updateProcess(
		com.liferay.commerce.data.integration.manager.model.Process process,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _processService.updateProcess(process, serviceContext);
	}

	@Override
	public ProcessService getWrappedService() {
		return _processService;
	}

	@Override
	public void setWrappedService(ProcessService processService) {
		_processService = processService;
	}

	private ProcessService _processService;
}