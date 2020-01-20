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

package com.liferay.commerce.pricing.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for CommercePricingClassRel. This utility wraps
 * <code>com.liferay.commerce.pricing.service.impl.CommercePricingClassRelLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRelLocalService
 * @generated
 */
public class CommercePricingClassRelLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.pricing.service.impl.CommercePricingClassRelLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the commerce pricing class rel to the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 * @return the commerce pricing class rel that was added
	 */
	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
		addCommercePricingClassRel(
			com.liferay.commerce.pricing.model.CommercePricingClassRel
				commercePricingClassRel) {

		return getService().addCommercePricingClassRel(commercePricingClassRel);
	}

	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
			addCommercePricingClassRel(
				long commercePricingClassId, String className, long classPK,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCommercePricingClassRel(
			commercePricingClassId, className, classPK, serviceContext);
	}

	/**
	 * Creates a new commerce pricing class rel with the primary key. Does not add the commerce pricing class rel to the database.
	 *
	 * @param commercePricingClassRelId the primary key for the new commerce pricing class rel
	 * @return the new commerce pricing class rel
	 */
	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
		createCommercePricingClassRel(long commercePricingClassRelId) {

		return getService().createCommercePricingClassRel(
			commercePricingClassRelId);
	}

	/**
	 * Deletes the commerce pricing class rel from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws PortalException
	 */
	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
			deleteCommercePricingClassRel(
				com.liferay.commerce.pricing.model.CommercePricingClassRel
					commercePricingClassRel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteCommercePricingClassRel(
			commercePricingClassRel);
	}

	/**
	 * Deletes the commerce pricing class rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws PortalException if a commerce pricing class rel with the primary key could not be found
	 */
	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
			deleteCommercePricingClassRel(long commercePricingClassRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteCommercePricingClassRel(
			commercePricingClassRelId);
	}

	public static void deleteCommercePricingClassRels(
			long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteCommercePricingClassRels(commercePricingClassId);
	}

	public static void deleteCommercePricingClassRels(
			String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteCommercePricingClassRels(className, classPK);
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.pricing.model.impl.CommercePricingClassRelModelImpl</code>.
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
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.pricing.model.impl.CommercePricingClassRelModelImpl</code>.
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

	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
		fetchCommercePricingClassRel(long commercePricingClassRelId) {

		return getService().fetchCommercePricingClassRel(
			commercePricingClassRelId);
	}

	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
		fetchCommercePricingClassRel(String className, long classPK) {

		return getService().fetchCommercePricingClassRel(className, classPK);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static long[] getClassPKs(
		long commercePricingClassId, String className) {

		return getService().getClassPKs(commercePricingClassId, className);
	}

	/**
	 * Returns the commerce pricing class rel with the primary key.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel
	 * @throws PortalException if a commerce pricing class rel with the primary key could not be found
	 */
	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
			getCommercePricingClassRel(long commercePricingClassRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getCommercePricingClassRel(
			commercePricingClassRelId);
	}

	/**
	 * Returns a range of all the commerce pricing class rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.pricing.model.impl.CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @return the range of commerce pricing class rels
	 */
	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
			getCommercePricingClassRels(int start, int end) {

		return getService().getCommercePricingClassRels(start, end);
	}

	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
			getCommercePricingClassRels(
				long commercePricingClassId, String className) {

		return getService().getCommercePricingClassRels(
			commercePricingClassId, className);
	}

	public static java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
			getCommercePricingClassRels(
				long commercePricingClassId, String className, int start,
				int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.commerce.pricing.model.CommercePricingClassRel>
						orderByComparator) {

		return getService().getCommercePricingClassRels(
			commercePricingClassId, className, start, end, orderByComparator);
	}

	/**
	 * Returns the number of commerce pricing class rels.
	 *
	 * @return the number of commerce pricing class rels
	 */
	public static int getCommercePricingClassRelsCount() {
		return getService().getCommercePricingClassRelsCount();
	}

	public static int getCommercePricingClassRelsCount(
		long commercePricingClassId, String className) {

		return getService().getCommercePricingClassRelsCount(
			commercePricingClassId, className);
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
	 * Updates the commerce pricing class rel in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 * @return the commerce pricing class rel that was updated
	 */
	public static com.liferay.commerce.pricing.model.CommercePricingClassRel
		updateCommercePricingClassRel(
			com.liferay.commerce.pricing.model.CommercePricingClassRel
				commercePricingClassRel) {

		return getService().updateCommercePricingClassRel(
			commercePricingClassRel);
	}

	public static CommercePricingClassRelLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CommercePricingClassRelLocalService,
		 CommercePricingClassRelLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CommercePricingClassRelLocalService.class);

		ServiceTracker
			<CommercePricingClassRelLocalService,
			 CommercePricingClassRelLocalService> serviceTracker =
				new ServiceTracker
					<CommercePricingClassRelLocalService,
					 CommercePricingClassRelLocalService>(
						 bundle.getBundleContext(),
						 CommercePricingClassRelLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}