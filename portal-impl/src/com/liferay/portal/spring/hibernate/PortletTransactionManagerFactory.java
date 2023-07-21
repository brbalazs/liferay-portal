/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.hibernate;

import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.spring.transaction.TransactionManagerFactory;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Shuyang Zhou
 */
public class PortletTransactionManagerFactory {

	public static PlatformTransactionManager create(
			DataSource dataSource,
			HibernateTransactionManager portalHibernateTransactionManager,
			SessionFactory portletSessionFactory)
		throws Exception {

		if (InfrastructureUtil.getDataSource() == dataSource) {
			return new PortletTransactionManager(
				portalHibernateTransactionManager, portletSessionFactory);
		}

		return TransactionManagerFactory.createTransactionManager(
			dataSource, portletSessionFactory);
	}

}