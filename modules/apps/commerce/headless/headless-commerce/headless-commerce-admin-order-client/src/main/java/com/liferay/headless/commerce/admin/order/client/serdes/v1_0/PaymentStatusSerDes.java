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

import com.liferay.headless.commerce.admin.order.client.dto.v1_0.PaymentStatus;
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
public class PaymentStatusSerDes {

	public static PaymentStatus toDTO(String json) {
		PaymentStatusJSONParser paymentStatusJSONParser =
			new PaymentStatusJSONParser();

		return paymentStatusJSONParser.parseToDTO(json);
	}

	public static PaymentStatus[] toDTOs(String json) {
		PaymentStatusJSONParser paymentStatusJSONParser =
			new PaymentStatusJSONParser();

		return paymentStatusJSONParser.parseToDTOs(json);
	}

	public static String toJSON(PaymentStatus paymentStatus) {
		if (paymentStatus == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (paymentStatus.getCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"code\": ");

			sb.append(paymentStatus.getCode());
		}

		if (paymentStatus.getLabel() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"label\": ");

			sb.append("\"");

			sb.append(_escape(paymentStatus.getLabel()));

			sb.append("\"");
		}

		if (paymentStatus.getLabelI18n() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"labelI18n\": ");

			sb.append("\"");

			sb.append(_escape(paymentStatus.getLabelI18n()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		PaymentStatusJSONParser paymentStatusJSONParser =
			new PaymentStatusJSONParser();

		return paymentStatusJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(PaymentStatus paymentStatus) {
		if (paymentStatus == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (paymentStatus.getCode() == null) {
			map.put("code", null);
		}
		else {
			map.put("code", String.valueOf(paymentStatus.getCode()));
		}

		if (paymentStatus.getLabel() == null) {
			map.put("label", null);
		}
		else {
			map.put("label", String.valueOf(paymentStatus.getLabel()));
		}

		if (paymentStatus.getLabelI18n() == null) {
			map.put("labelI18n", null);
		}
		else {
			map.put("labelI18n", String.valueOf(paymentStatus.getLabelI18n()));
		}

		return map;
	}

	public static class PaymentStatusJSONParser
		extends BaseJSONParser<PaymentStatus> {

		@Override
		protected PaymentStatus createDTO() {
			return new PaymentStatus();
		}

		@Override
		protected PaymentStatus[] createDTOArray(int size) {
			return new PaymentStatus[size];
		}

		@Override
		protected void setField(
			PaymentStatus paymentStatus, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "code")) {
				if (jsonParserFieldValue != null) {
					paymentStatus.setCode(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "label")) {
				if (jsonParserFieldValue != null) {
					paymentStatus.setLabel((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "labelI18n")) {
				if (jsonParserFieldValue != null) {
					paymentStatus.setLabelI18n((String)jsonParserFieldValue);
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