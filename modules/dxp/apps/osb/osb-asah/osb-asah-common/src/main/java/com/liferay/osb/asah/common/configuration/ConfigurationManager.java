/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.configuration;

import com.liferay.osb.asah.common.entity.DataSource;

/**
 * @author Vishal Reddy
 * @author Brian Wing Shun Chan
 */
public interface ConfigurationManager {

	public boolean addConfiguration(DataSource dataSource);

	public boolean deleteConfiguration(String dataSourceId);

	public Configuration getConfiguration(String dataSourceId);

	public Configuration[] getConfigurations(String projectId);

	public String getState(DataSource dataSource);

	public DataSource refresh(DataSource dataSource);

	public Configuration updateConfiguration(DataSource dataSource);

}