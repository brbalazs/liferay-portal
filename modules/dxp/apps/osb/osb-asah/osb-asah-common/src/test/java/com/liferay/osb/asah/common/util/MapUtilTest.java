/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Vishal Reddy
 */
public class MapUtilTest {

	@Test
	public void testGetLong() {
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("int", 5);
				put("long", 5L);
				put("negative", "-5");
				put("non-numeric", "test");
				put("null", null);
				put("positive", "+5");
				put("string", "5");
				put("stringArray", new String[] {"5", "4", "3", "2", "1"});
			}
		};

		Assertions.assertEquals(5, MapUtil.getLong(map, "int"));
		Assertions.assertEquals(5, MapUtil.getLong(map, "long"));
		Assertions.assertEquals(-5, MapUtil.getLong(map, "negative"));
		Assertions.assertEquals(5, MapUtil.getLong(map, "non-numeric", 5));
		Assertions.assertEquals(0, MapUtil.getLong(map, "null"));
		Assertions.assertEquals(5, MapUtil.getLong(map, "positive"));
		Assertions.assertEquals(5, MapUtil.getLong(map, "string"));
		Assertions.assertEquals(5, MapUtil.getLong(map, "stringArray"));
	}

	@Test
	public void testGetString() {
		Map<String, Object> map = new HashMap<String, Object>() {
			{
				put("int", 5);
				put("long", 5L);
				put("null", null);
				put("string", "5");
				put("stringArray", new String[] {"5", "4", "3", "2", "1"});
			}
		};

		Assertions.assertEquals("5", MapUtil.getString(map, "int"));
		Assertions.assertEquals("5", MapUtil.getString(map, "long"));
		Assertions.assertNull(MapUtil.getString(map, "null"));
		Assertions.assertEquals("5", MapUtil.getString(map, "null", "5"));
		Assertions.assertEquals("5", MapUtil.getString(map, "string"));
		Assertions.assertEquals("5", MapUtil.getString(map, "stringArray"));
	}

	@Test
	public void testMerge() {
		Map<String, Integer> map1 = new HashMap<String, Integer>() {
			{
				put("test1", 1);
				put("test2", 2);
			}
		};

		Map<String, Integer> map2 = new HashMap<String, Integer>() {
			{
				put("test1", 4);
				put("test3", 0);
			}
		};

		Assertions.assertEquals(
			new HashMap<String, Integer>() {
				{
					put("test1", 5);
					put("test2", 2);
					put("test3", 0);
				}
			},
			MapUtil.merge(Integer::sum, map1, map2));
	}

}