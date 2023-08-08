/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.EventAnalysisDTO;
import com.liferay.osb.asah.backend.graphql.schema.CreateEventAnalysisMutationDataFetcher;
import com.liferay.osb.asah.backend.graphql.schema.DeleteEventAnalysesMutationDataFetcher;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.model.AttributeType;
import com.liferay.osb.asah.common.model.DateGrouping;
import com.liferay.osb.asah.common.repository.EventAnalysisRepository;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rachael Koestartyo
 */
public class DeleteEventAnalysesMutationDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		_eventDefinition = _eventDefinitionRepository.save(
			_createEventDefinition(10));
	}

	@AfterEach
	public void tearDown() {
		_eventAnalysisRepository.deleteAll();

		_eventDefinitionRepository.delete(_eventDefinition);
	}

	@Test
	public void testGet() {
		EventAnalysisDTO eventAnalysisDTO =
			_createEventAnalysisMutationDataFetcher.get(
				_getDataFetchingEnvironment());

		Assertions.assertEquals(1, _eventAnalysisRepository.count());

		_deleteEventAnalysesMutationDataFetcher.get(
			_getDeleteDataFetchingEnvironment(eventAnalysisDTO.getId()));

		Assertions.assertEquals(0, _eventAnalysisRepository.count());
	}

	private EventDefinition _createEventDefinition(int index) {
		EventDefinition eventDefinition = new EventDefinition();

		eventDefinition.setBlocked(false);
		eventDefinition.setHidden(false);
		eventDefinition.setName("Event Definition " + index);
		eventDefinition.setType(EventDefinition.Type.CUSTOM);

		return eventDefinition;
	}

	private DataFetchingEnvironment _getDataFetchingEnvironment() {
		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		Map<String, Object> arguments = new HashMap<>();

		arguments.put("analysisType", "TOTAL");
		arguments.put("channelId", "1");
		arguments.put("compareToPrevious", Boolean.FALSE);
		arguments.put(
			"eventAnalysisBreakdowns",
			Collections.singletonList(
				new HashMap<String, Object>() {
					{
						put("attributeId", "100");
						put("attributeType", AttributeType.EVENT);
						put("binSize", 10);
						put("dataType", "STRING");
						put("dateGrouping", DateGrouping.MONTH);
						put("sortType", "ASC");
					}
				}));
		arguments.put(
			"eventAnalysisFilters",
			Collections.singletonList(
				new HashMap<String, Object>() {
					{
						put("attributeId", "100");
						put("attributeType", AttributeType.EVENT);
						put("dataType", "STRING");
						put("operator", "eq");
						put("values", Arrays.asList("one", "two", "three"));
					}
				}));
		arguments.put(
			"eventDefinitionId", String.valueOf(_eventDefinition.getId()));
		arguments.put("name", "Analysis 1");
		arguments.put("rangeKey", 1);
		arguments.put("userId", "20");
		arguments.put("userName", "Test");

		dataFetchingEnvironmentBuilder.arguments(arguments);

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		return dataFetchingEnvironmentBuilder.build();
	}

	private DataFetchingEnvironment _getDeleteDataFetchingEnvironment(
		String eventAnalysisId) {

		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		Map<String, Object> arguments = new HashMap<>();

		arguments.put(
			"eventAnalysisIds", Collections.singletonList(eventAnalysisId));

		dataFetchingEnvironmentBuilder.arguments(arguments);

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		return dataFetchingEnvironmentBuilder.build();
	}

	@Autowired
	private CreateEventAnalysisMutationDataFetcher
		_createEventAnalysisMutationDataFetcher;

	@Autowired
	private DeleteEventAnalysesMutationDataFetcher
		_deleteEventAnalysesMutationDataFetcher;

	@Autowired
	private EventAnalysisRepository _eventAnalysisRepository;

	private EventDefinition _eventDefinition;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

}