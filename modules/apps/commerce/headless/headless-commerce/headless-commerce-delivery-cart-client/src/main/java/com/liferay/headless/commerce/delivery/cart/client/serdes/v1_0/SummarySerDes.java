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

import java.math.BigDecimal;

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

		if (summary.getItemsQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"itemsQuantity\": ");

			sb.append(summary.getItemsQuantity());
		}

		if (summary.getShippingDiscountPercentageLevel1() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel1\": ");

			sb.append(summary.getShippingDiscountPercentageLevel1());
		}

		if (summary.getShippingDiscountPercentageLevel2() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel2\": ");

			sb.append(summary.getShippingDiscountPercentageLevel2());
		}

		if (summary.getShippingDiscountPercentageLevel3() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel3\": ");

			sb.append(summary.getShippingDiscountPercentageLevel3());
		}

		if (summary.getShippingDiscountPercentageLevel4() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountPercentageLevel4\": ");

			sb.append(summary.getShippingDiscountPercentageLevel4());
		}

		if (summary.getShippingDiscountValue() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingDiscountValue\": ");

			sb.append("\"");

			sb.append(_escape(summary.getShippingDiscountValue()));

			sb.append("\"");
		}

		if (summary.getShippingValue() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingValue\": ");

			sb.append("\"");

			sb.append(_escape(summary.getShippingValue()));

			sb.append("\"");
		}

		if (summary.getSubtotal() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotal\": ");

			sb.append("\"");

			sb.append(_escape(summary.getSubtotal()));

			sb.append("\"");
		}

		if (summary.getSubtotalDiscountPercentageLevel1() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel1\": ");

			sb.append(summary.getSubtotalDiscountPercentageLevel1());
		}

		if (summary.getSubtotalDiscountPercentageLevel2() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel2\": ");

			sb.append(summary.getSubtotalDiscountPercentageLevel2());
		}

		if (summary.getSubtotalDiscountPercentageLevel3() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel3\": ");

			sb.append(summary.getSubtotalDiscountPercentageLevel3());
		}

		if (summary.getSubtotalDiscountPercentageLevel4() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountPercentageLevel4\": ");

			sb.append(summary.getSubtotalDiscountPercentageLevel4());
		}

		if (summary.getSubtotalDiscountValue() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtotalDiscountValue\": ");

			sb.append("\"");

			sb.append(_escape(summary.getSubtotalDiscountValue()));

			sb.append("\"");
		}

		if (summary.getTaxValue() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"taxValue\": ");

			sb.append("\"");

			sb.append(_escape(summary.getTaxValue()));

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

		if (summary.getTotalDiscountPercentageLevel1() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel1\": ");

			sb.append(summary.getTotalDiscountPercentageLevel1());
		}

		if (summary.getTotalDiscountPercentageLevel2() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel2\": ");

			sb.append(summary.getTotalDiscountPercentageLevel2());
		}

		if (summary.getTotalDiscountPercentageLevel3() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel3\": ");

			sb.append(summary.getTotalDiscountPercentageLevel3());
		}

		if (summary.getTotalDiscountPercentageLevel4() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountPercentageLevel4\": ");

			sb.append(summary.getTotalDiscountPercentageLevel4());
		}

		if (summary.getTotalDiscountValue() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"totalDiscountValue\": ");

			sb.append("\"");

			sb.append(_escape(summary.getTotalDiscountValue()));

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

		if (summary.getItemsQuantity() == null) {
			map.put("itemsQuantity", null);
		}
		else {
			map.put(
				"itemsQuantity", String.valueOf(summary.getItemsQuantity()));
		}

		if (summary.getShippingDiscountPercentageLevel1() == null) {
			map.put("shippingDiscountPercentageLevel1", null);
		}
		else {
			map.put(
				"shippingDiscountPercentageLevel1",
				String.valueOf(summary.getShippingDiscountPercentageLevel1()));
		}

		if (summary.getShippingDiscountPercentageLevel2() == null) {
			map.put("shippingDiscountPercentageLevel2", null);
		}
		else {
			map.put(
				"shippingDiscountPercentageLevel2",
				String.valueOf(summary.getShippingDiscountPercentageLevel2()));
		}

		if (summary.getShippingDiscountPercentageLevel3() == null) {
			map.put("shippingDiscountPercentageLevel3", null);
		}
		else {
			map.put(
				"shippingDiscountPercentageLevel3",
				String.valueOf(summary.getShippingDiscountPercentageLevel3()));
		}

		if (summary.getShippingDiscountPercentageLevel4() == null) {
			map.put("shippingDiscountPercentageLevel4", null);
		}
		else {
			map.put(
				"shippingDiscountPercentageLevel4",
				String.valueOf(summary.getShippingDiscountPercentageLevel4()));
		}

		if (summary.getShippingDiscountValue() == null) {
			map.put("shippingDiscountValue", null);
		}
		else {
			map.put(
				"shippingDiscountValue",
				String.valueOf(summary.getShippingDiscountValue()));
		}

		if (summary.getShippingValue() == null) {
			map.put("shippingValue", null);
		}
		else {
			map.put(
				"shippingValue", String.valueOf(summary.getShippingValue()));
		}

		if (summary.getSubtotal() == null) {
			map.put("subtotal", null);
		}
		else {
			map.put("subtotal", String.valueOf(summary.getSubtotal()));
		}

		if (summary.getSubtotalDiscountPercentageLevel1() == null) {
			map.put("subtotalDiscountPercentageLevel1", null);
		}
		else {
			map.put(
				"subtotalDiscountPercentageLevel1",
				String.valueOf(summary.getSubtotalDiscountPercentageLevel1()));
		}

		if (summary.getSubtotalDiscountPercentageLevel2() == null) {
			map.put("subtotalDiscountPercentageLevel2", null);
		}
		else {
			map.put(
				"subtotalDiscountPercentageLevel2",
				String.valueOf(summary.getSubtotalDiscountPercentageLevel2()));
		}

		if (summary.getSubtotalDiscountPercentageLevel3() == null) {
			map.put("subtotalDiscountPercentageLevel3", null);
		}
		else {
			map.put(
				"subtotalDiscountPercentageLevel3",
				String.valueOf(summary.getSubtotalDiscountPercentageLevel3()));
		}

		if (summary.getSubtotalDiscountPercentageLevel4() == null) {
			map.put("subtotalDiscountPercentageLevel4", null);
		}
		else {
			map.put(
				"subtotalDiscountPercentageLevel4",
				String.valueOf(summary.getSubtotalDiscountPercentageLevel4()));
		}

		if (summary.getSubtotalDiscountValue() == null) {
			map.put("subtotalDiscountValue", null);
		}
		else {
			map.put(
				"subtotalDiscountValue",
				String.valueOf(summary.getSubtotalDiscountValue()));
		}

		if (summary.getTaxValue() == null) {
			map.put("taxValue", null);
		}
		else {
			map.put("taxValue", String.valueOf(summary.getTaxValue()));
		}

		if (summary.getTotal() == null) {
			map.put("total", null);
		}
		else {
			map.put("total", String.valueOf(summary.getTotal()));
		}

		if (summary.getTotalDiscountPercentageLevel1() == null) {
			map.put("totalDiscountPercentageLevel1", null);
		}
		else {
			map.put(
				"totalDiscountPercentageLevel1",
				String.valueOf(summary.getTotalDiscountPercentageLevel1()));
		}

		if (summary.getTotalDiscountPercentageLevel2() == null) {
			map.put("totalDiscountPercentageLevel2", null);
		}
		else {
			map.put(
				"totalDiscountPercentageLevel2",
				String.valueOf(summary.getTotalDiscountPercentageLevel2()));
		}

		if (summary.getTotalDiscountPercentageLevel3() == null) {
			map.put("totalDiscountPercentageLevel3", null);
		}
		else {
			map.put(
				"totalDiscountPercentageLevel3",
				String.valueOf(summary.getTotalDiscountPercentageLevel3()));
		}

		if (summary.getTotalDiscountPercentageLevel4() == null) {
			map.put("totalDiscountPercentageLevel4", null);
		}
		else {
			map.put(
				"totalDiscountPercentageLevel4",
				String.valueOf(summary.getTotalDiscountPercentageLevel4()));
		}

		if (summary.getTotalDiscountValue() == null) {
			map.put("totalDiscountValue", null);
		}
		else {
			map.put(
				"totalDiscountValue",
				String.valueOf(summary.getTotalDiscountValue()));
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

			if (Objects.equals(jsonParserFieldName, "itemsQuantity")) {
				if (jsonParserFieldValue != null) {
					summary.setItemsQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"shippingDiscountPercentageLevel1")) {

				if (jsonParserFieldValue != null) {
					summary.setShippingDiscountPercentageLevel1(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"shippingDiscountPercentageLevel2")) {

				if (jsonParserFieldValue != null) {
					summary.setShippingDiscountPercentageLevel2(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"shippingDiscountPercentageLevel3")) {

				if (jsonParserFieldValue != null) {
					summary.setShippingDiscountPercentageLevel3(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"shippingDiscountPercentageLevel4")) {

				if (jsonParserFieldValue != null) {
					summary.setShippingDiscountPercentageLevel4(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "shippingDiscountValue")) {

				if (jsonParserFieldValue != null) {
					summary.setShippingDiscountValue(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingValue")) {
				if (jsonParserFieldValue != null) {
					summary.setShippingValue((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "subtotal")) {
				if (jsonParserFieldValue != null) {
					summary.setSubtotal((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"subtotalDiscountPercentageLevel1")) {

				if (jsonParserFieldValue != null) {
					summary.setSubtotalDiscountPercentageLevel1(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"subtotalDiscountPercentageLevel2")) {

				if (jsonParserFieldValue != null) {
					summary.setSubtotalDiscountPercentageLevel2(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"subtotalDiscountPercentageLevel3")) {

				if (jsonParserFieldValue != null) {
					summary.setSubtotalDiscountPercentageLevel3(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"subtotalDiscountPercentageLevel4")) {

				if (jsonParserFieldValue != null) {
					summary.setSubtotalDiscountPercentageLevel4(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "subtotalDiscountValue")) {

				if (jsonParserFieldValue != null) {
					summary.setSubtotalDiscountValue(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "taxValue")) {
				if (jsonParserFieldValue != null) {
					summary.setTaxValue((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "total")) {
				if (jsonParserFieldValue != null) {
					summary.setTotal((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "totalDiscountPercentageLevel1")) {

				if (jsonParserFieldValue != null) {
					summary.setTotalDiscountPercentageLevel1(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "totalDiscountPercentageLevel2")) {

				if (jsonParserFieldValue != null) {
					summary.setTotalDiscountPercentageLevel2(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "totalDiscountPercentageLevel3")) {

				if (jsonParserFieldValue != null) {
					summary.setTotalDiscountPercentageLevel3(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "totalDiscountPercentageLevel4")) {

				if (jsonParserFieldValue != null) {
					summary.setTotalDiscountPercentageLevel4(
						(BigDecimal)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "totalDiscountValue")) {

				if (jsonParserFieldValue != null) {
					summary.setTotalDiscountValue((String)jsonParserFieldValue);
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