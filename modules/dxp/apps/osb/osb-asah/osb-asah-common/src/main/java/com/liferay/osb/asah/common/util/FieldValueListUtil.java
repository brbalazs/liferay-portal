/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Marcos Martins
 */
public class FieldValueListUtil {

	public static JSONArray toJSONArray(List<FieldValueList> fieldValueLists) {
		JSONArray jsonArray = new JSONArray();

		fieldValueLists.forEach(
			fieldValueList -> jsonArray.put(_toJSONObject(fieldValueList)));

		return jsonArray;
	}

	private static JSONObject _toJSONObject(FieldValueList fieldValueList) {
		JSONObject jsonObject = new JSONObject();

		FieldValue dataSourceIdFieldValue = fieldValueList.get(
			_FIELD_DATA_SOURCE_ID_INDEX);

		jsonObject.put("dataSourceId", dataSourceIdFieldValue.getValue());

		FieldValue nameFieldValue = fieldValueList.get(_FIELD_NAME_INDEX);

		jsonObject.put("name", nameFieldValue.getValue());

		FieldValue valueFieldValue = fieldValueList.get(_FIELD_VALUE_INDEX);

		jsonObject.put("value", valueFieldValue.getValue());

		return jsonObject;
	}

	private static final int _FIELD_DATA_SOURCE_ID_INDEX = 0;

	private static final int _FIELD_NAME_INDEX = 1;

	private static final int _FIELD_VALUE_INDEX = 2;

}