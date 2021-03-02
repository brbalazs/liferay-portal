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

package com.liferay.portal.resiliency.spi.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.resiliency.spi.model.SPIDefinition;

import java.util.List;

/**
 * Provides the remote service utility for SPIDefinition. This utility wraps
 * <code>com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Michael C. Han
 * @see SPIDefinitionService
 * @generated
 */
public class SPIDefinitionServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static SPIDefinition addSPIDefinition(
			String name, String connectorAddress, int connectorPort,
			String description, String jvmArguments, String portletIds,
			String servletContextNames, String typeSettings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addSPIDefinition(
			name, connectorAddress, connectorPort, description, jvmArguments,
			portletIds, servletContextNames, typeSettings, serviceContext);
	}

	public static SPIDefinition deleteSPIDefinition(long spiDefinitionId)
		throws PortalException {

		return getService().deleteSPIDefinition(spiDefinitionId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.util.Tuple
			getPortletIdsAndServletContextNames()
		throws PortalException {

		return getService().getPortletIdsAndServletContextNames();
	}

	public static SPIDefinition getSPIDefinition(long spiDefinitionId)
		throws PortalException {

		return getService().getSPIDefinition(spiDefinitionId);
	}

	public static SPIDefinition getSPIDefinition(long companyId, String name)
		throws PortalException {

		return getService().getSPIDefinition(companyId, name);
	}

	public static List<SPIDefinition> getSPIDefinitions()
		throws PortalException {

		return getService().getSPIDefinitions();
	}

	public static void startSPI(long spiDefinitionId) throws PortalException {
		getService().startSPI(spiDefinitionId);
	}

	public static long startSPIinBackground(long spiDefinitionId)
		throws PortalException {

		return getService().startSPIinBackground(spiDefinitionId);
	}

	public static void stopSPI(long spiDefinitionId) throws PortalException {
		getService().stopSPI(spiDefinitionId);
	}

	public static long stopSPIinBackground(long spiDefinitionId)
		throws PortalException {

		return getService().stopSPIinBackground(spiDefinitionId);
	}

	public static SPIDefinition updateSPIDefinition(
			long spiDefinitionId, String connectorAddress, int connectorPort,
			String description, String jvmArguments, String portletIds,
			String servletContextNames, String typeSettings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateSPIDefinition(
			spiDefinitionId, connectorAddress, connectorPort, description,
			jvmArguments, portletIds, servletContextNames, typeSettings,
			serviceContext);
	}

	public static SPIDefinition updateTypeSettings(
			long userId, long spiDefinitionId, String recoveryOptions,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateTypeSettings(
			userId, spiDefinitionId, recoveryOptions, serviceContext);
	}

	public static void clearService() {
		_service = null;
	}

	public static SPIDefinitionService getService() {
		return _service;
	}

	private static volatile SPIDefinitionService _service;

}