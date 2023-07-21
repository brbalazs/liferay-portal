/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.dao.jdbc.aop.DynamicDataSourceTargetSource;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;

import javax.mail.Session;

import javax.sql.DataSource;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 */
@OSGiBeanProperties(service = InfrastructureUtil.class)
public class InfrastructureUtil {

	public static DataSource getDataSource() {
		return _dataSource;
	}

	public static DynamicDataSourceTargetSource
		getDynamicDataSourceTargetSource() {

		return _dynamicDataSourceTargetSource;
	}

	public static Session getMailSession() {
		return _mailSession;
	}

	public static Object getTransactionManager() {
		return _transactionManager;
	}

	public void setDataSource(DataSource dataSource) {
		_dataSource = dataSource;
	}

	public void setDynamicDataSourceTargetSource(
		DynamicDataSourceTargetSource dynamicDataSourceTargetSource) {

		_dynamicDataSourceTargetSource = dynamicDataSourceTargetSource;
	}

	public void setMailSession(Session mailSession) {
		_mailSession = mailSession;
	}

	public void setTransactionManager(Object transactionManager) {
		_transactionManager = transactionManager;
	}

	private static DataSource _dataSource;
	private static DynamicDataSourceTargetSource _dynamicDataSourceTargetSource;
	private static Session _mailSession;
	private static Object _transactionManager;

}