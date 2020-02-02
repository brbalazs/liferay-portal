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
import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.CartPost;
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
public class CartPostSerDes {

	public static CartPost toDTO(String json) {
		CartPostJSONParser cartPostJSONParser = new CartPostJSONParser();

		return cartPostJSONParser.parseToDTO(json);
	}

	public static CartPost[] toDTOs(String json) {
		CartPostJSONParser cartPostJSONParser = new CartPostJSONParser();

		return cartPostJSONParser.parseToDTOs(json);
	}

	public static String toJSON(CartPost cartPost) {
		if (cartPost == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (cartPost.getAccountId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountId\": ");

			sb.append(cartPost.getAccountId());
		}

		if (cartPost.getBillingAddress() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"billingAddress\": ");

			sb.append(String.valueOf(cartPost.getBillingAddress()));
		}

		if (cartPost.getBillingAddressId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"billingAddressId\": ");

			sb.append(cartPost.getBillingAddressId());
		}

		if (cartPost.getCartItemPosts() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"cartItemPosts\": ");

			sb.append("[");

			for (int i = 0; i < cartPost.getCartItemPosts().length; i++) {
				sb.append(String.valueOf(cartPost.getCartItemPosts()[i]));

				if ((i + 1) < cartPost.getCartItemPosts().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (cartPost.getCustomFields() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"customFields\": ");

			sb.append(_toJSON(cartPost.getCustomFields()));
		}

		if (cartPost.getPaymentMethod() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"paymentMethod\": ");

			sb.append("\"");

			sb.append(_escape(cartPost.getPaymentMethod()));

			sb.append("\"");
		}

		if (cartPost.getShippingAddress() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingAddress\": ");

			sb.append(String.valueOf(cartPost.getShippingAddress()));
		}

		if (cartPost.getShippingAddressId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingAddressId\": ");

			sb.append(cartPost.getShippingAddressId());
		}

		if (cartPost.getShippingMethod() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingMethod\": ");

			sb.append("\"");

			sb.append(_escape(cartPost.getShippingMethod()));

			sb.append("\"");
		}

		if (cartPost.getShippingOption() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingOption\": ");

			sb.append("\"");

			sb.append(_escape(cartPost.getShippingOption()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		CartPostJSONParser cartPostJSONParser = new CartPostJSONParser();

		return cartPostJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(CartPost cartPost) {
		if (cartPost == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (cartPost.getAccountId() == null) {
			map.put("accountId", null);
		}
		else {
			map.put("accountId", String.valueOf(cartPost.getAccountId()));
		}

		if (cartPost.getBillingAddress() == null) {
			map.put("billingAddress", null);
		}
		else {
			map.put(
				"billingAddress", String.valueOf(cartPost.getBillingAddress()));
		}

		if (cartPost.getBillingAddressId() == null) {
			map.put("billingAddressId", null);
		}
		else {
			map.put(
				"billingAddressId",
				String.valueOf(cartPost.getBillingAddressId()));
		}

		if (cartPost.getCartItemPosts() == null) {
			map.put("cartItemPosts", null);
		}
		else {
			map.put(
				"cartItemPosts", String.valueOf(cartPost.getCartItemPosts()));
		}

		if (cartPost.getCustomFields() == null) {
			map.put("customFields", null);
		}
		else {
			map.put("customFields", String.valueOf(cartPost.getCustomFields()));
		}

		if (cartPost.getPaymentMethod() == null) {
			map.put("paymentMethod", null);
		}
		else {
			map.put(
				"paymentMethod", String.valueOf(cartPost.getPaymentMethod()));
		}

		if (cartPost.getShippingAddress() == null) {
			map.put("shippingAddress", null);
		}
		else {
			map.put(
				"shippingAddress",
				String.valueOf(cartPost.getShippingAddress()));
		}

		if (cartPost.getShippingAddressId() == null) {
			map.put("shippingAddressId", null);
		}
		else {
			map.put(
				"shippingAddressId",
				String.valueOf(cartPost.getShippingAddressId()));
		}

		if (cartPost.getShippingMethod() == null) {
			map.put("shippingMethod", null);
		}
		else {
			map.put(
				"shippingMethod", String.valueOf(cartPost.getShippingMethod()));
		}

		if (cartPost.getShippingOption() == null) {
			map.put("shippingOption", null);
		}
		else {
			map.put(
				"shippingOption", String.valueOf(cartPost.getShippingOption()));
		}

		return map;
	}

	public static class CartPostJSONParser extends BaseJSONParser<CartPost> {

		@Override
		protected CartPost createDTO() {
			return new CartPost();
		}

		@Override
		protected CartPost[] createDTOArray(int size) {
			return new CartPost[size];
		}

		@Override
		protected void setField(
			CartPost cartPost, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "accountId")) {
				if (jsonParserFieldValue != null) {
					cartPost.setAccountId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "billingAddress")) {
				if (jsonParserFieldValue != null) {
					cartPost.setBillingAddress(
						BillingAddressSerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "billingAddressId")) {
				if (jsonParserFieldValue != null) {
					cartPost.setBillingAddressId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "cartItemPosts")) {
				if (jsonParserFieldValue != null) {
					cartPost.setCartItemPosts(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> CartItemPostSerDes.toDTO((String)object)
						).toArray(
							size -> new CartItemPost[size]
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "customFields")) {
				if (jsonParserFieldValue != null) {
					cartPost.setCustomFields(
						(Map)CartPostSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "paymentMethod")) {
				if (jsonParserFieldValue != null) {
					cartPost.setPaymentMethod((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingAddress")) {
				if (jsonParserFieldValue != null) {
					cartPost.setShippingAddress(
						ShippingAddressSerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingAddressId")) {
				if (jsonParserFieldValue != null) {
					cartPost.setShippingAddressId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingMethod")) {
				if (jsonParserFieldValue != null) {
					cartPost.setShippingMethod((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingOption")) {
				if (jsonParserFieldValue != null) {
					cartPost.setShippingOption((String)jsonParserFieldValue);
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