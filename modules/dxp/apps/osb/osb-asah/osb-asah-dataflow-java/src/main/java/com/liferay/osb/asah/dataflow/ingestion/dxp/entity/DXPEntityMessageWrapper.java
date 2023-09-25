/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.io.Serializable;

/**
 * @author Marcellus Tavares
 */
public class DXPEntityMessageWrapper implements Serializable {

	public String dataSourceId;
	public String payload;
	public String projectId;
	public String resourceName;
	public String uploadTime;
	public String uploadType;

}