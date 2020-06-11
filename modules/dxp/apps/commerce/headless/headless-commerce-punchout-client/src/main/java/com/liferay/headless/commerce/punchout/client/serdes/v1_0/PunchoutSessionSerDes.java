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

package com.liferay.headless.commerce.punchout.client.serdes.v1_0;

import com.liferay.headless.commerce.punchout.client.dto.v1_0.PunchoutSession;
import com.liferay.headless.commerce.punchout.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Jaclyn Ong
 * @generated
 */
@Generated("")
public class PunchoutSessionSerDes {

	public static PunchoutSession toDTO(String json) {
		PunchoutSessionJSONParser punchoutSessionJSONParser =
			new PunchoutSessionJSONParser();

		return punchoutSessionJSONParser.parseToDTO(json);
	}

	public static PunchoutSession[] toDTOs(String json) {
		PunchoutSessionJSONParser punchoutSessionJSONParser =
			new PunchoutSessionJSONParser();

		return punchoutSessionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(PunchoutSession punchoutSession) {
		if (punchoutSession == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (punchoutSession.getBuyerAccountReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerAccountReferenceCode\": ");

			sb.append("\"");

			sb.append(_escape(punchoutSession.getBuyerAccountReferenceCode()));

			sb.append("\"");
		}

		if (punchoutSession.getBuyerGroup() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerGroup\": ");

			sb.append(String.valueOf(punchoutSession.getBuyerGroup()));
		}

		if (punchoutSession.getBuyerOrganization() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerOrganization\": ");

			sb.append(String.valueOf(punchoutSession.getBuyerOrganization()));
		}

		if (punchoutSession.getBuyerUser() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"buyerUser\": ");

			sb.append(String.valueOf(punchoutSession.getBuyerUser()));
		}

		if (punchoutSession.getCart() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"cart\": ");

			sb.append(String.valueOf(punchoutSession.getCart()));
		}

		if (punchoutSession.getPunchoutReturnURL() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"punchoutReturnURL\": ");

			sb.append("\"");

			sb.append(_escape(punchoutSession.getPunchoutReturnURL()));

			sb.append("\"");
		}

		if (punchoutSession.getPunchoutSessionType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"punchoutSessionType\": ");

			sb.append("\"");

			sb.append(_escape(punchoutSession.getPunchoutSessionType()));

			sb.append("\"");
		}

		if (punchoutSession.getPunchoutStartURL() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"punchoutStartURL\": ");

			sb.append("\"");

			sb.append(_escape(punchoutSession.getPunchoutStartURL()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		PunchoutSessionJSONParser punchoutSessionJSONParser =
			new PunchoutSessionJSONParser();

		return punchoutSessionJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(PunchoutSession punchoutSession) {
		if (punchoutSession == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (punchoutSession.getBuyerAccountReferenceCode() == null) {
			map.put("buyerAccountReferenceCode", null);
		}
		else {
			map.put(
				"buyerAccountReferenceCode",
				String.valueOf(punchoutSession.getBuyerAccountReferenceCode()));
		}

		if (punchoutSession.getBuyerGroup() == null) {
			map.put("buyerGroup", null);
		}
		else {
			map.put(
				"buyerGroup", String.valueOf(punchoutSession.getBuyerGroup()));
		}

		if (punchoutSession.getBuyerOrganization() == null) {
			map.put("buyerOrganization", null);
		}
		else {
			map.put(
				"buyerOrganization",
				String.valueOf(punchoutSession.getBuyerOrganization()));
		}

		if (punchoutSession.getBuyerUser() == null) {
			map.put("buyerUser", null);
		}
		else {
			map.put(
				"buyerUser", String.valueOf(punchoutSession.getBuyerUser()));
		}

		if (punchoutSession.getCart() == null) {
			map.put("cart", null);
		}
		else {
			map.put("cart", String.valueOf(punchoutSession.getCart()));
		}

		if (punchoutSession.getPunchoutReturnURL() == null) {
			map.put("punchoutReturnURL", null);
		}
		else {
			map.put(
				"punchoutReturnURL",
				String.valueOf(punchoutSession.getPunchoutReturnURL()));
		}

		if (punchoutSession.getPunchoutSessionType() == null) {
			map.put("punchoutSessionType", null);
		}
		else {
			map.put(
				"punchoutSessionType",
				String.valueOf(punchoutSession.getPunchoutSessionType()));
		}

		if (punchoutSession.getPunchoutStartURL() == null) {
			map.put("punchoutStartURL", null);
		}
		else {
			map.put(
				"punchoutStartURL",
				String.valueOf(punchoutSession.getPunchoutStartURL()));
		}

		return map;
	}

	public static class PunchoutSessionJSONParser
		extends BaseJSONParser<PunchoutSession> {

		@Override
		protected PunchoutSession createDTO() {
			return new PunchoutSession();
		}

		@Override
		protected PunchoutSession[] createDTOArray(int size) {
			return new PunchoutSession[size];
		}

		@Override
		protected void setField(
			PunchoutSession punchoutSession, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "buyerAccountReferenceCode")) {

				if (jsonParserFieldValue != null) {
					punchoutSession.setBuyerAccountReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "buyerGroup")) {
				if (jsonParserFieldValue != null) {
					punchoutSession.setBuyerGroup(
						GroupSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "buyerOrganization")) {
				if (jsonParserFieldValue != null) {
					punchoutSession.setBuyerOrganization(
						OrganizationSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "buyerUser")) {
				if (jsonParserFieldValue != null) {
					punchoutSession.setBuyerUser(
						UserSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "cart")) {
				if (jsonParserFieldValue != null) {
					punchoutSession.setCart(
						CartSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "punchoutReturnURL")) {
				if (jsonParserFieldValue != null) {
					punchoutSession.setPunchoutReturnURL(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "punchoutSessionType")) {

				if (jsonParserFieldValue != null) {
					punchoutSession.setPunchoutSessionType(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "punchoutStartURL")) {
				if (jsonParserFieldValue != null) {
					punchoutSession.setPunchoutStartURL(
						(String)jsonParserFieldValue);
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