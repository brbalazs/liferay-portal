/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.graphql.schema.CustomEventLimitReachedDataFetcher;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.IterableUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcos Martins
 */
public class CustomEventLimitReachedDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		List<EventDefinition> eventDefinitions = IntStream.range(
			1, 100
		).mapToObj(
			this::_createEventDefinition
		).collect(
			Collectors.toList()
		);

		eventDefinitions.add(
			_createBlockedEventDefinition(
				100, EventDefinition.BlockedReasonType.BLOCKED_BY_USER));
		eventDefinitions.add(
			_createBlockedEventDefinition(
				101, EventDefinition.BlockedReasonType.BLOCKED_BY_USER));

		_eventDefinitions = IterableUtils.toList(
			_eventDefinitionRepository.saveAll(eventDefinitions));
	}

	@AfterEach
	public void tearDown() {
		_eventDefinitionRepository.deleteAll(_eventDefinitions);
	}

	@Test
	public void testGetThresholdNotReached() {
		Assertions.assertFalse(
			_customEventLimitReachedNotificationDataFetcher.get(
				_getDataFetchingEnvironment()));
	}

	@Test
	public void testGetThresholdReachedWithBlockedOverflow() {
		List<EventDefinition> eventDefinitions = Arrays.asList(
			_createEventDefinition(102),
			_createBlockedEventDefinition(
				103, EventDefinition.BlockedReasonType.THRESHOLD_OVERFLOW));

		_eventDefinitions.addAll(
			IterableUtils.toList(
				_eventDefinitionRepository.saveAll(eventDefinitions)));

		Assertions.assertTrue(
			_customEventLimitReachedNotificationDataFetcher.get(
				_getDataFetchingEnvironment()));
	}

	@Test
	public void testGetThresholdReachedWithNoBlockedOverflow() {
		EventDefinition eventDefinition = _createEventDefinition(102);

		_eventDefinitions.add(_eventDefinitionRepository.save(eventDefinition));

		Assertions.assertFalse(
			_customEventLimitReachedNotificationDataFetcher.get(
				_getDataFetchingEnvironment()));
	}

	private EventDefinition _createBlockedEventDefinition(
		int index, EventDefinition.BlockedReasonType blockedReasonType) {

		EventDefinition eventDefinition = _createEventDefinition(index);

		eventDefinition.setBlocked(true);
		eventDefinition.setBlockedLastSeenDate(new Date());
		eventDefinition.setBlockedLastSeenURL("http://text.com");
		eventDefinition.setBlockedReasonType(blockedReasonType);

		return eventDefinition;
	}

	private EventDefinition _createEventDefinition(int index) {
		EventDefinition eventDefinition = new EventDefinition();

		eventDefinition.setBlocked(false);
		eventDefinition.setHidden(false);
		eventDefinition.setName("customEventDefinitionReached" + index);
		eventDefinition.setType(EventDefinition.Type.CUSTOM);

		return eventDefinition;
	}

	private DataFetchingEnvironment _getDataFetchingEnvironment() {
		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		return dataFetchingEnvironmentBuilder.build();
	}

	@Autowired
	private CustomEventLimitReachedDataFetcher
		_customEventLimitReachedNotificationDataFetcher;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

	private List<EventDefinition> _eventDefinitions;

}