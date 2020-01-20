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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartItem;
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
public class CartItemSerDes {

	public static CartItem toDTO(String json) {
		CartItemJSONParser cartItemJSONParser = new CartItemJSONParser();

		return cartItemJSONParser.parseToDTO(json);
	}

	public static CartItem[] toDTOs(String json) {
		CartItemJSONParser cartItemJSONParser = new CartItemJSONParser();

		return cartItemJSONParser.parseToDTOs(json);
	}

	public static String toJSON(CartItem cartItem) {
		if (cartItem == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (cartItem.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(cartItem.getId());
		}

		if (cartItem.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(cartItem.getName()));

			sb.append("\"");
		}

		if (cartItem.getPrice() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"price\": ");

			sb.append(String.valueOf(cartItem.getPrice()));
		}

		if (cartItem.getQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"quantity\": ");

			sb.append(cartItem.getQuantity());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		CartItemJSONParser cartItemJSONParser = new CartItemJSONParser();

		return cartItemJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(CartItem cartItem) {
		if (cartItem == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (cartItem.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(cartItem.getId()));
		}

		if (cartItem.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(cartItem.getName()));
		}

		if (cartItem.getPrice() == null) {
			map.put("price", null);
		}
		else {
			map.put("price", String.valueOf(cartItem.getPrice()));
		}

		if (cartItem.getQuantity() == null) {
			map.put("quantity", null);
		}
		else {
			map.put("quantity", String.valueOf(cartItem.getQuantity()));
		}

		return map;
	}

	public static class CartItemJSONParser extends BaseJSONParser<CartItem> {

		@Override
		protected CartItem createDTO() {
			return new CartItem();
		}

		@Override
		protected CartItem[] createDTOArray(int size) {
			return new CartItem[size];
		}

		@Override
		protected void setField(
			CartItem cartItem, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					cartItem.setId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					cartItem.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "price")) {
				if (jsonParserFieldValue != null) {
					cartItem.setPrice(
						PriceSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "quantity")) {
				if (jsonParserFieldValue != null) {
					cartItem.setQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
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