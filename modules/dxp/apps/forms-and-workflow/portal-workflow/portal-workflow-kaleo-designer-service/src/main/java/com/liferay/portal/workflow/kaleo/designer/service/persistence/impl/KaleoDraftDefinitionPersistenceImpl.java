/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.designer.service.persistence.impl;

import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.portal.workflow.kaleo.designer.exception.NoSuchKaleoDraftDefinitionException;
import com.liferay.portal.workflow.kaleo.designer.model.KaleoDraftDefinition;
import com.liferay.portal.workflow.kaleo.designer.model.impl.KaleoDraftDefinitionImpl;
import com.liferay.portal.workflow.kaleo.designer.model.impl.KaleoDraftDefinitionModelImpl;
import com.liferay.portal.workflow.kaleo.designer.service.persistence.KaleoDraftDefinitionPersistence;
import com.liferay.portal.workflow.kaleo.designer.service.persistence.KaleoDraftDefinitionUtil;

import java.io.Serializable;

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
 * The persistence implementation for the kaleo draft definition service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Eduardo Lundgren
 * @generated
 */
public class KaleoDraftDefinitionPersistenceImpl
	extends BasePersistenceImpl<KaleoDraftDefinition>
	implements KaleoDraftDefinitionPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>KaleoDraftDefinitionUtil</code> to access the kaleo draft definition persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		KaleoDraftDefinitionImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private int _databaseInMaxParameters;
	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByCompanyId;
	private FinderPath _finderPathWithoutPaginationFindByCompanyId;
	private FinderPath _finderPathCountByCompanyId;

	/**
	 * Returns all the kaleo draft definitions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByCompanyId(long companyId) {
		return findByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo draft definitions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @return the range of matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByCompanyId(
		long companyId, int start, int end) {

		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo draft definitions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<KaleoDraftDefinition> orderByComparator) {

		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo draft definitions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<KaleoDraftDefinition> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCompanyId;
				finderArgs = new Object[] {companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCompanyId;
			finderArgs = new Object[] {
				companyId, start, end, orderByComparator
			};
		}

		List<KaleoDraftDefinition> list = null;

		if (useFinderCache) {
			list = (List<KaleoDraftDefinition>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoDraftDefinition kaleoDraftDefinition : list) {
					if (companyId != kaleoDraftDefinition.getCompanyId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_KALEODRAFTDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoDraftDefinitionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<KaleoDraftDefinition>)QueryUtil.list(
					query, getDialect(), start, end);

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
	 * Returns the first kaleo draft definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition findByCompanyId_First(
			long companyId,
			OrderByComparator<KaleoDraftDefinition> orderByComparator)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = fetchByCompanyId_First(
			companyId, orderByComparator);

		if (kaleoDraftDefinition != null) {
			return kaleoDraftDefinition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchKaleoDraftDefinitionException(sb.toString());
	}

	/**
	 * Returns the first kaleo draft definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo draft definition, or <code>null</code> if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByCompanyId_First(
		long companyId,
		OrderByComparator<KaleoDraftDefinition> orderByComparator) {

		List<KaleoDraftDefinition> list = findByCompanyId(
			companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo draft definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition findByCompanyId_Last(
			long companyId,
			OrderByComparator<KaleoDraftDefinition> orderByComparator)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = fetchByCompanyId_Last(
			companyId, orderByComparator);

		if (kaleoDraftDefinition != null) {
			return kaleoDraftDefinition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchKaleoDraftDefinitionException(sb.toString());
	}

	/**
	 * Returns the last kaleo draft definition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo draft definition, or <code>null</code> if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByCompanyId_Last(
		long companyId,
		OrderByComparator<KaleoDraftDefinition> orderByComparator) {

		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<KaleoDraftDefinition> list = findByCompanyId(
			companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo draft definitions before and after the current kaleo draft definition in the ordered set where companyId = &#63;.
	 *
	 * @param kaleoDraftDefinitionId the primary key of the current kaleo draft definition
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition[] findByCompanyId_PrevAndNext(
			long kaleoDraftDefinitionId, long companyId,
			OrderByComparator<KaleoDraftDefinition> orderByComparator)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = findByPrimaryKey(
			kaleoDraftDefinitionId);

		Session session = null;

		try {
			session = openSession();

			KaleoDraftDefinition[] array = new KaleoDraftDefinitionImpl[3];

			array[0] = getByCompanyId_PrevAndNext(
				session, kaleoDraftDefinition, companyId, orderByComparator,
				true);

			array[1] = kaleoDraftDefinition;

			array[2] = getByCompanyId_PrevAndNext(
				session, kaleoDraftDefinition, companyId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoDraftDefinition getByCompanyId_PrevAndNext(
		Session session, KaleoDraftDefinition kaleoDraftDefinition,
		long companyId,
		OrderByComparator<KaleoDraftDefinition> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_KALEODRAFTDEFINITION_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoDraftDefinitionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoDraftDefinition)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoDraftDefinition> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo draft definitions where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (KaleoDraftDefinition kaleoDraftDefinition :
				findByCompanyId(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(kaleoDraftDefinition);
		}
	}

	/**
	 * Returns the number of kaleo draft definitions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching kaleo draft definitions
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = _finderPathCountByCompanyId;

		Object[] finderArgs = new Object[] {companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_KALEODRAFTDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

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

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 =
		"kaleoDraftDefinition.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByC_N_V;
	private FinderPath _finderPathWithoutPaginationFindByC_N_V;
	private FinderPath _finderPathCountByC_N_V;

	/**
	 * Returns all the kaleo draft definitions where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @return the matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByC_N_V(
		long companyId, String name, int version) {

		return findByC_N_V(
			companyId, name, version, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the kaleo draft definitions where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @return the range of matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByC_N_V(
		long companyId, String name, int version, int start, int end) {

		return findByC_N_V(companyId, name, version, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo draft definitions where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByC_N_V(
		long companyId, String name, int version, int start, int end,
		OrderByComparator<KaleoDraftDefinition> orderByComparator) {

		return findByC_N_V(
			companyId, name, version, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo draft definitions where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findByC_N_V(
		long companyId, String name, int version, int start, int end,
		OrderByComparator<KaleoDraftDefinition> orderByComparator,
		boolean useFinderCache) {

		name = Objects.toString(name, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByC_N_V;
				finderArgs = new Object[] {companyId, name, version};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByC_N_V;
			finderArgs = new Object[] {
				companyId, name, version, start, end, orderByComparator
			};
		}

		List<KaleoDraftDefinition> list = null;

		if (useFinderCache) {
			list = (List<KaleoDraftDefinition>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoDraftDefinition kaleoDraftDefinition : list) {
					if ((companyId != kaleoDraftDefinition.getCompanyId()) ||
						!name.equals(kaleoDraftDefinition.getName()) ||
						(version != kaleoDraftDefinition.getVersion())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_KALEODRAFTDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_C_N_V_COMPANYID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_N_V_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_C_N_V_NAME_2);
			}

			sb.append(_FINDER_COLUMN_C_N_V_VERSION_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoDraftDefinitionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindName) {
					queryPos.add(name);
				}

				queryPos.add(version);

				list = (List<KaleoDraftDefinition>)QueryUtil.list(
					query, getDialect(), start, end);

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
	 * Returns the first kaleo draft definition in the ordered set where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition findByC_N_V_First(
			long companyId, String name, int version,
			OrderByComparator<KaleoDraftDefinition> orderByComparator)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = fetchByC_N_V_First(
			companyId, name, version, orderByComparator);

		if (kaleoDraftDefinition != null) {
			return kaleoDraftDefinition;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append(", name=");
		sb.append(name);

		sb.append(", version=");
		sb.append(version);

		sb.append("}");

		throw new NoSuchKaleoDraftDefinitionException(sb.toString());
	}

	/**
	 * Returns the first kaleo draft definition in the ordered set where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo draft definition, or <code>null</code> if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByC_N_V_First(
		long companyId, String name, int version,
		OrderByComparator<KaleoDraftDefinition> orderByComparator) {

		List<KaleoDraftDefinition> list = findByC_N_V(
			companyId, name, version, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo draft definition in the ordered set where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition findByC_N_V_Last(
			long companyId, String name, int version,
			OrderByComparator<KaleoDraftDefinition> orderByComparator)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = fetchByC_N_V_Last(
			companyId, name, version, orderByComparator);

		if (kaleoDraftDefinition != null) {
			return kaleoDraftDefinition;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append(", name=");
		sb.append(name);

		sb.append(", version=");
		sb.append(version);

		sb.append("}");

		throw new NoSuchKaleoDraftDefinitionException(sb.toString());
	}

	/**
	 * Returns the last kaleo draft definition in the ordered set where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo draft definition, or <code>null</code> if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByC_N_V_Last(
		long companyId, String name, int version,
		OrderByComparator<KaleoDraftDefinition> orderByComparator) {

		int count = countByC_N_V(companyId, name, version);

		if (count == 0) {
			return null;
		}

		List<KaleoDraftDefinition> list = findByC_N_V(
			companyId, name, version, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo draft definitions before and after the current kaleo draft definition in the ordered set where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * @param kaleoDraftDefinitionId the primary key of the current kaleo draft definition
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition[] findByC_N_V_PrevAndNext(
			long kaleoDraftDefinitionId, long companyId, String name,
			int version,
			OrderByComparator<KaleoDraftDefinition> orderByComparator)
		throws NoSuchKaleoDraftDefinitionException {

		name = Objects.toString(name, "");

		KaleoDraftDefinition kaleoDraftDefinition = findByPrimaryKey(
			kaleoDraftDefinitionId);

		Session session = null;

		try {
			session = openSession();

			KaleoDraftDefinition[] array = new KaleoDraftDefinitionImpl[3];

			array[0] = getByC_N_V_PrevAndNext(
				session, kaleoDraftDefinition, companyId, name, version,
				orderByComparator, true);

			array[1] = kaleoDraftDefinition;

			array[2] = getByC_N_V_PrevAndNext(
				session, kaleoDraftDefinition, companyId, name, version,
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

	protected KaleoDraftDefinition getByC_N_V_PrevAndNext(
		Session session, KaleoDraftDefinition kaleoDraftDefinition,
		long companyId, String name, int version,
		OrderByComparator<KaleoDraftDefinition> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_KALEODRAFTDEFINITION_WHERE);

		sb.append(_FINDER_COLUMN_C_N_V_COMPANYID_2);

		boolean bindName = false;

		if (name.isEmpty()) {
			sb.append(_FINDER_COLUMN_C_N_V_NAME_3);
		}
		else {
			bindName = true;

			sb.append(_FINDER_COLUMN_C_N_V_NAME_2);
		}

		sb.append(_FINDER_COLUMN_C_N_V_VERSION_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoDraftDefinitionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (bindName) {
			queryPos.add(name);
		}

		queryPos.add(version);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoDraftDefinition)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoDraftDefinition> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo draft definitions where companyId = &#63; and name = &#63; and version = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 */
	@Override
	public void removeByC_N_V(long companyId, String name, int version) {
		for (KaleoDraftDefinition kaleoDraftDefinition :
				findByC_N_V(
					companyId, name, version, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(kaleoDraftDefinition);
		}
	}

	/**
	 * Returns the number of kaleo draft definitions where companyId = &#63; and name = &#63; and version = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @return the number of matching kaleo draft definitions
	 */
	@Override
	public int countByC_N_V(long companyId, String name, int version) {
		name = Objects.toString(name, "");

		FinderPath finderPath = _finderPathCountByC_N_V;

		Object[] finderArgs = new Object[] {companyId, name, version};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_KALEODRAFTDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_C_N_V_COMPANYID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_N_V_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_C_N_V_NAME_2);
			}

			sb.append(_FINDER_COLUMN_C_N_V_VERSION_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindName) {
					queryPos.add(name);
				}

				queryPos.add(version);

				count = (Long)query.uniqueResult();

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

	private static final String _FINDER_COLUMN_C_N_V_COMPANYID_2 =
		"kaleoDraftDefinition.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_N_V_NAME_2 =
		"kaleoDraftDefinition.name = ? AND ";

	private static final String _FINDER_COLUMN_C_N_V_NAME_3 =
		"(kaleoDraftDefinition.name IS NULL OR kaleoDraftDefinition.name = '') AND ";

	private static final String _FINDER_COLUMN_C_N_V_VERSION_2 =
		"kaleoDraftDefinition.version = ?";

	private FinderPath _finderPathFetchByC_N_V_D;
	private FinderPath _finderPathCountByC_N_V_D;

	/**
	 * Returns the kaleo draft definition where companyId = &#63; and name = &#63; and version = &#63; and draftVersion = &#63; or throws a <code>NoSuchKaleoDraftDefinitionException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param draftVersion the draft version
	 * @return the matching kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition findByC_N_V_D(
			long companyId, String name, int version, int draftVersion)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = fetchByC_N_V_D(
			companyId, name, version, draftVersion);

		if (kaleoDraftDefinition == null) {
			StringBundler sb = new StringBundler(10);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("companyId=");
			sb.append(companyId);

			sb.append(", name=");
			sb.append(name);

			sb.append(", version=");
			sb.append(version);

			sb.append(", draftVersion=");
			sb.append(draftVersion);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchKaleoDraftDefinitionException(sb.toString());
		}

		return kaleoDraftDefinition;
	}

	/**
	 * Returns the kaleo draft definition where companyId = &#63; and name = &#63; and version = &#63; and draftVersion = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param draftVersion the draft version
	 * @return the matching kaleo draft definition, or <code>null</code> if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByC_N_V_D(
		long companyId, String name, int version, int draftVersion) {

		return fetchByC_N_V_D(companyId, name, version, draftVersion, true);
	}

	/**
	 * Returns the kaleo draft definition where companyId = &#63; and name = &#63; and version = &#63; and draftVersion = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param draftVersion the draft version
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching kaleo draft definition, or <code>null</code> if a matching kaleo draft definition could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByC_N_V_D(
		long companyId, String name, int version, int draftVersion,
		boolean useFinderCache) {

		name = Objects.toString(name, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {companyId, name, version, draftVersion};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByC_N_V_D, finderArgs, this);
		}

		if (result instanceof KaleoDraftDefinition) {
			KaleoDraftDefinition kaleoDraftDefinition =
				(KaleoDraftDefinition)result;

			if ((companyId != kaleoDraftDefinition.getCompanyId()) ||
				!Objects.equals(name, kaleoDraftDefinition.getName()) ||
				(version != kaleoDraftDefinition.getVersion()) ||
				(draftVersion != kaleoDraftDefinition.getDraftVersion())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_SQL_SELECT_KALEODRAFTDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_C_N_V_D_COMPANYID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_N_V_D_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_C_N_V_D_NAME_2);
			}

			sb.append(_FINDER_COLUMN_C_N_V_D_VERSION_2);

			sb.append(_FINDER_COLUMN_C_N_V_D_DRAFTVERSION_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindName) {
					queryPos.add(name);
				}

				queryPos.add(version);

				queryPos.add(draftVersion);

				List<KaleoDraftDefinition> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByC_N_V_D, finderArgs, list);
					}
				}
				else {
					KaleoDraftDefinition kaleoDraftDefinition = list.get(0);

					result = kaleoDraftDefinition;

					cacheResult(kaleoDraftDefinition);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(
						_finderPathFetchByC_N_V_D, finderArgs);
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
			return (KaleoDraftDefinition)result;
		}
	}

	/**
	 * Removes the kaleo draft definition where companyId = &#63; and name = &#63; and version = &#63; and draftVersion = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param draftVersion the draft version
	 * @return the kaleo draft definition that was removed
	 */
	@Override
	public KaleoDraftDefinition removeByC_N_V_D(
			long companyId, String name, int version, int draftVersion)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = findByC_N_V_D(
			companyId, name, version, draftVersion);

		return remove(kaleoDraftDefinition);
	}

	/**
	 * Returns the number of kaleo draft definitions where companyId = &#63; and name = &#63; and version = &#63; and draftVersion = &#63;.
	 *
	 * @param companyId the company ID
	 * @param name the name
	 * @param version the version
	 * @param draftVersion the draft version
	 * @return the number of matching kaleo draft definitions
	 */
	@Override
	public int countByC_N_V_D(
		long companyId, String name, int version, int draftVersion) {

		name = Objects.toString(name, "");

		FinderPath finderPath = _finderPathCountByC_N_V_D;

		Object[] finderArgs = new Object[] {
			companyId, name, version, draftVersion
		};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(5);

			sb.append(_SQL_COUNT_KALEODRAFTDEFINITION_WHERE);

			sb.append(_FINDER_COLUMN_C_N_V_D_COMPANYID_2);

			boolean bindName = false;

			if (name.isEmpty()) {
				sb.append(_FINDER_COLUMN_C_N_V_D_NAME_3);
			}
			else {
				bindName = true;

				sb.append(_FINDER_COLUMN_C_N_V_D_NAME_2);
			}

			sb.append(_FINDER_COLUMN_C_N_V_D_VERSION_2);

			sb.append(_FINDER_COLUMN_C_N_V_D_DRAFTVERSION_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				if (bindName) {
					queryPos.add(name);
				}

				queryPos.add(version);

				queryPos.add(draftVersion);

				count = (Long)query.uniqueResult();

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

	private static final String _FINDER_COLUMN_C_N_V_D_COMPANYID_2 =
		"kaleoDraftDefinition.companyId = ? AND ";

	private static final String _FINDER_COLUMN_C_N_V_D_NAME_2 =
		"kaleoDraftDefinition.name = ? AND ";

	private static final String _FINDER_COLUMN_C_N_V_D_NAME_3 =
		"(kaleoDraftDefinition.name IS NULL OR kaleoDraftDefinition.name = '') AND ";

	private static final String _FINDER_COLUMN_C_N_V_D_VERSION_2 =
		"kaleoDraftDefinition.version = ? AND ";

	private static final String _FINDER_COLUMN_C_N_V_D_DRAFTVERSION_2 =
		"kaleoDraftDefinition.draftVersion = ?";

	public KaleoDraftDefinitionPersistenceImpl() {
		setModelClass(KaleoDraftDefinition.class);
	}

	/**
	 * Caches the kaleo draft definition in the entity cache if it is enabled.
	 *
	 * @param kaleoDraftDefinition the kaleo draft definition
	 */
	@Override
	public void cacheResult(KaleoDraftDefinition kaleoDraftDefinition) {
		entityCache.putResult(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			kaleoDraftDefinition.getPrimaryKey(), kaleoDraftDefinition);

		finderCache.putResult(
			_finderPathFetchByC_N_V_D,
			new Object[] {
				kaleoDraftDefinition.getCompanyId(),
				kaleoDraftDefinition.getName(),
				kaleoDraftDefinition.getVersion(),
				kaleoDraftDefinition.getDraftVersion()
			},
			kaleoDraftDefinition);

		kaleoDraftDefinition.resetOriginalValues();
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the kaleo draft definitions in the entity cache if it is enabled.
	 *
	 * @param kaleoDraftDefinitions the kaleo draft definitions
	 */
	@Override
	public void cacheResult(List<KaleoDraftDefinition> kaleoDraftDefinitions) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (kaleoDraftDefinitions.size() >
				 _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (KaleoDraftDefinition kaleoDraftDefinition :
				kaleoDraftDefinitions) {

			if (entityCache.getResult(
					KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
					KaleoDraftDefinitionImpl.class,
					kaleoDraftDefinition.getPrimaryKey()) == null) {

				cacheResult(kaleoDraftDefinition);
			}
			else {
				kaleoDraftDefinition.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all kaleo draft definitions.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(KaleoDraftDefinitionImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the kaleo draft definition.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(KaleoDraftDefinition kaleoDraftDefinition) {
		entityCache.removeResult(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			kaleoDraftDefinition.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(
			(KaleoDraftDefinitionModelImpl)kaleoDraftDefinition, true);
	}

	@Override
	public void clearCache(List<KaleoDraftDefinition> kaleoDraftDefinitions) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (KaleoDraftDefinition kaleoDraftDefinition :
				kaleoDraftDefinitions) {

			entityCache.removeResult(
				KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
				KaleoDraftDefinitionImpl.class,
				kaleoDraftDefinition.getPrimaryKey());

			clearUniqueFindersCache(
				(KaleoDraftDefinitionModelImpl)kaleoDraftDefinition, true);
		}
	}

	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
				KaleoDraftDefinitionImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		KaleoDraftDefinitionModelImpl kaleoDraftDefinitionModelImpl) {

		Object[] args = new Object[] {
			kaleoDraftDefinitionModelImpl.getCompanyId(),
			kaleoDraftDefinitionModelImpl.getName(),
			kaleoDraftDefinitionModelImpl.getVersion(),
			kaleoDraftDefinitionModelImpl.getDraftVersion()
		};

		finderCache.putResult(
			_finderPathCountByC_N_V_D, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByC_N_V_D, args, kaleoDraftDefinitionModelImpl,
			false);
	}

	protected void clearUniqueFindersCache(
		KaleoDraftDefinitionModelImpl kaleoDraftDefinitionModelImpl,
		boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				kaleoDraftDefinitionModelImpl.getCompanyId(),
				kaleoDraftDefinitionModelImpl.getName(),
				kaleoDraftDefinitionModelImpl.getVersion(),
				kaleoDraftDefinitionModelImpl.getDraftVersion()
			};

			finderCache.removeResult(_finderPathCountByC_N_V_D, args);
			finderCache.removeResult(_finderPathFetchByC_N_V_D, args);
		}

		if ((kaleoDraftDefinitionModelImpl.getColumnBitmask() &
			 _finderPathFetchByC_N_V_D.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				kaleoDraftDefinitionModelImpl.getOriginalCompanyId(),
				kaleoDraftDefinitionModelImpl.getOriginalName(),
				kaleoDraftDefinitionModelImpl.getOriginalVersion(),
				kaleoDraftDefinitionModelImpl.getOriginalDraftVersion()
			};

			finderCache.removeResult(_finderPathCountByC_N_V_D, args);
			finderCache.removeResult(_finderPathFetchByC_N_V_D, args);
		}
	}

	/**
	 * Creates a new kaleo draft definition with the primary key. Does not add the kaleo draft definition to the database.
	 *
	 * @param kaleoDraftDefinitionId the primary key for the new kaleo draft definition
	 * @return the new kaleo draft definition
	 */
	@Override
	public KaleoDraftDefinition create(long kaleoDraftDefinitionId) {
		KaleoDraftDefinition kaleoDraftDefinition =
			new KaleoDraftDefinitionImpl();

		kaleoDraftDefinition.setNew(true);
		kaleoDraftDefinition.setPrimaryKey(kaleoDraftDefinitionId);

		kaleoDraftDefinition.setCompanyId(CompanyThreadLocal.getCompanyId());

		return kaleoDraftDefinition;
	}

	/**
	 * Removes the kaleo draft definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoDraftDefinitionId the primary key of the kaleo draft definition
	 * @return the kaleo draft definition that was removed
	 * @throws NoSuchKaleoDraftDefinitionException if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition remove(long kaleoDraftDefinitionId)
		throws NoSuchKaleoDraftDefinitionException {

		return remove((Serializable)kaleoDraftDefinitionId);
	}

	/**
	 * Removes the kaleo draft definition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the kaleo draft definition
	 * @return the kaleo draft definition that was removed
	 * @throws NoSuchKaleoDraftDefinitionException if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition remove(Serializable primaryKey)
		throws NoSuchKaleoDraftDefinitionException {

		Session session = null;

		try {
			session = openSession();

			KaleoDraftDefinition kaleoDraftDefinition =
				(KaleoDraftDefinition)session.get(
					KaleoDraftDefinitionImpl.class, primaryKey);

			if (kaleoDraftDefinition == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchKaleoDraftDefinitionException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(kaleoDraftDefinition);
		}
		catch (NoSuchKaleoDraftDefinitionException noSuchEntityException) {
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
	protected KaleoDraftDefinition removeImpl(
		KaleoDraftDefinition kaleoDraftDefinition) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(kaleoDraftDefinition)) {
				kaleoDraftDefinition = (KaleoDraftDefinition)session.get(
					KaleoDraftDefinitionImpl.class,
					kaleoDraftDefinition.getPrimaryKeyObj());
			}

			if (kaleoDraftDefinition != null) {
				session.delete(kaleoDraftDefinition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (kaleoDraftDefinition != null) {
			clearCache(kaleoDraftDefinition);
		}

		return kaleoDraftDefinition;
	}

	@Override
	public KaleoDraftDefinition updateImpl(
		KaleoDraftDefinition kaleoDraftDefinition) {

		boolean isNew = kaleoDraftDefinition.isNew();

		if (!(kaleoDraftDefinition instanceof KaleoDraftDefinitionModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(kaleoDraftDefinition.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					kaleoDraftDefinition);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in kaleoDraftDefinition proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom KaleoDraftDefinition implementation " +
					kaleoDraftDefinition.getClass());
		}

		KaleoDraftDefinitionModelImpl kaleoDraftDefinitionModelImpl =
			(KaleoDraftDefinitionModelImpl)kaleoDraftDefinition;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (kaleoDraftDefinition.getCreateDate() == null)) {
			if (serviceContext == null) {
				kaleoDraftDefinition.setCreateDate(date);
			}
			else {
				kaleoDraftDefinition.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!kaleoDraftDefinitionModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				kaleoDraftDefinition.setModifiedDate(date);
			}
			else {
				kaleoDraftDefinition.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(kaleoDraftDefinition);

				kaleoDraftDefinition.setNew(false);
			}
			else {
				kaleoDraftDefinition = (KaleoDraftDefinition)session.merge(
					kaleoDraftDefinition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!KaleoDraftDefinitionModelImpl.COLUMN_BITMASK_ENABLED) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {
				kaleoDraftDefinitionModelImpl.getCompanyId()
			};

			finderCache.removeResult(_finderPathCountByCompanyId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByCompanyId, args);

			args = new Object[] {
				kaleoDraftDefinitionModelImpl.getCompanyId(),
				kaleoDraftDefinitionModelImpl.getName(),
				kaleoDraftDefinitionModelImpl.getVersion()
			};

			finderCache.removeResult(_finderPathCountByC_N_V, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByC_N_V, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((kaleoDraftDefinitionModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByCompanyId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					kaleoDraftDefinitionModelImpl.getOriginalCompanyId()
				};

				finderCache.removeResult(_finderPathCountByCompanyId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCompanyId, args);

				args = new Object[] {
					kaleoDraftDefinitionModelImpl.getCompanyId()
				};

				finderCache.removeResult(_finderPathCountByCompanyId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCompanyId, args);
			}

			if ((kaleoDraftDefinitionModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByC_N_V.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					kaleoDraftDefinitionModelImpl.getOriginalCompanyId(),
					kaleoDraftDefinitionModelImpl.getOriginalName(),
					kaleoDraftDefinitionModelImpl.getOriginalVersion()
				};

				finderCache.removeResult(_finderPathCountByC_N_V, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_N_V, args);

				args = new Object[] {
					kaleoDraftDefinitionModelImpl.getCompanyId(),
					kaleoDraftDefinitionModelImpl.getName(),
					kaleoDraftDefinitionModelImpl.getVersion()
				};

				finderCache.removeResult(_finderPathCountByC_N_V, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByC_N_V, args);
			}
		}

		entityCache.putResult(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			kaleoDraftDefinition.getPrimaryKey(), kaleoDraftDefinition, false);

		clearUniqueFindersCache(kaleoDraftDefinitionModelImpl, false);
		cacheUniqueFindersCache(kaleoDraftDefinitionModelImpl);

		kaleoDraftDefinition.resetOriginalValues();

		return kaleoDraftDefinition;
	}

	/**
	 * Returns the kaleo draft definition with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the kaleo draft definition
	 * @return the kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition findByPrimaryKey(Serializable primaryKey)
		throws NoSuchKaleoDraftDefinitionException {

		KaleoDraftDefinition kaleoDraftDefinition = fetchByPrimaryKey(
			primaryKey);

		if (kaleoDraftDefinition == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchKaleoDraftDefinitionException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return kaleoDraftDefinition;
	}

	/**
	 * Returns the kaleo draft definition with the primary key or throws a <code>NoSuchKaleoDraftDefinitionException</code> if it could not be found.
	 *
	 * @param kaleoDraftDefinitionId the primary key of the kaleo draft definition
	 * @return the kaleo draft definition
	 * @throws NoSuchKaleoDraftDefinitionException if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition findByPrimaryKey(long kaleoDraftDefinitionId)
		throws NoSuchKaleoDraftDefinitionException {

		return findByPrimaryKey((Serializable)kaleoDraftDefinitionId);
	}

	/**
	 * Returns the kaleo draft definition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the kaleo draft definition
	 * @return the kaleo draft definition, or <code>null</code> if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = entityCache.getResult(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		KaleoDraftDefinition kaleoDraftDefinition =
			(KaleoDraftDefinition)serializable;

		if (kaleoDraftDefinition == null) {
			Session session = null;

			try {
				session = openSession();

				kaleoDraftDefinition = (KaleoDraftDefinition)session.get(
					KaleoDraftDefinitionImpl.class, primaryKey);

				if (kaleoDraftDefinition != null) {
					cacheResult(kaleoDraftDefinition);
				}
				else {
					entityCache.putResult(
						KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
						KaleoDraftDefinitionImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception exception) {
				entityCache.removeResult(
					KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
					KaleoDraftDefinitionImpl.class, primaryKey);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return kaleoDraftDefinition;
	}

	/**
	 * Returns the kaleo draft definition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param kaleoDraftDefinitionId the primary key of the kaleo draft definition
	 * @return the kaleo draft definition, or <code>null</code> if a kaleo draft definition with the primary key could not be found
	 */
	@Override
	public KaleoDraftDefinition fetchByPrimaryKey(long kaleoDraftDefinitionId) {
		return fetchByPrimaryKey((Serializable)kaleoDraftDefinitionId);
	}

	@Override
	public Map<Serializable, KaleoDraftDefinition> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, KaleoDraftDefinition> map =
			new HashMap<Serializable, KaleoDraftDefinition>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			KaleoDraftDefinition kaleoDraftDefinition = fetchByPrimaryKey(
				primaryKey);

			if (kaleoDraftDefinition != null) {
				map.put(primaryKey, kaleoDraftDefinition);
			}

			return map;
		}

		if ((_databaseInMaxParameters > 0) &&
			(primaryKeys.size() > _databaseInMaxParameters)) {

			Iterator<Serializable> iterator = primaryKeys.iterator();

			while (iterator.hasNext()) {
				Set<Serializable> page = new HashSet<>();

				for (int i = 0;
					 (i < _databaseInMaxParameters) && iterator.hasNext();
					 i++) {

					page.add(iterator.next());
				}

				map.putAll(fetchByPrimaryKeys(page));
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = entityCache.getResult(
				KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
				KaleoDraftDefinitionImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (KaleoDraftDefinition)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler sb = new StringBundler(
			(uncachedPrimaryKeys.size() * 2) + 1);

		sb.append(_SQL_SELECT_KALEODRAFTDEFINITION_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (KaleoDraftDefinition kaleoDraftDefinition :
					(List<KaleoDraftDefinition>)query.list()) {

				map.put(
					kaleoDraftDefinition.getPrimaryKeyObj(),
					kaleoDraftDefinition);

				cacheResult(kaleoDraftDefinition);

				uncachedPrimaryKeys.remove(
					kaleoDraftDefinition.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				entityCache.putResult(
					KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
					KaleoDraftDefinitionImpl.class, primaryKey, nullModel);
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
	 * Returns all the kaleo draft definitions.
	 *
	 * @return the kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo draft definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @return the range of kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo draft definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findAll(
		int start, int end,
		OrderByComparator<KaleoDraftDefinition> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo draft definitions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoDraftDefinitionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo draft definitions
	 * @param end the upper bound of the range of kaleo draft definitions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of kaleo draft definitions
	 */
	@Override
	public List<KaleoDraftDefinition> findAll(
		int start, int end,
		OrderByComparator<KaleoDraftDefinition> orderByComparator,
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

		List<KaleoDraftDefinition> list = null;

		if (useFinderCache) {
			list = (List<KaleoDraftDefinition>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_KALEODRAFTDEFINITION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_KALEODRAFTDEFINITION;

				sql = sql.concat(KaleoDraftDefinitionModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<KaleoDraftDefinition>)QueryUtil.list(
					query, getDialect(), start, end);

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
	 * Removes all the kaleo draft definitions from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (KaleoDraftDefinition kaleoDraftDefinition : findAll()) {
			remove(kaleoDraftDefinition);
		}
	}

	/**
	 * Returns the number of kaleo draft definitions.
	 *
	 * @return the number of kaleo draft definitions
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_KALEODRAFTDEFINITION);

				count = (Long)query.uniqueResult();

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
	protected Map<String, Integer> getTableColumnsMap() {
		return KaleoDraftDefinitionModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the kaleo draft definition persistence.
	 */
	public void afterPropertiesSet() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get("value.object.finder.cache.list.threshold"));

		_finderPathWithPaginationFindAll = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByCompanyId = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByCompanyId = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] {Long.class.getName()},
			KaleoDraftDefinitionModelImpl.COMPANYID_COLUMN_BITMASK |
			KaleoDraftDefinitionModelImpl.DRAFTVERSION_COLUMN_BITMASK);

		_finderPathCountByCompanyId = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] {Long.class.getName()});

		_finderPathWithPaginationFindByC_N_V = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByC_N_V",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByC_N_V = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByC_N_V",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			},
			KaleoDraftDefinitionModelImpl.COMPANYID_COLUMN_BITMASK |
			KaleoDraftDefinitionModelImpl.NAME_COLUMN_BITMASK |
			KaleoDraftDefinitionModelImpl.VERSION_COLUMN_BITMASK |
			KaleoDraftDefinitionModelImpl.DRAFTVERSION_COLUMN_BITMASK);

		_finderPathCountByC_N_V = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_N_V",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			});

		_finderPathFetchByC_N_V_D = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED,
			KaleoDraftDefinitionImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByC_N_V_D",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			},
			KaleoDraftDefinitionModelImpl.COMPANYID_COLUMN_BITMASK |
			KaleoDraftDefinitionModelImpl.NAME_COLUMN_BITMASK |
			KaleoDraftDefinitionModelImpl.VERSION_COLUMN_BITMASK |
			KaleoDraftDefinitionModelImpl.DRAFTVERSION_COLUMN_BITMASK);

		_finderPathCountByC_N_V_D = new FinderPath(
			KaleoDraftDefinitionModelImpl.ENTITY_CACHE_ENABLED,
			KaleoDraftDefinitionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByC_N_V_D",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(), Integer.class.getName()
			});

		KaleoDraftDefinitionUtil.setPersistence(this);
	}

	public void destroy() {
		KaleoDraftDefinitionUtil.setPersistence(null);

		entityCache.removeCache(KaleoDraftDefinitionImpl.class.getName());

		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);

		DBType dbType = DBManagerUtil.getDBType(sessionFactory.getDialect());

		_databaseInMaxParameters = GetterUtil.getInteger(
			PropsUtil.get(
				"database.in.max.parameters", new Filter(dbType.getName())));
	}

	@ServiceReference(type = EntityCache.class)
	protected EntityCache entityCache;

	@ServiceReference(type = FinderCache.class)
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_KALEODRAFTDEFINITION =
		"SELECT kaleoDraftDefinition FROM KaleoDraftDefinition kaleoDraftDefinition";

	private static final String _SQL_SELECT_KALEODRAFTDEFINITION_WHERE_PKS_IN =
		"SELECT kaleoDraftDefinition FROM KaleoDraftDefinition kaleoDraftDefinition WHERE kaleoDraftDefinitionId IN (";

	private static final String _SQL_SELECT_KALEODRAFTDEFINITION_WHERE =
		"SELECT kaleoDraftDefinition FROM KaleoDraftDefinition kaleoDraftDefinition WHERE ";

	private static final String _SQL_COUNT_KALEODRAFTDEFINITION =
		"SELECT COUNT(kaleoDraftDefinition) FROM KaleoDraftDefinition kaleoDraftDefinition";

	private static final String _SQL_COUNT_KALEODRAFTDEFINITION_WHERE =
		"SELECT COUNT(kaleoDraftDefinition) FROM KaleoDraftDefinition kaleoDraftDefinition WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"kaleoDraftDefinition.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No KaleoDraftDefinition exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No KaleoDraftDefinition exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoDraftDefinitionPersistenceImpl.class);

}