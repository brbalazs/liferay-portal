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

package com.liferay.headless.commerce.punchout.client.serdes.v1_0;

import com.liferay.headless.commerce.punchout.client.dto.v1_0.Organization;
import com.liferay.headless.commerce.punchout.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Jaclyn Ong
 * @generated
 */
@Generated("")
public class OrganizationSerDes {

	public static Organization toDTO(String json) {
		OrganizationJSONParser organizationJSONParser =
			new OrganizationJSONParser();

		return organizationJSONParser.parseToDTO(json);
	}

	public static Organization[] toDTOs(String json) {
		OrganizationJSONParser organizationJSONParser =
			new OrganizationJSONParser();

		return organizationJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Organization organization) {
		if (organization == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (organization.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append("\"");

			sb.append(_escape(organization.getId()));

			sb.append("\"");
		}

		if (organization.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(organization.getName()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		OrganizationJSONParser organizationJSONParser =
			new OrganizationJSONParser();

		return organizationJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Organization organization) {
		if (organization == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (organization.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(organization.getId()));
		}

		if (organization.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(organization.getName()));
		}

		return map;
	}

	public static class OrganizationJSONParser
		extends BaseJSONParser<Organization> {

		@Override
		protected Organization createDTO() {
			return new Organization();
		}

		@Override
		protected Organization[] createDTOArray(int size) {
			return new Organization[size];
		}

		@Override
		protected void setField(
			Organization organization, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					organization.setId((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					organization.setName((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\":");

			Object value = entry.getValue();

			Class<?> valueClass = value.getClass();

			if (value instanceof Map) {
				sb.append(_toJSON((Map)value));
			}
			else if (valueClass.isArray()) {
				Object[] values = (Object[])value;

				sb.append("[");

				for (int i = 0; i < values.length; i++) {
					sb.append("\"");
					sb.append(_escape(values[i]));
					sb.append("\"");

					if ((i + 1) < values.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(entry.getValue()));
				sb.append("\"");
			}
			else {
				sb.append(String.valueOf(entry.getValue()));
			}

			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}