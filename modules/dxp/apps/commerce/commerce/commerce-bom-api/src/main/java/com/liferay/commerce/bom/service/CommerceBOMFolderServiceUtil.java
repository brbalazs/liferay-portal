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

package com.liferay.commerce.bom.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for CommerceBOMFolder. This utility wraps
 * {@link com.liferay.commerce.bom.service.impl.CommerceBOMFolderServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMFolderService
 * @see com.liferay.commerce.bom.service.base.CommerceBOMFolderServiceBaseImpl
 * @see com.liferay.commerce.bom.service.impl.CommerceBOMFolderServiceImpl
 * @generated
 */
@ProviderType
public class CommerceBOMFolderServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.commerce.bom.service.impl.CommerceBOMFolderServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMFolder addCommerceBOMFolder(
		long userId, long parentCommerceBOMFolderId, String name, long imageId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addCommerceBOMFolder(userId, parentCommerceBOMFolderId,
			name, imageId);
	}

	public static com.liferay.commerce.bom.model.CommerceBOMFolder getCommerceBOMFolder(
		long commerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getCommerceBOMFolder(commerceBOMFolderId);
	}

	public static java.util.List<com.liferay.commerce.bom.model.CommerceBOMFolder> getCommerceBOMFolders(
		long companyId, long parentCommerceBOMFolderId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getCommerceBOMFolders(companyId, parentCommerceBOMFolderId,
			start, end);
	}

	public static int getCommerceBOMFoldersCount(long companyId,
		long parentCommerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getCommerceBOMFoldersCount(companyId,
			parentCommerceBOMFolderId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.commerce.bom.model.CommerceBOMFolder updateCommerceBOMFolder(
		long commerceBOMFolderId, String name, long imageId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateCommerceBOMFolder(commerceBOMFolderId, name, imageId);
	}

	public static CommerceBOMFolderService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommerceBOMFolderService, CommerceBOMFolderService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommerceBOMFolderService.class);

		ServiceTracker<CommerceBOMFolderService, CommerceBOMFolderService> serviceTracker =
			new ServiceTracker<CommerceBOMFolderService, CommerceBOMFolderService>(bundle.getBundleContext(),
				CommerceBOMFolderService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}