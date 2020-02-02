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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartItemPost;
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
public class CartItemPostSerDes {

	public static CartItemPost toDTO(String json) {
		CartItemPostJSONParser cartItemPostJSONParser =
			new CartItemPostJSONParser();

		return cartItemPostJSONParser.parseToDTO(json);
	}

	public static CartItemPost[] toDTOs(String json) {
		CartItemPostJSONParser cartItemPostJSONParser =
			new CartItemPostJSONParser();

		return cartItemPostJSONParser.parseToDTOs(json);
	}

	public static String toJSON(CartItemPost cartItemPost) {
		if (cartItemPost == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (cartItemPost.getCustomFields() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"customFields\": ");

			sb.append(_toJSON(cartItemPost.getCustomFields()));
		}

		if (cartItemPost.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(cartItemPost.getId());
		}

		if (cartItemPost.getOptions() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"options\": ");

			sb.append("\"");

			sb.append(_escape(cartItemPost.getOptions()));

			sb.append("\"");
		}

		if (cartItemPost.getQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"quantity\": ");

			sb.append(cartItemPost.getQuantity());
		}

		if (cartItemPost.getSkuId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"skuId\": ");

			sb.append(cartItemPost.getSkuId());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		CartItemPostJSONParser cartItemPostJSONParser =
			new CartItemPostJSONParser();

		return cartItemPostJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(CartItemPost cartItemPost) {
		if (cartItemPost == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (cartItemPost.getCustomFields() == null) {
			map.put("customFields", null);
		}
		else {
			map.put(
				"customFields", String.valueOf(cartItemPost.getCustomFields()));
		}

		if (cartItemPost.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(cartItemPost.getId()));
		}

		if (cartItemPost.getOptions() == null) {
			map.put("options", null);
		}
		else {
			map.put("options", String.valueOf(cartItemPost.getOptions()));
		}

		if (cartItemPost.getQuantity() == null) {
			map.put("quantity", null);
		}
		else {
			map.put("quantity", String.valueOf(cartItemPost.getQuantity()));
		}

		if (cartItemPost.getSkuId() == null) {
			map.put("skuId", null);
		}
		else {
			map.put("skuId", String.valueOf(cartItemPost.getSkuId()));
		}

		return map;
	}

	public static class CartItemPostJSONParser
		extends BaseJSONParser<CartItemPost> {

		@Override
		protected CartItemPost createDTO() {
			return new CartItemPost();
		}

		@Override
		protected CartItemPost[] createDTOArray(int size) {
			return new CartItemPost[size];
		}

		@Override
		protected void setField(
			CartItemPost cartItemPost, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "customFields")) {
				if (jsonParserFieldValue != null) {
					cartItemPost.setCustomFields(
						(Map)CartItemPostSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					cartItemPost.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "options")) {
				if (jsonParserFieldValue != null) {
					cartItemPost.setOptions((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "quantity")) {
				if (jsonParserFieldValue != null) {
					cartItemPost.setQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "skuId")) {
				if (jsonParserFieldValue != null) {
					cartItemPost.setSkuId(
						Long.valueOf((String)jsonParserFieldValue));
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