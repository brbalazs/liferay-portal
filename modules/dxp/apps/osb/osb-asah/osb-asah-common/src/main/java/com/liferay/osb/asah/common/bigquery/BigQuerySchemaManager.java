/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.bigquery;

/**
 * @author Marcellus Tavares
 */
public interface BigQuerySchemaManager {

	public void createFunction(String projectId, String functionsName);

	public void createOrReplaceView(String projectId, String viewName);

	public void createSchema(String projectId);

	public void createTable(String projectId, String tableName);

	public void createTables(String projectId);

	public void deleteSchema(String projectId);

	public void dropTable(String projectId, String tableName);

	public void updateTablesExpiration(Long expirationTime, String projectId);

}