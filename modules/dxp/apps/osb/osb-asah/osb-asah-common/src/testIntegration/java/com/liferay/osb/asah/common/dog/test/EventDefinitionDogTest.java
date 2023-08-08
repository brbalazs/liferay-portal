/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * @author Leslie Wong
 */
public class EventDefinitionDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testAddDefinition() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent", EventDefinition.Type.CUSTOM, null);

		Assertions.assertNotNull(eventDefinition);
		Assertions.assertEquals(
			"Testing an event", eventDefinition.getDescription());
		Assertions.assertEquals("Test Event", eventDefinition.getDisplayName());
		Assertions.assertNotNull(eventDefinition.getId());
		Assertions.assertEquals("testEvent", eventDefinition.getName());
		Assertions.assertEquals(
			EventDefinition.Type.CUSTOM, eventDefinition.getType());
		Assertions.assertFalse(eventDefinition.isBlocked());
	}

	@Test
	public void testAddDefinitionDuplicateDisplayName() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent1", EventDefinition.Type.CUSTOM, null);

		Assertions.assertEquals(
			"Test Event", eventDefinition1.getDisplayName());

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent2", EventDefinition.Type.CUSTOM, null);

		Assertions.assertEquals(
			"Test Event (1)", eventDefinition2.getDisplayName());

		EventDefinition eventDefinition3 =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent3", EventDefinition.Type.CUSTOM, null);

		Assertions.assertEquals(
			"Test Event (2)", eventDefinition3.getDisplayName());
	}

	@SQLResource(resourcePath = "test_add_definition_limit_reached.sql")
	@Test
	public void testAddDefinitionLimitReached() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event 1", null,
				"testEvent1", EventDefinition.Type.CUSTOM, null);

		Assertions.assertNotNull(eventDefinition);
		Assertions.assertEquals(
			"Testing an event", eventDefinition.getDescription());
		Assertions.assertEquals(
			"Test Event 1", eventDefinition.getDisplayName());
		Assertions.assertNotNull(eventDefinition.getId());
		Assertions.assertEquals("testEvent1", eventDefinition.getName());
		Assertions.assertEquals(
			EventDefinition.Type.CUSTOM, eventDefinition.getType());
		Assertions.assertFalse(eventDefinition.isBlocked());

		Date dayDate = DateUtil.newDayDate();
		String url = "http://localhost:8080";

		eventDefinition = _eventDefinitionDog.addEventDefinition(
			"CustomEvent", "Testing an event", "Test Event 2", dayDate,
			"testEvent2", EventDefinition.Type.CUSTOM, url);

		Assertions.assertNotNull(eventDefinition);
		Assertions.assertEquals(
			"Testing an event", eventDefinition.getDescription());
		Assertions.assertEquals(
			"Test Event 2", eventDefinition.getDisplayName());
		Assertions.assertNotNull(eventDefinition.getId());
		Assertions.assertEquals("testEvent2", eventDefinition.getName());
		Assertions.assertEquals(
			EventDefinition.Type.CUSTOM, eventDefinition.getType());
		Assertions.assertTrue(eventDefinition.isBlocked());
		Assertions.assertEquals(
			dayDate, eventDefinition.getBlockedLastSeenDate());
		Assertions.assertEquals(url, eventDefinition.getBlockedLastSeenURL());
	}

	@Test
	public void testAddDefinitionNoDisplayName1() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", null, null, "testEvent",
				EventDefinition.Type.CUSTOM, null);

		Assertions.assertNotNull(eventDefinition);
		Assertions.assertEquals(
			"Testing an event", eventDefinition.getDescription());
		Assertions.assertEquals("testEvent", eventDefinition.getDisplayName());
		Assertions.assertNotNull(eventDefinition.getId());
		Assertions.assertEquals("testEvent", eventDefinition.getName());
		Assertions.assertEquals(
			EventDefinition.Type.CUSTOM, eventDefinition.getType());
		Assertions.assertFalse(eventDefinition.isBlocked());
	}

	@Test
	public void testAddDefinitionNoDisplayName2() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "", null, "testEvent",
				EventDefinition.Type.CUSTOM, null);

		Assertions.assertNotNull(eventDefinition);
		Assertions.assertEquals(
			"Testing an event", eventDefinition.getDescription());
		Assertions.assertEquals("testEvent", eventDefinition.getDisplayName());
		Assertions.assertNotNull(eventDefinition.getId());
		Assertions.assertEquals("testEvent", eventDefinition.getName());
		Assertions.assertEquals(
			EventDefinition.Type.CUSTOM, eventDefinition.getType());
		Assertions.assertFalse(eventDefinition.isBlocked());
	}

	@Test
	public void testAddDefinitionNoDisplayName3() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", null, null, "testEvent",
				EventDefinition.Type.CUSTOM, null);

		Assertions.assertEquals("testEvent", eventDefinition1.getDisplayName());

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", null, null, "TestEvent",
				EventDefinition.Type.CUSTOM, null);

		Assertions.assertEquals(
			"TestEvent (1)", eventDefinition2.getDisplayName());
	}

	@BQSQLResource(resourcePath = "test_block_event_definition_bq.sql")
	@SQLResource(resourcePath = "test_block_event_definition.sql")
	@Test
	public void testBlockEventDefinition() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("subscribed");

		Assertions.assertNotNull(eventDefinition);

		String expectedDescription = eventDefinition.getDescription();
		String expectedDisplayName = eventDefinition.getDisplayName();

		_eventDefinitionDog.blockEventDefinitions(
			Collections.singletonList(eventDefinition.getId()));

		eventDefinition = _eventDefinitionDog.fetchEventDefinitionByName(
			"subscribed");

		Assertions.assertEquals(
			expectedDescription, eventDefinition.getDescription());
		Assertions.assertEquals(
			expectedDisplayName, eventDefinition.getDisplayName());

		ZonedDateTime zonedDateTime = ZonedDateTime.of(
			2021, 4, 19, 12, 35, 0, 0, ZoneOffset.UTC);

		Assertions.assertEquals(
			Date.from(zonedDateTime.toInstant()),
			eventDefinition.getBlockedLastSeenDate());

		Assertions.assertEquals(
			"http://localhost:8089/web/guest/home",
			eventDefinition.getBlockedLastSeenURL());
	}

	@BQSQLResource(resourcePath = "test_block_event_definitions_bq.sql")
	@SQLResource(resourcePath = "test_block_event_definitions.sql")
	@Test
	public void testBlockEventDefinitions() {
		Page<EventDefinition> eventDefinitions =
			_eventDefinitionDog.getEventDefinitionPage(
				false, null, null, 0, 3, Sort.asc("name"),
				EventDefinition.Type.CUSTOM);

		List<Long> eventDefinitionIds = ListUtil.map(
			eventDefinitions.getContent(), EventDefinition::getId);

		_eventDefinitionDog.blockEventDefinitions(eventDefinitionIds);

		ZonedDateTime addNotificationZonedDateTime = ZonedDateTime.of(
			2021, 4, 19, 0, 0, 0, 0, ZoneOffset.UTC);
		ZonedDateTime subscribedZonedDateTime = ZonedDateTime.of(
			2021, 3, 29, 0, 0, 0, 0, ZoneOffset.UTC);
		ZonedDateTime unsubscribedZonedDateTime = ZonedDateTime.of(
			2021, 2, 16, 0, 0, 0, 0, ZoneOffset.UTC);

		Map<String, Tuple2> expectedBlockedEventInfos =
			new HashMap<String, Tuple2>() {
				{
					put(
						"addNotification",
						Tuples.of(
							Date.from(addNotificationZonedDateTime.toInstant()),
							"http://localhost:8089/web/guest/home"));
					put(
						"subscribed",
						Tuples.of(
							Date.from(subscribedZonedDateTime.toInstant()),
							"http://localhost:80/web/guest/home"));
					put(
						"unsubscribed",
						Tuples.of(
							Date.from(unsubscribedZonedDateTime.toInstant()),
							"http://localhost:8087/web/guest/home"));
				}
			};

		for (Long eventDefinitionId : eventDefinitionIds) {
			EventDefinition eventDefinition =
				_eventDefinitionDog.getEventDefinition(eventDefinitionId);

			Tuple2 tuple2 = expectedBlockedEventInfos.get(
				eventDefinition.getName());

			Assertions.assertEquals(
				tuple2.getT1(), eventDefinition.getBlockedLastSeenDate());

			Assertions.assertEquals(
				tuple2.getT2(), eventDefinition.getBlockedLastSeenURL());
		}
	}

	@Test
	public void testCountEventDefinitions() {
		long count = _eventDefinitionDog.countEventDefinitions(
			false, null, null, EventDefinition.Type.DEFAULT);

		Assertions.assertEquals(27, count);

		count = _eventDefinitionDog.countEventDefinitions(
			false, null, null, EventDefinition.Type.CUSTOM);

		Assertions.assertEquals(0, count);
	}

	@Test
	public void testCountEventDefinitionsWithKeyword() {
		Assertions.assertEquals(
			5,
			_eventDefinitionDog.countEventDefinitions(
				false, null, "page", EventDefinition.Type.DEFAULT));
	}

	@Test
	public void testFetchEventDefinitionByDisplayName() {
		EventDefinition expectedEventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent", EventDefinition.Type.CUSTOM, null);

		Assertions.assertEquals(
			expectedEventDefinition,
			_eventDefinitionDog.fetchEventDefinitionByDisplayName(
				"Test Event"));
	}

	@Test
	public void testFetchEventDefinitionByDisplayNameIgnoreCase() {
		EventDefinition expectedEventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent", EventDefinition.Type.CUSTOM, null);

		Assertions.assertEquals(
			expectedEventDefinition,
			_eventDefinitionDog.fetchEventDefinitionByDisplayName(
				"test event"));
	}

	@Test
	public void testFetchEventDefinitionByDisplayNameNonexistent() {
		Assertions.assertNull(
			_eventDefinitionDog.fetchEventDefinitionByDisplayName(
				"Does Not Exist"));
	}

	@Test
	public void testFetchEventDefinitionByName() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent", EventDefinition.Type.CUSTOM, null);

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.fetchEventDefinitionByName("testEvent");

		Assertions.assertEquals(eventDefinition1, eventDefinition2);
	}

	@Test
	public void testGetDisplayedEventDefinitions() {
		_assertEventDefinitions(
			_eventDefinitionDog.getEventDefinitionPage(
				false, false, null, 0, 20, Sort.asc("name"),
				EventDefinition.Type.DEFAULT),
			new ArrayList<String>() {
				{
					add("assetClicked");
					add("assetDownloaded");
					add("assetSubmitted");
					add("assetViewed");
					add("blogClicked");
					add("blogViewed");
					add("ctaClicked");
					add("documentDownloaded");
					add("documentPreviewed");
					add("formSubmitted");
					add("formViewed");
					add("pageRead");
					add("pageViewed");
					add("webContentClicked");
					add("webContentViewed");
				}
			});
	}

	@Test
	public void testGetEventDefinition() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent", EventDefinition.Type.CUSTOM, null);

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.getEventDefinition(eventDefinition1.getId());

		Assertions.assertEquals(eventDefinition1, eventDefinition2);
	}

	@Test
	public void testGetEventDefinitions() {
		_assertEventDefinitions(
			_eventDefinitionDog.getEventDefinitionPage(
				false, null, null, 0, 5, Sort.asc("name"),
				EventDefinition.Type.DEFAULT),
			new ArrayList<String>() {
				{
					add("assetClicked");
					add("assetDownloaded");
					add("assetDepthReached");
					add("assetSubmitted");
					add("assetViewed");
				}
			});
	}

	@SQLResource(resourcePath = "test_get_event_definitions_with_keyword.sql")
	@Test
	public void testGetEventDefinitionsWithKeywordMatchDescription() {
		_assertEventDefinitions(
			_eventDefinitionDog.getEventDefinitionPage(
				false, null, "item", 0, 5, Sort.asc("name"),
				EventDefinition.Type.CUSTOM),
			new ArrayList<String>() {
				{
					add("addedToCart");
					add("removedFromCart");
				}
			});
	}

	@SQLResource(resourcePath = "test_get_event_definitions_with_keyword.sql")
	@Test
	public void testGetEventDefinitionsWithKeywordMatchDisplayName() {
		_assertEventDefinitions(
			_eventDefinitionDog.getEventDefinitionPage(
				false, null, "Shopping", 0, 5, Sort.asc("name"),
				EventDefinition.Type.CUSTOM),
			new ArrayList<String>() {
				{
					add("addedToCart");
					add("removedFromCart");
				}
			});
	}

	@SQLResource(resourcePath = "test_get_event_definitions_with_keyword.sql")
	@Test
	public void testGetEventDefinitionsWithKeywordMatchName() {
		_assertEventDefinitions(
			_eventDefinitionDog.getEventDefinitionPage(
				false, null, "applied", 0, 5, Sort.asc("name"),
				EventDefinition.Type.CUSTOM),
			Collections.singletonList("codeApplied"));
	}

	@Test
	public void testGetHiddenEventDefinitions() {
		_assertEventDefinitions(
			_eventDefinitionDog.getEventDefinitionPage(
				false, true, null, 0, 20, Sort.asc("name"),
				EventDefinition.Type.DEFAULT),
			new ArrayList<String>() {
				{
					add("VOTE");
					add("assetDepthReached");
					add("blogDepthReached");
					add("fieldBlurred");
					add("fieldFocused");
					add("pageDepthReached");
					add("pageLoaded");
					add("pageUnloaded");
					add("posted");
					add("shared");
					add("tabBlurred");
					add("tabFocused");
				}
			});
	}

	@Test
	public void testHideEventDefinitions() {
		EventDefinition assetClickedEventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("assetClicked");
		EventDefinition assetDownloadedEventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("assetDownloaded");

		_eventDefinitionDog.hideEventDefinitions(
			Arrays.asList(
				assetClickedEventDefinition.getId(),
				assetDownloadedEventDefinition.getId()));

		assetClickedEventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("assetClicked");

		Assertions.assertTrue(assetClickedEventDefinition.isHidden());

		assetClickedEventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("assetDownloaded");

		Assertions.assertTrue(assetClickedEventDefinition.isHidden());
	}

	@SQLResource(resourcePath = "test_unblock_event_definition.sql")
	@Test
	public void testUnblockEventDefinition() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("subscribed");

		Long eventDefinitionId = eventDefinition.getId();

		_eventDefinitionDog.unblockEventDefinitions(
			Collections.singletonList(eventDefinitionId));

		eventDefinition = _eventDefinitionDog.getEventDefinition(
			eventDefinitionId);

		Assertions.assertFalse(eventDefinition.isBlocked());
		Assertions.assertNull(eventDefinition.getBlockedLastSeenDate());
		Assertions.assertNull(eventDefinition.getBlockedLastSeenURL());
		Assertions.assertNull(eventDefinition.getDescription());
		Assertions.assertEquals("subscribed", eventDefinition.getDisplayName());
	}

	@SQLResource(
		resourcePath = "test_unblock_event_definition_limit_overflow.sql"
	)
	@Test
	public void testUnblockEventDefinitionLimitOverflow() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("subscribed100");

		Long eventDefinitionId = eventDefinition.getId();

		Assertions.assertThrows(
			OSBAsahException.class,
			() -> _eventDefinitionDog.unblockEventDefinitions(
				Collections.singletonList(eventDefinitionId)));
	}

	@SQLResource(resourcePath = "test_unblock_event_definitions.sql")
	@Test
	public void testUnblockEventDefinitions() {
		List<Long> eventDefinitionIds = ListUtil.map(
			Arrays.asList(
				"addedToCart", "addedToWishList", "checkedOut",
				"orderCancelled"),
			name -> {
				EventDefinition eventDefinition =
					_eventDefinitionDog.fetchEventDefinitionByName(name);

				return eventDefinition.getId();
			});

		_eventDefinitionDog.unblockEventDefinitions(eventDefinitionIds);

		for (Long eventDefinitionId : eventDefinitionIds) {
			EventDefinition eventDefinition =
				_eventDefinitionDog.getEventDefinition(eventDefinitionId);

			Assertions.assertFalse(eventDefinition.isBlocked());
			Assertions.assertNull(eventDefinition.getBlockedLastSeenDate());
			Assertions.assertNull(eventDefinition.getBlockedLastSeenURL());
			Assertions.assertNull(eventDefinition.getDescription());
			Assertions.assertEquals(
				eventDefinition.getName(), eventDefinition.getDisplayName());
		}

		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("reviewAdded");

		Assertions.assertTrue(eventDefinition.isBlocked());
		Assertions.assertNotNull(eventDefinition.getBlockedLastSeenDate());
		Assertions.assertNotNull(eventDefinition.getBlockedLastSeenURL());
		Assertions.assertNull(eventDefinition.getDescription());
		Assertions.assertNull(eventDefinition.getDisplayName());
	}

	@SQLResource(
		resourcePath = "test_unblock_event_definitions_limit_overflow.sql"
	)
	@Test
	public void testUnblockEventDefinitionsLimitOverflow() {
		Page<EventDefinition> eventDefinitions =
			_eventDefinitionDog.getEventDefinitionPage(
				true, null, null, 0, 5, Sort.asc("name"),
				EventDefinition.Type.CUSTOM);

		Assertions.assertThrows(
			OSBAsahException.class,
			() -> _eventDefinitionDog.unblockEventDefinitions(
				ListUtil.map(
					eventDefinitions.getContent(), EventDefinition::getId)));
	}

	@Test
	public void testUnhideEventDefinitions() {
		EventDefinition assetClickedEventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("assetClicked");

		Assertions.assertFalse(assetClickedEventDefinition.isHidden());

		_eventDefinitionDog.hideEventDefinitions(
			Arrays.asList(assetClickedEventDefinition.getId()));

		assetClickedEventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("assetClicked");

		Assertions.assertTrue(assetClickedEventDefinition.isHidden());

		_eventDefinitionDog.unhideEventDefinitions(
			Arrays.asList(assetClickedEventDefinition.getId()));

		assetClickedEventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("assetClicked");

		Assertions.assertFalse(assetClickedEventDefinition.isHidden());
	}

	@Test
	public void testUpdateEventDefinitionBlockedEventDefinition() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		eventDefinition1.setBlockedLastSeenDate(DateUtil.newDayDate());
		eventDefinition1.setBlockedLastSeenURL("testUrl");

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.updateEventDefinition(
				eventDefinition1.getBlockedLastSeenDate(),
				eventDefinition1.getBlockedLastSeenURL(), null, null,
				eventDefinition1.getId());

		Assertions.assertEquals(eventDefinition1, eventDefinition2);
	}

	@Test
	public void testUpdateEventDefinitionDescription() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		String newDescription = RandomTestUtil.randomString();

		eventDefinition1.setDescription(newDescription);

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.updateEventDefinition(
				null, null, newDescription, null, eventDefinition1.getId());

		Assertions.assertEquals(eventDefinition1, eventDefinition2);
	}

	@Test
	public void testUpdateEventDefinitionDescriptionNull() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		String newDescription = RandomTestUtil.randomString();

		eventDefinition1.setDescription(newDescription);

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.updateEventDefinition(
				null, null, newDescription, null, eventDefinition1.getId());

		Assertions.assertEquals(eventDefinition1, eventDefinition2);

		newDescription = "";

		eventDefinition1.setDescription(newDescription);

		eventDefinition2 = _eventDefinitionDog.updateEventDefinition(
			null, null, newDescription, null, eventDefinition1.getId());

		Assertions.assertEquals(eventDefinition1, eventDefinition2);
	}

	@Test
	public void testUpdateEventDefinitionDisplayName() {
		EventDefinition eventDefinition1 =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		String newDisplayName = RandomTestUtil.randomString();

		eventDefinition1.setDisplayName(newDisplayName);

		EventDefinition eventDefinition2 =
			_eventDefinitionDog.updateEventDefinition(
				null, null, null, newDisplayName, eventDefinition1.getId());

		Assertions.assertEquals(eventDefinition1, eventDefinition2);
	}

	private void _assertEventDefinitions(
		Page<EventDefinition> actualEventDefinitionPage,
		List<String> expectedEventDefinitionNames) {

		Assertions.assertEquals(
			expectedEventDefinitionNames.size(),
			actualEventDefinitionPage.getNumberOfElements(),
			actualEventDefinitionPage.toString());

		for (EventDefinition actualEventDefinition :
				actualEventDefinitionPage) {

			Assertions.assertTrue(
				expectedEventDefinitionNames.contains(
					actualEventDefinition.getName()));
		}
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}