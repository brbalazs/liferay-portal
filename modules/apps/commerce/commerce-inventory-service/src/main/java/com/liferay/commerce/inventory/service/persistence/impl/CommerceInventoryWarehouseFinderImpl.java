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

package com.liferay.commerce.inventory.service.persistence.impl;

import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.model.impl.CommerceInventoryWarehouseImpl;
import com.liferay.commerce.inventory.service.persistence.CommerceInventoryWarehouseFinder;
import com.liferay.portal.dao.orm.custom.sql.CustomSQL;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.Iterator;
import java.util.List;

/**
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 */
public class CommerceInventoryWarehouseFinderImpl
	extends CommerceInventoryWarehouseFinderBaseImpl
	implements CommerceInventoryWarehouseFinder {

	public static final String COUNT_ADMINUI_WAREHOUSES_BY_COMPANYID_AND_SKU =
		CommerceInventoryWarehouseFinder.class.getName() +
			".countAdminUIWarehousesByCompanyIdAndSku";

	public static final String FIND_ADMINUI_WAREHOUSES_BY_COMPANYID_AND_SKU =
		CommerceInventoryWarehouseFinder.class.getName() +
			".findAdminUIWarehousesByCompanyIdAndSku";

	public static final String FIND_BY_G_S =
		CommerceInventoryWarehouseFinder.class.getName() + ".findByG_S";

	public static final String FIND_BY_C_G_A =
		CommerceInventoryWarehouseFinder.class.getName() + ".findByC_G_A";

	@Override
	public int countAdminUIWarehousesByCompanyIdAndSku(
		long companyId, String sku) {

		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(
				getClass(), COUNT_ADMINUI_WAREHOUSES_BY_COMPANYID_AND_SKU);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar(_COUNT_VALUE, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(companyId);
			qPos.add(sku);

			int count = 0;

			Iterator<Long> itr = q.iterate();

			while (itr.hasNext()) {
				Long l = itr.next();

				if (l != null) {
					count += l.intValue();
				}
			}

			return count;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<Object[]> findAdminUIWarehousesByCompanyIdAndSku(
		long companyId, String sku, int start, int end) {

		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(
				getClass(), FIND_ADMINUI_WAREHOUSES_BY_COMPANYID_AND_SKU);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addScalar(_NAME, Type.STRING);
			q.addScalar(_SUM_STOCK, Type.INTEGER);
			q.addScalar(_SUM_RESERVED, Type.INTEGER);
			q.addScalar(_SUM_AWAITING, Type.INTEGER);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(companyId);
			qPos.add(sku);

			return (List<Object[]>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<CommerceInventoryWarehouse> findByG_S(
		long groupId, String sku) {

		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_BY_G_S);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity(
				CommerceInventoryWarehouseImpl.TABLE_NAME,
				CommerceInventoryWarehouseImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(sku);

			return (List<CommerceInventoryWarehouse>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	public List<CommerceInventoryWarehouse> findByC_G_A(
		long companyId, long groupId, boolean active) {

		Session session = null;

		try {
			session = openSession();

			String sql = _customSQL.get(getClass(), FIND_BY_C_G_A);

			SQLQuery q = session.createSynchronizedSQLQuery(sql);

			q.addEntity(
				CommerceInventoryWarehouseImpl.TABLE_NAME,
				CommerceInventoryWarehouseImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(companyId);
			qPos.add(groupId);
			qPos.add(active);

			return (List<CommerceInventoryWarehouse>)QueryUtil.list(
				q, getDialect(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _COUNT_VALUE = "COUNT_VALUE";

	private static final String _NAME = "NAME";

	private static final String _SUM_AWAITING = "SUM_AWAITING";

	private static final String _SUM_RESERVED = "SUM_RESERVED";

	private static final String _SUM_STOCK = "SUM_STOCK";

	@ServiceReference(type = CustomSQL.class)
	private CustomSQL _customSQL;

}