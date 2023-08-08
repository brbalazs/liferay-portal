/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.json.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author Vishal Reddy
 */
public class StringUtilTest {

	@Test
	public void testGet() {
		Assertions.assertEquals("", StringUtil.get(null));
		Assertions.assertEquals("1", StringUtil.get(1));
		Assertions.assertEquals("a", StringUtil.get("a"));
	}

	@Test
	public void testIsQuoted() {
		Assertions.assertFalse(StringUtil.isQuoted("'Hello World"));
		Assertions.assertTrue(StringUtil.isQuoted("'Hello World'"));
		Assertions.assertTrue(StringUtil.isQuoted("\"Hello World\""));
		Assertions.assertFalse(StringUtil.isQuoted("\"Hello World"));
		Assertions.assertFalse(StringUtil.isQuoted("Hello World"));

		// String with single character

		Assertions.assertFalse(StringUtil.isQuoted("'"));
		Assertions.assertFalse(StringUtil.isQuoted("\""));

		// String with two characters

		Assertions.assertTrue(StringUtil.isQuoted("''"));
		Assertions.assertTrue(StringUtil.isQuoted("\"\""));

		// Blank string

		Assertions.assertFalse(StringUtil.isQuoted(""));
	}

	@Test
	public void testReplaceString() {
		Assertions.assertEquals(
			"Aloha World HELLO WORLD Aloha World",
			StringUtil.replace(
				"Hello World HELLO WORLD Hello World", "Hello", "Aloha"));
	}

	@Test
	public void testReplaceStringArray() {
		Assertions.assertEquals(
			"Aloha World ALOHA WORLD Aloha World",
			StringUtil.replace(
				"Hello World HELLO WORLD Hello World",
				new String[] {"Hello", "HELLO"},
				new String[] {"Aloha", "ALOHA"}));
	}

	@Test
	public void testToObject() {
		Assertions.assertEquals(
			"'Hello World'", StringUtil.toObject("'''Hello World'''"));
		Assertions.assertEquals(0.5, StringUtil.toObject("0.5"));
		Assertions.assertEquals(5L, StringUtil.toObject("5"));
		Assertions.assertFalse((boolean)StringUtil.toObject("FALSE"));
		Assertions.assertNull(StringUtil.toObject("NULL"));
		Assertions.assertTrue((boolean)StringUtil.toObject("TRUE"));
		Assertions.assertFalse((boolean)StringUtil.toObject("false"));
		Assertions.assertNull(StringUtil.toObject("null"));
		Assertions.assertTrue((boolean)StringUtil.toObject("true"));

		JSONAssert.assertEquals(
			JSONUtil.put(JSONUtil.put("foo", "bar")),
			(JSONArray)StringUtil.toObject("[{\"foo\": \"bar\"}]"), true);
		JSONAssert.assertEquals(
			JSONUtil.put("foo", "bar"),
			(JSONObject)StringUtil.toObject("{\"foo\": \"bar\"}"), true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(1234, 5678),
			(JSONArray)StringUtil.toObject("[1234,5678]"), true);
		JSONAssert.assertEquals(
			JSONUtil.putAll(1234, 5678),
			(JSONArray)StringUtil.toObject("[1234, 5678]"), true);

		try {
			StringUtil.toObject("[1234,");
		}
		catch (IllegalArgumentException illegalArgumentException) {
			Assertions.assertEquals(
				"Unknown object [1234, used in filter",
				illegalArgumentException.getMessage());
		}

		try {
			StringUtil.toObject("{1234");
		}
		catch (IllegalArgumentException illegalArgumentException) {
			Assertions.assertEquals(
				"Unknown object {1234 used in filter",
				illegalArgumentException.getMessage());
		}
	}

	@Test
	public void testUnquote() {
		Assertions.assertEquals("", StringUtil.unquote(""));
		Assertions.assertEquals(
			"Hello World", StringUtil.unquote("'Hello World'"));
		Assertions.assertEquals(
			"'Hello World", StringUtil.unquote("'Hello World"));
		Assertions.assertEquals(
			"Hello World", StringUtil.unquote("\"Hello World\""));
		Assertions.assertEquals(
			"Hello World", StringUtil.unquote("Hello World"));
	}

	@Test
	public void testUnquoteAndDecodeInnerQuotes() {
		Assertions.assertEquals("", StringUtil.unquoteAndDecodeInnerQuotes(""));
		Assertions.assertEquals(
			"'Hello World'",
			StringUtil.unquoteAndDecodeInnerQuotes("'''Hello World'''"));
	}

}