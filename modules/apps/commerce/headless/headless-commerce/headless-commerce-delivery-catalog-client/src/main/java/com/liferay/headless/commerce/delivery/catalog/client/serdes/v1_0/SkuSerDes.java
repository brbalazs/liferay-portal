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

package com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0;

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.Sku;
import com.liferay.headless.commerce.delivery.catalog.client.json.BaseJSONParser;

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
public class SkuSerDes {

	public static Sku toDTO(String json) {
		SkuJSONParser skuJSONParser = new SkuJSONParser();

		return skuJSONParser.parseToDTO(json);
	}

	public static Sku[] toDTOs(String json) {
		SkuJSONParser skuJSONParser = new SkuJSONParser();

		return skuJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Sku sku) {
		if (sku == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (sku.getAllowedOrderQuantities() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"allowedOrderQuantities\": ");

			sb.append("[");

			for (int i = 0; i < sku.getAllowedOrderQuantities().length; i++) {
				sb.append("\"");

				sb.append(_escape(sku.getAllowedOrderQuantities()[i]));

				sb.append("\"");

				if ((i + 1) < sku.getAllowedOrderQuantities().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (sku.getAvailability() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"availability\": ");

			sb.append(String.valueOf(sku.getAvailability()));
		}

		if (sku.getMaxOrderQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxOrderQuantity\": ");

			sb.append(sku.getMaxOrderQuantity());
		}

		if (sku.getMinOrderQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"minOrderQuantity\": ");

			sb.append(sku.getMinOrderQuantity());
		}

		if (sku.getPrice() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"price\": ");

			sb.append(String.valueOf(sku.getPrice()));
		}

		if (sku.getSku() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sku\": ");

			sb.append("\"");

			sb.append(_escape(sku.getSku()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		SkuJSONParser skuJSONParser = new SkuJSONParser();

		return skuJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Sku sku) {
		if (sku == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (sku.getAllowedOrderQuantities() == null) {
			map.put("allowedOrderQuantities", null);
		}
		else {
			map.put(
				"allowedOrderQuantities",
				String.valueOf(sku.getAllowedOrderQuantities()));
		}

		if (sku.getAvailability() == null) {
			map.put("availability", null);
		}
		else {
			map.put("availability", String.valueOf(sku.getAvailability()));
		}

		if (sku.getMaxOrderQuantity() == null) {
			map.put("maxOrderQuantity", null);
		}
		else {
			map.put(
				"maxOrderQuantity", String.valueOf(sku.getMaxOrderQuantity()));
		}

		if (sku.getMinOrderQuantity() == null) {
			map.put("minOrderQuantity", null);
		}
		else {
			map.put(
				"minOrderQuantity", String.valueOf(sku.getMinOrderQuantity()));
		}

		if (sku.getPrice() == null) {
			map.put("price", null);
		}
		else {
			map.put("price", String.valueOf(sku.getPrice()));
		}

		if (sku.getSku() == null) {
			map.put("sku", null);
		}
		else {
			map.put("sku", String.valueOf(sku.getSku()));
		}

		return map;
	}

	public static class SkuJSONParser extends BaseJSONParser<Sku> {

		@Override
		protected Sku createDTO() {
			return new Sku();
		}

		@Override
		protected Sku[] createDTOArray(int size) {
			return new Sku[size];
		}

		@Override
		protected void setField(
			Sku sku, String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "allowedOrderQuantities")) {
				if (jsonParserFieldValue != null) {
					sku.setAllowedOrderQuantities(
						toStrings((Object[])jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "availability")) {
				if (jsonParserFieldValue != null) {
					sku.setAvailability(
						AvailabilitySerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "maxOrderQuantity")) {
				if (jsonParserFieldValue != null) {
					sku.setMaxOrderQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "minOrderQuantity")) {
				if (jsonParserFieldValue != null) {
					sku.setMinOrderQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "price")) {
				if (jsonParserFieldValue != null) {
					sku.setPrice(
						PriceSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sku")) {
				if (jsonParserFieldValue != null) {
					sku.setSku((String)jsonParserFieldValue);
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