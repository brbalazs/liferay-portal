/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.json;

import com.liferay.osb.asah.common.function.UnsafeFunction;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Brian Wing Shun Chan
 * @author Rachael Koestartyo
 */
public class JSONUtil {

	public static void addToStringCollection(
		Collection<String> collection, JSONArray jsonArray) {

		if (jsonArray == null) {
			return;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			collection.add(jsonArray.getString(i));
		}
	}

	public static void addToStringCollection(
		Collection<String> collection, JSONArray jsonArray,
		String jsonObjectKey) {

		if (jsonArray == null) {
			return;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Object value = jsonObject.opt(jsonObjectKey);

			if (value != null) {
				collection.add((String)value);
			}
		}
	}

	public static JSONArray concat(JSONArray... jsonArrays) {
		JSONArray newJSONArray = _createJSONArray();

		for (JSONArray jsonArray : jsonArrays) {
			for (int i = 0; i < jsonArray.length(); i++) {
				newJSONArray.put(jsonArray.opt(i));
			}
		}

		return newJSONArray;
	}

	public static Collector<Object, JSONArray, JSONArray> createCollector() {
		return Collector.of(
			JSONUtil::_createJSONArray, JSONArray::put, JSONUtil::concat);
	}

	public static boolean equals(JSONArray jsonArray1, JSONArray jsonArray2) {
		return Objects.equals(jsonArray1.toString(), jsonArray2.toString());
	}

	public static boolean equals(
		JSONObject jsonObject1, JSONObject jsonObject2) {

		return Objects.equals(jsonObject1.toString(), jsonObject2.toString());
	}

	public static JSONObject find(
		JSONArray jsonArray, String key, Object value) {

		if (jsonArray == null) {
			return null;
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			if (Objects.equals(value, jsonObject.get(key))) {
				return jsonObject;
			}
		}

		return null;
	}

	public static Object getValue(Object object, String... paths) {
		Object value = null;

		String[] parts = paths[0].split("/");

		String type = parts[0];
		String key = parts[1];

		if (type.equals("JSONArray")) {
			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				value = jsonArray.getJSONArray(Integer.parseInt(key));
			}
			else if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject)object;

				value = jsonObject.getJSONArray(key);
			}
		}
		else if (type.equals("JSONObject")) {
			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				value = jsonArray.getJSONObject(Integer.parseInt(key));
			}
			else if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject)object;

				value = jsonObject.getJSONObject(key);
			}
		}
		else if (type.equals("Object")) {
			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				value = jsonArray.get(Integer.parseInt(key));
			}
			else if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject)object;

				value = jsonObject.get(key);
			}
		}

		if (paths.length == 1) {
			return value;
		}

		return getValue(value, Arrays.copyOfRange(paths, 1, paths.length));
	}

	public static boolean hasValue(JSONArray jsonArray, Object value) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (Objects.equals(value, jsonArray.get(i))) {
				return true;
			}
		}

		return false;
	}

	public static JSONObject merge(
			JSONObject jsonObject1, JSONObject jsonObject2)
		throws JSONException {

		if (jsonObject1 == null) {
			return _createJSONObject(jsonObject2.toString());
		}

		if (jsonObject2 == null) {
			return _createJSONObject(jsonObject1.toString());
		}

		JSONObject jsonObject3 = _createJSONObject(jsonObject1.toString());

		Iterator<String> iterator = jsonObject2.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			jsonObject3.put(key, jsonObject2.get(key));
		}

		return jsonObject3;
	}

	public static Long optLong(
		Long defaultValue, JSONObject jsonObject, String key) {

		if (!jsonObject.has(key) ||
			StringUtils.isBlank(jsonObject.optString(key))) {

			return defaultValue;
		}

		return jsonObject.getLong(key);
	}

	public static JSONArray put(Object value) {
		JSONArray jsonArray = _createJSONArray();

		jsonArray.put(value);

		return jsonArray;
	}

	public static JSONObject put(String key, Object value) {
		JSONObject jsonObject = _createJSONObject();

		return jsonObject.put(key, value);
	}

	public static JSONArray putAll(Object... values) {
		JSONArray jsonArray = _createJSONArray();

		for (Object value : values) {
			jsonArray.put(value);
		}

		return jsonArray;
	}

	public static void removeValue(JSONArray jsonArray, Object object) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (Objects.equals(object, jsonArray.get(i))) {
				jsonArray.remove(i);
			}
		}
	}

	public static JSONArray replace(
		JSONArray jsonArray, String jsonObjectKey, JSONObject newJSONObject) {

		if (jsonArray == null) {
			return null;
		}

		JSONArray newJSONArray = _createJSONArray();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			if (Objects.equals(
					jsonObject.getString(jsonObjectKey),
					newJSONObject.getString(jsonObjectKey))) {

				newJSONArray.put(newJSONObject);
			}
			else {
				newJSONArray.put(jsonObject);
			}
		}

		return newJSONArray;
	}

	public static <T> T[] toArray(
			JSONArray jsonArray,
			UnsafeFunction<JSONObject, T, Exception> unsafeFunction,
			Class<?> clazz)
		throws Exception {

		List<T> list = toList(jsonArray, unsafeFunction);

		return list.toArray((T[])Array.newInstance(clazz, 0));
	}

	public static <T> JSONArray toJSONArray(
			List<T> list, UnsafeFunction<T, Object, Exception> unsafeFunction)
		throws Exception {

		JSONArray jsonArray = _createJSONArray();

		if (list == null) {
			return jsonArray;
		}

		for (T t : list) {
			jsonArray.put(unsafeFunction.apply(t));
		}

		return jsonArray;
	}

	public static <T> JSONArray toJSONArray(
			T[] array, UnsafeFunction<T, Object, Exception> unsafeFunction)
		throws Exception {

		JSONArray jsonArray = _createJSONArray();

		if (array == null) {
			return jsonArray;
		}

		for (T t : array) {
			jsonArray.put(unsafeFunction.apply(t));
		}

		return jsonArray;
	}

	public static Map<String, JSONObject> toJSONObjectMap(
		JSONArray jsonArray, String jsonObjectKey) {

		Map<String, JSONObject> values = new HashMap<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			values.put(
				String.valueOf(jsonObject.get(jsonObjectKey)), jsonObject);
		}

		return values;
	}

	public static <T> List<T> toList(
			JSONArray jsonArray,
			UnsafeFunction<JSONObject, T, Exception> unsafeFunction)
		throws Exception {

		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<T> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(unsafeFunction.apply(jsonArray.getJSONObject(i)));
		}

		return values;
	}

	public static long[] toLongArray(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new long[0];
		}

		long[] values = new long[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			values[i] = jsonArray.getLong(i);
		}

		return values;
	}

	public static long[] toLongArray(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new long[0];
		}

		List<Long> values = toLongList(jsonArray, jsonObjectKey);

		return ArrayUtils.toPrimitive(values.toArray(new Long[0]));
	}

	public static List<Long> toLongList(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<Long> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(jsonArray.getLong(i));
		}

		return values;
	}

	public static List<Long> toLongList(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<Long> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Object value = jsonObject.opt(jsonObjectKey);

			if (value != null) {
				values.add(jsonObject.getLong(jsonObjectKey));
			}
		}

		return values;
	}

	public static Set<Long> toLongSet(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new HashSet<>();
		}

		Set<Long> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(jsonArray.getLong(i));
		}

		return values;
	}

	public static Set<Long> toLongSet(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new HashSet<>();
		}

		Set<Long> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Object value = jsonObject.opt(jsonObjectKey);

			if (value != null) {
				values.add(jsonObject.getLong(jsonObjectKey));
			}
		}

		return values;
	}

	public static Map<String, Object> toMap(JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}

		return jsonObject.toMap();
	}

	public static Object[] toObjectArray(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new Object[0];
		}

		Object[] values = new Object[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			values[i] = jsonArray.get(i);
		}

		return values;
	}

	public static Object[] toObjectArray(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new Object[0];
		}

		List<Object> values = toObjectList(jsonArray, jsonObjectKey);

		return values.toArray(new Object[0]);
	}

	public static List<Object> toObjectList(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<Object> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(jsonArray.get(i));
		}

		return values;
	}

	public static List<Object> toObjectList(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<Object> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Object value = jsonObject.opt(jsonObjectKey);

			if (value != null) {
				values.add(value);
			}
		}

		return values;
	}

	public static Set<Object> toObjectSet(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new HashSet<>();
		}

		Set<Object> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(jsonArray.get(i));
		}

		return values;
	}

	public static Set<Object> toObjectSet(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new HashSet<>();
		}

		Set<Object> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Object value = jsonObject.opt(jsonObjectKey);

			if (value != null) {
				values.add(value);
			}
		}

		return values;
	}

	public static Stream<Object> toObjectStream(JSONArray jsonArray) {
		return StreamSupport.stream(jsonArray.spliterator(), false);
	}

	public static <T> List<T> toSafeList(
		JSONArray jsonArray, Function<JSONObject, T> safeFunction) {

		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<T> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(safeFunction.apply(jsonArray.getJSONObject(i)));
		}

		return values;
	}

	public static String[] toStringArray(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new String[0];
		}

		String[] values = new String[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			values[i] = jsonArray.getString(i);
		}

		return values;
	}

	public static String[] toStringArray(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new String[0];
		}

		List<String> values = toStringList(jsonArray, jsonObjectKey);

		return values.toArray(new String[0]);
	}

	public static List<String> toStringList(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<String> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(jsonArray.getString(i));
		}

		return values;
	}

	public static List<String> toStringList(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new ArrayList<>();
		}

		List<String> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Object value = jsonObject.opt(jsonObjectKey);

			if (value != null) {
				values.add((String)value);
			}
		}

		return values;
	}

	public static Set<String> toStringSet(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new HashSet<>();
		}

		Set<String> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(jsonArray.getString(i));
		}

		return values;
	}

	public static Set<String> toStringSet(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new HashSet<>();
		}

		Set<String> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			Object value = jsonObject.opt(jsonObjectKey);

			if (value != null) {
				values.add((String)value);
			}
		}

		return values;
	}

	private static JSONArray _createJSONArray() {
		return new JSONArray();
	}

	private static JSONObject _createJSONObject() {
		return new JSONObject();
	}

	private static JSONObject _createJSONObject(String json)
		throws JSONException {

		return new JSONObject(json);
	}

}