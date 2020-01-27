/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.price.list.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for CommercePriceListPriceModifierRel. This utility wraps
 * <code>com.liferay.commerce.price.list.service.impl.CommercePriceListPriceModifierRelLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Alessio Antonio Rendina
 * @see CommercePriceListPriceModifierRelLocalService
 * @generated
 */
public class CommercePriceListPriceModifierRelLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.price.list.service.impl.CommercePriceListPriceModifierRelLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the commerce price list price modifier rel to the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceListPriceModifierRel the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel that was added
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
			addCommercePriceListPriceModifierRel(
				com.liferay.commerce.price.list.model.
					CommercePriceListPriceModifierRel
						commercePriceListPriceModifierRel) {

		return getService().addCommercePriceListPriceModifierRel(
			commercePriceListPriceModifierRel);
	}

	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
				addCommercePriceListPriceModifierRel(
					long commercePriceListId, long commercePriceModifierId,
					int order,
					com.liferay.portal.kernel.service.ServiceContext
						serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCommercePriceListPriceModifierRel(
			commercePriceListId, commercePriceModifierId, order,
			serviceContext);
	}

	/**
	 * Creates a new commerce price list price modifier rel with the primary key. Does not add the commerce price list price modifier rel to the database.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key for the new commerce price list price modifier rel
	 * @return the new commerce price list price modifier rel
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
			createCommercePriceListPriceModifierRel(
				long commercePriceListPriceModifierRelId) {

		return getService().createCommercePriceListPriceModifierRel(
			commercePriceListPriceModifierRelId);
	}

	/**
	 * Deletes the commerce price list price modifier rel from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceListPriceModifierRel the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel that was removed
	 * @throws PortalException
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
				deleteCommercePriceListPriceModifierRel(
					com.liferay.commerce.price.list.model.
						CommercePriceListPriceModifierRel
							commercePriceListPriceModifierRel)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteCommercePriceListPriceModifierRel(
			commercePriceListPriceModifierRel);
	}

	/**
	 * Deletes the commerce price list price modifier rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel that was removed
	 * @throws PortalException if a commerce price list price modifier rel with the primary key could not be found
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
				deleteCommercePriceListPriceModifierRel(
					long commercePriceListPriceModifierRelId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteCommercePriceListPriceModifierRel(
			commercePriceListPriceModifierRelId);
	}

	public static void deleteCommercePriceListPriceModifierRels(
		long commercePriceListId) {

		getService().deleteCommercePriceListPriceModifierRels(
			commercePriceListId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

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
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
			fetchCommercePriceListPriceModifierRel(
				long commercePriceListPriceModifierRelId) {

		return getService().fetchCommercePriceListPriceModifierRel(
			commercePriceListPriceModifierRelId);
	}

	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
			fetchCommercePriceListPriceModifierRel(
				long commercePriceModifierId, long commercePriceListId) {

		return getService().fetchCommercePriceListPriceModifierRel(
			commercePriceModifierId, commercePriceListId);
	}

	/**
	 * Returns the commerce price list price modifier rel with the matching UUID and company.
	 *
	 * @param uuid the commerce price list price modifier rel's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
			fetchCommercePriceListPriceModifierRelByUuidAndCompanyId(
				String uuid, long companyId) {

		return getService().
			fetchCommercePriceListPriceModifierRelByUuidAndCompanyId(
				uuid, companyId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the commerce price list price modifier rel with the primary key.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel
	 * @throws PortalException if a commerce price list price modifier rel with the primary key could not be found
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
				getCommercePriceListPriceModifierRel(
					long commercePriceListPriceModifierRelId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommercePriceListPriceModifierRel(
			commercePriceListPriceModifierRelId);
	}

	/**
	 * Returns the commerce price list price modifier rel with the matching UUID and company.
	 *
	 * @param uuid the commerce price list price modifier rel's UUID
	 * @param companyId the primary key of the company
	 * @return the matching commerce price list price modifier rel
	 * @throws PortalException if a matching commerce price list price modifier rel could not be found
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
				getCommercePriceListPriceModifierRelByUuidAndCompanyId(
					String uuid, long companyId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().
			getCommercePriceListPriceModifierRelByUuidAndCompanyId(
				uuid, companyId);
	}

	/**
	 * Returns a range of all the commerce price list price modifier rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.price.list.model.impl.CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @return the range of commerce price list price modifier rels
	 */
	public static java.util.List
		<com.liferay.commerce.price.list.model.
			CommercePriceListPriceModifierRel>
				getCommercePriceListPriceModifierRels(int start, int end) {

		return getService().getCommercePriceListPriceModifierRels(start, end);
	}

	public static java.util.List
		<com.liferay.commerce.price.list.model.
			CommercePriceListPriceModifierRel>
				getCommercePriceListPriceModifierRels(
					long commercePriceListId) {

		return getService().getCommercePriceListPriceModifierRels(
			commercePriceListId);
	}

	/**
	 * Returns the number of commerce price list price modifier rels.
	 *
	 * @return the number of commerce price list price modifier rels
	 */
	public static int getCommercePriceListPriceModifierRelsCount() {
		return getService().getCommercePriceListPriceModifierRelsCount();
	}

	public static com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery
		getExportActionableDynamicQuery(
			com.liferay.exportimport.kernel.lar.PortletDataContext
				portletDataContext) {

		return getService().getExportActionableDynamicQuery(portletDataContext);
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

	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the commerce price list price modifier rel in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceListPriceModifierRel the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel that was updated
	 */
	public static
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
			updateCommercePriceListPriceModifierRel(
				com.liferay.commerce.price.list.model.
					CommercePriceListPriceModifierRel
						commercePriceListPriceModifierRel) {

		return getService().updateCommercePriceListPriceModifierRel(
			commercePriceListPriceModifierRel);
	}

	public static CommercePriceListPriceModifierRelLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CommercePriceListPriceModifierRelLocalService,
		 CommercePriceListPriceModifierRelLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CommercePriceListPriceModifierRelLocalService.class);

		ServiceTracker
			<CommercePriceListPriceModifierRelLocalService,
			 CommercePriceListPriceModifierRelLocalService> serviceTracker =
				new ServiceTracker
					<CommercePriceListPriceModifierRelLocalService,
					 CommercePriceListPriceModifierRelLocalService>(
						 bundle.getBundleContext(),
						 CommercePriceListPriceModifierRelLocalService.class,
						 null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}