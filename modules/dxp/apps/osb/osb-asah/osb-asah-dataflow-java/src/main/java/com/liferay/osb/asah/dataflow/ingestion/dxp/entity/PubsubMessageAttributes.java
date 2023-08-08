/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Riccardo Ferrari
 */
public class PubsubMessageAttributes extends HashMap<String, String> {

	public PubsubMessageAttributes(Map<String, String> attributes) {
		if (attributes != null) {
			putAll(attributes);
		}
	}

	public long getCount() {
		return Long.parseLong(getOrDefault("count", "0"));
	}

	public String getDataSourceId() {
		return get("dataSourceId");
	}

	public String getProjectId() {
		return get("projectId");
	}

	public String getResourceName() {
		return get("resourceName");
	}

	public String getUploadTime() {
		return get("uploadTime");
	}

	public String getUploadType() {
		return get("uploadType");
	}

	public boolean isLast() {
		return Boolean.parseBoolean(getOrDefault("last", "false"));
	}

}