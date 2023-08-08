/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.model.AnalysisType;
import com.liferay.osb.asah.common.model.AttributeType;
import com.liferay.osb.asah.common.model.BreakdownRow;
import com.liferay.osb.asah.common.model.DateGrouping;
import com.liferay.osb.asah.common.model.EventAnalysisBreakdown;
import com.liferay.osb.asah.common.model.EventAnalysisFilter;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.EventAttributeDefinitionRepository;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.common.util.GetterUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 * @author Matthew Kong
 */
@Import(JDBCTestConfiguration.class)
public class BQEventRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		_preferenceDog.savePreference("time-zone-id", "UTC");
	}

	@BQSQLResource(resourcePath = "test_bq_events.sql")
	@Test
	public void testCountBQEvents() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Assertions.assertEquals(
			1,
			_bqEventRepository.countBQEvents(
				"Form", "3", 1L, 1L, "fieldBlurred",
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime()));
	}

	@BQSQLResource(resourcePath = "test_bq_events.sql")
	@Test
	public void testCountBQEventsLast24Hours() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Assertions.assertEquals(
			3,
			_bqEventRepository.countBQEvents(
				1L, "1", null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_events.sql")
	@Test
	public void testCountBQEventsWithKeywordsLast24Hours() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Assertions.assertEquals(
			1,
			_bqEventRepository.countBQEvents(
				1L, "1", "form", timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testCountTotalBQEvents1() {
		Assertions.assertEquals(
			8L,
			_bqEventRepository.countTotalBQEvents(
				1L,
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2021-05-10", "2021-06-01"))),
				246810L,
				DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
				DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testCountTotalBQEvents2() {
		Optional<EventAttributeDefinition> eventAttributeDefinitionOptional =
			_eventAttributeDefinitionRepository.findByName("pageTitle");

		Assertions.assertTrue(eventAttributeDefinitionOptional.isPresent());

		EventAttributeDefinition eventAttributeDefinition =
			eventAttributeDefinitionOptional.get();

		Assertions.assertEquals(
			1L,
			_bqEventRepository.countTotalBQEvents(
				1L,
				Arrays.asList(
					new EventAnalysisFilter(
						String.valueOf(eventAttributeDefinition.getId()),
						AttributeType.EVENT,
						EventAttributeDefinition.DataType.STRING, null,
						"pageTitle", "contains", Arrays.asList("Test 16")),
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2021-01-01", "2021-06-01"))),
				246810L,
				DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
				DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_global_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_global_property_values.sql")
	@Test
	public void testCountTotalBQEventsWithOnlyGlobalAttributes() {
		Optional<EventAttributeDefinition> eventAttributeDefinitionOptional =
			_eventAttributeDefinitionRepository.findByName("pageTitle");

		Assertions.assertTrue(eventAttributeDefinitionOptional.isPresent());

		EventAttributeDefinition eventAttributeDefinition =
			eventAttributeDefinitionOptional.get();

		Assertions.assertEquals(
			1,
			_bqEventRepository.countTotalBQEvents(
				1L,
				Collections.singletonList(
					new EventAnalysisFilter(
						String.valueOf(eventAttributeDefinition.getId()),
						AttributeType.EVENT,
						EventAttributeDefinition.DataType.STRING, null,
						"pageTitle", "contains", Arrays.asList("test"))),
				246810L,
				DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
				DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testCountTotalBQEventsWithTimeZone() {
		Assertions.assertEquals(
			6L,
			_bqEventRepository.countTotalBQEvents(
				1L,
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2021-05-10", "2021-06-01"))),
				246810L,
				DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
				DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
				"America/Los_Angeles"));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testCountUniqueIndividuals() {
		Assertions.assertEquals(
			6L,
			_bqEventRepository.countUniqueIndividuals(
				1L,
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2021-05-10", "2021-06-01"))),
				246810L,
				DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
				DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testCountUniqueIndividualsWithTimeZone() {
		Assertions.assertEquals(
			5L,
			_bqEventRepository.countUniqueIndividuals(
				1L,
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2021-05-10", "2021-06-01"))),
				246810L,
				DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
				DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
				"America/Los_Angeles"));
	}

	@BQSQLResource(
		resourcePath = "test_get_average_bq_event_count_per_individual_bq.sql"
	)
	@SQLResource(
		resourcePath = "test_get_average_bq_event_count_per_individual.sql"
	)
	@Test
	public void testGetAverageBQEventCountPerIndividual() {
		MatcherAssert.assertThat(
			BigDecimal.valueOf(2),
			Matchers.comparesEqualTo(
				_bqEventRepository.getAverageBQEventCountPerIndividual(
					1L,
					Collections.singletonList(
						new EventAnalysisFilter(
							"56789", AttributeType.EVENT,
							EventAttributeDefinition.DataType.DATE, null,
							"testDate", "between",
							Arrays.asList("2021-05-10", "2021-05-13"))),
					246810L,
					DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
					DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
					_timeZoneDog.getTimeZoneId())));
	}

	@BQSQLResource(
		resourcePath = "test_get_average_bq_event_count_per_individual_bq.sql"
	)
	@SQLResource(
		resourcePath = "test_get_average_bq_event_count_per_individual.sql"
	)
	@Test
	public void testGetAverageBQEventCountPerIndividualWithTimeZone() {
		MatcherAssert.assertThat(
			BigDecimal.valueOf(1.5),
			Matchers.comparesEqualTo(
				_bqEventRepository.getAverageBQEventCountPerIndividual(
					1L,
					Collections.singletonList(
						new EventAnalysisFilter(
							"56789", AttributeType.EVENT,
							EventAttributeDefinition.DataType.DATE, null,
							"testDate", "between",
							Arrays.asList("2021-05-10", "2021-05-13"))),
					246810L,
					DateUtil.toUTCDate(LocalDateTime.of(2021, 6, 1, 23, 59)),
					DateUtil.toUTCDate(LocalDateTime.of(2021, 5, 10, 0, 0)),
					"America/Los_Angeles")));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesAverage() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.AVERAGE, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"12345", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.STRING, null, null,
						"testUrl", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put(
						"https://www.beryl.com/blogs",
						BigDecimal.valueOf(1.50));
					put(
						"https://www.beryl.com/design",
						BigDecimal.valueOf(1.00));
					put(
						"https://www.beryl.com/products",
						BigDecimal.valueOf(1.00));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesBoolean() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"67890", AttributeType.EVENT, 1,
						EventAttributeDefinition.DataType.BOOLEAN, null, null,
						"testMember", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("false", BigDecimal.valueOf(4));
					put("true", BigDecimal.valueOf(6));
					put("undefined", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesCountBoolean() {
		Assertions.assertEquals(
			3,
			_bqEventRepository.getBQEventPropertyValuesCount(
				1L,
				new EventAnalysisBreakdown(
					"67890", AttributeType.EVENT, null,
					EventAttributeDefinition.DataType.BOOLEAN, null, null,
					"testMember", "DESC"),
				null, 246810L,
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesCountDate() {
		Assertions.assertEquals(
			5,
			_bqEventRepository.getBQEventPropertyValuesCount(
				1L,
				new EventAnalysisBreakdown(
					"56789", AttributeType.EVENT, null,
					EventAttributeDefinition.DataType.DATE, DateGrouping.MONTH,
					null, "testDate", "DESC"),
				null, 246810L,
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesCountDateWithTimeZone() {
		Assertions.assertEquals(
			2,
			_bqEventRepository.getBQEventPropertyValuesCount(
				1L,
				new EventAnalysisBreakdown(
					"56789", AttributeType.EVENT, null,
					EventAttributeDefinition.DataType.DATE, DateGrouping.DAY,
					null, "testDate", "DESC"),
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2021-05-10", "2021-06-01"))),
				246810L,
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 10, 0, 0)),
				"America/Los_Angeles"));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesCountDuration() {
		Assertions.assertEquals(
			6,
			_bqEventRepository.getBQEventPropertyValuesCount(
				1L,
				new EventAnalysisBreakdown(
					"78901", AttributeType.EVENT, 2000,
					EventAttributeDefinition.DataType.DURATION, null, null,
					"testDuration", "DESC"),
				null, 246810L,
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesCountNumber() {
		Assertions.assertEquals(
			8,
			_bqEventRepository.getBQEventPropertyValuesCount(
				1L,
				new EventAnalysisBreakdown(
					"45678", AttributeType.EVENT, 1,
					EventAttributeDefinition.DataType.NUMBER, null, null,
					"testRating", "DESC"),
				null, 246810L,
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesDayGroupingWithTimeZone() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"56789", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.DATE,
						DateGrouping.DAY, null, "testDate", "DESC")),
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2021-05-10", "2021-06-01"))),
				246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 10, 0, 0)),
				"America/Los_Angeles");

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("2021-05-12", BigDecimal.valueOf(5));
					put("2021-05-13", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesDuration() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"78901", AttributeType.EVENT, 2000,
						EventAttributeDefinition.DataType.DURATION, null, null,
						"testDuration", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("0", BigDecimal.valueOf(3));
					put("2000", BigDecimal.valueOf(3));
					put("4000", BigDecimal.valueOf(2));
					put("8000", BigDecimal.valueOf(1));
					put("10000", BigDecimal.valueOf(1));
					put("undefined", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesMonthGrouping() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"56789", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.DATE,
						DateGrouping.MONTH, null, "testDate", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("2019-05", BigDecimal.valueOf(1));
					put("2020-05", BigDecimal.valueOf(1));
					put("2021-01", BigDecimal.valueOf(1));
					put("2021-05", BigDecimal.valueOf(7));
					put("undefined", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesNullValues() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"78901", AttributeType.EVENT, 10000,
						EventAttributeDefinition.DataType.DURATION, null, null,
						"testDuration", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("0", BigDecimal.valueOf(9));
					put("10000", BigDecimal.valueOf(1));
					put("undefined", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesNumber() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"45678", AttributeType.EVENT, 3,
						EventAttributeDefinition.DataType.NUMBER, null, null,
						"testRating", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("0", BigDecimal.valueOf(6));
					put("3", BigDecimal.valueOf(3));
					put("9", BigDecimal.valueOf(2));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesTotal() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"12345", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.STRING, null, null,
						"testUrl", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("https://www.beryl.com/blogs", BigDecimal.valueOf(3));
					put("https://www.beryl.com/design", BigDecimal.valueOf(4));
					put(
						"https://www.beryl.com/products",
						BigDecimal.valueOf(4));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesUnique() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.UNIQUE, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"12345", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.STRING, null, null,
						"testUrl", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("https://www.beryl.com/blogs", BigDecimal.valueOf(2));
					put("https://www.beryl.com/design", BigDecimal.valueOf(4));
					put(
						"https://www.beryl.com/products",
						BigDecimal.valueOf(4));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesWithBreakdownItem() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"12345", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.STRING, null, null,
						"testUrl", "DESC")),
				Collections.singletonList(
					new EventAnalysisFilter(
						"34567", AttributeType.EVENT,
						EventAttributeDefinition.DataType.STRING, null,
						"testCode", "eq", Collections.singletonList("400"))),
				246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("https://www.beryl.com/blogs", BigDecimal.valueOf(1));
					put("https://www.beryl.com/design", BigDecimal.valueOf(2));
					put(
						"https://www.beryl.com/products",
						BigDecimal.valueOf(4));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesWithBreakdownItemWithReservedWords1() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"12456", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.BOOLEAN, null, null,
						"like", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("true", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesWithBreakdownItemWithReservedWords2() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"13456", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.STRING, null, null,
						"name", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("liferay1", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesWithNoBreakdown() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false, Collections.emptyList(), null,
				246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			1, breakdownRows.size(), breakdownRows.toString());

		BreakdownRow breakdownRow = breakdownRows.get(0);

		Assertions.assertEquals(1, breakdownRow.getBreakdownColumnsCount());

		BreakdownRow.BreakdownColumn breakdownColumn =
			breakdownRow.getBreakdownColumn(0);

		Number number = GetterUtil.getNumber(breakdownColumn.getValue());

		Assertions.assertEquals(20, number.intValue());
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetBQEventPropertyValuesYearGrouping() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"56789", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.DATE,
						DateGrouping.YEAR, null, "testDate", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("2019", BigDecimal.valueOf(1));
					put("2020", BigDecimal.valueOf(1));
					put("2021", BigDecimal.valueOf(8));
					put("undefined", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_7_days.sql"
	)
	@Test
	public void testGetBQEventsCountGroupByEventDateLast7Days() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, Integer> bqEventsCountGroupByEventDate =
			_bqEventRepository.getBQEventsCountGroupByEventDate(
				1L, null, Interval.DAY, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			2, bqEventsCountGroupByEventDate.size(),
			bqEventsCountGroupByEventDate.toString());

		Collection<Integer> values = bqEventsCountGroupByEventDate.values();

		MatcherAssert.assertThat(
			new Integer[] {1, 2},
			Matchers.arrayContainingInAnyOrder(values.toArray(new Integer[0])));
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_7_days.sql"
	)
	@Disabled
	@Test
	public void testGetBQEventsCountGroupByEventDateLast7DaysWithTimeZone() {
		_preferenceDog.savePreference("time-zone-id", "America/Los_Angeles");

		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, Integer> bqEventsCountGroupByEventDate =
			_bqEventRepository.getBQEventsCountGroupByEventDate(
				1L, null, Interval.DAY, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			2, bqEventsCountGroupByEventDate.size(),
			bqEventsCountGroupByEventDate.toString());

		ZonedDateTime zonedDateTime = ZonedDateTime.now(
			_timeZoneDog.getZoneId());

		Map<String, Integer> expectedCountGroupByEventDate =
			new HashMap<String, Integer>() {
				{
					put(
						_getLocalDateString(
							ChronoUnit.DAYS, -3, null, zonedDateTime),
						2);
					put(
						_getLocalDateString(
							ChronoUnit.DAYS, -6, null, zonedDateTime),
						1);
				}
			};

		Assertions.assertEquals(
			expectedCountGroupByEventDate, bqEventsCountGroupByEventDate);
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_24_hours.sql"
	)
	@Test
	public void testGetBQEventsCountGroupByEventDateLast24Hours() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Map<String, Integer> bqEventsCountGroupByEventDate =
			_bqEventRepository.getBQEventsCountGroupByEventDate(
				1L, null, Interval.HOUR, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			1, bqEventsCountGroupByEventDate.size(),
			bqEventsCountGroupByEventDate.toString());

		Collection<Integer> values = bqEventsCountGroupByEventDate.values();

		Iterator<Integer> iterator = values.iterator();

		Assertions.assertEquals(4, iterator.next());
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_24_hours.sql"
	)
	@Test
	public void testGetBQEventsCountGroupByEventDateLast24HoursWithTimeZone() {
		_preferenceDog.savePreference("time-zone-id", "America/Los_Angeles");

		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Map<String, Integer> bqEventsCountGroupByEventDate =
			_bqEventRepository.getBQEventsCountGroupByEventDate(
				1L, null, Interval.HOUR, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			1, bqEventsCountGroupByEventDate.size(),
			bqEventsCountGroupByEventDate.toString());

		ZonedDateTime zonedDateTime = ZonedDateTime.now(
			_timeZoneDog.getZoneId());

		Map<String, Integer> expectedBQEventsCountGroupByEventDate =
			Collections.singletonMap(
				_getLocalDateString(ChronoUnit.HOURS, null, -1, zonedDateTime),
				4);

		Assertions.assertEquals(
			expectedBQEventsCountGroupByEventDate,
			bqEventsCountGroupByEventDate);
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetEventPropertyValuesCountString() {
		Assertions.assertEquals(
			3,
			_bqEventRepository.getBQEventPropertyValuesCount(
				1L,
				new EventAnalysisBreakdown(
					"12345", AttributeType.EVENT, 0,
					EventAttributeDefinition.DataType.STRING, null, null,
					"testUrl", "DESC"),
				null, 246810L,
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId()));
	}

	@BQSQLResource(resourcePath = "test_bq_event_property_values_bq.sql")
	@SQLResource(resourcePath = "test_bq_event_property_values.sql")
	@Test
	public void testGetEventPropertyValuesDayGrouping() {
		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				AnalysisType.TOTAL, 1L, false,
				Arrays.asList(
					new EventAnalysisBreakdown(
						"56789", AttributeType.EVENT, 0,
						EventAttributeDefinition.DataType.DATE,
						DateGrouping.DAY, null, "testDate", "DESC")),
				null, 246810L, PageRequest.of(0, 10),
				TimeRange.of(
					LocalDateTime.of(2021, 6, 1, 23, 59),
					LocalDateTime.of(2021, 5, 15, 0, 0)),
				_timeZoneDog.getTimeZoneId());

		_assertBreakdowRowEquals(
			breakdownRows,
			new HashMap<String, BigDecimal>() {
				{
					put("2019-05-10", BigDecimal.valueOf(1));
					put("2020-05-13", BigDecimal.valueOf(1));
					put("2021-01-13", BigDecimal.valueOf(1));
					put("2021-05-01", BigDecimal.valueOf(1));
					put("2021-05-10", BigDecimal.valueOf(2));
					put("2021-05-13", BigDecimal.valueOf(4));
					put("undefined", BigDecimal.valueOf(1));
				}
			});
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_7_days.sql"
	)
	@Test
	public void testGetEventSessionsCountGroupByEventDateLast7Days() {
		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, Integer> eventSessionsCountGroupByEventDate =
			_bqEventRepository.getEventSessionsCountGroupByEventDate(
				1L, null, Interval.DAY, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			2, eventSessionsCountGroupByEventDate.size(),
			eventSessionsCountGroupByEventDate.toString());

		Collection<Integer> values =
			eventSessionsCountGroupByEventDate.values();

		MatcherAssert.assertThat(
			new Integer[] {1, 2},
			Matchers.arrayContainingInAnyOrder(values.toArray(new Integer[0])));
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_7_days.sql"
	)
	@Disabled
	@Test
	public void testGetEventSessionsCountGroupByEventDateLast7DaysWithTimeZone() {
		_preferenceDog.savePreference("time-zone-id", "America/Los_Angeles");

		TimeRange timeRange = TimeRange.LAST_7_DAYS;

		Map<String, Integer> eventSessionsCountGroupByEventDate =
			_bqEventRepository.getEventSessionsCountGroupByEventDate(
				1L, null, Interval.DAY, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			2, eventSessionsCountGroupByEventDate.size(),
			eventSessionsCountGroupByEventDate.toString());

		ZonedDateTime zonedDateTime = ZonedDateTime.now(
			_timeZoneDog.getZoneId());

		Map<String, Integer> expectedEventSessionsCountGroupByEventDate =
			new HashMap<String, Integer>() {
				{
					put(
						_getLocalDateString(
							ChronoUnit.DAYS, -3, null, zonedDateTime),
						2);
					put(
						_getLocalDateString(
							ChronoUnit.DAYS, -6, null, zonedDateTime),
						1);
				}
			};

		Assertions.assertEquals(
			expectedEventSessionsCountGroupByEventDate,
			eventSessionsCountGroupByEventDate);
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_24_hours.sql"
	)
	@Test
	public void testGetEventSessionsCountGroupByEventDateLast24Hours() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Map<String, Integer> eventSessionsCountGroupByEventDate =
			_bqEventRepository.getEventSessionsCountGroupByEventDate(
				1L, null, Interval.HOUR, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			1, eventSessionsCountGroupByEventDate.size(),
			eventSessionsCountGroupByEventDate.toString());

		Collection<Integer> values =
			eventSessionsCountGroupByEventDate.values();

		Iterator<Integer> iterator = values.iterator();

		Assertions.assertEquals(2, iterator.next());
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_last_24_hours.sql"
	)
	@Test
	public void testGetEventSessionsCountGroupByEventDateLast24HoursWithTimeZone() {
		_preferenceDog.savePreference("time-zone-id", "America/Los_Angeles");

		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		Map<String, Integer> eventSessionsCountGroupByEventDate =
			_bqEventRepository.getEventSessionsCountGroupByEventDate(
				1L, null, Interval.HOUR, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			1, eventSessionsCountGroupByEventDate.size(),
			eventSessionsCountGroupByEventDate.toString());

		ZonedDateTime zonedDateTime = ZonedDateTime.now(
			_timeZoneDog.getZoneId());

		Map<String, Integer> expectedEventSessionsCountGroupByEventDate =
			Collections.singletonMap(
				_getLocalDateString(ChronoUnit.HOURS, null, -1, zonedDateTime),
				2);

		Assertions.assertEquals(
			expectedEventSessionsCountGroupByEventDate,
			eventSessionsCountGroupByEventDate);
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_custom_range.sql"
	)
	@Test
	public void testGetEventSessionsCountGroupByEventDateMonthlyInterval() {
		TimeRange timeRange = TimeRange.of(
			LocalDate.parse("2022-12-31"), LocalDate.parse("2022-12-01"));

		Map<String, Integer> eventSessionsCountGroupByEventDate =
			_bqEventRepository.getEventSessionsCountGroupByEventDate(
				1L, null, Interval.MONTH, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			1, eventSessionsCountGroupByEventDate.size(),
			eventSessionsCountGroupByEventDate.toString());

		Collection<Integer> values =
			eventSessionsCountGroupByEventDate.values();

		MatcherAssert.assertThat(
			new Integer[] {7},
			Matchers.arrayContainingInAnyOrder(values.toArray(new Integer[0])));
	}

	@BQSQLResource(
		resourcePath = "test_bq_event_count_grouped_by_event_date_custom_range.sql"
	)
	@Test
	public void testGetEventSessionsCountGroupByEventDateWeeklyInterval() {
		TimeRange timeRange = TimeRange.of(
			LocalDate.parse("2022-12-31"), LocalDate.parse("2022-12-01"));

		Map<String, Integer> eventSessionsCountGroupByEventDate =
			_bqEventRepository.getEventSessionsCountGroupByEventDate(
				1L, null, Interval.WEEK, null, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(
			5, eventSessionsCountGroupByEventDate.size(),
			eventSessionsCountGroupByEventDate.toString());

		Collection<Integer> values =
			eventSessionsCountGroupByEventDate.values();

		MatcherAssert.assertThat(
			new Integer[] {1, 1, 2, 2, 2},
			Matchers.arrayContainingInAnyOrder(values.toArray(new Integer[0])));
	}

	@BQSQLResource(resourcePath = "test_bq_events.sql")
	@Test
	public void testSearchBQEventsLast24Hours() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		List<BQEvent> bqEvents = _bqEventRepository.searchBQEvents(
			1L, "1", null, PageRequest.of(0, 50, Sort.desc("eventDate")),
			timeRange.getEndLocalDateTime(), timeRange.getStartLocalDateTime(),
			_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(3, bqEvents.size(), bqEvents.toString());

		Stream<BQEvent> stream = bqEvents.stream();

		Assertions.assertArrayEquals(
			new String[] {"blogClicked", "formViewed", "assetClicked"},
			stream.map(
				bqEvent -> {
					Optional<EventDefinition> eventDefinitionOptional =
						_eventDefinitionRepository.findByName(
							bqEvent.getEventId());

					EventDefinition eventDefinition =
						eventDefinitionOptional.get();

					return eventDefinition.getName();
				}
			).toArray());
	}

	@BQSQLResource(resourcePath = "test_bq_events.sql")
	@Disabled
	@Test
	public void testSearchBQEventsWithKeywordsLast24Hours() {
		TimeRange timeRange = TimeRange.LAST_24_HOURS;

		List<BQEvent> bqEvents = _bqEventRepository.searchBQEvents(
			1L, "1", "form", PageRequest.of(0, 50),
			timeRange.getEndLocalDateTime(), timeRange.getStartLocalDateTime(),
			_timeZoneDog.getTimeZoneId());

		Assertions.assertEquals(1, bqEvents.size(), bqEvents.toString());
	}

	private void _assertBreakdowRowEquals(
		List<BreakdownRow> breakdownRows,
		Map<String, BigDecimal> expectedValues) {

		Stream<BreakdownRow> stream = breakdownRows.stream();

		Map<String, BigDecimal> actualValues = stream.collect(
			Collectors.toMap(
				breakdownRow -> (String)breakdownRow.getBreakdownColumn(
					0
				).getValue(),
				breakdownRow -> new BigDecimal(
					String.valueOf(
						breakdownRow.getBreakdownColumn(
							breakdownRow.getBreakdownColumnsCount() - 1
						).getValue()))));

		Assertions.assertEquals(expectedValues.size(), actualValues.size());

		Set<String> keys = expectedValues.keySet();

		Assertions.assertTrue(keys.containsAll(actualValues.keySet()));

		keys = actualValues.keySet();

		Assertions.assertTrue(keys.containsAll(expectedValues.keySet()));

		for (Map.Entry<String, BigDecimal> entry : expectedValues.entrySet()) {
			BigDecimal actualValue = actualValues.get(entry.getKey());

			actualValue = actualValue.setScale(2, RoundingMode.HALF_UP);

			Assertions.assertEquals(0, actualValue.compareTo(entry.getValue()));
		}
	}

	private String _getLocalDateString(
		ChronoUnit chronoUnit, Integer deltaDays, Integer deltaHours,
		ZonedDateTime zonedDateTime) {

		if (deltaDays != null) {
			zonedDateTime = zonedDateTime.plusDays(deltaDays);
		}

		if (deltaHours != null) {
			zonedDateTime = zonedDateTime.plusHours(deltaHours);
		}

		if (chronoUnit != null) {
			zonedDateTime = zonedDateTime.truncatedTo(chronoUnit);
		}

		LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();

		return localDateTime.toString();
	}

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private EventAttributeDefinitionRepository
		_eventAttributeDefinitionRepository;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

	@Autowired
	private PreferenceDog _preferenceDog;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}