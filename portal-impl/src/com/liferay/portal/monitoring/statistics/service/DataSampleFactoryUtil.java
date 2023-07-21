/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.monitoring.statistics.service;

import com.liferay.portal.kernel.monitoring.DataSample;
import com.liferay.portal.kernel.monitoring.DataSampleFactory;
import com.liferay.portal.kernel.monitoring.MethodSignature;
import com.liferay.portal.kernel.monitoring.PortletRequestType;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * @author Michael C. Han
 */
public class DataSampleFactoryUtil {

	public static DataSample createPortalRequestDataSample(
		long companyId, long groupId, String referer, String remoteAddr,
		String remoteUser, String requestURI, String requestURL,
		String userAgent) {

		return _getDataSampleFactory().createPortalRequestDataSample(
			companyId, groupId, referer, remoteAddr, remoteUser, requestURI,
			requestURL, userAgent);
	}

	public static DataSample createPortletRequestDataSample(
		PortletRequestType requestType, PortletRequest portletRequest,
		PortletResponse portletResponse) {

		return _getDataSampleFactory().createPortletRequestDataSample(
			requestType, portletRequest, portletResponse);
	}

	public static DataSample createServiceRequestDataSample(
		MethodSignature methodSignature) {

		return _getDataSampleFactory().createServiceRequestDataSample(
			methodSignature);
	}

	private static DataSampleFactory _getDataSampleFactory() {
		return _instance._serviceTracker.getService();
	}

	private DataSampleFactoryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(DataSampleFactory.class);

		_serviceTracker.open();
	}

	private static final DataSampleFactoryUtil _instance =
		new DataSampleFactoryUtil();

	private final ServiceTracker<DataSampleFactory, DataSampleFactory>
		_serviceTracker;

}