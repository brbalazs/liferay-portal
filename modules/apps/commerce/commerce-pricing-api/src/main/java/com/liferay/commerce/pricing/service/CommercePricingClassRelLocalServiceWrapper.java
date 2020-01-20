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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommercePricingClassRelLocalService}.
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRelLocalService
 * @generated
 */
public class CommercePricingClassRelLocalServiceWrapper
	implements CommercePricingClassRelLocalService,
			   ServiceWrapper<CommercePricingClassRelLocalService> {

	public CommercePricingClassRelLocalServiceWrapper(
		CommercePricingClassRelLocalService
			commercePricingClassRelLocalService) {

		_commercePricingClassRelLocalService =
			commercePricingClassRelLocalService;
	}

	/**
	 * Adds the commerce pricing class rel to the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 * @return the commerce pricing class rel that was added
	 */
	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
		addCommercePricingClassRel(
			com.liferay.commerce.pricing.model.CommercePricingClassRel
				commercePricingClassRel) {

		return _commercePricingClassRelLocalService.addCommercePricingClassRel(
			commercePricingClassRel);
	}

	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
			addCommercePricingClassRel(
				long commercePricingClassId, String className, long classPK,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelLocalService.addCommercePricingClassRel(
			commercePricingClassId, className, classPK, serviceContext);
	}

	/**
	 * Creates a new commerce pricing class rel with the primary key. Does not add the commerce pricing class rel to the database.
	 *
	 * @param commercePricingClassRelId the primary key for the new commerce pricing class rel
	 * @return the new commerce pricing class rel
	 */
	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
		createCommercePricingClassRel(long commercePricingClassRelId) {

		return _commercePricingClassRelLocalService.
			createCommercePricingClassRel(commercePricingClassRelId);
	}

	/**
	 * Deletes the commerce pricing class rel from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws PortalException
	 */
	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
			deleteCommercePricingClassRel(
				com.liferay.commerce.pricing.model.CommercePricingClassRel
					commercePricingClassRel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelLocalService.
			deleteCommercePricingClassRel(commercePricingClassRel);
	}

	/**
	 * Deletes the commerce pricing class rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws PortalException if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
			deleteCommercePricingClassRel(long commercePricingClassRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelLocalService.
			deleteCommercePricingClassRel(commercePricingClassRelId);
	}

	@Override
	public void deleteCommercePricingClassRels(long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_commercePricingClassRelLocalService.deleteCommercePricingClassRels(
			commercePricingClassId);
	}

	@Override
	public void deleteCommercePricingClassRels(String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException {

		_commercePricingClassRelLocalService.deleteCommercePricingClassRels(
			className, classPK);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _commercePricingClassRelLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _commercePricingClassRelLocalService.dynamicQuery(dynamicQuery);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _commercePricingClassRelLocalService.dynamicQuery(
			dynamicQuery, start, end);
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
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _commercePricingClassRelLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _commercePricingClassRelLocalService.dynamicQueryCount(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _commercePricingClassRelLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
		fetchCommercePricingClassRel(long commercePricingClassRelId) {

		return _commercePricingClassRelLocalService.
			fetchCommercePricingClassRel(commercePricingClassRelId);
	}

	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
		fetchCommercePricingClassRel(String className, long classPK) {

		return _commercePricingClassRelLocalService.
			fetchCommercePricingClassRel(className, classPK);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _commercePricingClassRelLocalService.getActionableDynamicQuery();
	}

	@Override
	public long[] getClassPKs(long commercePricingClassId, String className) {
		return _commercePricingClassRelLocalService.getClassPKs(
			commercePricingClassId, className);
	}

	/**
	 * Returns the commerce pricing class rel with the primary key.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel
	 * @throws PortalException if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
			getCommercePricingClassRel(long commercePricingClassRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelLocalService.getCommercePricingClassRel(
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
	@Override
	public java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
			getCommercePricingClassRels(int start, int end) {

		return _commercePricingClassRelLocalService.getCommercePricingClassRels(
			start, end);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
			getCommercePricingClassRels(
				long commercePricingClassId, String className) {

		return _commercePricingClassRelLocalService.getCommercePricingClassRels(
			commercePricingClassId, className);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
			getCommercePricingClassRels(
				long commercePricingClassId, String className, int start,
				int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.commerce.pricing.model.CommercePricingClassRel>
						orderByComparator) {

		return _commercePricingClassRelLocalService.getCommercePricingClassRels(
			commercePricingClassId, className, start, end, orderByComparator);
	}

	/**
	 * Returns the number of commerce pricing class rels.
	 *
	 * @return the number of commerce pricing class rels
	 */
	@Override
	public int getCommercePricingClassRelsCount() {
		return _commercePricingClassRelLocalService.
			getCommercePricingClassRelsCount();
	}

	@Override
	public int getCommercePricingClassRelsCount(
		long commercePricingClassId, String className) {

		return _commercePricingClassRelLocalService.
			getCommercePricingClassRelsCount(commercePricingClassId, className);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _commercePricingClassRelLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _commercePricingClassRelLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelLocalService.getPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Updates the commerce pricing class rel in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 * @return the commerce pricing class rel that was updated
	 */
	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
		updateCommercePricingClassRel(
			com.liferay.commerce.pricing.model.CommercePricingClassRel
				commercePricingClassRel) {

		return _commercePricingClassRelLocalService.
			updateCommercePricingClassRel(commercePricingClassRel);
	}

	@Override
	public CommercePricingClassRelLocalService getWrappedService() {
		return _commercePricingClassRelLocalService;
	}

	@Override
	public void setWrappedService(
		CommercePricingClassRelLocalService
			commercePricingClassRelLocalService) {

		_commercePricingClassRelLocalService =
			commercePricingClassRelLocalService;
	}

	private CommercePricingClassRelLocalService
		_commercePricingClassRelLocalService;

}