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

package com.liferay.commerce.pricing.service.persistence.impl;

import com.liferay.commerce.pricing.exception.NoSuchPricingClassRelException;
import com.liferay.commerce.pricing.model.CommercePricingClassRel;
import com.liferay.commerce.pricing.model.impl.CommercePricingClassRelImpl;
import com.liferay.commerce.pricing.model.impl.CommercePricingClassRelModelImpl;
import com.liferay.commerce.pricing.service.persistence.CommercePricingClassRelPersistence;
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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the commerce pricing class rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Riccardo Alberti
 * @generated
 */
public class CommercePricingClassRelPersistenceImpl
	extends BasePersistenceImpl<CommercePricingClassRel>
	implements CommercePricingClassRelPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>CommercePricingClassRelUtil</code> to access the commerce pricing class rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		CommercePricingClassRelImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByCommercePricingClassId;
	private FinderPath _finderPathWithoutPaginationFindByCommercePricingClassId;
	private FinderPath _finderPathCountByCommercePricingClassId;

	/**
	 * Returns all the commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @return the matching commerce pricing class rels
	 */
	@Override
	public List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId) {

		return findByCommercePricingClassId(
			commercePricingClassId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end) {

		return findByCommercePricingClassId(
			commercePricingClassId, start, end, null);
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
	@Override
	public List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return findByCommercePricingClassId(
			commercePricingClassId, start, end, orderByComparator, true);
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
	@Override
	public List<CommercePricingClassRel> findByCommercePricingClassId(
		long commercePricingClassId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath =
					_finderPathWithoutPaginationFindByCommercePricingClassId;
				finderArgs = new Object[] {commercePricingClassId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCommercePricingClassId;
			finderArgs = new Object[] {
				commercePricingClassId, start, end, orderByComparator
			};
		}

		List<CommercePricingClassRel> list = null;

		if (useFinderCache) {
			list = (List<CommercePricingClassRel>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePricingClassRel commercePricingClassRel : list) {
					if (commercePricingClassId !=
							commercePricingClassRel.
								getCommercePricingClassId()) {

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

			query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE);

			query.append(
				_FINDER_COLUMN_COMMERCEPRICINGCLASSID_COMMERCEPRICINGCLASSID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(CommercePricingClassRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePricingClassId);

				list = (List<CommercePricingClassRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel findByCommercePricingClassId_First(
			long commercePricingClassId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel =
			fetchByCommercePricingClassId_First(
				commercePricingClassId, orderByComparator);

		if (commercePricingClassRel != null) {
			return commercePricingClassRel;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("commercePricingClassId=");
		msg.append(commercePricingClassId);

		msg.append("}");

		throw new NoSuchPricingClassRelException(msg.toString());
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByCommercePricingClassId_First(
		long commercePricingClassId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		List<CommercePricingClassRel> list = findByCommercePricingClassId(
			commercePricingClassId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel findByCommercePricingClassId_Last(
			long commercePricingClassId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel =
			fetchByCommercePricingClassId_Last(
				commercePricingClassId, orderByComparator);

		if (commercePricingClassRel != null) {
			return commercePricingClassRel;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("commercePricingClassId=");
		msg.append(commercePricingClassId);

		msg.append("}");

		throw new NoSuchPricingClassRelException(msg.toString());
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByCommercePricingClassId_Last(
		long commercePricingClassId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		int count = countByCommercePricingClassId(commercePricingClassId);

		if (count == 0) {
			return null;
		}

		List<CommercePricingClassRel> list = findByCommercePricingClassId(
			commercePricingClassId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public CommercePricingClassRel[] findByCommercePricingClassId_PrevAndNext(
			long commercePricingClassRelId, long commercePricingClassId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = findByPrimaryKey(
			commercePricingClassRelId);

		Session session = null;

		try {
			session = openSession();

			CommercePricingClassRel[] array =
				new CommercePricingClassRelImpl[3];

			array[0] = getByCommercePricingClassId_PrevAndNext(
				session, commercePricingClassRel, commercePricingClassId,
				orderByComparator, true);

			array[1] = commercePricingClassRel;

			array[2] = getByCommercePricingClassId_PrevAndNext(
				session, commercePricingClassRel, commercePricingClassId,
				orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePricingClassRel getByCommercePricingClassId_PrevAndNext(
		Session session, CommercePricingClassRel commercePricingClassRel,
		long commercePricingClassId,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
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

		query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE);

		query.append(
			_FINDER_COLUMN_COMMERCEPRICINGCLASSID_COMMERCEPRICINGCLASSID_2);

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
			query.append(CommercePricingClassRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(commercePricingClassId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePricingClassRel)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<CommercePricingClassRel> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce pricing class rels where commercePricingClassId = &#63; from the database.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 */
	@Override
	public void removeByCommercePricingClassId(long commercePricingClassId) {
		for (CommercePricingClassRel commercePricingClassRel :
				findByCommercePricingClassId(
					commercePricingClassId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(commercePricingClassRel);
		}
	}

	/**
	 * Returns the number of commerce pricing class rels where commercePricingClassId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @return the number of matching commerce pricing class rels
	 */
	@Override
	public int countByCommercePricingClassId(long commercePricingClassId) {
		FinderPath finderPath = _finderPathCountByCommercePricingClassId;

		Object[] finderArgs = new Object[] {commercePricingClassId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_COMMERCEPRICINGCLASSREL_WHERE);

			query.append(
				_FINDER_COLUMN_COMMERCEPRICINGCLASSID_COMMERCEPRICINGCLASSID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePricingClassId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_COMMERCEPRICINGCLASSID_COMMERCEPRICINGCLASSID_2 =
			"commercePricingClassRel.commercePricingClassId = ?";

	private FinderPath _finderPathWithPaginationFindByCPC_CN;
	private FinderPath _finderPathWithoutPaginationFindByCPC_CN;
	private FinderPath _finderPathCountByCPC_CN;

	/**
	 * Returns all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @return the matching commerce pricing class rels
	 */
	@Override
	public List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId) {

		return findByCPC_CN(
			commercePricingClassId, classNameId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end) {

		return findByCPC_CN(
			commercePricingClassId, classNameId, start, end, null);
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
	@Override
	public List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return findByCPC_CN(
			commercePricingClassId, classNameId, start, end, orderByComparator,
			true);
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
	@Override
	public List<CommercePricingClassRel> findByCPC_CN(
		long commercePricingClassId, long classNameId, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCPC_CN;
				finderArgs = new Object[] {commercePricingClassId, classNameId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCPC_CN;
			finderArgs = new Object[] {
				commercePricingClassId, classNameId, start, end,
				orderByComparator
			};
		}

		List<CommercePricingClassRel> list = null;

		if (useFinderCache) {
			list = (List<CommercePricingClassRel>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePricingClassRel commercePricingClassRel : list) {
					if ((commercePricingClassId !=
							commercePricingClassRel.
								getCommercePricingClassId()) ||
						(classNameId !=
							commercePricingClassRel.getClassNameId())) {

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

			query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE);

			query.append(_FINDER_COLUMN_CPC_CN_COMMERCEPRICINGCLASSID_2);

			query.append(_FINDER_COLUMN_CPC_CN_CLASSNAMEID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(CommercePricingClassRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePricingClassId);

				qPos.add(classNameId);

				list = (List<CommercePricingClassRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public CommercePricingClassRel findByCPC_CN_First(
			long commercePricingClassId, long classNameId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = fetchByCPC_CN_First(
			commercePricingClassId, classNameId, orderByComparator);

		if (commercePricingClassRel != null) {
			return commercePricingClassRel;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("commercePricingClassId=");
		msg.append(commercePricingClassId);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append("}");

		throw new NoSuchPricingClassRelException(msg.toString());
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByCPC_CN_First(
		long commercePricingClassId, long classNameId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		List<CommercePricingClassRel> list = findByCPC_CN(
			commercePricingClassId, classNameId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public CommercePricingClassRel findByCPC_CN_Last(
			long commercePricingClassId, long classNameId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = fetchByCPC_CN_Last(
			commercePricingClassId, classNameId, orderByComparator);

		if (commercePricingClassRel != null) {
			return commercePricingClassRel;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("commercePricingClassId=");
		msg.append(commercePricingClassId);

		msg.append(", classNameId=");
		msg.append(classNameId);

		msg.append("}");

		throw new NoSuchPricingClassRelException(msg.toString());
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByCPC_CN_Last(
		long commercePricingClassId, long classNameId,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		int count = countByCPC_CN(commercePricingClassId, classNameId);

		if (count == 0) {
			return null;
		}

		List<CommercePricingClassRel> list = findByCPC_CN(
			commercePricingClassId, classNameId, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public CommercePricingClassRel[] findByCPC_CN_PrevAndNext(
			long commercePricingClassRelId, long commercePricingClassId,
			long classNameId,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = findByPrimaryKey(
			commercePricingClassRelId);

		Session session = null;

		try {
			session = openSession();

			CommercePricingClassRel[] array =
				new CommercePricingClassRelImpl[3];

			array[0] = getByCPC_CN_PrevAndNext(
				session, commercePricingClassRel, commercePricingClassId,
				classNameId, orderByComparator, true);

			array[1] = commercePricingClassRel;

			array[2] = getByCPC_CN_PrevAndNext(
				session, commercePricingClassRel, commercePricingClassId,
				classNameId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePricingClassRel getByCPC_CN_PrevAndNext(
		Session session, CommercePricingClassRel commercePricingClassRel,
		long commercePricingClassId, long classNameId,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
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

		query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE);

		query.append(_FINDER_COLUMN_CPC_CN_COMMERCEPRICINGCLASSID_2);

		query.append(_FINDER_COLUMN_CPC_CN_CLASSNAMEID_2);

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
			query.append(CommercePricingClassRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(commercePricingClassId);

		qPos.add(classNameId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePricingClassRel)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<CommercePricingClassRel> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63; from the database.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 */
	@Override
	public void removeByCPC_CN(long commercePricingClassId, long classNameId) {
		for (CommercePricingClassRel commercePricingClassRel :
				findByCPC_CN(
					commercePricingClassId, classNameId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(commercePricingClassRel);
		}
	}

	/**
	 * Returns the number of commerce pricing class rels where commercePricingClassId = &#63; and classNameId = &#63;.
	 *
	 * @param commercePricingClassId the commerce pricing class ID
	 * @param classNameId the class name ID
	 * @return the number of matching commerce pricing class rels
	 */
	@Override
	public int countByCPC_CN(long commercePricingClassId, long classNameId) {
		FinderPath finderPath = _finderPathCountByCPC_CN;

		Object[] finderArgs = new Object[] {
			commercePricingClassId, classNameId
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_COMMERCEPRICINGCLASSREL_WHERE);

			query.append(_FINDER_COLUMN_CPC_CN_COMMERCEPRICINGCLASSID_2);

			query.append(_FINDER_COLUMN_CPC_CN_CLASSNAMEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(commercePricingClassId);

				qPos.add(classNameId);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_CPC_CN_COMMERCEPRICINGCLASSID_2 =
		"commercePricingClassRel.commercePricingClassId = ? AND ";

	private static final String _FINDER_COLUMN_CPC_CN_CLASSNAMEID_2 =
		"commercePricingClassRel.classNameId = ?";

	private FinderPath _finderPathWithPaginationFindByCN_CPK;
	private FinderPath _finderPathWithoutPaginationFindByCN_CPK;
	private FinderPath _finderPathCountByCN_CPK;

	/**
	 * Returns all the commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching commerce pricing class rels
	 */
	@Override
	public List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK) {

		return findByCN_CPK(
			classNameId, classPK, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end) {

		return findByCN_CPK(classNameId, classPK, start, end, null);
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
	@Override
	public List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return findByCN_CPK(
			classNameId, classPK, start, end, orderByComparator, true);
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
	@Override
	public List<CommercePricingClassRel> findByCN_CPK(
		long classNameId, long classPK, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCN_CPK;
				finderArgs = new Object[] {classNameId, classPK};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCN_CPK;
			finderArgs = new Object[] {
				classNameId, classPK, start, end, orderByComparator
			};
		}

		List<CommercePricingClassRel> list = null;

		if (useFinderCache) {
			list = (List<CommercePricingClassRel>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (CommercePricingClassRel commercePricingClassRel : list) {
					if ((classNameId !=
							commercePricingClassRel.getClassNameId()) ||
						(classPK != commercePricingClassRel.getClassPK())) {

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

			query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE);

			query.append(_FINDER_COLUMN_CN_CPK_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_CN_CPK_CLASSPK_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				query.append(CommercePricingClassRelModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				list = (List<CommercePricingClassRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public CommercePricingClassRel findByCN_CPK_First(
			long classNameId, long classPK,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = fetchByCN_CPK_First(
			classNameId, classPK, orderByComparator);

		if (commercePricingClassRel != null) {
			return commercePricingClassRel;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append("}");

		throw new NoSuchPricingClassRelException(msg.toString());
	}

	/**
	 * Returns the first commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByCN_CPK_First(
		long classNameId, long classPK,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		List<CommercePricingClassRel> list = findByCN_CPK(
			classNameId, classPK, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public CommercePricingClassRel findByCN_CPK_Last(
			long classNameId, long classPK,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = fetchByCN_CPK_Last(
			classNameId, classPK, orderByComparator);

		if (commercePricingClassRel != null) {
			return commercePricingClassRel;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("classNameId=");
		msg.append(classNameId);

		msg.append(", classPK=");
		msg.append(classPK);

		msg.append("}");

		throw new NoSuchPricingClassRelException(msg.toString());
	}

	/**
	 * Returns the last commerce pricing class rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce pricing class rel, or <code>null</code> if a matching commerce pricing class rel could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByCN_CPK_Last(
		long classNameId, long classPK,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		int count = countByCN_CPK(classNameId, classPK);

		if (count == 0) {
			return null;
		}

		List<CommercePricingClassRel> list = findByCN_CPK(
			classNameId, classPK, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public CommercePricingClassRel[] findByCN_CPK_PrevAndNext(
			long commercePricingClassRelId, long classNameId, long classPK,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = findByPrimaryKey(
			commercePricingClassRelId);

		Session session = null;

		try {
			session = openSession();

			CommercePricingClassRel[] array =
				new CommercePricingClassRelImpl[3];

			array[0] = getByCN_CPK_PrevAndNext(
				session, commercePricingClassRel, classNameId, classPK,
				orderByComparator, true);

			array[1] = commercePricingClassRel;

			array[2] = getByCN_CPK_PrevAndNext(
				session, commercePricingClassRel, classNameId, classPK,
				orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommercePricingClassRel getByCN_CPK_PrevAndNext(
		Session session, CommercePricingClassRel commercePricingClassRel,
		long classNameId, long classPK,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
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

		query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE);

		query.append(_FINDER_COLUMN_CN_CPK_CLASSNAMEID_2);

		query.append(_FINDER_COLUMN_CN_CPK_CLASSPK_2);

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
			query.append(CommercePricingClassRelModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(classNameId);

		qPos.add(classPK);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						commercePricingClassRel)) {

				qPos.add(orderByConditionValue);
			}
		}

		List<CommercePricingClassRel> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the commerce pricing class rels where classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 */
	@Override
	public void removeByCN_CPK(long classNameId, long classPK) {
		for (CommercePricingClassRel commercePricingClassRel :
				findByCN_CPK(
					classNameId, classPK, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(commercePricingClassRel);
		}
	}

	/**
	 * Returns the number of commerce pricing class rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the number of matching commerce pricing class rels
	 */
	@Override
	public int countByCN_CPK(long classNameId, long classPK) {
		FinderPath finderPath = _finderPathCountByCN_CPK;

		Object[] finderArgs = new Object[] {classNameId, classPK};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_COMMERCEPRICINGCLASSREL_WHERE);

			query.append(_FINDER_COLUMN_CN_CPK_CLASSNAMEID_2);

			query.append(_FINDER_COLUMN_CN_CPK_CLASSPK_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(classNameId);

				qPos.add(classPK);

				count = (Long)q.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_CN_CPK_CLASSNAMEID_2 =
		"commercePricingClassRel.classNameId = ? AND ";

	private static final String _FINDER_COLUMN_CN_CPK_CLASSPK_2 =
		"commercePricingClassRel.classPK = ?";

	public CommercePricingClassRelPersistenceImpl() {
		setModelClass(CommercePricingClassRel.class);
	}

	/**
	 * Caches the commerce pricing class rel in the entity cache if it is enabled.
	 *
	 * @param commercePricingClassRel the commerce pricing class rel
	 */
	@Override
	public void cacheResult(CommercePricingClassRel commercePricingClassRel) {
		entityCache.putResult(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			commercePricingClassRel.getPrimaryKey(), commercePricingClassRel);

		commercePricingClassRel.resetOriginalValues();
	}

	/**
	 * Caches the commerce pricing class rels in the entity cache if it is enabled.
	 *
	 * @param commercePricingClassRels the commerce pricing class rels
	 */
	@Override
	public void cacheResult(
		List<CommercePricingClassRel> commercePricingClassRels) {

		for (CommercePricingClassRel commercePricingClassRel :
				commercePricingClassRels) {

			if (entityCache.getResult(
					CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
					CommercePricingClassRelImpl.class,
					commercePricingClassRel.getPrimaryKey()) == null) {

				cacheResult(commercePricingClassRel);
			}
			else {
				commercePricingClassRel.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all commerce pricing class rels.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(CommercePricingClassRelImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the commerce pricing class rel.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommercePricingClassRel commercePricingClassRel) {
		entityCache.removeResult(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			commercePricingClassRel.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(
		List<CommercePricingClassRel> commercePricingClassRels) {

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (CommercePricingClassRel commercePricingClassRel :
				commercePricingClassRels) {

			entityCache.removeResult(
				CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
				CommercePricingClassRelImpl.class,
				commercePricingClassRel.getPrimaryKey());
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
				CommercePricingClassRelImpl.class, primaryKey);
		}
	}

	/**
	 * Creates a new commerce pricing class rel with the primary key. Does not add the commerce pricing class rel to the database.
	 *
	 * @param commercePricingClassRelId the primary key for the new commerce pricing class rel
	 * @return the new commerce pricing class rel
	 */
	@Override
	public CommercePricingClassRel create(long commercePricingClassRelId) {
		CommercePricingClassRel commercePricingClassRel =
			new CommercePricingClassRelImpl();

		commercePricingClassRel.setNew(true);
		commercePricingClassRel.setPrimaryKey(commercePricingClassRelId);

		commercePricingClassRel.setCompanyId(CompanyThreadLocal.getCompanyId());

		return commercePricingClassRel;
	}

	/**
	 * Removes the commerce pricing class rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public CommercePricingClassRel remove(long commercePricingClassRelId)
		throws NoSuchPricingClassRelException {

		return remove((Serializable)commercePricingClassRelId);
	}

	/**
	 * Removes the commerce pricing class rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel that was removed
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public CommercePricingClassRel remove(Serializable primaryKey)
		throws NoSuchPricingClassRelException {

		Session session = null;

		try {
			session = openSession();

			CommercePricingClassRel commercePricingClassRel =
				(CommercePricingClassRel)session.get(
					CommercePricingClassRelImpl.class, primaryKey);

			if (commercePricingClassRel == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPricingClassRelException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(commercePricingClassRel);
		}
		catch (NoSuchPricingClassRelException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected CommercePricingClassRel removeImpl(
		CommercePricingClassRel commercePricingClassRel) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(commercePricingClassRel)) {
				commercePricingClassRel = (CommercePricingClassRel)session.get(
					CommercePricingClassRelImpl.class,
					commercePricingClassRel.getPrimaryKeyObj());
			}

			if (commercePricingClassRel != null) {
				session.delete(commercePricingClassRel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (commercePricingClassRel != null) {
			clearCache(commercePricingClassRel);
		}

		return commercePricingClassRel;
	}

	@Override
	public CommercePricingClassRel updateImpl(
		CommercePricingClassRel commercePricingClassRel) {

		boolean isNew = commercePricingClassRel.isNew();

		if (!(commercePricingClassRel instanceof
				CommercePricingClassRelModelImpl)) {

			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(commercePricingClassRel.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					commercePricingClassRel);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in commercePricingClassRel proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom CommercePricingClassRel implementation " +
					commercePricingClassRel.getClass());
		}

		CommercePricingClassRelModelImpl commercePricingClassRelModelImpl =
			(CommercePricingClassRelModelImpl)commercePricingClassRel;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (commercePricingClassRel.getCreateDate() == null)) {
			if (serviceContext == null) {
				commercePricingClassRel.setCreateDate(now);
			}
			else {
				commercePricingClassRel.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!commercePricingClassRelModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				commercePricingClassRel.setModifiedDate(now);
			}
			else {
				commercePricingClassRel.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (commercePricingClassRel.isNew()) {
				session.save(commercePricingClassRel);

				commercePricingClassRel.setNew(false);
			}
			else {
				commercePricingClassRel =
					(CommercePricingClassRel)session.merge(
						commercePricingClassRel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!CommercePricingClassRelModelImpl.COLUMN_BITMASK_ENABLED) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {
				commercePricingClassRelModelImpl.getCommercePricingClassId()
			};

			finderCache.removeResult(
				_finderPathCountByCommercePricingClassId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByCommercePricingClassId, args);

			args = new Object[] {
				commercePricingClassRelModelImpl.getCommercePricingClassId(),
				commercePricingClassRelModelImpl.getClassNameId()
			};

			finderCache.removeResult(_finderPathCountByCPC_CN, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByCPC_CN, args);

			args = new Object[] {
				commercePricingClassRelModelImpl.getClassNameId(),
				commercePricingClassRelModelImpl.getClassPK()
			};

			finderCache.removeResult(_finderPathCountByCN_CPK, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByCN_CPK, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((commercePricingClassRelModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByCommercePricingClassId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					commercePricingClassRelModelImpl.
						getOriginalCommercePricingClassId()
				};

				finderCache.removeResult(
					_finderPathCountByCommercePricingClassId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCommercePricingClassId,
					args);

				args = new Object[] {
					commercePricingClassRelModelImpl.getCommercePricingClassId()
				};

				finderCache.removeResult(
					_finderPathCountByCommercePricingClassId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCommercePricingClassId,
					args);
			}

			if ((commercePricingClassRelModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByCPC_CN.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					commercePricingClassRelModelImpl.
						getOriginalCommercePricingClassId(),
					commercePricingClassRelModelImpl.getOriginalClassNameId()
				};

				finderCache.removeResult(_finderPathCountByCPC_CN, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCPC_CN, args);

				args = new Object[] {
					commercePricingClassRelModelImpl.
						getCommercePricingClassId(),
					commercePricingClassRelModelImpl.getClassNameId()
				};

				finderCache.removeResult(_finderPathCountByCPC_CN, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCPC_CN, args);
			}

			if ((commercePricingClassRelModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByCN_CPK.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					commercePricingClassRelModelImpl.getOriginalClassNameId(),
					commercePricingClassRelModelImpl.getOriginalClassPK()
				};

				finderCache.removeResult(_finderPathCountByCN_CPK, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCN_CPK, args);

				args = new Object[] {
					commercePricingClassRelModelImpl.getClassNameId(),
					commercePricingClassRelModelImpl.getClassPK()
				};

				finderCache.removeResult(_finderPathCountByCN_CPK, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCN_CPK, args);
			}
		}

		entityCache.putResult(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			commercePricingClassRel.getPrimaryKey(), commercePricingClassRel,
			false);

		commercePricingClassRel.resetOriginalValues();

		return commercePricingClassRel;
	}

	/**
	 * Returns the commerce pricing class rel with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public CommercePricingClassRel findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPricingClassRelException {

		CommercePricingClassRel commercePricingClassRel = fetchByPrimaryKey(
			primaryKey);

		if (commercePricingClassRel == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPricingClassRelException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return commercePricingClassRel;
	}

	/**
	 * Returns the commerce pricing class rel with the primary key or throws a <code>NoSuchPricingClassRelException</code> if it could not be found.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel
	 * @throws NoSuchPricingClassRelException if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public CommercePricingClassRel findByPrimaryKey(
			long commercePricingClassRelId)
		throws NoSuchPricingClassRelException {

		return findByPrimaryKey((Serializable)commercePricingClassRelId);
	}

	/**
	 * Returns the commerce pricing class rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel, or <code>null</code> if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		CommercePricingClassRel commercePricingClassRel =
			(CommercePricingClassRel)serializable;

		if (commercePricingClassRel == null) {
			Session session = null;

			try {
				session = openSession();

				commercePricingClassRel = (CommercePricingClassRel)session.get(
					CommercePricingClassRelImpl.class, primaryKey);

				if (commercePricingClassRel != null) {
					cacheResult(commercePricingClassRel);
				}
				else {
					entityCache.putResult(
						CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
						CommercePricingClassRelImpl.class, primaryKey,
						nullModel);
				}
			}
			catch (Exception e) {
				entityCache.removeResult(
					CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
					CommercePricingClassRelImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return commercePricingClassRel;
	}

	/**
	 * Returns the commerce pricing class rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commercePricingClassRelId the primary key of the commerce pricing class rel
	 * @return the commerce pricing class rel, or <code>null</code> if a commerce pricing class rel with the primary key could not be found
	 */
	@Override
	public CommercePricingClassRel fetchByPrimaryKey(
		long commercePricingClassRelId) {

		return fetchByPrimaryKey((Serializable)commercePricingClassRelId);
	}

	@Override
	public Map<Serializable, CommercePricingClassRel> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, CommercePricingClassRel> map =
			new HashMap<Serializable, CommercePricingClassRel>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			CommercePricingClassRel commercePricingClassRel = fetchByPrimaryKey(
				primaryKey);

			if (commercePricingClassRel != null) {
				map.put(primaryKey, commercePricingClassRel);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(
				CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
				CommercePricingClassRelImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (CommercePricingClassRel)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler(
			uncachedPrimaryKeys.size() * 2 + 1);

		query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE_PKS_IN);

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

			for (CommercePricingClassRel commercePricingClassRel :
					(List<CommercePricingClassRel>)q.list()) {

				map.put(
					commercePricingClassRel.getPrimaryKeyObj(),
					commercePricingClassRel);

				cacheResult(commercePricingClassRel);

				uncachedPrimaryKeys.remove(
					commercePricingClassRel.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(
					CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
					CommercePricingClassRelImpl.class, primaryKey, nullModel);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the commerce pricing class rels.
	 *
	 * @return the commerce pricing class rels
	 */
	@Override
	public List<CommercePricingClassRel> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<CommercePricingClassRel> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<CommercePricingClassRel> findAll(
		int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
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
	@Override
	public List<CommercePricingClassRel> findAll(
		int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator,
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

		List<CommercePricingClassRel> list = null;

		if (useFinderCache) {
			list = (List<CommercePricingClassRel>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_COMMERCEPRICINGCLASSREL);

				appendOrderByComparator(
					query, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_COMMERCEPRICINGCLASSREL;

				sql = sql.concat(
					CommercePricingClassRelModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				list = (List<CommercePricingClassRel>)QueryUtil.list(
					q, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception e) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the commerce pricing class rels from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (CommercePricingClassRel commercePricingClassRel : findAll()) {
			remove(commercePricingClassRel);
		}
	}

	/**
	 * Returns the number of commerce pricing class rels.
	 *
	 * @return the number of commerce pricing class rels
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
					_SQL_COUNT_COMMERCEPRICINGCLASSREL);

				count = (Long)q.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return CommercePricingClassRelModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the commerce pricing class rel persistence.
	 */
	public void afterPropertiesSet() {
		_finderPathWithPaginationFindAll = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByCommercePricingClassId = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByCommercePricingClassId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByCommercePricingClassId =
			new FinderPath(
				CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
				CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
				CommercePricingClassRelImpl.class,
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByCommercePricingClassId",
				new String[] {Long.class.getName()},
				CommercePricingClassRelModelImpl.
					COMMERCEPRICINGCLASSID_COLUMN_BITMASK |
				CommercePricingClassRelModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByCommercePricingClassId = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByCommercePricingClassId",
			new String[] {Long.class.getName()});

		_finderPathWithPaginationFindByCPC_CN = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCPC_CN",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByCPC_CN = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCPC_CN",
			new String[] {Long.class.getName(), Long.class.getName()},
			CommercePricingClassRelModelImpl.
				COMMERCEPRICINGCLASSID_COLUMN_BITMASK |
			CommercePricingClassRelModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			CommercePricingClassRelModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByCPC_CN = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCPC_CN",
			new String[] {Long.class.getName(), Long.class.getName()});

		_finderPathWithPaginationFindByCN_CPK = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCN_CPK",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByCN_CPK = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED,
			CommercePricingClassRelImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCN_CPK",
			new String[] {Long.class.getName(), Long.class.getName()},
			CommercePricingClassRelModelImpl.CLASSNAMEID_COLUMN_BITMASK |
			CommercePricingClassRelModelImpl.CLASSPK_COLUMN_BITMASK |
			CommercePricingClassRelModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByCN_CPK = new FinderPath(
			CommercePricingClassRelModelImpl.ENTITY_CACHE_ENABLED,
			CommercePricingClassRelModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCN_CPK",
			new String[] {Long.class.getName(), Long.class.getName()});
	}

	public void destroy() {
		entityCache.removeCache(CommercePricingClassRelImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_COMMERCEPRICINGCLASSREL =
		"SELECT commercePricingClassRel FROM CommercePricingClassRel commercePricingClassRel";

	private static final String
		_SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE_PKS_IN =
			"SELECT commercePricingClassRel FROM CommercePricingClassRel commercePricingClassRel WHERE commercePricingClassRelId IN (";

	private static final String _SQL_SELECT_COMMERCEPRICINGCLASSREL_WHERE =
		"SELECT commercePricingClassRel FROM CommercePricingClassRel commercePricingClassRel WHERE ";

	private static final String _SQL_COUNT_COMMERCEPRICINGCLASSREL =
		"SELECT COUNT(commercePricingClassRel) FROM CommercePricingClassRel commercePricingClassRel";

	private static final String _SQL_COUNT_COMMERCEPRICINGCLASSREL_WHERE =
		"SELECT COUNT(commercePricingClassRel) FROM CommercePricingClassRel commercePricingClassRel WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"commercePricingClassRel.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No CommercePricingClassRel exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No CommercePricingClassRel exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePricingClassRelPersistenceImpl.class);

}