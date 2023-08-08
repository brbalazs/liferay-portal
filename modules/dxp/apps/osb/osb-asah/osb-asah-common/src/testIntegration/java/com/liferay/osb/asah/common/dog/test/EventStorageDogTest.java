/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.dog.EventAttributeDefinitionDog;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.dog.EventStorageDog;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.entity.EventDefinitionEventAttributeDefinition;
import com.liferay.osb.asah.common.model.AnalyticsEvent;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Leslie Wong
 */
@Import(JDBCTestConfiguration.class)
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		MockitoTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class,
		ResetMocksTestExecutionListener.class
	}
)
public class EventStorageDogTest implements OSBAsahCommonSpringTestContext {

	@BeforeEach
	public void setUp() {
		_channel = _channelDog.addChannel("Test Channel");
	}

	@SQLResource(resourcePath = "test_store_blocked_event.sql")
	@Test
	public void testStoreBlockedEvent() {
		long expectedEventAttributeDefinitionCount =
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL);
		long expectedEventDefinitionCount =
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM);

		_storeAnalyticsEventDefinition(
			"blockedEvent",
			Collections.singletonMap("canonicalUrl", "http://127.0.0.1"),
			Collections.emptyMap());

		Assertions.assertEquals(
			expectedEventDefinitionCount,
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM),
			0.0);
		Assertions.assertEquals(
			expectedEventAttributeDefinitionCount,
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL),
			0.0);
	}

	@SQLResource(resourcePath = "test_store_definition_limit_reached.sql")
	@Test
	public void testStoreEventDefinitionLimitReached() {
		long expectedEventAttributeDefinitionCount =
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL);
		long expectedEventDefinitionCount =
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM);

		_storeAnalyticsEventDefinition(
			"newEventDefinition",
			Collections.singletonMap("canonicalUrl", "http://127.0.0.1"),
			Collections.emptyMap());

		Assertions.assertEquals(
			expectedEventDefinitionCount,
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM),
			0.0);
		Assertions.assertEquals(
			expectedEventAttributeDefinitionCount,
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL),
			0.0);
	}

	@Test
	public void testStoreExistingDefinitionNewAttributeDefinition() {
		long eventAttributeDefinitionCount =
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL);

		long expectedEventAttributeDefinitionCount =
			eventAttributeDefinitionCount + 1;

		long expectedEventDefinitionCount =
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM);

		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		_storeAnalyticsEventDefinition(
			"pageViewed",
			Collections.singletonMap("canonicalUrl", "http://127.0.0.1"),
			Collections.singletonMap("newTestEventAttribute", "testValue"));

		Assertions.assertEquals(
			expectedEventAttributeDefinitionCount,
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL),
			0.0);
		Assertions.assertEquals(
			expectedEventDefinitionCount,
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM),
			0.0);
		Assertions.assertEquals(
			Collections.singleton(eventDefinition.getId()),
			_getEventDefinitionIds("newTestEventAttribute"));
	}

	@Test
	public void testStoreExistingDefinitionNewAttributeDefinitionReference() {
		long expectedEventAttributeDefinitionCount =
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL);
		long expectedEventDefinitionCount =
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM);

		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		Set<Long> expectedEventDefinitionIds = _getEventDefinitionIds(
			"category");

		expectedEventDefinitionIds.add(eventDefinition.getId());

		_storeAnalyticsEventDefinition(
			"pageViewed",
			Collections.singletonMap("canonicalUrl", "http://127.0.0.1"),
			Collections.singletonMap("category", "testValue"));

		Assertions.assertEquals(
			expectedEventAttributeDefinitionCount,
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL),
			0.0);
		Assertions.assertEquals(
			expectedEventDefinitionCount,
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM),
			0.0);
		Assertions.assertEquals(
			expectedEventDefinitionIds, _getEventDefinitionIds("category"));
	}

	@Test
	public void testStoreExistingEventDefinition() {
		long expectedEventAttributeDefinitionCount =
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL);
		long expectedEventDefinitionCount =
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM);

		_storeAnalyticsEventDefinition(
			"pageViewed",
			Collections.singletonMap("canonicalUrl", "http://127.0.0.1"),
			Collections.singletonMap("title", "My First Site"));

		Assertions.assertEquals(
			expectedEventAttributeDefinitionCount,
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL),
			0.0);
		Assertions.assertEquals(
			expectedEventDefinitionCount,
			_eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM),
			0.0);
	}

	private Set<Long> _getEventDefinitionIds(
		String eventAttributeDefinitionName) {

		EventAttributeDefinition eventAttributeDefinition =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				eventAttributeDefinitionName);

		Set<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions =
				eventAttributeDefinition.
					getEventDefinitionEventAttributeDefinitions();

		Stream<EventDefinitionEventAttributeDefinition> stream =
			eventDefinitionEventAttributeDefinitions.stream();

		return stream.map(
			EventDefinitionEventAttributeDefinition::getEventDefinitionId
		).collect(
			Collectors.toSet()
		);
	}

	private void _storeAnalyticsEventDefinition(
		String eventId, Map<String, String> eventContext,
		Map<String, String> eventProperties) {

		AnalyticsEvent analyticsEvent = new AnalyticsEvent();

		analyticsEvent.setApplicationId("Page");
		analyticsEvent.setChannelId(String.valueOf(_channel.getId()));
		analyticsEvent.setCreateDate(DateUtil.newDayDate());
		analyticsEvent.setContext(eventContext);
		analyticsEvent.setDataSourceId("1");
		analyticsEvent.setEventId(eventId);
		analyticsEvent.setEventProperties(eventProperties);
		analyticsEvent.setId("1");
		analyticsEvent.setIndividualId("1");
		analyticsEvent.setKnownIndividual(true);
		analyticsEvent.setProjectId("123456789");
		analyticsEvent.setSegmentNames(Collections.emptySet());
		analyticsEvent.setUserId(RandomTestUtil.randomUUID());

		_eventStorageDog.storeEventDefinition(analyticsEvent);
	}

	private Channel _channel;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

	@Autowired
	private EventStorageDog _eventStorageDog;

}