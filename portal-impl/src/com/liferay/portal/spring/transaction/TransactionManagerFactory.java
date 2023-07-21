/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.transaction;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.SortedProperties;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.util.Enumeration;
import java.util.Properties;

import javax.sql.DataSource;

import jodd.bean.BeanUtil;

import org.hibernate.SessionFactory;

import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 * @author Brian Wing Shun Chan
 */
public class TransactionManagerFactory {

	public static AbstractPlatformTransactionManager createTransactionManager(
			DataSource dataSource, SessionFactory sessionFactory)
		throws Exception {

		ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();

		Class<?> clazz = classLoader.loadClass(
			PropsValues.TRANSACTION_MANAGER_IMPL);

		AbstractPlatformTransactionManager abstractPlatformTransactionManager =
			(AbstractPlatformTransactionManager)clazz.newInstance();

		Properties properties = PropsUtil.getProperties(
			"transaction.manager.property.", true);

		Enumeration<String> enu =
			(Enumeration<String>)properties.propertyNames();

		while (enu.hasMoreElements()) {
			String key = enu.nextElement();

			String value = properties.getProperty(key);

			BeanUtil.setProperty(
				abstractPlatformTransactionManager, key, value);
		}

		if (abstractPlatformTransactionManager instanceof
				HibernateTransactionManager) {

			HibernateTransactionManager hibernateTransactionManager =
				(HibernateTransactionManager)abstractPlatformTransactionManager;

			hibernateTransactionManager.setDataSource(dataSource);
			hibernateTransactionManager.setSessionFactory(sessionFactory);
		}

		if (_log.isDebugEnabled()) {
			clazz = abstractPlatformTransactionManager.getClass();

			_log.debug("Created transaction manager " + clazz.getName());

			SortedProperties sortedProperties = new SortedProperties(
				properties);

			sortedProperties.list(System.out);
		}

		return abstractPlatformTransactionManager;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TransactionManagerFactory.class);

}