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
 * Provides the remote service utility for CommerceBOMDefinition. This utility wraps
 * {@link com.liferay.commerce.bom.service.impl.CommerceBOMDefinitionServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMDefinitionService
 * @see com.liferay.commerce.bom.service.base.CommerceBOMDefinitionServiceBaseImpl
 * @see com.liferay.commerce.bom.service.impl.CommerceBOMDefinitionServiceImpl
 * @generated
 */
@ProviderType
public class CommerceBOMDefinitionServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.commerce.bom.service.impl.CommerceBOMDefinitionServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.bom.model.CommerceBOMDefinition addCommerceBOMDefinition(
		long userId, String name, long imageId, String friendlyUrl,
		long commerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addCommerceBOMDefinition(userId, name, imageId,
			friendlyUrl, commerceBOMFolderId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.commerce.bom.model.CommerceBOMDefinition updateCommerceBOMDefinition(
		long commerceBOMDefinitionId, String name, long imageId,
		String friendlyUrl, long commerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateCommerceBOMDefinition(commerceBOMDefinitionId, name,
			imageId, friendlyUrl, commerceBOMFolderId);
	}

	public static CommerceBOMDefinitionService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommerceBOMDefinitionService, CommerceBOMDefinitionService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommerceBOMDefinitionService.class);

		ServiceTracker<CommerceBOMDefinitionService, CommerceBOMDefinitionService> serviceTracker =
			new ServiceTracker<CommerceBOMDefinitionService, CommerceBOMDefinitionService>(bundle.getBundleContext(),
				CommerceBOMDefinitionService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}