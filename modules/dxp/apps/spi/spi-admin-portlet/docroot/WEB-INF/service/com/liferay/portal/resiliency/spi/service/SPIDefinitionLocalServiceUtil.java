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

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.resiliency.spi.model.SPIDefinition;

import java.io.Serializable;

import java.util.List;

/**
 * Provides the local service utility for SPIDefinition. This utility wraps
 * <code>com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Michael C. Han
 * @see SPIDefinitionLocalService
 * @generated
 */
public class SPIDefinitionLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.resiliency.spi.service.impl.SPIDefinitionLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static SPIDefinition addSPIDefinition(
			long userId, String name, String connectorAddress,
			int connectorPort, String description, String jvmArguments,
			String portletIds, String servletContextNames, String typeSettings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addSPIDefinition(
			userId, name, connectorAddress, connectorPort, description,
			jvmArguments, portletIds, servletContextNames, typeSettings,
			serviceContext);
	}

	/**
	 * Adds the spi definition to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SPIDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param spiDefinition the spi definition
	 * @return the spi definition that was added
	 */
	public static SPIDefinition addSPIDefinition(SPIDefinition spiDefinition) {
		return getService().addSPIDefinition(spiDefinition);
	}

	/**
	 * Creates a new spi definition with the primary key. Does not add the spi definition to the database.
	 *
	 * @param spiDefinitionId the primary key for the new spi definition
	 * @return the new spi definition
	 */
	public static SPIDefinition createSPIDefinition(long spiDefinitionId) {
		return getService().createSPIDefinition(spiDefinitionId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the spi definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SPIDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param spiDefinitionId the primary key of the spi definition
	 * @return the spi definition that was removed
	 * @throws PortalException if a spi definition with the primary key could not be found
	 */
	public static SPIDefinition deleteSPIDefinition(long spiDefinitionId)
		throws PortalException {

		return getService().deleteSPIDefinition(spiDefinitionId);
	}

	/**
	 * Deletes the spi definition from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SPIDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param spiDefinition the spi definition
	 * @return the spi definition that was removed
	 * @throws PortalException
	 */
	public static SPIDefinition deleteSPIDefinition(SPIDefinition spiDefinition)
		throws PortalException {

		return getService().deleteSPIDefinition(spiDefinition);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.resiliency.spi.model.impl.SPIDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.resiliency.spi.model.impl.SPIDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static SPIDefinition fetchSPIDefinition(long spiDefinitionId) {
		return getService().fetchSPIDefinition(spiDefinitionId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static com.liferay.portal.kernel.util.Tuple
		getPortletIdsAndServletContextNames() {

		return getService().getPortletIdsAndServletContextNames();
	}

	/**
	 * Returns the spi definition with the primary key.
	 *
	 * @param spiDefinitionId the primary key of the spi definition
	 * @return the spi definition
	 * @throws PortalException if a spi definition with the primary key could not be found
	 */
	public static SPIDefinition getSPIDefinition(long spiDefinitionId)
		throws PortalException {

		return getService().getSPIDefinition(spiDefinitionId);
	}

	public static SPIDefinition getSPIDefinition(long companyId, String name)
		throws PortalException {

		return getService().getSPIDefinition(companyId, name);
	}

	public static List<SPIDefinition> getSPIDefinitions() {
		return getService().getSPIDefinitions();
	}

	/**
	 * Returns a range of all the spi definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.portal.resiliency.spi.model.impl.SPIDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of spi definitions
	 * @param end the upper bound of the range of spi definitions (not inclusive)
	 * @return the range of spi definitions
	 */
	public static List<SPIDefinition> getSPIDefinitions(int start, int end) {
		return getService().getSPIDefinitions(start, end);
	}

	public static List<SPIDefinition> getSPIDefinitions(
		long companyId, int status) {

		return getService().getSPIDefinitions(companyId, status);
	}

	public static List<SPIDefinition> getSPIDefinitions(
		long companyId, int[] statuses) {

		return getService().getSPIDefinitions(companyId, statuses);
	}

	/**
	 * Returns the number of spi definitions.
	 *
	 * @return the number of spi definitions
	 */
	public static int getSPIDefinitionsCount() {
		return getService().getSPIDefinitionsCount();
	}

	public static void startSPI(long spiDefinitionId) throws PortalException {
		getService().startSPI(spiDefinitionId);
	}

	public static long startSPIinBackground(long userId, long spiDefinitionId)
		throws PortalException {

		return getService().startSPIinBackground(userId, spiDefinitionId);
	}

	public static long startSPIinBackground(
			long userId, long spiDefinitionId, boolean automatedRestart)
		throws PortalException {

		return getService().startSPIinBackground(
			userId, spiDefinitionId, automatedRestart);
	}

	public static void stopSPI(long spiDefinitionId) throws PortalException {
		getService().stopSPI(spiDefinitionId);
	}

	public static long stopSPIinBackground(long userId, long spiDefinitionId)
		throws PortalException {

		return getService().stopSPIinBackground(userId, spiDefinitionId);
	}

	public static SPIDefinition updateSPIDefinition(
			long spiDefinitionId, int status, String statusMessage)
		throws PortalException {

		return getService().updateSPIDefinition(
			spiDefinitionId, status, statusMessage);
	}

	public static SPIDefinition updateSPIDefinition(
			long userId, long spiDefinitionId, String connectorAddress,
			int connectorPort, String description, String jvmArguments,
			String portletIds, String servletContextNames, String typeSettings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateSPIDefinition(
			userId, spiDefinitionId, connectorAddress, connectorPort,
			description, jvmArguments, portletIds, servletContextNames,
			typeSettings, serviceContext);
	}

	/**
	 * Updates the spi definition in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SPIDefinitionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param spiDefinition the spi definition
	 * @return the spi definition that was updated
	 */
	public static SPIDefinition updateSPIDefinition(
		SPIDefinition spiDefinition) {

		return getService().updateSPIDefinition(spiDefinition);
	}

	public static SPIDefinition updateTypeSettings(
			long userId, long spiDefinitionId, String typeSettings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateTypeSettings(
			userId, spiDefinitionId, typeSettings, serviceContext);
	}

	public static void clearService() {
		_service = null;
	}

	public static SPIDefinitionLocalService getService() {
		return _service;
	}

	private static volatile SPIDefinitionLocalService _service;

}