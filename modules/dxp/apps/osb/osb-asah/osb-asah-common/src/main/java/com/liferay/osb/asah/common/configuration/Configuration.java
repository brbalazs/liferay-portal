/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.configuration;

/**
 * @author Brian Wing Shun Chan
 */
public interface Configuration {

	public String getDataSourceId();

	public String getDataSourceState();

	public String getDataSourceStatus();

	public String getProjectId();

	public void setDataSourceId(String dataSourceId);

	public void setDataSourceState(String dataSourceState);

	public void setDataSourceStatus(String dataSourceStatus);

	public void setProjectId(String projectId);

}