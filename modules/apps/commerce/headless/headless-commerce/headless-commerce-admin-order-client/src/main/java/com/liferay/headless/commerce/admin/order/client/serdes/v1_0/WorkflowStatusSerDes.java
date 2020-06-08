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

package com.liferay.headless.commerce.admin.order.client.serdes.v1_0;

import com.liferay.headless.commerce.admin.order.client.dto.v1_0.WorkflowStatus;
import com.liferay.headless.commerce.admin.order.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public class WorkflowStatusSerDes {

	public static WorkflowStatus toDTO(String json) {
		WorkflowStatusJSONParser workflowStatusJSONParser =
			new WorkflowStatusJSONParser();

		return workflowStatusJSONParser.parseToDTO(json);
	}

	public static WorkflowStatus[] toDTOs(String json) {
		WorkflowStatusJSONParser workflowStatusJSONParser =
			new WorkflowStatusJSONParser();

		return workflowStatusJSONParser.parseToDTOs(json);
	}

	public static String toJSON(WorkflowStatus workflowStatus) {
		if (workflowStatus == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (workflowStatus.getCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"code\": ");

			sb.append(workflowStatus.getCode());
		}

		if (workflowStatus.getLabel() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"label\": ");

			sb.append("\"");

			sb.append(_escape(workflowStatus.getLabel()));

			sb.append("\"");
		}

		if (workflowStatus.getLabelI18n() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"labelI18n\": ");

			sb.append("\"");

			sb.append(_escape(workflowStatus.getLabelI18n()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		WorkflowStatusJSONParser workflowStatusJSONParser =
			new WorkflowStatusJSONParser();

		return workflowStatusJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(WorkflowStatus workflowStatus) {
		if (workflowStatus == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (workflowStatus.getCode() == null) {
			map.put("code", null);
		}
		else {
			map.put("code", String.valueOf(workflowStatus.getCode()));
		}

		if (workflowStatus.getLabel() == null) {
			map.put("label", null);
		}
		else {
			map.put("label", String.valueOf(workflowStatus.getLabel()));
		}

		if (workflowStatus.getLabelI18n() == null) {
			map.put("labelI18n", null);
		}
		else {
			map.put("labelI18n", String.valueOf(workflowStatus.getLabelI18n()));
		}

		return map;
	}

	public static class WorkflowStatusJSONParser
		extends BaseJSONParser<WorkflowStatus> {

		@Override
		protected WorkflowStatus createDTO() {
			return new WorkflowStatus();
		}

		@Override
		protected WorkflowStatus[] createDTOArray(int size) {
			return new WorkflowStatus[size];
		}

		@Override
		protected void setField(
			WorkflowStatus workflowStatus, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "code")) {
				if (jsonParserFieldValue != null) {
					workflowStatus.setCode(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "label")) {
				if (jsonParserFieldValue != null) {
					workflowStatus.setLabel((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "labelI18n")) {
				if (jsonParserFieldValue != null) {
					workflowStatus.setLabelI18n((String)jsonParserFieldValue);
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