/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.SiteVisitorHeatMapDog;
import com.liferay.osb.asah.backend.model.HeatMapMetric;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQPageRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Leslie Wong
 */
@Disabled
public class SiteVisitorHeatMapDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		_preferenceDog.savePreference("time-zone-id", "UTC");
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsCustomRange() {
		LocalDate localDate = LocalDate.now(ZoneOffset.UTC);

		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.of(localDate, localDate.minusDays(49)));

		Assertions.assertEquals(
			168, heatMapMetrics.size(), heatMapMetrics.toString());

		double[] actualValues = _getActualValues(heatMapMetrics);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(2, 10), 1.0);
					put(Pair.of(4, 1), 1.0);
					put(Pair.of(5, 10), 1.0);
					put(Pair.of(6, 1), 1.0);
					put(Pair.of(6, 13), 1.0);
					put(Pair.of(6, 20), 3.0);
				}
			};

		Assertions.assertArrayEquals(
			_getExpectedValues(expectedValuesMap, "UTC", heatMapMetrics.size()),
			actualValues, 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsLast24Hours() {
		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.LAST_24_HOURS);

		double[] expectedValues = new double[heatMapMetrics.size()];

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(heatMapMetrics), 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsLast28Days() {
		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.LAST_28_DAYS);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(2, 10), 1.0);
					put(Pair.of(4, 1), 1.0);
					put(Pair.of(5, 10), 1.0);
					put(Pair.of(6, 1), 1.0);
					put(Pair.of(6, 20), 2.0);
				}
			};

		Assertions.assertArrayEquals(
			_getExpectedValues(expectedValuesMap, "UTC", heatMapMetrics.size()),
			_getActualValues(heatMapMetrics), 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsLast30Days() {
		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.LAST_30_DAYS);

		double[] actualValues = _getActualValues(heatMapMetrics);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(2, 10), 1.0);
					put(Pair.of(4, 1), 1.0);
					put(Pair.of(5, 10), 1.0);
					put(Pair.of(6, 1), 1.0);
					put(Pair.of(6, 20), 3.0);
				}
			};

		Assertions.assertArrayEquals(
			_getExpectedValues(expectedValuesMap, "UTC", heatMapMetrics.size()),
			actualValues, 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsLast90Days() {
		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.LAST_90_DAYS);

		Assertions.assertEquals(
			168, heatMapMetrics.size(), heatMapMetrics.toString());

		double[] actualValues = _getActualValues(heatMapMetrics);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(0, 23), 1.0);
					put(Pair.of(2, 0), 1.0);
					put(Pair.of(2, 10), 1.0);
					put(Pair.of(4, 1), 1.0);
					put(Pair.of(5, 10), 1.0);
					put(Pair.of(6, 1), 1.0);
					put(Pair.of(6, 13), 1.0);
					put(Pair.of(6, 20), 3.0);
				}
			};

		Assertions.assertArrayEquals(
			_getExpectedValues(expectedValuesMap, "UTC", heatMapMetrics.size()),
			actualValues, 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsLast180Days() {
		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.LAST_180_DAYS);

		Assertions.assertEquals(
			168, heatMapMetrics.size(), heatMapMetrics.toString());

		double[] actualValues = _getActualValues(heatMapMetrics);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(0, 15), 1.0);
					put(Pair.of(0, 23), 1.0);
					put(Pair.of(1, 15), 1.0);
					put(Pair.of(2, 0), 1.0);
					put(Pair.of(2, 10), 1.0);
					put(Pair.of(3, 0), 1.0);
					put(Pair.of(4, 1), 1.0);
					put(Pair.of(5, 10), 1.0);
					put(Pair.of(6, 1), 1.0);
					put(Pair.of(6, 13), 1.0);
					put(Pair.of(6, 20), 3.0);
				}
			};

		Assertions.assertArrayEquals(
			_getExpectedValues(expectedValuesMap, "UTC", heatMapMetrics.size()),
			actualValues, 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsLastYear() {
		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.LAST_YEAR);

		Assertions.assertEquals(
			168, heatMapMetrics.size(), heatMapMetrics.toString());

		double[] actualValues = _getActualValues(heatMapMetrics);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(0, 15), 1.0);
					put(Pair.of(0, 23), 1.0);
					put(Pair.of(1, 15), 1.0);
					put(Pair.of(2, 0), 1.0);
					put(Pair.of(2, 10), 1.0);
					put(Pair.of(3, 0), 1.0);
					put(Pair.of(3, 12), 1.0);
					put(Pair.of(4, 1), 1.0);
					put(Pair.of(5, 10), 1.0);
					put(Pair.of(6, 1), 1.0);
					put(Pair.of(6, 13), 1.0);
					put(Pair.of(6, 20), 3.0);
				}
			};

		Assertions.assertArrayEquals(
			_getExpectedValues(expectedValuesMap, "UTC", heatMapMetrics.size()),
			actualValues, 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsWithTimeZone() {
		_preferenceDog.savePreference("time-zone-id", "-07:00");

		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.LAST_90_DAYS);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(0, 16), 1.0);
					put(Pair.of(1, 17), 1.0);
					put(Pair.of(2, 3), 1.0);
					put(Pair.of(3, 18), 1.0);
					put(Pair.of(5, 18), 1.0);
					put(Pair.of(5, 3), 1.0);
					put(Pair.of(6, 13), 3.0);
					put(Pair.of(6, 6), 1.0);
				}
			};

		double[] expectedValues = _getExpectedValues(
			expectedValuesMap, "-07:00", heatMapMetrics.size());

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(heatMapMetrics), 0);

		_preferenceDog.savePreference("time-zone-id", "-03:00");

		heatMapMetrics = _siteVisitorHeatMapDog.getHeatMapMetrics(
			null, "1", TimeRange.LAST_90_DAYS);

		expectedValuesMap = new HashMap<Pair<Integer, Integer>, Double>() {
			{
				put(Pair.of(0, 20), 1.0);
				put(Pair.of(1, 21), 1.0);
				put(Pair.of(2, 7), 1.0);
				put(Pair.of(3, 22), 1.0);
				put(Pair.of(5, 22), 1.0);
				put(Pair.of(5, 7), 1.0);
				put(Pair.of(6, 10), 1.0);
				put(Pair.of(6, 17), 3.0);
			}
		};

		expectedValues = _getExpectedValues(
			expectedValuesMap, "-03:00", heatMapMetrics.size());

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(heatMapMetrics), 0);
	}

	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/visitor_heat_map_page_info.json"
	)
	@Test
	public void testVisitorHeatMapMetricsYesterday() {
		List<HeatMapMetric> heatMapMetrics =
			_siteVisitorHeatMapDog.getHeatMapMetrics(
				null, "1", TimeRange.YESTERDAY);

		Map<Pair<Integer, Integer>, Double> expectedValuesMap =
			new HashMap<Pair<Integer, Integer>, Double>() {
				{
					put(Pair.of(6, 20), 2.0);
				}
			};

		Assertions.assertArrayEquals(
			_getExpectedValues(expectedValuesMap, "UTC", heatMapMetrics.size()),
			_getActualValues(heatMapMetrics), 0);
	}

	private double[] _getActualValues(List<HeatMapMetric> heatMapMetrics) {
		double[] actualValues = new double[heatMapMetrics.size()];

		for (int i = 0; i < heatMapMetrics.size(); i++) {
			HeatMapMetric heatMapMetric = heatMapMetrics.get(i);

			actualValues[i] = heatMapMetric.getValue();
		}

		return actualValues;
	}

	private double[] _getExpectedValues(
		Map<Pair<Integer, Integer>, Double> expectedValueMap, String timeZoneId,
		int size) {

		double[] expectedValues = new double[size];

		LocalDateTime localDateTime = DateUtil.toLocalDateTime(
			new Date(), ZoneId.of(timeZoneId));

		DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();

		for (Map.Entry<Pair<Integer, Integer>, Double> entry :
				expectedValueMap.entrySet()) {

			Pair<Integer, Integer> key = entry.getKey();

			int day = key.getLeft() + dayOfWeek.getValue();

			if (day >= 7) {
				day = day - 7;
			}

			int hour = key.getRight();

			int index = (day * 24) + hour;

			expectedValues[index] = entry.getValue();
		}

		return expectedValues;
	}

	@Autowired
	private PreferenceDog _preferenceDog;

	@Autowired
	private SiteVisitorHeatMapDog _siteVisitorHeatMapDog;

}