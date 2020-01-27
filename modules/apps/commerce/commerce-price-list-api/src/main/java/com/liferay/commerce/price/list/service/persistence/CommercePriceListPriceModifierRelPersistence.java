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

package com.liferay.commerce.price.list.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.price.list.exception.NoSuchPriceListPriceModifierRelException;
import com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import java.io.Serializable;

import java.util.Map;
import java.util.Set;

/**
 * The persistence interface for the commerce price list price modifier rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CommercePriceListPriceModifierRelUtil
 * @generated
 */
@ProviderType
public interface CommercePriceListPriceModifierRelPersistence
	extends BasePersistence<CommercePriceListPriceModifierRel> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CommercePriceListPriceModifierRelUtil} to access the commerce price list price modifier rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */
	@Override
	public Map<Serializable, CommercePriceListPriceModifierRel>
		fetchByPrimaryKeys(Set<Serializable> primaryKeys);

	/**
	 * Returns all the commerce price list price modifier rels where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid(
		String uuid);

	/**
	 * Returns a range of all the commerce price list price modifier rels where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @return the range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid(
		String uuid, int start, int end);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns the commerce price list price modifier rels before and after the current commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the current commerce price list price modifier rel
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	public CommercePriceListPriceModifierRel[] findByUuid_PrevAndNext(
			long commercePriceListPriceModifierRelId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Removes all the commerce price list price modifier rels where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of commerce price list price modifier rels where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching commerce price list price modifier rels
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns all the commerce price list price modifier rels where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId);

	/**
	 * Returns a range of all the commerce price list price modifier rels where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @return the range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns the commerce price list price modifier rels before and after the current commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the current commerce price list price modifier rel
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	public CommercePriceListPriceModifierRel[] findByUuid_C_PrevAndNext(
			long commercePriceListPriceModifierRelId, String uuid,
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Removes all the commerce price list price modifier rels where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of commerce price list price modifier rels where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching commerce price list price modifier rels
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns all the commerce price list price modifier rels where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @return the matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel>
		findByCommercePriceListId(long commercePriceListId);

	/**
	 * Returns a range of all the commerce price list price modifier rels where commercePriceListId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @return the range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel>
		findByCommercePriceListId(long commercePriceListId, int start, int end);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels where commercePriceListId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel>
		findByCommercePriceListId(
			long commercePriceListId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels where commercePriceListId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel>
		findByCommercePriceListId(
			long commercePriceListId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator,
			boolean useFinderCache);

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel findByCommercePriceListId_First(
			long commercePriceListId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByCommercePriceListId_First(
		long commercePriceListId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel findByCommercePriceListId_Last(
			long commercePriceListId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByCommercePriceListId_Last(
		long commercePriceListId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns the commerce price list price modifier rels before and after the current commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the current commerce price list price modifier rel
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	public CommercePriceListPriceModifierRel[]
			findByCommercePriceListId_PrevAndNext(
				long commercePriceListPriceModifierRelId,
				long commercePriceListId,
				com.liferay.portal.kernel.util.OrderByComparator
					<CommercePriceListPriceModifierRel> orderByComparator)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Removes all the commerce price list price modifier rels where commercePriceListId = &#63; from the database.
	 *
	 * @param commercePriceListId the commerce price list ID
	 */
	public void removeByCommercePriceListId(long commercePriceListId);

	/**
	 * Returns the number of commerce price list price modifier rels where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @return the number of matching commerce price list price modifier rels
	 */
	public int countByCommercePriceListId(long commercePriceListId);

	/**
	 * Returns the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; or throws a <code>NoSuchPriceListPriceModifierRelException</code> if it could not be found.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel findByC_C(
			long commercePriceModifierId, long commercePriceListId)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByC_C(
		long commercePriceModifierId, long commercePriceListId);

	/**
	 * Returns the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByC_C(
		long commercePriceModifierId, long commercePriceListId,
		boolean useFinderCache);

	/**
	 * Removes the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; from the database.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the commerce price list price modifier rel that was removed
	 */
	public CommercePriceListPriceModifierRel removeByC_C(
			long commercePriceModifierId, long commercePriceListId)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the number of commerce price list price modifier rels where commercePriceModifierId = &#63; and commercePriceListId = &#63;.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the number of matching commerce price list price modifier rels
	 */
	public int countByC_C(
		long commercePriceModifierId, long commercePriceListId);

	/**
	 * Caches the commerce price list price modifier rel in the entity cache if it is enabled.
	 *
	 * @param commercePriceListPriceModifierRel the commerce price list price modifier rel
	 */
	public void cacheResult(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel);

	/**
	 * Caches the commerce price list price modifier rels in the entity cache if it is enabled.
	 *
	 * @param commercePriceListPriceModifierRels the commerce price list price modifier rels
	 */
	public void cacheResult(
		java.util.List<CommercePriceListPriceModifierRel>
			commercePriceListPriceModifierRels);

	/**
	 * Creates a new commerce price list price modifier rel with the primary key. Does not add the commerce price list price modifier rel to the database.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key for the new commerce price list price modifier rel
	 * @return the new commerce price list price modifier rel
	 */
	public CommercePriceListPriceModifierRel create(
		long commercePriceListPriceModifierRelId);

	/**
	 * Removes the commerce price list price modifier rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel that was removed
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	public CommercePriceListPriceModifierRel remove(
			long commercePriceListPriceModifierRelId)
		throws NoSuchPriceListPriceModifierRelException;

	public CommercePriceListPriceModifierRel updateImpl(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel);

	/**
	 * Returns the commerce price list price modifier rel with the primary key or throws a <code>NoSuchPriceListPriceModifierRelException</code> if it could not be found.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	public CommercePriceListPriceModifierRel findByPrimaryKey(
			long commercePriceListPriceModifierRelId)
		throws NoSuchPriceListPriceModifierRelException;

	/**
	 * Returns the commerce price list price modifier rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel, or <code>null</code> if a commerce price list price modifier rel with the primary key could not be found
	 */
	public CommercePriceListPriceModifierRel fetchByPrimaryKey(
		long commercePriceListPriceModifierRelId);

	/**
	 * Returns all the commerce price list price modifier rels.
	 *
	 * @return the commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findAll();

	/**
	 * Returns a range of all the commerce price list price modifier rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @return the range of commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findAll(
		int start, int end);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator);

	/**
	 * Returns an ordered range of all the commerce price list price modifier rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommercePriceListPriceModifierRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce price list price modifier rels
	 * @param end the upper bound of the range of commerce price list price modifier rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of commerce price list price modifier rels
	 */
	public java.util.List<CommercePriceListPriceModifierRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommercePriceListPriceModifierRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the commerce price list price modifier rels from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of commerce price list price modifier rels.
	 *
	 * @return the number of commerce price list price modifier rels
	 */
	public int countAll();

	@Override
	public Set<String> getBadColumnNames();

}