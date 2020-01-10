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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.OrderItem;
import com.liferay.headless.commerce.delivery.cart.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class CartSerDes {

	public static Cart toDTO(String json) {
		CartJSONParser cartJSONParser = new CartJSONParser();

		return cartJSONParser.parseToDTO(json);
	}

	public static Cart[] toDTOs(String json) {
		CartJSONParser cartJSONParser = new CartJSONParser();

		return cartJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Cart cart) {
		if (cart == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (cart.getAccountId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountId\": ");

			sb.append(cart.getAccountId());
		}

		if (cart.getOrderId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"orderId\": ");

			sb.append(cart.getOrderId());
		}

		if (cart.getOrderItems() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"orderItems\": ");

			sb.append("[");

			for (int i = 0; i < cart.getOrderItems().length; i++) {
				sb.append(String.valueOf(cart.getOrderItems()[i]));

				if ((i + 1) < cart.getOrderItems().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (cart.getSummary() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"summary\": ");

			sb.append(String.valueOf(cart.getSummary()));
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		CartJSONParser cartJSONParser = new CartJSONParser();

		return cartJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Cart cart) {
		if (cart == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (cart.getAccountId() == null) {
			map.put("accountId", null);
		}
		else {
			map.put("accountId", String.valueOf(cart.getAccountId()));
		}

		if (cart.getOrderId() == null) {
			map.put("orderId", null);
		}
		else {
			map.put("orderId", String.valueOf(cart.getOrderId()));
		}

		if (cart.getOrderItems() == null) {
			map.put("orderItems", null);
		}
		else {
			map.put("orderItems", String.valueOf(cart.getOrderItems()));
		}

		if (cart.getSummary() == null) {
			map.put("summary", null);
		}
		else {
			map.put("summary", String.valueOf(cart.getSummary()));
		}

		return map;
	}

	public static class CartJSONParser extends BaseJSONParser<Cart> {

		@Override
		protected Cart createDTO() {
			return new Cart();
		}

		@Override
		protected Cart[] createDTOArray(int size) {
			return new Cart[size];
		}

		@Override
		protected void setField(
			Cart cart, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "accountId")) {
				if (jsonParserFieldValue != null) {
					cart.setAccountId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "orderId")) {
				if (jsonParserFieldValue != null) {
					cart.setOrderId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "orderItems")) {
				if (jsonParserFieldValue != null) {
					cart.setOrderItems(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> OrderItemSerDes.toDTO((String)object)
						).toArray(
							size -> new OrderItem[size]
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "summary")) {
				if (jsonParserFieldValue != null) {
					cart.setSummary(
						SummarySerDes.toDTO((String)jsonParserFieldValue));
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