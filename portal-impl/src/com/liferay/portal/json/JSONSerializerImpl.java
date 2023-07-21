/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.json;

import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.json.JSONTransformer;
import com.liferay.portal.kernel.util.JavaDetector;

import jodd.json.JoddJson;
import jodd.json.JsonContext;
import jodd.json.JsonSerializer;
import jodd.json.TypeJsonSerializer;

import jodd.util.SystemUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Igor Spasic
 */
public class JSONSerializerImpl implements JSONSerializer {

	public JSONSerializerImpl() {
		if (JavaDetector.isIBM()) {
			SystemUtil.disableUnsafeUsage();
		}

		_jsonSerializer = new JsonSerializer();
	}

	@Override
	public JSONSerializerImpl exclude(String... fields) {
		_jsonSerializer.exclude(fields);

		return this;
	}

	@Override
	public JSONSerializerImpl include(String... fields) {
		_jsonSerializer.include(fields);

		return this;
	}

	@Override
	public String serialize(Object target) {
		return _jsonSerializer.serialize(target);
	}

	@Override
	public String serializeDeep(Object target) {
		JsonSerializer jsonSerializer = _jsonSerializer.deep(true);

		return jsonSerializer.serialize(target);
	}

	@Override
	public JSONSerializerImpl transform(
		JSONTransformer jsonTransformer, Class<?> type) {

		TypeJsonSerializer<?> typeJsonSerializer = null;

		if (jsonTransformer instanceof TypeJsonSerializer) {
			typeJsonSerializer = (TypeJsonSerializer<?>)jsonTransformer;
		}
		else {
			typeJsonSerializer = new JoddJsonTransformer(jsonTransformer);
		}

		_jsonSerializer.use(type, typeJsonSerializer);

		return this;
	}

	@Override
	public JSONSerializerImpl transform(
		JSONTransformer jsonTransformer, String field) {

		TypeJsonSerializer<?> typeJsonSerializer = null;

		if (jsonTransformer instanceof TypeJsonSerializer) {
			typeJsonSerializer = (TypeJsonSerializer<?>)jsonTransformer;
		}
		else {
			typeJsonSerializer = new JoddJsonTransformer(jsonTransformer);
		}

		_jsonSerializer.use(field, typeJsonSerializer);

		return this;
	}

	private final JsonSerializer _jsonSerializer;

	private static class JSONArrayTypeJSONSerializer
		implements TypeJsonSerializer<JSONArray> {

		@Override
		public void serialize(JsonContext jsonContext, JSONArray jsonArray) {
			jsonContext.write(jsonArray.toString());
		}

	}

	private static class JSONObjectTypeJSONSerializer
		implements TypeJsonSerializer<JSONObject> {

		@Override
		public void serialize(JsonContext jsonContext, JSONObject jsonObject) {
			jsonContext.write(jsonObject.toString());
		}

	}

	private static class LongToStringTypeJSONSerializer
		implements TypeJsonSerializer<Long> {

		@Override
		public void serialize(JsonContext jsonContext, Long value) {
			jsonContext.writeString(String.valueOf(value));
		}

	}

	static {
		JoddJson.defaultSerializers.register(
			JSONArray.class, new JSONArrayTypeJSONSerializer());
		JoddJson.defaultSerializers.register(
			JSONObject.class, new JSONObjectTypeJSONSerializer());
		JoddJson.defaultSerializers.register(
			Long.TYPE, new LongToStringTypeJSONSerializer());
		JoddJson.defaultSerializers.register(
			Long.class, new LongToStringTypeJSONSerializer());
	}

}