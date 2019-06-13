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

package com.liferay.commerce.application.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for CommerceApplicationModel. This utility wraps
 * {@link com.liferay.commerce.application.service.impl.CommerceApplicationModelServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Luca Pellizzon
 * @see CommerceApplicationModelService
 * @see com.liferay.commerce.application.service.base.CommerceApplicationModelServiceBaseImpl
 * @see com.liferay.commerce.application.service.impl.CommerceApplicationModelServiceImpl
 * @generated
 */
@ProviderType
public class CommerceApplicationModelServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.commerce.application.service.impl.CommerceApplicationModelServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.commerce.application.model.CommerceApplicationModel addCommerceApplicationModel(
		long userId, long commerceApplicationBrandId, long cProductId,
		String name, String year)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addCommerceApplicationModel(userId,
			commerceApplicationBrandId, cProductId, name, year);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.commerce.application.model.CommerceApplicationModel updateCommerceApplicationModel(
		long commerceApplicationModelId, long commerceApplicationBrandId,
		String name, String year)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .updateCommerceApplicationModel(commerceApplicationModelId,
			commerceApplicationBrandId, name, year);
	}

	public static CommerceApplicationModelService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommerceApplicationModelService, CommerceApplicationModelService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommerceApplicationModelService.class);

		ServiceTracker<CommerceApplicationModelService, CommerceApplicationModelService> serviceTracker =
			new ServiceTracker<CommerceApplicationModelService, CommerceApplicationModelService>(bundle.getBundleContext(),
				CommerceApplicationModelService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}