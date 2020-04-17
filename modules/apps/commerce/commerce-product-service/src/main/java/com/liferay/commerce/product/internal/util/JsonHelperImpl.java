/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.internal.util;

import com.liferay.commerce.product.util.JsonHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Igor Beslic
 */
@Component(immediate = true, service = JsonHelper.class)
public class JsonHelperImpl implements JsonHelper {

	public JsonHelperImpl() {
	}

	public JsonHelperImpl(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	public boolean equals(String jsonArrayString1, String jsonArrayString2) {
		JSONArray jsonArray1 = _toJSONArray(jsonArrayString1);
		JSONArray jsonArray2 = _toJSONArray(jsonArrayString2);

		if (jsonArray1.length() != jsonArray2.length()) {
			return false;
		}

		Map<String, String> map1 = _toMap(jsonArray1);
		Map<String, String> map2 = _toMap(jsonArray2);

		return map1.equals(map2);
	}

	public String getFirstElementStringValue(String jsonArrayString) {
		if (!isArray(jsonArrayString)) {
			throw new IllegalArgumentException(
				String.format(
					"%s is not a valid JSON array expression",
					jsonArrayString));
		}

		int start = jsonArrayString.indexOf(StringPool.QUOTE);

		if (start == -1) {
			throw new IndexOutOfBoundsException(
				String.format(
					"%s JSON array has no first element", jsonArrayString));
		}

		return jsonArrayString.substring(
			start + 1, jsonArrayString.indexOf(StringPool.QUOTE, start + 1));
	}

	public JSONArray getValueAsJSONArray(String key, JSONObject jsonObject) {
		JSONArray valueJSONArray = jsonObject.getJSONArray(key);

		if (valueJSONArray != null) {
			return valueJSONArray;
		}

		valueJSONArray = _jsonFactory.createJSONArray();

		String valueString = jsonObject.getString(key);

		if (valueString == null) {
			return valueJSONArray;
		}

		valueJSONArray.put(valueString);

		return valueJSONArray;
	}

	public boolean isArray(String json) {
		if (Validator.isNull(json)) {
			return false;
		}

		if (json.startsWith(StringPool.OPEN_BRACKET) &&
			json.endsWith(StringPool.CLOSE_BRACKET)) {

			return true;
		}

		return false;
	}

	public boolean isEmpty(String json) {
		if (Validator.isNull(json)) {
			return true;
		}

		if (Objects.equals(json, "[]") || Objects.equals(json, "{}")) {
			return true;
		}

		return false;
	}

	@Override
	public JSONArray toJSONArray(Map<String, List<String>> keyValues) {
		JSONArray jsonArray = _jsonFactory.createJSONArray();

		for (Map.Entry<String, List<String>> keyValuesEntry :
				keyValues.entrySet()) {

			String key = keyValuesEntry.getKey();

			JSONObject arrayEntryJSONObject = _jsonFactory.createJSONObject();

			arrayEntryJSONObject.put("key", key);

			List<String> values = keyValuesEntry.getValue();

			JSONArray valuesJSONArray = _jsonFactory.createJSONArray();

			for (String value : values) {
				valuesJSONArray.put(value);
			}

			arrayEntryJSONObject.put("value", valuesJSONArray);

			jsonArray.put(arrayEntryJSONObject);
		}

		return jsonArray;
	}

	private JSONArray _toJSONArray(String jsonArrayString) {
		if (!isArray(jsonArrayString)) {
			throw new IllegalArgumentException(
				jsonArrayString + " is not valid JSON array");
		}

		try {
			return _jsonFactory.createJSONArray(jsonArrayString);
		}
		catch (JSONException jsonException) {
			throw new IllegalArgumentException(
				jsonArrayString + " is not valid JSON array");
		}
	}

	private Map<String, String> _toMap(JSONArray jsonArray) {
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String key = jsonObject.getString("key");
			String value = jsonObject.getString("value");

			map.put(key, value);
		}

		return map;
	}

	@Reference
	private JSONFactory _jsonFactory;

}