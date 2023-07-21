/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.external.data.source.test.service.destroyer;

import com.liferay.portal.kernel.dao.jdbc.DataSourceFactoryUtil;

import javax.sql.DataSource;

/**
 * @author Preston Crary
 */
public class DataSourceDestroyer {

	public DataSourceDestroyer(DataSource dataSource) {
		_dataSource = dataSource;
	}

	@SuppressWarnings("unused")
	public void destroy() throws Exception {
		DataSourceFactoryUtil.destroyDataSource(_dataSource);
	}

	private final DataSource _dataSource;

}