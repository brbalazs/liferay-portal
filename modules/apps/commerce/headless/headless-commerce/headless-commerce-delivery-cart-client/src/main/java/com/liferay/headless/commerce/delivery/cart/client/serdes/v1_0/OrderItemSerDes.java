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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.OrderItem;
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
public class OrderItemSerDes {

	public static OrderItem toDTO(String json) {
		OrderItemJSONParser orderItemJSONParser = new OrderItemJSONParser();

		return orderItemJSONParser.parseToDTO(json);
	}

	public static OrderItem[] toDTOs(String json) {
		OrderItemJSONParser orderItemJSONParser = new OrderItemJSONParser();

		return orderItemJSONParser.parseToDTOs(json);
	}

	public static String toJSON(OrderItem orderItem) {
		if (orderItem == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (orderItem.getBillingAddress() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"billingAddress\": ");

			sb.append(String.valueOf(orderItem.getBillingAddress()));
		}

		if (orderItem.getBillingAddressId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"billingAddressId\": ");

			sb.append(orderItem.getBillingAddressId());
		}

		if (orderItem.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(orderItem.getId());
		}

		if (orderItem.getOptions() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"options\": ");

			sb.append("\"");

			sb.append(_escape(orderItem.getOptions()));

			sb.append("\"");
		}

		if (orderItem.getProductId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"productId\": ");

			sb.append(orderItem.getProductId());
		}

		if (orderItem.getQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"quantity\": ");

			sb.append(orderItem.getQuantity());
		}

		if (orderItem.getShippingAddress() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingAddress\": ");

			sb.append(String.valueOf(orderItem.getShippingAddress()));
		}

		if (orderItem.getShippingAddressId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingAddressId\": ");

			sb.append(orderItem.getShippingAddressId());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		OrderItemJSONParser orderItemJSONParser = new OrderItemJSONParser();

		return orderItemJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(OrderItem orderItem) {
		if (orderItem == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (orderItem.getBillingAddress() == null) {
			map.put("billingAddress", null);
		}
		else {
			map.put(
				"billingAddress",
				String.valueOf(orderItem.getBillingAddress()));
		}

		if (orderItem.getBillingAddressId() == null) {
			map.put("billingAddressId", null);
		}
		else {
			map.put(
				"billingAddressId",
				String.valueOf(orderItem.getBillingAddressId()));
		}

		if (orderItem.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(orderItem.getId()));
		}

		if (orderItem.getOptions() == null) {
			map.put("options", null);
		}
		else {
			map.put("options", String.valueOf(orderItem.getOptions()));
		}

		if (orderItem.getProductId() == null) {
			map.put("productId", null);
		}
		else {
			map.put("productId", String.valueOf(orderItem.getProductId()));
		}

		if (orderItem.getQuantity() == null) {
			map.put("quantity", null);
		}
		else {
			map.put("quantity", String.valueOf(orderItem.getQuantity()));
		}

		if (orderItem.getShippingAddress() == null) {
			map.put("shippingAddress", null);
		}
		else {
			map.put(
				"shippingAddress",
				String.valueOf(orderItem.getShippingAddress()));
		}

		if (orderItem.getShippingAddressId() == null) {
			map.put("shippingAddressId", null);
		}
		else {
			map.put(
				"shippingAddressId",
				String.valueOf(orderItem.getShippingAddressId()));
		}

		return map;
	}

	public static class OrderItemJSONParser extends BaseJSONParser<OrderItem> {

		@Override
		protected OrderItem createDTO() {
			return new OrderItem();
		}

		@Override
		protected OrderItem[] createDTOArray(int size) {
			return new OrderItem[size];
		}

		@Override
		protected void setField(
			OrderItem orderItem, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "billingAddress")) {
				if (jsonParserFieldValue != null) {
					orderItem.setBillingAddress(
						BillingAddressSerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "billingAddressId")) {
				if (jsonParserFieldValue != null) {
					orderItem.setBillingAddressId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					orderItem.setId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "options")) {
				if (jsonParserFieldValue != null) {
					orderItem.setOptions((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "productId")) {
				if (jsonParserFieldValue != null) {
					orderItem.setProductId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "quantity")) {
				if (jsonParserFieldValue != null) {
					orderItem.setQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingAddress")) {
				if (jsonParserFieldValue != null) {
					orderItem.setShippingAddress(
						ShippingAddressSerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingAddressId")) {
				if (jsonParserFieldValue != null) {
					orderItem.setShippingAddressId(
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