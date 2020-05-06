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

package com.liferay.osb.faro.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for FaroEmail. This utility wraps
 * <code>com.liferay.osb.faro.service.impl.FaroEmailLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Matthew Kong
 * @see FaroEmailLocalService
 * @generated
 */
public class FaroEmailLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.osb.faro.service.impl.FaroEmailLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static java.util.ResourceBundle getResourceBundle(
		java.util.Locale locale) {

		return getService().getResourceBundle(locale);
	}

	public static String getTemplate(String name) throws Exception {
		return getService().getTemplate(name);
	}

	public static FaroEmailLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<FaroEmailLocalService, FaroEmailLocalService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(FaroEmailLocalService.class);

		ServiceTracker<FaroEmailLocalService, FaroEmailLocalService>
			serviceTracker =
				new ServiceTracker
					<FaroEmailLocalService, FaroEmailLocalService>(
						bundle.getBundleContext(), FaroEmailLocalService.class,
						null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}