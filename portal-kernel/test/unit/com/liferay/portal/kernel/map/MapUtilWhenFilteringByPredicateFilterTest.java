/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.map;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PredicateFilter;
import com.liferay.portal.kernel.util.PrefixPredicateFilter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sampsa Sohlman
 * @author Manuel de la Peña
 * @author Péter Borkuti
 */
public class MapUtilWhenFilteringByPredicateFilterTest {

	@Test
	public void testShouldAllowFilterBySuperType() {
		Map<String, Integer> inputMap = new HashMap<>();

		inputMap.put("1", 1);
		inputMap.put("2", 2);
		inputMap.put("3", 3);
		inputMap.put("4", 4);
		inputMap.put("5", 5);

		Map<String, Integer> outputMap = MapUtil.filterByValues(
			inputMap,
			new PredicateFilter<Number>() {

				@Override
				public boolean filter(Number number) {
					if ((number.intValue() % 2) == 0) {
						return true;
					}

					return false;
				}

			});

		Assert.assertEquals(outputMap.toString(), 2, outputMap.size());
		Assert.assertEquals((Integer)2, outputMap.get("2"));
		Assert.assertEquals((Integer)4, outputMap.get("4"));
	}

	@Test
	public void testShouldAllowFilterBySuperTypeAndOutputToSupertype() {
		Map<String, Integer> inputMap = new HashMap<>();

		inputMap.put("1", 1);
		inputMap.put("2", 2);
		inputMap.put("3", 3);
		inputMap.put("4", 4);
		inputMap.put("5", 5);

		HashMap<String, Number> outputMap = new HashMap<>();

		MapUtil.filter(
			inputMap, outputMap,
			new PredicateFilter<Map.Entry<?, Number>>() {

				@Override
				public boolean filter(Map.Entry<?, Number> entry) {
					Number number = entry.getValue();

					if ((number.intValue() % 2) == 0) {
						return true;
					}

					return false;
				}

			});

		Assert.assertEquals(outputMap.toString(), 2, outputMap.size());
		Assert.assertEquals(2, outputMap.get("2"));
		Assert.assertEquals(4, outputMap.get("4"));
	}

	@Test
	public void testShouldReturnMapFilteredByEven() {
		Map<String, String> inputMap = new HashMap<>();

		inputMap.put("1", "one");
		inputMap.put("2", "two");
		inputMap.put("3", "three");
		inputMap.put("4", "four");
		inputMap.put("5", "five");

		Map<String, String> outputMap = MapUtil.filter(
			inputMap,
			new PredicateFilter<Map.Entry<String, ?>>() {

				@Override
				public boolean filter(Map.Entry<String, ?> entry) {
					int value = GetterUtil.getInteger(entry.getKey());

					if ((value % 2) == 0) {
						return true;
					}

					return false;
				}

			});

		Assert.assertEquals(outputMap.toString(), 2, outputMap.size());
		Assert.assertEquals("two", outputMap.get("2"));
		Assert.assertEquals("four", outputMap.get("4"));
	}

	@Test
	public void testShouldReturnMapFilteredByPrefix() {
		Map<String, String> inputMap = new HashMap<>();

		inputMap.put("2", "two");
		inputMap.put("4", "four");
		inputMap.put("x1", "one");
		inputMap.put("x3", "three");
		inputMap.put("x5", "five");

		Map<String, String> outputMap = MapUtil.filterByKeys(
			inputMap, new PrefixPredicateFilter("x"));

		Assert.assertEquals(outputMap.toString(), 2, outputMap.size());
		Assert.assertEquals("two", outputMap.get("2"));
		Assert.assertEquals("four", outputMap.get("4"));
	}

}