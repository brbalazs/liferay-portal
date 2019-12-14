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
 * Provides the remote service utility for CommerceBOMFolderApplicationRel. This utility wraps
 * {@link com.liferay.commerce.bom.service.impl.CommerceBOMFolderApplicationRelServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMFolderApplicationRelService
 * @see com.liferay.commerce.bom.service.base.CommerceBOMFolderApplicationRelServiceBaseImpl
 * @see com.liferay.commerce.bom.service.impl.CommerceBOMFolderApplicationRelServiceImpl
 * @generated
 */
@ProviderType
public class CommerceBOMFolderApplicationRelServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.commerce.bom.service.impl.CommerceBOMFolderApplicationRelServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMFolderApplicationRel addCommerceBOMFolderApplicationRel(
		long userId, long commerceBOMFolderId, long commerceApplicationModelId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addCommerceBOMFolderApplicationRel(userId,
			commerceBOMFolderId, commerceApplicationModelId);
	}

	public static void deleteCommerceBOMFolderApplicationRel(
		long commerceBOMFolderApplicationRelId)
		throws com.liferay.portal.kernel.exception.PortalException {
		getService()
			.deleteCommerceBOMFolderApplicationRel(commerceBOMFolderApplicationRelId);
	}

	public static java.util.List<com.liferay.commerce.bom.model.CommerceBOMFolderApplicationRel> getCommerceBOMFolderApplicationRelsByCAMId(
		long commerceApplicationModelId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getCommerceBOMFolderApplicationRelsByCAMId(commerceApplicationModelId,
			start, end);
	}

	public static java.util.List<com.liferay.commerce.bom.model.CommerceBOMFolderApplicationRel> getCommerceBOMFolderApplicationRelsByCommerceBOMFolderId(
		long commerceBOMFolderId, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getCommerceBOMFolderApplicationRelsByCommerceBOMFolderId(commerceBOMFolderId,
			start, end);
	}

	public static int getCommerceBOMFolderApplicationRelsCountByCAMId(
		long commerceApplicationModelId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getCommerceBOMFolderApplicationRelsCountByCAMId(commerceApplicationModelId);
	}

	public static int getCommerceBOMFolderApplicationRelsCountByCommerceBOMFolderId(
		long commerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .getCommerceBOMFolderApplicationRelsCountByCommerceBOMFolderId(commerceBOMFolderId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CommerceBOMFolderApplicationRelService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommerceBOMFolderApplicationRelService, CommerceBOMFolderApplicationRelService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommerceBOMFolderApplicationRelService.class);

		ServiceTracker<CommerceBOMFolderApplicationRelService, CommerceBOMFolderApplicationRelService> serviceTracker =
			new ServiceTracker<CommerceBOMFolderApplicationRelService, CommerceBOMFolderApplicationRelService>(bundle.getBundleContext(),
				CommerceBOMFolderApplicationRelService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}