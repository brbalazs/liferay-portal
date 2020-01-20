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

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.pricing.exception.NoSuchPricingClassRelException;
import com.liferay.commerce.pricing.model.CommercePricingClassRel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import java.io.Serializable;

import java.util.Map;
import java.util.Set;

/**
 * The persistence interface for the commerce pricing class rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRelUtil
 * @generated
 */
@ProviderType
public interface CommercePricingClassRelPersistence
	extends BasePersistence<CommercePricingClassRel> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CommercePricingClassRelUtil} to access the commerce pricing class rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */
	@Override
	public Map<Serializable, CommercePricingClassRel> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys);

	/**
	 * Returns all the commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @return the matching commerce pricing class rels
	 */
	public java.util.List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId);

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
	public java.util.List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end);

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
	public java.util.List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

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
	public java.util.List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel findByCommercePricingClassId_First(
			long commercePricingClassId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel fetchByCommercePricingClassId_First(
		long commercePricingClassId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel findByCommercePricingClassId_Last(
			long commercePricingClassId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel fetchByCommercePricingClassId_Last(
		long commercePricingClassId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

	/**
	 * Returns the commerce pricing class rels before and after the current commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassRelId the primary key of the current commerce pricing class rel
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public CommercePricingClassRel[] findByCommercePricingClassId_PrevAndNext(
			long commercePricingClassRelId, long commercePricingClassId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Removes all the commerce pricing class rels where commercePricingClassId = &#63; from the database.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 */
	public void removeByCommercePricingClassId(long commercePricingClassId);

	/**
	 * Returns the number of commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @return the number of matching commerce pricing class rels
	 */
	public int countByCommercePricingClassId(long commercePricingClassId);

	/**
	 * Returns all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @return the matching commerce pricing class rels
	 */
	public java.util.List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId);

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
	public java.util.List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end);

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
	public java.util.List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

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
	public java.util.List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel findByCPC_CN_First(
			long commercePricingClassId, long classNameId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel fetchByCPC_CN_First(
		long commercePricingClassId, long classNameId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel findByCPC_CN_Last(
			long commercePricingClassId, long classNameId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel fetchByCPC_CN_Last(
		long commercePricingClassId, long classNameId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

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
	public CommercePricingClassRel[] findByCPC_CN_PrevAndNext(
			long commercePricingClassRelId, long commercePricingClassId,
			long classNameId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Removes all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63; from the database.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 */
	public void removeByCPC_CN(long commercePricingClassId, long classNameId);

	/**
	 * Returns the number of commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @return the number of matching commerce pricing class rels
	 */
	public int countByCPC_CN(long commercePricingClassId, long classNameId);

	/**
	 * Returns all the commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching commerce pricing class rels
	 */
	public java.util.List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK);

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
	public java.util.List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end);

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
	public java.util.List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

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
	public java.util.List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel findByCN_CPK_First(
			long classNameId, long classPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Returns the first commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel fetchByCN_CPK_First(
		long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

	/**
	 * Returns the last commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel findByCN_CPK_Last(
			long classNameId, long classPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Returns the last commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	public CommercePricingClassRel fetchByCN_CPK_Last(
		long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

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
	public CommercePricingClassRel[] findByCN_CPK_PrevAndNext(
			long commercePricingClassRelId, long classNameId, long classPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException;

	/**
	 * Removes all the commerce pricing class rels where classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 */
	public void removeByCN_CPK(long classNameId, long classPK);

	/**
	 * Returns the number of commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the number of matching commerce pricing class rels
	 */
	public int countByCN_CPK(long classNameId, long classPK);

	/**
	 * Caches the commerce pricing class rel in the entity cache if it is enabled.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 */
	public void cacheResult(CommercePricingClassRel commercePricingClassRel);

	/**
	 * Caches the commerce pricing class rels in the entity cache if it is enabled.
	 *
	 * @param commercePricingClassRels the commerce pricing class rels
	 */
	public void cacheResult(
		java.util.List<CommercePricingClassRel> commercePricingClassRels);

	/**
	 * Creates a new commerce pricing class rel with the primary key. Does not add the commerce pricing class rel to the database.
	 *
	 * @param commercePricingClassRelId the primary key for the new commerce pricing class rel
	 * @return the new commerce pricing class rel
	 */
	public CommercePricingClassRel create(long commercePricingClassRelId);

	/**
	 * Removes the commerce pricing class rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public CommercePricingClassRel remove(long commercePricingClassRelId)
		throws NoSuchPricingClassRelException;

	public CommercePricingClassRel updateImpl(
		CommercePricingClassRel commercePricingClassRel);

	/**
	 * Returns the commerce pricing class rel with the primary key or throws a <code>NoSuchPricingClassRelException</code> if it could not be found.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	public CommercePricingClassRel findByPrimaryKey(
			long commercePricingClassRelId)
		throws NoSuchPricingClassRelException;

	/**
	 * Returns the commerce pricing class rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel, or <code>null</code> if a commerce pricing class rel with the primary key could not be found
	 */
	public CommercePricingClassRel fetchByPrimaryKey(
		long commercePricingClassRelId);

	/**
	 * Returns all the commerce pricing class rels.
	 *
	 * @return the commerce pricing class rels
	 */
	public java.util.List<CommercePricingClassRel> findAll();

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
	public java.util.List<CommercePricingClassRel> findAll(int start, int end);

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
	public java.util.List<CommercePricingClassRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator);

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
	public java.util.List<CommercePricingClassRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the commerce pricing class rels from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of commerce pricing class rels.
	 *
	 * @return the number of commerce pricing class rels
	 */
	public int countAll();

}