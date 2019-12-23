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

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.Price;
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
public class PriceSerDes {

	public static Price toDTO(String json) {
		PriceJSONParser priceJSONParser = new PriceJSONParser();

		return priceJSONParser.parseToDTO(json);
	}

	public static Price[] toDTOs(String json) {
		PriceJSONParser priceJSONParser = new PriceJSONParser();

		return priceJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Price price) {
		if (price == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (price.getPrice() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"price\": ");

			sb.append("\"");

			sb.append(_escape(price.getPrice()));

			sb.append("\"");
		}

		if (price.getPromoPrice() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"promoPrice\": ");

			sb.append("\"");

			sb.append(_escape(price.getPromoPrice()));

			sb.append("\"");
		}

		if (price.getTierPrice() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"tierPrice\": ");

			sb.append("\"");

			sb.append(_escape(price.getTierPrice()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		PriceJSONParser priceJSONParser = new PriceJSONParser();

		return priceJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Price price) {
		if (price == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (price.getPrice() == null) {
			map.put("price", null);
		}
		else {
			map.put("price", String.valueOf(price.getPrice()));
		}

		if (price.getPromoPrice() == null) {
			map.put("promoPrice", null);
		}
		else {
			map.put("promoPrice", String.valueOf(price.getPromoPrice()));
		}

		if (price.getTierPrice() == null) {
			map.put("tierPrice", null);
		}
		else {
			map.put("tierPrice", String.valueOf(price.getTierPrice()));
		}

		return map;
	}

	public static class PriceJSONParser extends BaseJSONParser<Price> {

		@Override
		protected Price createDTO() {
			return new Price();
		}

		@Override
		protected Price[] createDTOArray(int size) {
			return new Price[size];
		}

		@Override
		protected void setField(
			Price price, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "price")) {
				if (jsonParserFieldValue != null) {
					price.setPrice((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "promoPrice")) {
				if (jsonParserFieldValue != null) {
					price.setPromoPrice((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "tierPrice")) {
				if (jsonParserFieldValue != null) {
					price.setTierPrice((String)jsonParserFieldValue);
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