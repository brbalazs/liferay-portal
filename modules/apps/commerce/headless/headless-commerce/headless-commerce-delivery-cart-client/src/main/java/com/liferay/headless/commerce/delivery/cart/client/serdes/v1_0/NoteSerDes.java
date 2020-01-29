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

import com.liferay.headless.commerce.delivery.cart.client.dto.v1_0.Note;
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
public class NoteSerDes {

	public static Note toDTO(String json) {
		NoteJSONParser noteJSONParser = new NoteJSONParser();

		return noteJSONParser.parseToDTO(json);
	}

	public static Note[] toDTOs(String json) {
		NoteJSONParser noteJSONParser = new NoteJSONParser();

		return noteJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Note note) {
		if (note == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (note.getAuthor() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"author\": ");

			sb.append("\"");

			sb.append(_escape(note.getAuthor()));

			sb.append("\"");
		}

		if (note.getContent() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"content\": ");

			sb.append("\"");

			sb.append(_escape(note.getContent()));

			sb.append("\"");
		}

		if (note.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(note.getId());
		}

		if (note.getOrderId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"orderId\": ");

			sb.append(note.getOrderId());
		}

		if (note.getRestricted() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"restricted\": ");

			sb.append(note.getRestricted());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		NoteJSONParser noteJSONParser = new NoteJSONParser();

		return noteJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Note note) {
		if (note == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (note.getAuthor() == null) {
			map.put("author", null);
		}
		else {
			map.put("author", String.valueOf(note.getAuthor()));
		}

		if (note.getContent() == null) {
			map.put("content", null);
		}
		else {
			map.put("content", String.valueOf(note.getContent()));
		}

		if (note.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(note.getId()));
		}

		if (note.getOrderId() == null) {
			map.put("orderId", null);
		}
		else {
			map.put("orderId", String.valueOf(note.getOrderId()));
		}

		if (note.getRestricted() == null) {
			map.put("restricted", null);
		}
		else {
			map.put("restricted", String.valueOf(note.getRestricted()));
		}

		return map;
	}

	public static class NoteJSONParser extends BaseJSONParser<Note> {

		@Override
		protected Note createDTO() {
			return new Note();
		}

		@Override
		protected Note[] createDTOArray(int size) {
			return new Note[size];
		}

		@Override
		protected void setField(
			Note note, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "author")) {
				if (jsonParserFieldValue != null) {
					note.setAuthor((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "content")) {
				if (jsonParserFieldValue != null) {
					note.setContent((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					note.setId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "orderId")) {
				if (jsonParserFieldValue != null) {
					note.setOrderId(Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "restricted")) {
				if (jsonParserFieldValue != null) {
					note.setRestricted((Boolean)jsonParserFieldValue);
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