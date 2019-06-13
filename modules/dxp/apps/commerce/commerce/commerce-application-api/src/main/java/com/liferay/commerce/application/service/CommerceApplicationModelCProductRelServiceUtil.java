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
 * Provides the remote service utility for CommerceApplicationModelCProductRel. This utility wraps
 * {@link com.liferay.commerce.application.service.impl.CommerceApplicationModelCProductRelServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on a remote server. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Luca Pellizzon
 * @see CommerceApplicationModelCProductRelService
 * @see com.liferay.commerce.application.service.base.CommerceApplicationModelCProductRelServiceBaseImpl
 * @see com.liferay.commerce.application.service.impl.CommerceApplicationModelCProductRelServiceImpl
 * @generated
 */
@ProviderType
public class CommerceApplicationModelCProductRelServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.commerce.application.service.impl.CommerceApplicationModelCProductRelServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CommerceApplicationModelCProductRelService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<CommerceApplicationModelCProductRelService, CommerceApplicationModelCProductRelService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(CommerceApplicationModelCProductRelService.class);

		ServiceTracker<CommerceApplicationModelCProductRelService, CommerceApplicationModelCProductRelService> serviceTracker =
			new ServiceTracker<CommerceApplicationModelCProductRelService, CommerceApplicationModelCProductRelService>(bundle.getBundleContext(),
				CommerceApplicationModelCProductRelService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}