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

package com.liferay.commerce.price.list.service.persistence.impl;

import com.liferay.commerce.price.list.exception.NoSuchPriceListPriceModifierRelException;
import com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel;
import com.liferay.commerce.price.list.model.impl.CommercePriceListPriceModifierRelImpl;
import com.liferay.commerce.price.list.model.impl.CommercePriceListPriceModifierRelModelImpl;
import com.liferay.commerce.price.list.service.persistence.CommercePriceListPriceModifierRelPersistence;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The persistence implementation for the commerce price list price modifier rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @generated
 */
public class CommercePriceListPriceModifierRelPersistenceImpl
	extends BasePersistenceImpl<CommercePriceListPriceModifierRel>
	implements CommercePriceListPriceModifierRelPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CommercePriceListPriceModifierRelUtil</code> to access the commerce price list price modifier rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CommercePriceListPriceModifierRelImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the commerce price list price modifier rels where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching commerce price list price modifier rels
	 */
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<CommercePriceListPriceModifierRel> list = null;

		if (useFinderCache) {
			list =
				(List<CommercePriceListPriceModifierRel>)finderCache.getResult(
					finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceListPriceModifierRel
						commercePriceListPriceModifierRel : list) {

					if (!uuid.equals(
							commercePriceListPriceModifierRel.getUuid())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(
					CommercePriceListPriceModifierRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				list = (List<CommercePriceListPriceModifierRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByUuid_First(
			String uuid,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByUuid_First(uuid, orderByComparator);

		if (commercePriceListPriceModifierRel != null) {
			return commercePriceListPriceModifierRel;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append("}");

		throw new NoSuchPriceListPriceModifierRelException(msg.toString());
	}

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByUuid_First(
		String uuid,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		List<CommercePriceListPriceModifierRel> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByUuid_Last(
			String uuid,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByUuid_Last(uuid, orderByComparator);

		if (commercePriceListPriceModifierRel != null) {
			return commercePriceListPriceModifierRel;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append("}");

		throw new NoSuchPriceListPriceModifierRelException(msg.toString());
	}

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByUuid_Last(
		String uuid,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<CommercePriceListPriceModifierRel> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price list price modifier rels before and after the current commerce price list price modifier rel in the ordered set where uuid = &#63;.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the current commerce price list price modifier rel
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel[] findByUuid_PrevAndNext(
			long commercePriceListPriceModifierRelId, String uuid,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		uuid = Objects.toString(uuid, "");

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			findByPrimaryKey(commercePriceListPriceModifierRelId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceListPriceModifierRel[] array =
				new CommercePriceListPriceModifierRelImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, commercePriceListPriceModifierRel, uuid,
				orderByComparator, true);

			array[1] = commercePriceListPriceModifierRel;

			array[2] = getByUuid_PrevAndNext(
				session, commercePriceListPriceModifierRel, uuid,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceListPriceModifierRel getByUuid_PrevAndNext(
		Session session,
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel,
		String uuid,
		OrderByComparator<CommercePriceListPriceModifierRel> orderByComparator,
		boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			query.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			query.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(
				CommercePriceListPriceModifierRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindUuid) {
			qPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceListPriceModifierRel)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceListPriceModifierRel> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price list price modifier rels where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel :
					findByUuid(
						uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(commercePriceListPriceModifierRel);
		}
	}

	/**
	 * Returns the number of commerce price list price modifier rels where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching commerce price list price modifier rels
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"commercePriceListPriceModifierRel.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(commercePriceListPriceModifierRel.uuid IS NULL OR commercePriceListPriceModifierRel.uuid = '')";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the commerce price list price modifier rels where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching commerce price list price modifier rels
	 */
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId) {

		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<CommercePriceListPriceModifierRel> list = null;

		if (useFinderCache) {
			list =
				(List<CommercePriceListPriceModifierRel>)finderCache.getResult(
					finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceListPriceModifierRel
						commercePriceListPriceModifierRel : list) {

					if (!uuid.equals(
							commercePriceListPriceModifierRel.getUuid()) ||
						(companyId !=
							commercePriceListPriceModifierRel.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(
					CommercePriceListPriceModifierRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(companyId);

				list = (List<CommercePriceListPriceModifierRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByUuid_C_First(uuid, companyId, orderByComparator);

		if (commercePriceListPriceModifierRel != null) {
			return commercePriceListPriceModifierRel;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append("}");

		throw new NoSuchPriceListPriceModifierRelException(msg.toString());
	}

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		List<CommercePriceListPriceModifierRel> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByUuid_C_Last(uuid, companyId, orderByComparator);

		if (commercePriceListPriceModifierRel != null) {
			return commercePriceListPriceModifierRel;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append("}");

		throw new NoSuchPriceListPriceModifierRelException(msg.toString());
	}

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceListPriceModifierRel> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

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
	@Override
	public CommercePriceListPriceModifierRel[] findByUuid_C_PrevAndNext(
			long commercePriceListPriceModifierRelId, String uuid,
			long companyId,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		uuid = Objects.toString(uuid, "");

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			findByPrimaryKey(commercePriceListPriceModifierRelId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceListPriceModifierRel[] array =
				new CommercePriceListPriceModifierRelImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, commercePriceListPriceModifierRel, uuid, companyId,
				orderByComparator, true);

			array[1] = commercePriceListPriceModifierRel;

			array[2] = getByUuid_C_PrevAndNext(
				session, commercePriceListPriceModifierRel, uuid, companyId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceListPriceModifierRel getByUuid_C_PrevAndNext(
		Session session,
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel,
		String uuid, long companyId,
		OrderByComparator<CommercePriceListPriceModifierRel> orderByComparator,
		boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(4);
		}

		query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			query.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			query.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(
				CommercePriceListPriceModifierRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindUuid) {
			qPos.add(uuid);
		}

		qPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceListPriceModifierRel)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceListPriceModifierRel> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price list price modifier rels where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel :
					findByUuid_C(
						uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
						null)) {

			remove(commercePriceListPriceModifierRel);
		}
	}

	/**
	 * Returns the number of commerce price list price modifier rels where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching commerce price list price modifier rels
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"commercePriceListPriceModifierRel.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(commercePriceListPriceModifierRel.uuid IS NULL OR commercePriceListPriceModifierRel.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"commercePriceListPriceModifierRel.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByCommercePriceListId;
	private FinderPath _finderPathWithoutPaginationFindByCommercePriceListId;
	private FinderPath _finderPathCountByCommercePriceListId;

	/**
	 * Returns all the commerce price list price modifier rels where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @return the matching commerce price list price modifier rels
	 */
	@Override
	public List<CommercePriceListPriceModifierRel> findByCommercePriceListId(
		long commercePriceListId) {

		return findByCommercePriceListId(
			commercePriceListId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByCommercePriceListId(
		long commercePriceListId, int start, int end) {

		return findByCommercePriceListId(commercePriceListId, start, end, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByCommercePriceListId(
		long commercePriceListId, int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		return findByCommercePriceListId(
			commercePriceListId, start, end, orderByComparator, true);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findByCommercePriceListId(
		long commercePriceListId, int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCommercePriceListId;
				finderArgs = new Object[] {commercePriceListId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCommercePriceListId;
			finderArgs = new Object[] {
				commercePriceListId, start, end, orderByComparator
			};
		}

		List<CommercePriceListPriceModifierRel> list = null;

		if (useFinderCache) {
			list =
				(List<CommercePriceListPriceModifierRel>)finderCache.getResult(
					finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePriceListPriceModifierRel
						commercePriceListPriceModifierRel : list) {

					if (commercePriceListId !=
							commercePriceListPriceModifierRel.
								getCommercePriceListId()) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			query.append(
				_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(
					CommercePriceListPriceModifierRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePriceListId);

				list = (List<CommercePriceListPriceModifierRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByCommercePriceListId_First(
			long commercePriceListId,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByCommercePriceListId_First(
				commercePriceListId, orderByComparator);

		if (commercePriceListPriceModifierRel != null) {
			return commercePriceListPriceModifierRel;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("commercePriceListId=");
		msg.append(commercePriceListId);

		msg.append("}");

		throw new NoSuchPriceListPriceModifierRelException(msg.toString());
	}

	/**
	 * Returns the first commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByCommercePriceListId_First(
		long commercePriceListId,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		List<CommercePriceListPriceModifierRel> list =
			findByCommercePriceListId(
				commercePriceListId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByCommercePriceListId_Last(
			long commercePriceListId,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByCommercePriceListId_Last(
				commercePriceListId, orderByComparator);

		if (commercePriceListPriceModifierRel != null) {
			return commercePriceListPriceModifierRel;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("commercePriceListId=");
		msg.append(commercePriceListId);

		msg.append("}");

		throw new NoSuchPriceListPriceModifierRelException(msg.toString());
	}

	/**
	 * Returns the last commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByCommercePriceListId_Last(
		long commercePriceListId,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		int count = countByCommercePriceListId(commercePriceListId);

		if (count == 0) {
			return null;
		}

		List<CommercePriceListPriceModifierRel> list =
			findByCommercePriceListId(
				commercePriceListId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the commerce price list price modifier rels before and after the current commerce price list price modifier rel in the ordered set where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the current commerce price list price modifier rel
	 * @param commercePriceListId the commerce price list ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel[]
			findByCommercePriceListId_PrevAndNext(
				long commercePriceListPriceModifierRelId,
				long commercePriceListId,
				OrderByComparator<CommercePriceListPriceModifierRel>
					orderByComparator)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			findByPrimaryKey(commercePriceListPriceModifierRelId);

		Session session = null;

		try {
			session = openSession();

			CommercePriceListPriceModifierRel[] array =
				new CommercePriceListPriceModifierRelImpl[3];

			array[0] = getByCommercePriceListId_PrevAndNext(
				session, commercePriceListPriceModifierRel, commercePriceListId,
				orderByComparator, true);

			array[1] = commercePriceListPriceModifierRel;

			array[2] = getByCommercePriceListId_PrevAndNext(
				session, commercePriceListPriceModifierRel, commercePriceListId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePriceListPriceModifierRel
		getByCommercePriceListId_PrevAndNext(
			Session session,
			CommercePriceListPriceModifierRel commercePriceListPriceModifierRel,
			long commercePriceListId,
			OrderByComparator<CommercePriceListPriceModifierRel>
				orderByComparator,
			boolean previous) {

		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

		query.append(_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(
				CommercePriceListPriceModifierRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(commercePriceListId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePriceListPriceModifierRel)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<CommercePriceListPriceModifierRel> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce price list price modifier rels where commercePriceListId = &#63; from the database.
	 *
	 * @param commercePriceListId the commerce price list ID
	 */
	@Override
	public void removeByCommercePriceListId(long commercePriceListId) {
		for (CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel :
					findByCommercePriceListId(
						commercePriceListId, QueryUtil.ALL_POS,
						QueryUtil.ALL_POS, null)) {

			remove(commercePriceListPriceModifierRel);
		}
	}

	/**
	 * Returns the number of commerce price list price modifier rels where commercePriceListId = &#63;.
	 *
	 * @param commercePriceListId the commerce price list ID
	 * @return the number of matching commerce price list price modifier rels
	 */
	@Override
	public int countByCommercePriceListId(long commercePriceListId) {
		FinderPath finderPath = _finderPathCountByCommercePriceListId;

		Object[] finderArgs = new Object[] {commercePriceListId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			query.append(
				_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePriceListId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_COMMERCEPRICELISTID_COMMERCEPRICELISTID_2 =
			"commercePriceListPriceModifierRel.commercePriceListId = ?";

	private FinderPath _finderPathFetchByC_C;
	private FinderPath _finderPathCountByC_C;

	/**
	 * Returns the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; or throws a <code>NoSuchPriceListPriceModifierRelException</code> if it could not be found.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the matching commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByC_C(
			long commercePriceModifierId, long commercePriceListId)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByC_C(commercePriceModifierId, commercePriceListId);

		if (commercePriceListPriceModifierRel == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("commercePriceModifierId=");
			msg.append(commercePriceModifierId);

			msg.append(", commercePriceListId=");
			msg.append(commercePriceListId);

			msg.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(msg.toString());
			}

			throw new NoSuchPriceListPriceModifierRelException(msg.toString());
		}

		return commercePriceListPriceModifierRel;
	}

	/**
	 * Returns the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByC_C(
		long commercePriceModifierId, long commercePriceListId) {

		return fetchByC_C(commercePriceModifierId, commercePriceListId, true);
	}

	/**
	 * Returns the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce price list price modifier rel, or <code>null</code> if a matching commerce price list price modifier rel could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByC_C(
		long commercePriceModifierId, long commercePriceListId,
		boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {
				commercePriceModifierId, commercePriceListId
			};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByC_C, finderArgs, this);
		}

		if (result instanceof CommercePriceListPriceModifierRel) {
			CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel =
					(CommercePriceListPriceModifierRel)result;

			if ((commercePriceModifierId !=
					commercePriceListPriceModifierRel.
						getCommercePriceModifierId()) ||
				(commercePriceListId !=
					commercePriceListPriceModifierRel.
						getCommercePriceListId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			query.append(_FINDER_COLUMN_C_C_COMMERCEPRICEMODIFIERID_2);

			query.append(_FINDER_COLUMN_C_C_COMMERCEPRICELISTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePriceModifierId);

				qPos.add(commercePriceListId);

				List<CommercePriceListPriceModifierRel> list = q.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_C, finderArgs, list);
					}
				}
				else {
					CommercePriceListPriceModifierRel
						commercePriceListPriceModifierRel = list.get(0);

					result = commercePriceListPriceModifierRel;

					cacheResult(commercePriceListPriceModifierRel);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(_finderPathFetchByC_C, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (CommercePriceListPriceModifierRel)result;
		}
	}

	/**
	 * Removes the commerce price list price modifier rel where commercePriceModifierId = &#63; and commercePriceListId = &#63; from the database.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the commerce price list price modifier rel that was removed
	 */
	@Override
	public CommercePriceListPriceModifierRel removeByC_C(
			long commercePriceModifierId, long commercePriceListId)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			findByC_C(commercePriceModifierId, commercePriceListId);

		return remove(commercePriceListPriceModifierRel);
	}

	/**
	 * Returns the number of commerce price list price modifier rels where commercePriceModifierId = &#63; and commercePriceListId = &#63;.
	 *
	 * @param commercePriceModifierId the commerce price modifier ID
	 * @param commercePriceListId the commerce price list ID
	 * @return the number of matching commerce price list price modifier rels
	 */
	@Override
	public int countByC_C(
		long commercePriceModifierId, long commercePriceListId) {

		FinderPath finderPath = _finderPathCountByC_C;

		Object[] finderArgs = new Object[] {
			commercePriceModifierId, commercePriceListId
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE);

			query.append(_FINDER_COLUMN_C_C_COMMERCEPRICEMODIFIERID_2);

			query.append(_FINDER_COLUMN_C_C_COMMERCEPRICELISTID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePriceModifierId);

				qPos.add(commercePriceListId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_C_C_COMMERCEPRICEMODIFIERID_2 =
		"commercePriceListPriceModifierRel.commercePriceModifierId = ? AND ";

	private static final String _FINDER_COLUMN_C_C_COMMERCEPRICELISTID_2 =
		"commercePriceListPriceModifierRel.commercePriceListId = ?";

	public CommercePriceListPriceModifierRelPersistenceImpl() {
		setModelClass(CommercePriceListPriceModifierRel.class);

		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");
		dbColumnNames.put(
			"commercePriceListPriceModifierRelId", "CPLPriceModifierRelId");
		dbColumnNames.put("order", "order_");

		try {
			Field field = BasePersistenceImpl.class.getDeclaredField(
				"_dbColumnNames");

			field.setAccessible(true);

			field.set(this, dbColumnNames);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception, exception);
			}
		}
	}

	/**
	 * Caches the commerce price list price modifier rel in the entity cache if it is enabled.
	 *
	 * @param commercePriceListPriceModifierRel the commerce price list price modifier rel
	 */
	@Override
	public void cacheResult(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel) {

		entityCache.putResult(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			commercePriceListPriceModifierRel.getPrimaryKey(),
			commercePriceListPriceModifierRel);

		finderCache.putResult(
			_finderPathFetchByC_C,
			new Object[] {
				commercePriceListPriceModifierRel.getCommercePriceModifierId(),
				commercePriceListPriceModifierRel.getCommercePriceListId()
			},
			commercePriceListPriceModifierRel);

		commercePriceListPriceModifierRel.resetOriginalValues();
	}

	/**
	 * Caches the commerce price list price modifier rels in the entity cache if it is enabled.
	 *
	 * @param commercePriceListPriceModifierRels the commerce price list price modifier rels
	 */
	@Override
	public void cacheResult(
		List<CommercePriceListPriceModifierRel>
			commercePriceListPriceModifierRels) {

		for (CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel :
					commercePriceListPriceModifierRels) {

			if (entityCache.getResult(
					CommercePriceListPriceModifierRelModelImpl.
						ENTITY_CACHE_ENABLED,
					CommercePriceListPriceModifierRelImpl.class,
					commercePriceListPriceModifierRel.getPrimaryKey()) ==
						null) {

				cacheResult(commercePriceListPriceModifierRel);
			}
			else {
				commercePriceListPriceModifierRel.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all commerce price list price modifier rels.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommercePriceListPriceModifierRelImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the commerce price list price modifier rel.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel) {

		entityCache.removeResult(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			commercePriceListPriceModifierRel.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(
			(CommercePriceListPriceModifierRelModelImpl)
				commercePriceListPriceModifierRel,
			true);
	}

	@Override
	public void clearCache(
		List<CommercePriceListPriceModifierRel>
			commercePriceListPriceModifierRels) {

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel :
					commercePriceListPriceModifierRels) {

			entityCache.removeResult(
				CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
				CommercePriceListPriceModifierRelImpl.class,
				commercePriceListPriceModifierRel.getPrimaryKey());

			clearUniqueFindersCache(
				(CommercePriceListPriceModifierRelModelImpl)
					commercePriceListPriceModifierRel,
				true);
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
				CommercePriceListPriceModifierRelImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		CommercePriceListPriceModifierRelModelImpl
			commercePriceListPriceModifierRelModelImpl) {

		Object[] args = new Object[] {
			commercePriceListPriceModifierRelModelImpl.
				getCommercePriceModifierId(),
			commercePriceListPriceModifierRelModelImpl.getCommercePriceListId()
		};

		finderCache.putResult(
			_finderPathCountByC_C, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByC_C, args,
			commercePriceListPriceModifierRelModelImpl, false);
	}

	protected void clearUniqueFindersCache(
		CommercePriceListPriceModifierRelModelImpl
			commercePriceListPriceModifierRelModelImpl,
		boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				commercePriceListPriceModifierRelModelImpl.
					getCommercePriceModifierId(),
				commercePriceListPriceModifierRelModelImpl.
					getCommercePriceListId()
			};

			finderCache.removeResult(_finderPathCountByC_C, args);
			finderCache.removeResult(_finderPathFetchByC_C, args);
		}

		if ((commercePriceListPriceModifierRelModelImpl.getColumnBitmask() &
			 _finderPathFetchByC_C.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				commercePriceListPriceModifierRelModelImpl.
					getOriginalCommercePriceModifierId(),
				commercePriceListPriceModifierRelModelImpl.
					getOriginalCommercePriceListId()
			};

			finderCache.removeResult(_finderPathCountByC_C, args);
			finderCache.removeResult(_finderPathFetchByC_C, args);
		}
	}

	/**
	 * Creates a new commerce price list price modifier rel with the primary key. Does not add the commerce price list price modifier rel to the database.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key for the new commerce price list price modifier rel
	 * @return the new commerce price list price modifier rel
	 */
	@Override
	public CommercePriceListPriceModifierRel create(
		long commercePriceListPriceModifierRelId) {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			new CommercePriceListPriceModifierRelImpl();

		commercePriceListPriceModifierRel.setNew(true);
		commercePriceListPriceModifierRel.setPrimaryKey(
			commercePriceListPriceModifierRelId);

		String uuid = PortalUUIDUtil.generate();

		commercePriceListPriceModifierRel.setUuid(uuid);

		commercePriceListPriceModifierRel.setCompanyId(
			CompanyThreadLocal.getCompanyId());

		return commercePriceListPriceModifierRel;
	}

	/**
	 * Removes the commerce price list price modifier rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel that was removed
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel remove(
			long commercePriceListPriceModifierRelId)
		throws NoSuchPriceListPriceModifierRelException {

		return remove((Serializable)commercePriceListPriceModifierRelId);
	}

	/**
	 * Removes the commerce price list price modifier rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel that was removed
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel remove(Serializable primaryKey)
		throws NoSuchPriceListPriceModifierRelException {

		Session session = null;

		try {
			session = openSession();

			CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel =
					(CommercePriceListPriceModifierRel)session.get(
						CommercePriceListPriceModifierRelImpl.class,
						primaryKey);

			if (commercePriceListPriceModifierRel == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPriceListPriceModifierRelException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(commercePriceListPriceModifierRel);
		}
		catch (NoSuchPriceListPriceModifierRelException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected CommercePriceListPriceModifierRel removeImpl(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commercePriceListPriceModifierRel)) {
				commercePriceListPriceModifierRel =
					(CommercePriceListPriceModifierRel)session.get(
						CommercePriceListPriceModifierRelImpl.class,
						commercePriceListPriceModifierRel.getPrimaryKeyObj());
			}

			if (commercePriceListPriceModifierRel != null) {
				session.delete(commercePriceListPriceModifierRel);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (commercePriceListPriceModifierRel != null) {
			clearCache(commercePriceListPriceModifierRel);
		}

		return commercePriceListPriceModifierRel;
	}

	@Override
	public CommercePriceListPriceModifierRel updateImpl(
		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel) {

		boolean isNew = commercePriceListPriceModifierRel.isNew();

		if (!(commercePriceListPriceModifierRel instanceof
				CommercePriceListPriceModifierRelModelImpl)) {

			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(
					commercePriceListPriceModifierRel.getClass())) {

				invocationHandler = ProxyUtil.getInvocationHandler(
					commercePriceListPriceModifierRel);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commercePriceListPriceModifierRel proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommercePriceListPriceModifierRel implementation " +
					commercePriceListPriceModifierRel.getClass());
		}

		CommercePriceListPriceModifierRelModelImpl
			commercePriceListPriceModifierRelModelImpl =
				(CommercePriceListPriceModifierRelModelImpl)
					commercePriceListPriceModifierRel;

		if (Validator.isNull(commercePriceListPriceModifierRel.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			commercePriceListPriceModifierRel.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew &&
			(commercePriceListPriceModifierRel.getCreateDate() == null)) {

			if (serviceContext == null) {
				commercePriceListPriceModifierRel.setCreateDate(now);
			}
			else {
				commercePriceListPriceModifierRel.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!commercePriceListPriceModifierRelModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commercePriceListPriceModifierRel.setModifiedDate(now);
			}
			else {
				commercePriceListPriceModifierRel.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (commercePriceListPriceModifierRel.isNew()) {
				session.save(commercePriceListPriceModifierRel);

				commercePriceListPriceModifierRel.setNew(false);
			}
			else {
				commercePriceListPriceModifierRel =
					(CommercePriceListPriceModifierRel)session.merge(
						commercePriceListPriceModifierRel);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!CommercePriceListPriceModifierRelModelImpl.
				COLUMN_BITMASK_ENABLED) {

			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {
				commercePriceListPriceModifierRelModelImpl.getUuid()
			};

			finderCache.removeResult(_finderPathCountByUuid, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByUuid, args);

			args = new Object[] {
				commercePriceListPriceModifierRelModelImpl.getUuid(),
				commercePriceListPriceModifierRelModelImpl.getCompanyId()
			};

			finderCache.removeResult(_finderPathCountByUuid_C, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByUuid_C, args);

			args = new Object[] {
				commercePriceListPriceModifierRelModelImpl.
					getCommercePriceListId()
			};

			finderCache.removeResult(
				_finderPathCountByCommercePriceListId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByCommercePriceListId, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((commercePriceListPriceModifierRelModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByUuid.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					commercePriceListPriceModifierRelModelImpl.getOriginalUuid()
				};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);

				args = new Object[] {
					commercePriceListPriceModifierRelModelImpl.getUuid()
				};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);
			}

			if ((commercePriceListPriceModifierRelModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByUuid_C.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					commercePriceListPriceModifierRelModelImpl.
						getOriginalUuid(),
					commercePriceListPriceModifierRelModelImpl.
						getOriginalCompanyId()
				};

				finderCache.removeResult(_finderPathCountByUuid_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid_C, args);

				args = new Object[] {
					commercePriceListPriceModifierRelModelImpl.getUuid(),
					commercePriceListPriceModifierRelModelImpl.getCompanyId()
				};

				finderCache.removeResult(_finderPathCountByUuid_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid_C, args);
			}

			if ((commercePriceListPriceModifierRelModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByCommercePriceListId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					commercePriceListPriceModifierRelModelImpl.
						getOriginalCommercePriceListId()
				};

				finderCache.removeResult(
					_finderPathCountByCommercePriceListId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCommercePriceListId,
					args);

				args = new Object[] {
					commercePriceListPriceModifierRelModelImpl.
						getCommercePriceListId()
				};

				finderCache.removeResult(
					_finderPathCountByCommercePriceListId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCommercePriceListId,
					args);
			}
		}

		entityCache.putResult(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			commercePriceListPriceModifierRel.getPrimaryKey(),
			commercePriceListPriceModifierRel, false);

		clearUniqueFindersCache(
			commercePriceListPriceModifierRelModelImpl, false);
		cacheUniqueFindersCache(commercePriceListPriceModifierRelModelImpl);

		commercePriceListPriceModifierRel.resetOriginalValues();

		return commercePriceListPriceModifierRel;
	}

	/**
	 * Returns the commerce price list price modifier rel with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByPrimaryKey(
			Serializable primaryKey)
		throws NoSuchPriceListPriceModifierRelException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			fetchByPrimaryKey(primaryKey);

		if (commercePriceListPriceModifierRel == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPriceListPriceModifierRelException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return commercePriceListPriceModifierRel;
	}

	/**
	 * Returns the commerce price list price modifier rel with the primary key or throws a <code>NoSuchPriceListPriceModifierRelException</code> if it could not be found.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel
	 * @throws NoSuchPriceListPriceModifierRelException if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel findByPrimaryKey(
			long commercePriceListPriceModifierRelId)
		throws NoSuchPriceListPriceModifierRelException {

		return findByPrimaryKey(
			(Serializable)commercePriceListPriceModifierRelId);
	}

	/**
	 * Returns the commerce price list price modifier rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel, or <code>null</code> if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByPrimaryKey(
		Serializable primaryKey) {

		Serializable serializable = entityCache.getResult(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			(CommercePriceListPriceModifierRel)serializable;

		if (commercePriceListPriceModifierRel == null) {
			Session session = null;

			try {
				session = openSession();

				commercePriceListPriceModifierRel =
					(CommercePriceListPriceModifierRel)session.get(
						CommercePriceListPriceModifierRelImpl.class,
						primaryKey);

				if (commercePriceListPriceModifierRel != null) {
					cacheResult(commercePriceListPriceModifierRel);
				}
				else {
					entityCache.putResult(
						CommercePriceListPriceModifierRelModelImpl.
							ENTITY_CACHE_ENABLED,
						CommercePriceListPriceModifierRelImpl.class, primaryKey,
						nullModel);
				}
			}
			catch (Exception exception) {
				entityCache.removeResult(
					CommercePriceListPriceModifierRelModelImpl.
						ENTITY_CACHE_ENABLED,
					CommercePriceListPriceModifierRelImpl.class, primaryKey);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return commercePriceListPriceModifierRel;
	}

	/**
	 * Returns the commerce price list price modifier rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commercePriceListPriceModifierRelId the primary key of the commerce price list price modifier rel
	 * @return the commerce price list price modifier rel, or <code>null</code> if a commerce price list price modifier rel with the primary key could not be found
	 */
	@Override
	public CommercePriceListPriceModifierRel fetchByPrimaryKey(
		long commercePriceListPriceModifierRelId) {

		return fetchByPrimaryKey(
			(Serializable)commercePriceListPriceModifierRelId);
	}

	@Override
	public Map<Serializable, CommercePriceListPriceModifierRel>
		fetchByPrimaryKeys(Set<Serializable> primaryKeys) {

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, CommercePriceListPriceModifierRel> map =
			new HashMap<Serializable, CommercePriceListPriceModifierRel>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel = fetchByPrimaryKey(
					primaryKey);

			if (commercePriceListPriceModifierRel != null) {
				map.put(primaryKey, commercePriceListPriceModifierRel);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(
				CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
				CommercePriceListPriceModifierRelImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(
						primaryKey,
						(CommercePriceListPriceModifierRel)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler(
			uncachedPrimaryKeys.size() * 2 + 1);

		query.append(
			_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append((long)primaryKey);

			query.append(",");
		}

		query.setIndex(query.index() - 1);

		query.append(")");

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (CommercePriceListPriceModifierRel
					commercePriceListPriceModifierRel :
						(List<CommercePriceListPriceModifierRel>)q.list()) {

				map.put(
					commercePriceListPriceModifierRel.getPrimaryKeyObj(),
					commercePriceListPriceModifierRel);

				cacheResult(commercePriceListPriceModifierRel);

				uncachedPrimaryKeys.remove(
					commercePriceListPriceModifierRel.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(
					CommercePriceListPriceModifierRelModelImpl.
						ENTITY_CACHE_ENABLED,
					CommercePriceListPriceModifierRelImpl.class, primaryKey,
					nullModel);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the commerce price list price modifier rels.
	 *
	 * @return the commerce price list price modifier rels
	 */
	@Override
	public List<CommercePriceListPriceModifierRel> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findAll(int start, int end) {
		return findAll(start, end, null);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findAll(
		int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel>
			orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

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
	@Override
	public List<CommercePriceListPriceModifierRel> findAll(
		int start, int end,
		OrderByComparator<CommercePriceListPriceModifierRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<CommercePriceListPriceModifierRel> list = null;

		if (useFinderCache) {
			list =
				(List<CommercePriceListPriceModifierRel>)finderCache.getResult(
					finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL);

				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL;

				sql = sql.concat(
					CommercePriceListPriceModifierRelModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				list = (List<CommercePriceListPriceModifierRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the commerce price list price modifier rels from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommercePriceListPriceModifierRel
				commercePriceListPriceModifierRel : findAll()) {

			remove(commercePriceListPriceModifierRel);
		}
	}

	/**
	 * Returns the number of commerce price list price modifier rels.
	 *
	 * @return the number of commerce price list price modifier rels
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(
					_SQL_COUNT_COMMERCEPRICELISTPRICEMODIFIERREL);

				count = (Long)q.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CommercePriceListPriceModifierRelModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce price list price modifier rel persistence.
	 */
	public void afterPropertiesSet() {
		_finderPathWithPaginationFindAll = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()},
			CommercePriceListPriceModifierRelModelImpl.UUID_COLUMN_BITMASK |
			CommercePriceListPriceModifierRelModelImpl.ORDER_COLUMN_BITMASK);

		_finderPathCountByUuid = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByUuid", new String[] {String.class.getName()});

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			CommercePriceListPriceModifierRelModelImpl.UUID_COLUMN_BITMASK |
			CommercePriceListPriceModifierRelModelImpl.
				COMPANYID_COLUMN_BITMASK |
			CommercePriceListPriceModifierRelModelImpl.ORDER_COLUMN_BITMASK);

		_finderPathCountByUuid_C = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()});

		_finderPathWithPaginationFindByCommercePriceListId = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCommercePriceListId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByCommercePriceListId = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByCommercePriceListId", new String[] {Long.class.getName()},
			CommercePriceListPriceModifierRelModelImpl.
				COMMERCEPRICELISTID_COLUMN_BITMASK |
			CommercePriceListPriceModifierRelModelImpl.ORDER_COLUMN_BITMASK);

		_finderPathCountByCommercePriceListId = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCommercePriceListId", new String[] {Long.class.getName()});

		_finderPathFetchByC_C = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePriceListPriceModifierRelImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByC_C",
			new String[] {Long.class.getName(), Long.class.getName()},
			CommercePriceListPriceModifierRelModelImpl.
				COMMERCEPRICEMODIFIERID_COLUMN_BITMASK |
			CommercePriceListPriceModifierRelModelImpl.
				COMMERCEPRICELISTID_COLUMN_BITMASK);

		_finderPathCountByC_C = new FinderPath(
			CommercePriceListPriceModifierRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePriceListPriceModifierRelModelImpl.FINDER_CACHE_ENABLED,
			Long.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_C",
			new String[] {Long.class.getName(), Long.class.getName()});
	}

	public void destroy() {
		entityCache.removeCache(
			CommercePriceListPriceModifierRelImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL =
		"SELECT commercePriceListPriceModifierRel FROM CommercePriceListPriceModifierRel commercePriceListPriceModifierRel";

	private static final String
		_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE_PKS_IN =
			"SELECT commercePriceListPriceModifierRel FROM CommercePriceListPriceModifierRel commercePriceListPriceModifierRel WHERE CPLPriceModifierRelId IN (";

	private static final String
		_SQL_SELECT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE =
			"SELECT commercePriceListPriceModifierRel FROM CommercePriceListPriceModifierRel commercePriceListPriceModifierRel WHERE ";

	private static final String _SQL_COUNT_COMMERCEPRICELISTPRICEMODIFIERREL =
		"SELECT COUNT(commercePriceListPriceModifierRel) FROM CommercePriceListPriceModifierRel commercePriceListPriceModifierRel";

	private static final String
		_SQL_COUNT_COMMERCEPRICELISTPRICEMODIFIERREL_WHERE =
			"SELECT COUNT(commercePriceListPriceModifierRel) FROM CommercePriceListPriceModifierRel commercePriceListPriceModifierRel WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"commercePriceListPriceModifierRel.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No CommercePriceListPriceModifierRel exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No CommercePriceListPriceModifierRel exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePriceListPriceModifierRelPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid", "commercePriceListPriceModifierRelId", "order"});

}