/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.dao.db;

import aQute.bnd.annotation.ProviderType;

import java.util.Set;

import javax.sql.DataSource;

/**
 * @author Brian Wing Shun Chan
 */
@ProviderType
public interface DBManager {

	public DB getDB();

	public DB getDB(DBType dbType, DataSource dataSource);

	public DBType getDBType(Object dialect);

	public Set<DBType> getDBTypes();

	public void setDB(DB db);

}