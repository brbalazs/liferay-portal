/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.util.ListUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class JSONAvroTransformer {

	public GenericData.Record transform(JSONObject jsonObject, Schema schema) {
		return _toRecord(
			_objectMapper.convertValue(jsonObject, Map.class), schema);
	}

	private Object _toEnumSymbol(
		Schema.Field field, Schema fieldSchema, Object fieldValue) {

		List<String> enumSymbols = fieldSchema.getEnumSymbols();

		if (enumSymbols.contains(fieldValue)) {
			return new GenericData.EnumSymbol(fieldSchema, fieldValue);
		}

		throw new IllegalStateException(
			"Enum symbol incorrectly set for field " + field.name());
	}

	private List<Object> _toListItemType(
		Schema.Field field, Schema fieldSchema, List<Object> items) {

		return ListUtil.map(
			items,
			element -> _toType(field, fieldSchema.getElementType(), element));
	}

	private Map<String, Object> _toMapValueType(
		Schema.Field field, Schema fieldSchema, Map<String, Object> map) {

		Map<String, Object> result = new HashMap<>(map.size());

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			result.put(
				entry.getKey(),
				_toType(field, fieldSchema.getValueType(), entry.getValue()));
		}

		return result;
	}

	private GenericData.Record _toRecord(
		Map<String, Object> map, Schema schema) {

		GenericRecordBuilder genericRecordBuilder = new GenericRecordBuilder(
			schema);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Schema.Field field = schema.getField(entry.getKey());

			if (field == null) {
				continue;
			}

			genericRecordBuilder.set(
				field, _toType(field, field.schema(), entry.getValue()));
		}

		return genericRecordBuilder.build();
	}

	private <T> Object _toType(
		Schema.Field field, Class<T> type, Object fieldValue,
		Function<T, Object> mapperFunction) {

		if (!type.isInstance(fieldValue)) {
			throw new IllegalStateException(
				"Value was incorrectly set for field " + field.name());
		}

		return mapperFunction.apply((T)fieldValue);
	}

	private Object _toType(
		Schema.Field field, Schema fieldSchema, Object fieldValue) {

		Schema.Type fieldType = fieldSchema.getType();

		if (fieldType == Schema.Type.ARRAY) {
			return _toType(
				field, List.class, fieldValue,
				list -> _toListItemType(field, fieldSchema, list));
		}

		if (fieldType == Schema.Type.BOOLEAN) {
			return _toType(
				field, Boolean.class, fieldValue, Boolean::booleanValue);
		}

		if (fieldType == Schema.Type.BYTES) {
			return _toType(
				field, String.class, fieldValue,
				string -> ByteBuffer.wrap(
					string.getBytes(StandardCharsets.UTF_8)));
		}

		if (fieldType == Schema.Type.DOUBLE) {
			return _toType(
				field, Double.class, fieldValue, Double::doubleValue);
		}

		if (fieldType == Schema.Type.ENUM) {
			return _toType(
				field, String.class, fieldValue,
				string -> _toEnumSymbol(field, fieldSchema, string));
		}

		if (fieldType == Schema.Type.FLOAT) {
			return _toType(field, Float.class, fieldValue, Float::floatValue);
		}

		if (fieldType == Schema.Type.INT) {
			return _toType(field, Integer.class, fieldValue, Integer::intValue);
		}

		if (fieldType == Schema.Type.LONG) {
			return _toType(field, Long.class, fieldValue, Long::longValue);
		}

		if (fieldType == Schema.Type.MAP) {
			return _toType(
				field, Map.class, fieldValue,
				map -> _toMapValueType(field, fieldSchema, map));
		}

		if (fieldType == Schema.Type.NULL) {
			if (fieldValue != null) {
				throw new IllegalStateException(
					"Nonnull value set for field " + field.name());
			}

			return null;
		}

		if (fieldType == Schema.Type.RECORD) {
			return _toType(
				field, Map.class, fieldValue,
				map -> _toRecord(map, fieldSchema));
		}

		if (fieldType == Schema.Type.STRING) {
			return _toType(field, String.class, fieldValue, string -> string);
		}

		if (fieldType == Schema.Type.UNION) {
			return _toUnionType(field, fieldSchema, fieldValue);
		}

		throw new IllegalStateException("Unsupported type " + fieldType);
	}

	private Object _toUnionType(
		Schema.Field field, Schema fieldSchema, Object fieldValue) {

		for (Schema fieldSchemaType : fieldSchema.getTypes()) {
			try {
				return _toType(field, fieldSchemaType, fieldValue);
			}
			catch (IllegalStateException illegalStateException) {
				_log.error(illegalStateException, illegalStateException);
			}
		}

		throw new IllegalStateException(
			"Value was incorrectly set for field " + field.name());
	}

	private static final Log _log = LogFactory.getLog(
		JSONAvroTransformer.class);

	@Autowired
	private ObjectMapper _objectMapper;

}