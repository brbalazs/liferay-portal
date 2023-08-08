/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Shinn Lok
 */
public class JSONUtilTest {

	@Test
	public void testAddToStringCollection() {
		List<String> values = new ArrayList<String>() {
			{
				add("alpha");
				add("beta");
			}
		};

		JSONUtil.addToStringCollection(values, JSONUtil.put("gamma"));

		Assertions.assertEquals(3, values.size(), values.toString());
		Assertions.assertTrue(values.contains("gamma"));

		JSONUtil.addToStringCollection(values, null);

		Assertions.assertEquals(3, values.size(), values.toString());
	}

	@Test
	public void testAddToStringCollectionWithKey() {
		List<String> values = new ArrayList<String>() {
			{
				add("1");
				add("2");
			}
		};

		JSONUtil.addToStringCollection(
			values, JSONUtil.put(JSONUtil.put("alpha", "3")), "alpha");

		Assertions.assertEquals(3, values.size(), values.toString());
		Assertions.assertTrue(values.contains("3"));

		JSONUtil.addToStringCollection(values, null, "alpha");

		Assertions.assertEquals(3, values.size(), values.toString());
	}

	@Test
	public void testCreateCollector() {
		List<String> strings = Arrays.asList("foo", "bar", "baz");

		Stream<String> stringsStream = strings.stream();

		Assertions.assertTrue(
			JSONUtil.equals(
				JSONUtil.concat(JSONUtil.putAll("FOO", "BAR", "BAZ")),
				stringsStream.map(
					String::toUpperCase
				).collect(
					JSONUtil.createCollector()
				)));
	}

	@Test
	public void testGetValue() {

		// Nested JSON array

		JSONObject jsonObject = JSONUtil.put(
			"alpha",
			JSONUtil.put("beta", JSONUtil.put(JSONUtil.put("gamma", "delta"))));

		Assertions.assertEquals(
			"delta",
			JSONUtil.getValue(
				jsonObject, "JSONObject/alpha", "JSONArray/beta",
				"JSONObject/0", "Object/gamma"));

		// Nested JSON object

		jsonObject = JSONUtil.put(
			"alpha", JSONUtil.put("beta", JSONUtil.put("gamma")));

		Assertions.assertEquals(
			"gamma",
			JSONUtil.getValue(
				jsonObject, "JSONObject/alpha", "JSONArray/beta", "Object/0"));
	}

	@Test
	public void testHasValue() {
		Assertions.assertFalse(
			JSONUtil.hasValue(JSONUtil.putAll("alpha", "beta", "gamma"), "1"));
		Assertions.assertTrue(
			JSONUtil.hasValue(
				JSONUtil.putAll("alpha", "beta", "gamma"), "gamma"));
	}

	@Test
	public void testJSONArrayEqualsWithStrictChecking() {
		Assertions.assertTrue(
			JSONUtil.equals(
				JSONUtil.putAll("a", "1"), JSONUtil.putAll("a", "1")));

		Assertions.assertFalse(
			JSONUtil.equals(
				JSONUtil.putAll("a", "1"), JSONUtil.putAll("1", "a")));
	}

	@Test
	public void testJSONArrayObjectConcat() {
		Assertions.assertTrue(
			JSONUtil.equals(
				JSONUtil.concat(
					JSONUtil.put(JSONUtil.put("foo", "bar")),
					JSONUtil.put(JSONUtil.put("bar", "baz"))),
				JSONUtil.putAll(
					JSONUtil.put("foo", "bar"), JSONUtil.put("bar", "baz"))));
	}

	@Test
	public void testJSONArrayStringConcat() {
		Assertions.assertTrue(
			JSONUtil.equals(
				JSONUtil.concat(
					JSONUtil.putAll("foo", "bar", "baz"),
					JSONUtil.putAll("abc", "foo", "xyz")),
				JSONUtil.putAll("foo", "bar", "baz", "abc", "foo", "xyz")));
	}

	@Test
	public void testJSONObjectEquals() {
		Assertions.assertTrue(
			JSONUtil.equals(
				JSONUtil.put(
					"double", 0.532049
				).put(
					"foo", "bar"
				),
				JSONUtil.put(
					"double", 0.532049
				).put(
					"foo", "bar"
				)));

		Assertions.assertTrue(
			JSONUtil.equals(
				JSONUtil.put(
					"double", 0.532049
				).put(
					"foo", "bar"
				),
				JSONUtil.put(
					"foo", "bar"
				).put(
					"double", 0.532049
				)));

		Assertions.assertFalse(
			JSONUtil.equals(
				JSONUtil.put(
					"double", 0.532049
				).put(
					"foo", "bar"
				),
				JSONUtil.put(
					"double", 0.532049
				).put(
					"foo", "bar"
				).put(
					"integer", 5
				)));
	}

	@Test
	public void testMerge() {
		JSONObject jsonObject1 = JSONUtil.put("alpha", "1");
		JSONObject jsonObject2 = JSONUtil.put(
			"beta", "2"
		).put(
			"gamma", "3"
		);

		JSONObject jsonObject3 = JSONUtil.merge(jsonObject1, jsonObject2);

		Assertions.assertEquals(3, jsonObject3.length());
		Assertions.assertTrue(jsonObject3.has("alpha"));
		Assertions.assertTrue(jsonObject3.has("beta"));
		Assertions.assertTrue(jsonObject3.has("gamma"));

		JSONObject jsonObject4 = JSONUtil.merge(null, jsonObject2);

		Assertions.assertEquals(2, jsonObject4.length());
		Assertions.assertTrue(jsonObject4.has("beta"));
		Assertions.assertTrue(jsonObject4.has("gamma"));
	}

	@Test
	public void testPutKeyValue() {
		JSONObject jsonObject1 = _createJSONObject();

		jsonObject1.put("alpha", "beta");

		JSONObject jsonObject2 = JSONUtil.put("alpha", "beta");

		Assertions.assertEquals(
			jsonObject1.get("alpha"), jsonObject2.get("alpha"));
	}

	@Test
	public void testPutValue() {
		JSONArray jsonArray1 = _createJSONArray();

		jsonArray1.put("alpha");

		JSONArray jsonArray2 = JSONUtil.put("alpha");

		Assertions.assertEquals(jsonArray1.length(), jsonArray2.length());
		Assertions.assertEquals(jsonArray1.get(0), jsonArray2.get(0));
	}

	@Test
	public void testPutValues() {
		JSONArray jsonArray1 = _createJSONArray();

		jsonArray1.put(
			"alpha"
		).put(
			"beta"
		).put(
			"gamma"
		);

		JSONArray jsonArray2 = JSONUtil.putAll("alpha", "beta", "gamma");

		for (int i = 0; i < jsonArray1.length(); i++) {
			Assertions.assertEquals(jsonArray1.get(i), jsonArray2.get(i));
		}
	}

	@Test
	public void testRemoveValue() {
		JSONArray jsonArray1 = JSONUtil.putAll("alpha", "beta", "gamma");

		JSONUtil.removeValue(jsonArray1, "alpha");

		JSONArray jsonArray2 = JSONUtil.putAll("beta", "gamma");

		Assertions.assertEquals(jsonArray1.length(), jsonArray2.length());
		Assertions.assertEquals(jsonArray1.get(0), jsonArray2.get(0));
	}

	@Test
	public void testReplace() {
		JSONObject jsonObject = JSONUtil.put(
			"alpha", "1"
		).put(
			"beta", "3"
		).put(
			"gamma", "4"
		);

		Assertions.assertNull(JSONUtil.replace(null, "alpha", jsonObject));

		JSONArray jsonArray = JSONUtil.put(
			JSONUtil.put(
				"alpha", "1"
			).put(
				"beta", "2"
			)
		).put(
			JSONUtil.put("alpha", "1")
		);

		jsonArray = JSONUtil.replace(jsonArray, "alpha", jsonObject);

		jsonObject = jsonArray.getJSONObject(0);

		Assertions.assertEquals("3", jsonObject.get("beta"));
		Assertions.assertEquals("4", jsonObject.get("gamma"));

		jsonObject = jsonArray.getJSONObject(1);

		Assertions.assertEquals("3", jsonObject.get("beta"));
		Assertions.assertEquals("4", jsonObject.get("gamma"));
	}

	@Test
	public void testSerializeArraysWithObjects() {
		JSONArray jsonArray = JSONUtil.put(_createJSONObject());

		Assertions.assertEquals("[{}]", jsonArray.toString());
	}

	@Test
	public void testToArray() throws Exception {
		Assertions.assertArrayEquals(
			new String[] {"1", "2"},
			JSONUtil.toArray(
				JSONUtil.putAll(
					JSONUtil.put("foo", 1)
				).put(
					JSONUtil.put("foo", 2)
				),
				jsonObject -> String.valueOf(jsonObject.getInt("foo")),
				String.class));
	}

	@Test
	public void testToJSONArrayWithArray() throws Exception {
		JSONArray expectedJSONArray1 = _createJSONArray();

		JSONArray actualJSONArray1 = JSONUtil.toJSONArray(
			(String[])null, s -> JSONUtil.put("foo", Integer.valueOf(s)));

		Assertions.assertEquals(
			expectedJSONArray1.toString(), actualJSONArray1.toString());

		JSONArray expectedJSONArray2 = JSONUtil.put(
			JSONUtil.put("foo", 1)
		).put(
			JSONUtil.put("foo", 2)
		);

		JSONArray actualJSONArray2 = JSONUtil.toJSONArray(
			new String[] {"1", "2"},
			s -> JSONUtil.put("foo", Integer.valueOf(s)));

		Assertions.assertEquals(
			expectedJSONArray2.toString(), actualJSONArray2.toString());
	}

	@Test
	public void testToJSONArrayWithList() throws Exception {
		JSONArray expectedJSONArray1 = _createJSONArray();

		JSONArray actualJSONArray1 = JSONUtil.toJSONArray(
			(String[])null, s -> JSONUtil.put("foo", Integer.valueOf(s)));

		Assertions.assertEquals(
			expectedJSONArray1.toString(), actualJSONArray1.toString());

		JSONArray expectedJSONArray2 = JSONUtil.put(
			JSONUtil.put("foo", 1)
		).put(
			JSONUtil.put("foo", 2)
		);

		JSONArray actualJSONArray2 = JSONUtil.toJSONArray(
			new ArrayList<String>() {
				{
					add("1");
					add("2");
				}
			},
			s -> JSONUtil.put("foo", Integer.valueOf(s)));

		Assertions.assertEquals(
			expectedJSONArray2.toString(), actualJSONArray2.toString());
	}

	@Test
	public void testToJSONObjectMap() {
		Assertions.assertEquals(
			Collections.emptyMap(),
			JSONUtil.toJSONObjectMap(_createJSONArray(), null));

		Map<String, JSONObject> expectedJSONObjects =
			new HashMap<String, JSONObject>() {
				{
					put(
						"1",
						JSONUtil.put(
							"alpha", 1
						).put(
							"key", "1"
						));
					put(
						"2",
						JSONUtil.put(
							"beta", 1
						).put(
							"key", "2"
						));
					put(
						"3",
						JSONUtil.put(
							"gamma", 1
						).put(
							"key", "3"
						));
				}
			};

		Map<String, JSONObject> actualJSONObjects = JSONUtil.toJSONObjectMap(
			JSONUtil.putAll(
				JSONUtil.put(
					"alpha", 1
				).put(
					"key", "1"
				),
				JSONUtil.put(
					"beta", 1
				).put(
					"key", "2"
				),
				JSONUtil.put(
					"gamma", 1
				).put(
					"key", "3"
				)),
			"key");

		Assertions.assertEquals(
			expectedJSONObjects.toString(), actualJSONObjects.toString());
	}

	@Test
	public void testToList() throws Exception {
		Assertions.assertEquals(
			new ArrayList<Integer>(),
			JSONUtil.toList(null, jsonObject -> jsonObject.getInt("foo")));
		Assertions.assertEquals(
			new ArrayList<Integer>() {
				{
					add(1);
					add(2);
				}
			},
			JSONUtil.toList(
				JSONUtil.put(
					JSONUtil.put("foo", "1")
				).put(
					JSONUtil.put("foo", "2")
				),
				jsonObject -> jsonObject.getInt("foo")));
	}

	@Test
	public void testToLongArray() {
		Assertions.assertArrayEquals(new long[0], JSONUtil.toLongArray(null));
		Assertions.assertArrayEquals(
			new long[] {1, 2}, JSONUtil.toLongArray(JSONUtil.putAll(1, 2)));
	}

	@Test
	public void testToLongArrayWithKey() {
		Assertions.assertArrayEquals(
			new long[0], JSONUtil.toLongArray(null, "alpha"));
		Assertions.assertArrayEquals(
			new long[] {1, 2},
			JSONUtil.toLongArray(
				JSONUtil.putAll(
					JSONUtil.put("alpha", 1), JSONUtil.put("alpha", 2),
					JSONUtil.put("beta", 3)),
				"alpha"));
	}

	@Test
	public void testToLongList() {
		Assertions.assertEquals(
			Collections.emptyList(), JSONUtil.toLongList(null));
		Assertions.assertEquals(
			new ArrayList<Long>() {
				{
					add(1L);
					add(2L);
					add(3L);
				}
			},
			JSONUtil.toLongList(JSONUtil.putAll(1, 2, 3)));
	}

	@Test
	public void testToLongListWithKey() {
		Assertions.assertEquals(
			Collections.emptyList(), JSONUtil.toLongList(null, "alpha"));
		Assertions.assertEquals(
			new ArrayList<Long>() {
				{
					add(1L);
					add(2L);
					add(3L);
				}
			},
			JSONUtil.toLongList(
				JSONUtil.putAll(
					JSONUtil.put("alpha", 1), JSONUtil.put("alpha", 2),
					JSONUtil.put("alpha", 3)),
				"alpha"));
	}

	@Test
	public void testToLongSet() {
		Assertions.assertEquals(
			Collections.emptySet(), JSONUtil.toLongSet(null));
		Assertions.assertEquals(
			new HashSet<Long>() {
				{
					add(1L);
					add(2L);
					add(3L);
				}
			},
			JSONUtil.toLongSet(JSONUtil.putAll(1, 2, 3)));
	}

	@Test
	public void testToLongSetWithKey() {
		Assertions.assertEquals(
			Collections.emptySet(), JSONUtil.toLongSet(null, "alpha"));
		Assertions.assertEquals(
			new HashSet<Long>() {
				{
					add(1L);
					add(2L);
				}
			},
			JSONUtil.toLongSet(
				JSONUtil.putAll(
					JSONUtil.put("alpha", 1), JSONUtil.put("alpha", 2),
					JSONUtil.put("beta", 3)),
				"alpha"));
	}

	@Test
	public void testToObjectArray() {
		Assertions.assertArrayEquals(
			new Object[0], JSONUtil.toObjectArray(null));
		Assertions.assertArrayEquals(
			new Object[] {1, "beta", true},
			JSONUtil.toObjectArray(JSONUtil.putAll(1, "beta", true)));
	}

	@Test
	public void testToObjectArrayWithKey() {
		Assertions.assertArrayEquals(
			new Object[0], JSONUtil.toObjectArray(null, "alpha"));
		Assertions.assertArrayEquals(
			new Object[] {1, true},
			JSONUtil.toObjectArray(
				JSONUtil.putAll(
					JSONUtil.put("alpha", 1), JSONUtil.put("alpha", true),
					JSONUtil.put("beta", 3)),
				"alpha"));
	}

	@Test
	public void testToObjectList() {
		Assertions.assertEquals(
			Collections.emptyList(), JSONUtil.toObjectList(null));
		Assertions.assertEquals(
			new ArrayList<Object>() {
				{
					add(1);
					add("beta");
					add(true);
				}
			},
			JSONUtil.toObjectList(JSONUtil.putAll(1, "beta", true)));
	}

	@Test
	public void testToObjectListWithKey() {
		Assertions.assertEquals(
			Collections.emptyList(), JSONUtil.toObjectList(null, "alpha"));
		Assertions.assertEquals(
			new ArrayList<Object>() {
				{
					add(1);
					add("beta");
				}
			},
			JSONUtil.toObjectList(
				JSONUtil.putAll(
					JSONUtil.put("alpha", 1), JSONUtil.put("alpha", "beta"),
					JSONUtil.put("beta", true)),
				"alpha"));
	}

	@Test
	public void testToObjectSet() {
		Assertions.assertEquals(
			Collections.emptySet(), JSONUtil.toObjectSet(null));
		Assertions.assertEquals(
			new HashSet<Object>() {
				{
					add(1);
					add("beta");
					add(true);
				}
			},
			JSONUtil.toObjectSet(JSONUtil.putAll(1, "beta", true)));
	}

	@Test
	public void testToObjectSetWithKey() {
		Assertions.assertEquals(
			Collections.emptySet(), JSONUtil.toObjectSet(null, "alpha"));
		Assertions.assertEquals(
			new HashSet<Object>() {
				{
					add(1);
					add("beta");
				}
			},
			JSONUtil.toObjectSet(
				JSONUtil.putAll(
					JSONUtil.put("alpha", 1), JSONUtil.put("alpha", "beta"),
					JSONUtil.put("beta", true)),
				"alpha"));
	}

	@Test
	public void testToStringArray() {
		Assertions.assertArrayEquals(
			new String[0], JSONUtil.toStringArray(null));
		Assertions.assertArrayEquals(
			new String[] {"alpha", "beta", "gamma"},
			JSONUtil.toStringArray(JSONUtil.putAll("alpha", "beta", "gamma")));
	}

	@Test
	public void testToStringArrayWithKey() {
		Assertions.assertArrayEquals(
			new String[0], JSONUtil.toStringArray(null, "alpha"));
		Assertions.assertArrayEquals(
			new String[] {"alpha", "beta"},
			JSONUtil.toStringArray(
				JSONUtil.putAll(
					JSONUtil.put("alpha", "alpha"),
					JSONUtil.put("alpha", "beta"),
					JSONUtil.put("beta", "gamma")),
				"alpha"));
	}

	@Test
	public void testToStringList() {
		Assertions.assertEquals(
			Collections.emptyList(), JSONUtil.toStringList(null));
		Assertions.assertEquals(
			new ArrayList<String>() {
				{
					add("alpha");
					add("beta");
					add("gamma");
				}
			},
			JSONUtil.toStringList(JSONUtil.putAll("alpha", "beta", "gamma")));
	}

	@Test
	public void testToStringListWithKey() {
		Assertions.assertEquals(
			Collections.emptyList(), JSONUtil.toStringList(null, "alpha"));
		Assertions.assertEquals(
			new ArrayList<String>() {
				{
					add("alpha");
					add("beta");
				}
			},
			JSONUtil.toStringList(
				JSONUtil.putAll(
					JSONUtil.put("alpha", "alpha"),
					JSONUtil.put("alpha", "beta"),
					JSONUtil.put("beta", "gamma")),
				"alpha"));
	}

	@Test
	public void testToStringSet() {
		Assertions.assertEquals(
			Collections.emptySet(), JSONUtil.toStringSet(null));
		Assertions.assertEquals(
			new HashSet<String>() {
				{
					add("alpha");
					add("beta");
					add("gamma");
				}
			},
			JSONUtil.toStringSet(JSONUtil.putAll("alpha", "beta", "gamma")));
	}

	@Test
	public void testToStringSetWithKey() {
		Assertions.assertEquals(
			Collections.emptySet(), JSONUtil.toStringSet(null, "alpha"));
		Assertions.assertEquals(
			new HashSet<String>() {
				{
					add("alpha");
					add("beta");
				}
			},
			JSONUtil.toStringSet(
				JSONUtil.putAll(
					JSONUtil.put("alpha", "alpha"),
					JSONUtil.put("alpha", "beta"),
					JSONUtil.put("beta", "gamma")),
				"alpha"));
	}

	private JSONArray _createJSONArray() {
		return new JSONArray();
	}

	private JSONObject _createJSONObject() {
		return new JSONObject();
	}

}