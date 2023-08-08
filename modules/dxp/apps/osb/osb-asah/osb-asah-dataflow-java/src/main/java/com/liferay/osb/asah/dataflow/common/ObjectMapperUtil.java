/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.codehaus.jackson.map.util.ISO8601DateFormat;

/**
 * @author Marcellus Tavares
 */
public class ObjectMapperUtil {

	public static <T> T convertValue(Class<T> clazz, Object value) {
		return _objectMapper.convertValue(value, clazz);
	}

	public static <T> T readValue(Class<T> clazz, String json)
		throws JsonProcessingException {

		return _objectMapper.readValue(json, clazz);
	}

	public static String writeValueAsString(Object value) {
		try {
			return _objectMapper.writeValueAsString(value);
		}
		catch (JsonProcessingException jsonProcessingException) {
			return null;
		}
	}

	private static final ObjectMapper _objectMapper;

	static {
		_objectMapper = new ObjectMapper() {
			{
				configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				configure(
					DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD,
					JsonAutoDetect.Visibility.PUBLIC_ONLY);
				setVisibility(
					PropertyAccessor.GETTER,
					JsonAutoDetect.Visibility.PUBLIC_ONLY);
			}
		};
	}

}