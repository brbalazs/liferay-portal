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

package com.liferay.commerce.pricing.service.persistence;

import com.liferay.commerce.pricing.model.CommercePricingClassRel;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The persistence utility for the commerce pricing class rel service. This utility wraps <code>com.liferay.commerce.pricing.service.persistence.impl.CommercePricingClassRelPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRelPersistence
 * @generated
 */
public class CommercePricingClassRelUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(
		CommercePricingClassRel commercePricingClassRel) {

		getPersistence().clearCache(commercePricingClassRel);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, CommercePricingClassRel> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<CommercePricingClassRel> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<CommercePricingClassRel> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<CommercePricingClassRel> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static CommercePricingClassRel update(
		CommercePricingClassRel commercePricingClassRel) {

		return getPersistence().update(commercePricingClassRel);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static CommercePricingClassRel update(
		CommercePricingClassRel commercePricingClassRel,
		ServiceContext serviceContext) {

		return getPersistence().update(commercePricingClassRel, serviceContext);
	}

	/**
	 * Returns all the commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @return the matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId) {

		return getPersistence().findByCommercePricingClassId(
			commercePricingClassId);
	}

	/**
	 * Returns a range of all the commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @return the range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end) {

		return getPersistence().findByCommercePricingClassId(
			commercePricingClassId, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().findByCommercePricingClassId(
			commercePricingClassId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCommercePricingClassId(
			commercePricingClassId, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel findByCommercePricingClassId_First(
			long commercePricingClassId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCommercePricingClassId_First(
			commercePricingClassId, orderByComparator);
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel fetchByCommercePricingClassId_First(
		long commercePricingClassId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().fetchByCommercePricingClassId_First(
			commercePricingClassId, orderByComparator);
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel findByCommercePricingClassId_Last(
			long commercePricingClassId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCommercePricingClassId_Last(
			commercePricingClassId, orderByComparator);
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel fetchByCommercePricingClassId_Last(
		long commercePricingClassId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().fetchByCommercePricingClassId_Last(
			commercePricingClassId, orderByComparator);
	}

	/**
	 * Returns the commerce pricing class rels before and after the current commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassRelId the primary key of the current commerce pricing class rel
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public static CommercePricingClassRel[]
			findByCommercePricingClassId_PrevAndNext(
				long commercePricingClassRelId, long commercePricingClassId,
				OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCommercePricingClassId_PrevAndNext(
			commercePricingClassRelId, commercePricingClassId,
			orderByComparator);
	}

	/**
	 * Removes all the commerce pricing class rels where commercePricingClassId = &#63; from the database.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 */
	public static void removeByCommercePricingClassId(
		long commercePricingClassId) {

		getPersistence().removeByCommercePricingClassId(commercePricingClassId);
	}

	/**
	 * Returns the number of commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @return the number of matching commerce pricing class rels
	 */
	public static int countByCommercePricingClassId(
		long commercePricingClassId) {

		return getPersistence().countByCommercePricingClassId(
			commercePricingClassId);
	}

	/**
	 * Returns all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @return the matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId) {

		return getPersistence().findByCPC_CN(
			commercePricingClassId, classNameId);
	}

	/**
	 * Returns a range of all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @return the range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end) {

		return getPersistence().findByCPC_CN(
			commercePricingClassId, classNameId, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().findByCPC_CN(
			commercePricingClassId, classNameId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCPC_CN(
			commercePricingClassId, classNameId, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel findByCPC_CN_First(
			long commercePricingClassId, long classNameId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCPC_CN_First(
			commercePricingClassId, classNameId, orderByComparator);
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel fetchByCPC_CN_First(
		long commercePricingClassId, long classNameId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().fetchByCPC_CN_First(
			commercePricingClassId, classNameId, orderByComparator);
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel findByCPC_CN_Last(
			long commercePricingClassId, long classNameId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCPC_CN_Last(
			commercePricingClassId, classNameId, orderByComparator);
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel fetchByCPC_CN_Last(
		long commercePricingClassId, long classNameId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().fetchByCPC_CN_Last(
			commercePricingClassId, classNameId, orderByComparator);
	}

	/**
	 * Returns the commerce pricing class rels before and after the current commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassRelId the primary key of the current commerce pricing class rel
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public static CommercePricingClassRel[] findByCPC_CN_PrevAndNext(
			long commercePricingClassRelId, long commercePricingClassId,
			long classNameId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCPC_CN_PrevAndNext(
			commercePricingClassRelId, commercePricingClassId, classNameId,
			orderByComparator);
	}

	/**
	 * Removes all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63; from the database.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 */
	public static void removeByCPC_CN(
		long commercePricingClassId, long classNameId) {

		getPersistence().removeByCPC_CN(commercePricingClassId, classNameId);
	}

	/**
	 * Returns the number of commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @return the number of matching commerce pricing class rels
	 */
	public static int countByCPC_CN(
		long commercePricingClassId, long classNameId) {

		return getPersistence().countByCPC_CN(
			commercePricingClassId, classNameId);
	}

	/**
	 * Returns all the commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK) {

		return getPersistence().findByCN_CPK(classNameId, classPK);
	}

	/**
	 * Returns a range of all the commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @return the range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end) {

		return getPersistence().findByCN_CPK(classNameId, classPK, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().findByCN_CPK(
			classNameId, classPK, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCN_CPK(
			classNameId, classPK, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel findByCN_CPK_First(
			long classNameId, long classPK,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCN_CPK_First(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel fetchByCN_CPK_First(
		long classNameId, long classPK,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().fetchByCN_CPK_First(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel findByCN_CPK_Last(
			long classNameId, long classPK,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCN_CPK_Last(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public static CommercePricingClassRel fetchByCN_CPK_Last(
		long classNameId, long classPK,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().fetchByCN_CPK_Last(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the commerce pricing class rels before and after the current commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param commercePricingClassRelId the primary key of the current commerce pricing class rel
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public static CommercePricingClassRel[] findByCN_CPK_PrevAndNext(
			long commercePricingClassRelId, long classNameId, long classPK,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByCN_CPK_PrevAndNext(
			commercePricingClassRelId, classNameId, classPK, orderByComparator);
	}

	/**
	 * Removes all the commerce pricing class rels where classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 */
	public static void removeByCN_CPK(long classNameId, long classPK) {
		getPersistence().removeByCN_CPK(classNameId, classPK);
	}

	/**
	 * Returns the number of commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the number of matching commerce pricing class rels
	 */
	public static int countByCN_CPK(long classNameId, long classPK) {
		return getPersistence().countByCN_CPK(classNameId, classPK);
	}

	/**
	 * Caches the commerce pricing class rel in the entity cache if it is enabled.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 */
	public static void cacheResult(
		CommercePricingClassRel commercePricingClassRel) {

		getPersistence().cacheResult(commercePricingClassRel);
	}

	/**
	 * Caches the commerce pricing class rels in the entity cache if it is enabled.
	 *
	 * @param commercePricingClassRels the commerce pricing class rels
	 */
	public static void cacheResult(
		List<CommercePricingClassRel> commercePricingClassRels) {

		getPersistence().cacheResult(commercePricingClassRels);
	}

	/**
	 * Creates a new commerce pricing class rel with the primary key. Does not add the commerce pricing class rel to the database.
	 *
	 * @param commercePricingClassRelId the primary key for the new commerce pricing class rel
	 * @return the new commerce pricing class rel
	 */
	public static CommercePricingClassRel create(
		long commercePricingClassRelId) {

		return getPersistence().create(commercePricingClassRelId);
	}

	/**
	 * Removes the commerce pricing class rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public static CommercePricingClassRel remove(long commercePricingClassRelId)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().remove(commercePricingClassRelId);
	}

	public static CommercePricingClassRel updateImpl(
		CommercePricingClassRel commercePricingClassRel) {

		return getPersistence().updateImpl(commercePricingClassRel);
	}

	/**
	 * Returns the commerce pricing class rel with the primary key or throws a <code>NoSuchPricingClassRelException</code> if it could not be found.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public static CommercePricingClassRel findByPrimaryKey(
			long commercePricingClassRelId)
		throws com.liferay.commerce.pricing.exception.
			NoSuchPricingClassRelException {

		return getPersistence().findByPrimaryKey(commercePricingClassRelId);
	}

	/**
	 * Returns the commerce pricing class rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel, or <code>null</code> if a commerce pricing class rel with the primary key could not be found
	 */
	public static CommercePricingClassRel fetchByPrimaryKey(
		long commercePricingClassRelId) {

		return getPersistence().fetchByPrimaryKey(commercePricingClassRelId);
	}

	/**
	 * Returns all the commerce pricing class rels.
	 *
	 * @return the commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the commerce pricing class rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @return the range of commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findAll(
		int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce pricing class rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePricingClassRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce pricing class rels
	 * @param end the upper bound of the range of commerce pricing class rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of commerce pricing class rels
	 */
	public static List<CommercePricingClassRel> findAll(
		int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the commerce pricing class rels from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of commerce pricing class rels.
	 *
	 * @return the number of commerce pricing class rels
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static CommercePricingClassRelPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<CommercePricingClassRelPersistence, CommercePricingClassRelPersistence>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			CommercePricingClassRelPersistence.class);

		ServiceTracker
			<CommercePricingClassRelPersistence,
			 CommercePricingClassRelPersistence> serviceTracker =
				new ServiceTracker
					<CommercePricingClassRelPersistence,
					 CommercePricingClassRelPersistence>(
						 bundle.getBundleContext(),
						 CommercePricingClassRelPersistence.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}