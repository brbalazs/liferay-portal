/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql;

import com.liferay.osb.asah.common.entity.Project;

/**
 * @author Rachael Koestartyo
 */
public interface PostgreSQLSchemaManager {

	public void createGlobalSchema();

	public void createSchema(Project project);

	public void deleteSchema(String projectId);

	public boolean existsSchema(Project project);

	public boolean existsTable(Project project, String tableName);

}