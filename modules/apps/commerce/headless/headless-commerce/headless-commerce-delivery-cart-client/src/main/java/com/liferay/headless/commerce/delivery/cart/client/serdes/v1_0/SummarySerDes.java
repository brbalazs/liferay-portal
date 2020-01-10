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

package com.liferay.headless.commerce.delivery.cart.client.serdes.v1_0;

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Summary;
import com.liferay.headless.commerce.delivery.cart.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class SummarySerDes {

	public static Summary toDTO(String json) {
		SummaryJSONParser summaryJSONParser = new SummaryJSONParser();

		return summaryJSONParser.parseToDTO(json);
	}

	public static Summary[] toDTOs(String json) {
		SummaryJSONParser summaryJSONParser = new SummaryJSONParser();

		return summaryJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Summary summary) {
		if (summary == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (summary.getDiscount() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"discount\": ");

			sb.append("\"");

			sb.append(_escape(summary.getDiscount()));

			sb.append("\"");
		}

		if (summary.getItemsQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"itemsQuantity\": ");

			sb.append(summary.getItemsQuantity());
		}

		if (summary.getSubTotal() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subTotal\": ");

			sb.append("\"");

			sb.append(_escape(summary.getSubTotal()));

			sb.append("\"");
		}

		if (summary.getTotal() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"total\": ");

			sb.append("\"");

			sb.append(_escape(summary.getTotal()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		SummaryJSONParser summaryJSONParser = new SummaryJSONParser();

		return summaryJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Summary summary) {
		if (summary == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (summary.getDiscount() == null) {
			map.put("discount", null);
		}
		else {
			map.put("discount", String.valueOf(summary.getDiscount()));
		}

		if (summary.getItemsQuantity() == null) {
			map.put("itemsQuantity", null);
		}
		else {
			map.put(
				"itemsQuantity", String.valueOf(summary.getItemsQuantity()));
		}

		if (summary.getSubTotal() == null) {
			map.put("subTotal", null);
		}
		else {
			map.put("subTotal", String.valueOf(summary.getSubTotal()));
		}

		if (summary.getTotal() == null) {
			map.put("total", null);
		}
		else {
			map.put("total", String.valueOf(summary.getTotal()));
		}

		return map;
	}

	public static class SummaryJSONParser extends BaseJSONParser<Summary> {

		@Override
		protected Summary createDTO() {
			return new Summary();
		}

		@Override
		protected Summary[] createDTOArray(int size) {
			return new Summary[size];
		}

		@Override
		protected void setField(
			Summary summary, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "discount")) {
				if (jsonParserFieldValue != null) {
					summary.setDiscount((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "itemsQuantity")) {
				if (jsonParserFieldValue != null) {
					summary.setItemsQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "subTotal")) {
				if (jsonParserFieldValue != null) {
					summary.setSubTotal((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "total")) {
				if (jsonParserFieldValue != null) {
					summary.setTotal((String)jsonParserFieldValue);
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
			else {
				sb.append("\"");
				sb.append(_escape(entry.getValue()));
				sb.append("\"");
			}

			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}