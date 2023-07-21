/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.spring.hibernate.PortletHibernateConfiguration;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;

/**
 * @author Shuyang Zhou
 * @author Alexander Chow
 */
public class PortletSessionFactoryImpl extends SessionFactoryImpl {

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setDataSource(DataSource dataSource) {
		_dataSource = dataSource;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected SessionFactory createSessionFactory(DataSource dataSource) {
		PortletHibernateConfiguration portletHibernateConfiguration =
			new PortletHibernateConfiguration(
				getSessionFactoryClassLoader(), dataSource);

		portletHibernateConfiguration.setDataSource(dataSource);

		SessionFactory sessionFactory = null;

		try {
			sessionFactory =
				portletHibernateConfiguration.buildSessionFactory();
		}
		catch (Exception e) {
			_log.error(e, e);

			return null;
		}

		return sessionFactory;
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected DataSource getDataSource() {
		return _dataSource;
	}

	protected ClassLoader getDefaultSessionFactoryClassLoader() {
		return PortletClassLoaderUtil.getClassLoader();
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected SessionFactory getSessionFactory() {
		return getSessionFactoryImplementor();
	}

	@Override
	protected Session wrapSession(org.hibernate.Session session) {
		return super.wrapSession(session);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletSessionFactoryImpl.class);

	private DataSource _dataSource;

}