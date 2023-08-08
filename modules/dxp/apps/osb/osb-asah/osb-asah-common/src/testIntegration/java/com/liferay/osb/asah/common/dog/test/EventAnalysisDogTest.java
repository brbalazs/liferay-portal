/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.EventAnalysisDog;
import com.liferay.osb.asah.common.entity.EventAnalysis;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.model.AnalysisType;
import com.liferay.osb.asah.common.model.AttributeType;
import com.liferay.osb.asah.common.model.BreakdownItem;
import com.liferay.osb.asah.common.model.DateGrouping;
import com.liferay.osb.asah.common.model.EventAnalysisBreakdown;
import com.liferay.osb.asah.common.model.EventAnalysisFilter;
import com.liferay.osb.asah.common.model.EventAnalysisResult;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.EventAnalysisRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahDuplicateNameException;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahNameException;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Leslie Wong
 */
public class EventAnalysisDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@SQLResource(resourcePath = "test_event_analysis.sql")
	@Test
	public void testEventAnalysis() {
		EventAnalysis eventAnalysis = _eventAnalysisDog.addEventAnalysis(
			AnalysisType.TOTAL, 1L, false,
			Collections.singletonList(
				new EventAnalysisBreakdown(
					"1", AttributeType.EVENT, 10,
					EventAttributeDefinition.DataType.STRING, DateGrouping.DAY,
					"Test Description 1", "Test Display Name 1", "ASC")),
			Collections.singletonList(
				new EventAnalysisFilter(
					"1", AttributeType.EVENT,
					EventAttributeDefinition.DataType.STRING, "eq",
					"Test Description 1", "Test Display Name 1",
					Collections.singletonList("value"))),
			246810L, "Event Analysis", LocalDate.parse("2021-06-01"), null,
			LocalDate.parse("2021-05-15"), 100L, "Test");

		Assertions.assertEquals(1, _eventAnalysisRepository.count());

		Assertions.assertNotNull(eventAnalysis);
		Assertions.assertNotNull(eventAnalysis.getId());
		Assertions.assertNull(eventAnalysis.getRangeKey());
		Assertions.assertEquals(
			LocalDate.of(2021, 6, 1), eventAnalysis.getRangeEndLocalDate());
		Assertions.assertEquals(
			LocalDate.of(2021, 5, 15), eventAnalysis.getRangeStartLocalDate());

		EventAnalysis updatedEventAnalysis =
			_eventAnalysisDog.updateEventAnalysis(
				AnalysisType.TOTAL, 1L, false,
				Collections.singletonList(
					new EventAnalysisBreakdown(
						"1", AttributeType.EVENT, 10,
						EventAttributeDefinition.DataType.STRING,
						DateGrouping.DAY, "Test Description 1",
						"Test Display Name 1", "ASC")),
				Collections.singletonList(
					new EventAnalysisFilter(
						"1", AttributeType.EVENT,
						EventAttributeDefinition.DataType.STRING,
						"Test Description 1", "Test Display Name 1", "eq",
						Collections.singletonList("value"))),
				eventAnalysis.getId(), 246810L, "Event Analysis Update", null,
				0, null, 101L, "Test Test");

		Assertions.assertNotNull(updatedEventAnalysis);
		Assertions.assertNotNull(updatedEventAnalysis.getId());
		Assertions.assertNull(updatedEventAnalysis.getRangeEndLocalDate());
		Assertions.assertNull(updatedEventAnalysis.getRangeStartLocalDate());
		Assertions.assertEquals(
			100L, updatedEventAnalysis.getCreatedByUserId());
		Assertions.assertEquals(
			"Test", updatedEventAnalysis.getCreatedByUserName());
		Assertions.assertEquals(
			101L, updatedEventAnalysis.getModifiedByUserId());
		Assertions.assertEquals(
			"Test Test", updatedEventAnalysis.getModifiedByUserName());
		Assertions.assertEquals(
			"Event Analysis Update", updatedEventAnalysis.getName());
		Assertions.assertEquals(0, updatedEventAnalysis.getRangeKey());

		_eventAnalysisDog.deleteEventAnalyses(
			Collections.singletonList(updatedEventAnalysis.getId()));

		Assertions.assertEquals(0, _eventAnalysisRepository.count());
	}

	@SQLResource(resourcePath = "test_event_analysis_name.sql")
	@Test
	public void testEventAnalysisName() {
		Exception exception = Assertions.assertThrows(
			OSBAsahNameException.class, () -> _addEventAnalysis(1L, " "));

		Assertions.assertEquals("Name cannot be blank", exception.getMessage());

		String eventAnalysisName1 = "ADD EVENT ANALYSIS IN THE SAME CHANNEL";

		exception = Assertions.assertThrows(
			OSBAsahDuplicateNameException.class,
			() -> {
				_addEventAnalysis(1L, eventAnalysisName1.toLowerCase());
				_addEventAnalysis(1L, eventAnalysisName1);
			});

		Assertions.assertEquals("Name is already used", exception.getMessage());

		EventAnalysis eventAnalysis1 = _addEventAnalysis(
			2L, eventAnalysisName1);

		Assertions.assertNotNull(eventAnalysis1);

		Optional<Long> eventAnalysisIdOptional = Optional.of(
			eventAnalysis1.getId());

		Long eventAnalysisId = eventAnalysisIdOptional.orElse(0L);

		Optional<EventAnalysis> eventAnalysisOptional1 =
			_eventAnalysisRepository.findById(eventAnalysisId);

		EventAnalysis eventAnalysis2 = eventAnalysisOptional1.orElse(null);

		Assertions.assertNotNull(eventAnalysis2);

		exception = Assertions.assertThrows(
			OSBAsahNameException.class,
			() -> _updateEventAnalysis(eventAnalysis2, null));

		Assertions.assertEquals("Name cannot be blank", exception.getMessage());

		String eventAnalysisName2 = "UPDATE EVENT ANALYSIS IN THE SAME CHANNEL";

		exception = Assertions.assertThrows(
			OSBAsahDuplicateNameException.class,
			() -> {
				_addEventAnalysis(
					eventAnalysis2.getChannelId(),
					eventAnalysisName2.toLowerCase());
				_updateEventAnalysis(eventAnalysis2, eventAnalysisName2);
			});

		Assertions.assertEquals("Name is already used", exception.getMessage());

		EventAnalysis eventAnalysis3 = _updateEventAnalysis(
			eventAnalysis2, eventAnalysisName1);

		Assertions.assertNotNull(eventAnalysis3);

		eventAnalysisIdOptional = Optional.of(eventAnalysis3.getId());

		eventAnalysisId = eventAnalysisIdOptional.orElse(0L);

		Optional<EventAnalysis> eventAnalysisOptional2 =
			_eventAnalysisRepository.findById(eventAnalysisId);

		EventAnalysis eventAnalysis4 = eventAnalysisOptional2.orElse(null);

		Assertions.assertNotNull(eventAnalysis4);

		eventAnalysis4 = _updateEventAnalysis(
			eventAnalysis4, eventAnalysis4.getName());

		Assertions.assertNotNull(eventAnalysis4);

		eventAnalysisIdOptional = Optional.of(eventAnalysis4.getId());

		eventAnalysisId = eventAnalysisIdOptional.orElse(0L);

		Optional<EventAnalysis> eventAnalysisOptional3 =
			_eventAnalysisRepository.findById(eventAnalysisId);

		Assertions.assertNotNull(eventAnalysisOptional3.orElse(null));
	}

	@SQLResource(resourcePath = "test_get_event_analyses.sql")
	@Test
	public void testGetEventAnalysesPageSortByCreatedBy() {
		Page<EventAnalysis> eventAnalysisPage =
			_eventAnalysisDog.getEventAnalysisPage(
				1L, "1", 0, 10, Sort.asc("createdByUserName"));

		List<Long> eventAnalysisIds = ListUtil.map(
			eventAnalysisPage.getContent(), EventAnalysis::getId);

		Assertions.assertEquals(1, eventAnalysisIds.size());
		Assertions.assertEquals(2345L, eventAnalysisIds.get(0));
	}

	@SQLResource(resourcePath = "test_get_event_analyses.sql")
	@Test
	public void testGetEventAnalysesPageSortByName() {
		Page<EventAnalysis> eventAnalysisPage =
			_eventAnalysisDog.getEventAnalysisPage(
				1L, "1", 0, 10, Sort.asc("name"));

		List<Long> eventAnalysisIds = ListUtil.map(
			eventAnalysisPage.getContent(), EventAnalysis::getId);

		Assertions.assertEquals(1, eventAnalysisIds.size());
		Assertions.assertEquals(2345L, eventAnalysisIds.get(0));
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis.sql")
	@Test
	public void testGetEventAnalysisAverage() {
		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.AVERAGE, 1L, false, Collections.emptyList(),
				Collections.emptyList(), 246810L, 0, 1,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-15")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());

		BigDecimal value = BigDecimal.valueOf(2);

		Assertions.assertEquals(
			0, value.compareTo((BigDecimal)eventAnalysisResult.getValue()));
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownAverage() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_event_analysis_breakdown_average.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.AVERAGE, 1L, true,
					Collections.singletonList(eventAnalysisBreakdown),
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownBoolean() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"67890", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.BOOLEAN, null, null,
				"testMember", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_event_analysis_breakdown_boolean.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownDayGrouping() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"56789", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.DATE, DateGrouping.DAY, null,
				"testDate", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies" +
					"/expected_event_analysis_breakdown_day_grouping.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownDuration() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_event_analysis_breakdown_duration.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.UNIQUE, 1L, true,
					Collections.singletonList(
						new EventAnalysisBreakdown(
							"34567", AttributeType.EVENT, 2000,
							EventAttributeDefinition.DataType.DURATION, null,
							null, "testCode", "DESC")),
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownMonthGrouping() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"56789", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.DATE, DateGrouping.MONTH,
				null, "testDate", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies" +
					"/expected_event_analysis_breakdown_month_grouping.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownNullValues() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"78901", AttributeType.EVENT, 2000,
				EventAttributeDefinition.DataType.DURATION, null, null,
				"testDuration", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies" +
					"/expected_event_analysis_breakdown_null_values.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownNumber() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_event_analysis_breakdown_number.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					Collections.singletonList(
						new EventAnalysisBreakdown(
							"45678", AttributeType.EVENT, 2,
							EventAttributeDefinition.DataType.NUMBER, null,
							null, "testRating", "DESC")),
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownTotal() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"23456", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testTitle", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown3 =
			new EventAnalysisBreakdown(
				"34567", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testCode", "DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_event_analysis_breakdown_total.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
							add(eventAnalysisBreakdown3);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownUnique() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"34567", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testCode", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"23456", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testTitle", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown3 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_event_analysis_breakdown_unique.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.UNIQUE, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
							add(eventAnalysisBreakdown3);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownWhenDataExceedOnePage() {
		EventAnalysisBreakdown eventAnalysisBreakdown =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"ASC");

		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.TOTAL, 1L, true,
				Collections.singletonList(eventAnalysisBreakdown),
				Collections.emptyList(), 246810L, 0, 2,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2020-05-15")));

		List<BreakdownItem> breakdownItems =
			eventAnalysisResult.getBreakdownItems();

		Assertions.assertEquals(3, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(24L, eventAnalysisResult.getValue());

		BreakdownItem firstBreakdownItem = breakdownItems.get(0);
		BreakdownItem lastBreakdownItem = breakdownItems.get(1);

		Assertions.assertEquals(
			new BigDecimal(3), firstBreakdownItem.getValue());
		Assertions.assertEquals(
			new BigDecimal(4), lastBreakdownItem.getValue());

		eventAnalysisResult = _eventAnalysisDog.getEventAnalysisResult(
			AnalysisType.TOTAL, 1L, true,
			Collections.singletonList(eventAnalysisBreakdown),
			Collections.emptyList(), 246810L, 1, 2,
			TimeRange.of(
				LocalDate.parse("2021-06-01"), LocalDate.parse("2020-05-15")));

		breakdownItems = eventAnalysisResult.getBreakdownItems();

		Assertions.assertEquals(1, eventAnalysisResult.getPage());

		firstBreakdownItem = breakdownItems.get(0);

		Assertions.assertEquals(
			new BigDecimal(8), firstBreakdownItem.getValue());
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownWithDateFilterAfter() {
		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.TOTAL, 1L, true, Collections.emptyList(),
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "gt",
						Collections.singletonList("2021-03-14"))),
				246810L, 0, 10,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-10")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(9L, eventAnalysisResult.getValue());
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownWithDateFilterBefore() {
		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.TOTAL, 1L, true, Collections.emptyList(),
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "lt",
						Collections.singletonList("2021-03-14"))),
				246810L, 0, 10,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-10")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(5L, eventAnalysisResult.getValue());
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownWithDateFilterBetween() {
		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.TOTAL, 1L, true, Collections.emptyList(),
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "between",
						Arrays.asList("2020-01-01", "2021-05-10"))),
				246810L, 0, 10,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-10")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(7L, eventAnalysisResult.getValue());
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownWithDateFilterIs() {
		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.TOTAL, 1L, true, Collections.emptyList(),
				Collections.singletonList(
					new EventAnalysisFilter(
						"56789", AttributeType.EVENT,
						EventAttributeDefinition.DataType.DATE, null,
						"testDate", "eq",
						Collections.singletonList("2021-05-13"))),
				246810L, 0, 10,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-10")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(6L, eventAnalysisResult.getValue());
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownWithFilter() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"ASC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"23456", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testTitle", "ASC");
		EventAnalysisBreakdown eventAnalysisBreakdown3 =
			new EventAnalysisBreakdown(
				"34567", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testCode", "ASC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies" +
					"/expected_event_analysis_breakdown_with_filter.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
							add(eventAnalysisBreakdown3);
						}
					},
					Collections.singletonList(
						new EventAnalysisFilter(
							"12345", AttributeType.EVENT,
							EventAttributeDefinition.DataType.STRING, null,
							"testUrl", "eq",
							Collections.singletonList(
								"https://www.beryl.com/design"))),
					246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownWithFilterAndDifferentSortingType()
		throws Exception {

		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"ASC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"23456", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testTitle", "ASC");
		EventAnalysisBreakdown eventAnalysisBreakdown3 =
			new EventAnalysisBreakdown(
				"34567", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null,
				"testCode", "DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies" +
					"/expected_event_analysis_breakdown_with_filter_and_" +
						"different_sorting_type.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
							add(eventAnalysisBreakdown3);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_breakdown_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_breakdown.sql")
	@Test
	public void testGetEventAnalysisBreakdownYearGrouping() throws Exception {
		EventAnalysisBreakdown eventAnalysisBreakdown1 =
			new EventAnalysisBreakdown(
				"56789", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.DATE, DateGrouping.YEAR, null,
				"testDate", "DESC");
		EventAnalysisBreakdown eventAnalysisBreakdown2 =
			new EventAnalysisBreakdown(
				"12345", AttributeType.EVENT, 0,
				EventAttributeDefinition.DataType.STRING, null, null, "testUrl",
				"DESC");

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies" +
					"/expected_event_analysis_breakdown_year_grouping.json",
				this),
			_objectMapper.convertValue(
				_eventAnalysisDog.getEventAnalysisResult(
					AnalysisType.TOTAL, 1L, true,
					new ArrayList<EventAnalysisBreakdown>() {
						{
							add(eventAnalysisBreakdown1);
							add(eventAnalysisBreakdown2);
						}
					},
					Collections.emptyList(), 246810L, 0, 10,
					TimeRange.of(
						LocalDate.parse("2021-06-01"),
						LocalDate.parse("2021-05-15"))),
				JSONObject.class),
			true);
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_with_filter_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_with_filter.sql")
	@Test
	public void testGetEventAnalysisMultipleFilters() {
		EventAnalysisFilter eventAnalysisFilter1 = new EventAnalysisFilter(
			"12345", AttributeType.EVENT,
			EventAttributeDefinition.DataType.STRING, null, "Test Attribute 1",
			"contains", Collections.singletonList("test"));
		EventAnalysisFilter eventAnalysisFilter2 = new EventAnalysisFilter(
			"12345", AttributeType.EVENT,
			EventAttributeDefinition.DataType.STRING, null, "Test Attribute 1",
			"contains", Collections.singletonList("This"));
		EventAnalysisFilter eventAnalysisFilter3 = new EventAnalysisFilter(
			"23456", AttributeType.EVENT,
			EventAttributeDefinition.DataType.NUMBER, null, "Test Attribute 2",
			"between",
			new ArrayList<String>() {
				{
					add("200");
					add("300");
				}
			});

		List<EventAnalysisFilter> eventAnalysisFilters =
			new ArrayList<EventAnalysisFilter>() {
				{
					add(eventAnalysisFilter1);
					add(eventAnalysisFilter2);
					add(eventAnalysisFilter3);
				}
			};

		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.UNIQUE, 1L, false, Collections.emptyList(),
				eventAnalysisFilters, 246810L, 0, 1,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-18")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(1L, eventAnalysisResult.getValue());
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_with_filter_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis_with_filter.sql")
	@Test
	public void testGetEventAnalysisSingleFilter() {
		EventAnalysisFilter eventAnalysisFilter = new EventAnalysisFilter(
			"12345", AttributeType.EVENT,
			EventAttributeDefinition.DataType.STRING, null, "Test Attribute 1",
			"contains", Collections.singletonList("should"));

		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.AVERAGE, 1L, false, Collections.emptyList(),
				Collections.singletonList(eventAnalysisFilter), 246810L, 0, 1,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-19")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());

		BigDecimal value = BigDecimal.valueOf(1);

		Assertions.assertEquals(
			0, value.compareTo((BigDecimal)eventAnalysisResult.getValue()));
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis.sql")
	@Test
	public void testGetEventAnalysisTotal() {
		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.TOTAL, 2L, false, Collections.emptyList(),
				Collections.emptyList(), 246810L, 0, 1,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-15")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(7L, eventAnalysisResult.getValue());
	}

	@BQSQLResource(resourcePath = "test_get_event_analysis_bq.sql")
	@SQLResource(resourcePath = "test_get_event_analysis.sql")
	@Test
	public void testGetEventAnalysisUnique() {
		EventAnalysisResult eventAnalysisResult =
			_eventAnalysisDog.getEventAnalysisResult(
				AnalysisType.UNIQUE, 1L, false, Collections.emptyList(),
				Collections.emptyList(), 246810L, 0, 1,
				TimeRange.of(
					LocalDate.parse("2021-06-01"),
					LocalDate.parse("2021-05-15")));

		Assertions.assertEquals(1, eventAnalysisResult.getCount());
		Assertions.assertEquals(0, eventAnalysisResult.getPage());
		Assertions.assertEquals(10L, eventAnalysisResult.getValue());
	}

	private EventAnalysis _addEventAnalysis(Long channelId, String name) {
		return _eventAnalysisDog.addEventAnalysis(
			AnalysisType.TOTAL, channelId, false, Collections.emptyList(),
			Collections.emptyList(), 246810L, name,
			LocalDate.parse("2021-06-01"), null, LocalDate.parse("2021-05-15"),
			100L, "Test");
	}

	private EventAnalysis _updateEventAnalysis(
		EventAnalysis eventAnalysis, String name) {

		return _eventAnalysisDog.updateEventAnalysis(
			AnalysisType.valueOf(eventAnalysis.getEventAnalysisType()),
			eventAnalysis.getChannelId(), eventAnalysis.getCompareToPrevious(),
			Collections.emptyList(), Collections.emptyList(),
			eventAnalysis.getId(), eventAnalysis.getEventDefinitionId(), name,
			LocalDate.parse("2021-06-01"), null, LocalDate.parse("2021-05-15"),
			eventAnalysis.getModifiedByUserId(),
			eventAnalysis.getModifiedByUserName());
	}

	@Autowired
	private EventAnalysisDog _eventAnalysisDog;

	@Autowired
	private EventAnalysisRepository _eventAnalysisRepository;

	@Autowired
	private ObjectMapper _objectMapper;

}