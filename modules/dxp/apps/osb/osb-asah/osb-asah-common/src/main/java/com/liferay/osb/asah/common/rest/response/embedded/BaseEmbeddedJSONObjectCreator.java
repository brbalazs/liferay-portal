/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.rest.response.embedded;

import com.liferay.osb.asah.common.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Shinn Lok
 */
public abstract class BaseEmbeddedJSONObjectCreator
	implements EmbeddedJSONObjectCreator {

	@Override
	public Map<String, JSONObject> create(JSONArray jsonArray)
		throws Exception {

		Map<String, JSONObject> jsonObjects = new HashMap<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String id = jsonObject.getString("id");

			jsonObjects.put(id, create(id));
		}

		return jsonObjects;
	}

	protected JSONObject create(String id, JSONObject jsonObject)
		throws Exception {

		Map<String, JSONObject> jsonObjects = create(JSONUtil.put(jsonObject));

		if ((jsonObjects == null) || jsonObjects.isEmpty()) {
			return null;
		}

		return jsonObjects.get(id);
	}

}