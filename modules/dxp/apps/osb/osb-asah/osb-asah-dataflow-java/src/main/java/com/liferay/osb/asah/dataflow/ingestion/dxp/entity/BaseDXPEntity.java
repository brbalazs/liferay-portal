/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.io.Serializable;

import javax.annotation.Nullable;

/**
 * @author Riccardo Ferrari
 */
public class BaseDXPEntity implements Serializable {

	@Nullable
	public Long channelId;

	public String dataSourceId;
	public String projectId;
	public String uploadDate;
	public String uploadType;

}