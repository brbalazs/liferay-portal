/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.EventAttributeDefinitionDog;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.entity.EventDefinitionEventAttributeDefinition;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Leslie Wong
 */
public class EventAttributeDefinitionDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testAddEventAttributeDefinition() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		EventAttributeDefinition eventAttributeDefinition =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition", "Test Attribute Definition",
				eventDefinition.getId(), "testAttributeDefinition",
				"testValue");

		Assertions.assertEquals(
			EventAttributeDefinition.DataType.STRING,
			eventAttributeDefinition.getDataType());
		Assertions.assertEquals(
			"Testing attribute definition",
			eventAttributeDefinition.getDescription());
		Assertions.assertEquals(
			"Test Attribute Definition",
			eventAttributeDefinition.getDisplayName());

		List<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions = new ArrayList<>(
				eventAttributeDefinition.
					getEventDefinitionEventAttributeDefinitions());

		EventDefinitionEventAttributeDefinition
			eventDefinitionEventAttributeDefinition =
				eventDefinitionEventAttributeDefinitions.get(0);

		Assertions.assertEquals(
			eventDefinition.getId(),
			eventDefinitionEventAttributeDefinition.getEventDefinitionId());

		Assertions.assertNotNull(eventAttributeDefinition.getId());
	}

	@Test
	public void testAddEventAttributeDefinitionDuplicateDisplayName() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition 1", "Test Attribute Definition",
				eventDefinition.getId(), "testAttributeDefinition1",
				"testValue");

		Assertions.assertEquals(
			"Test Attribute Definition",
			eventAttributeDefinition1.getDisplayName());

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition", "Test Attribute Definition",
				eventDefinition.getId(), "testAttributeDefinition2",
				"testValue");

		Assertions.assertEquals(
			"Test Attribute Definition (1)",
			eventAttributeDefinition2.getDisplayName());

		EventAttributeDefinition eventAttributeDefinition3 =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition", "Test Attribute Definition",
				eventDefinition.getId(), "testAttributeDefinition3",
				"testValue");

		Assertions.assertEquals(
			"Test Attribute Definition (2)",
			eventAttributeDefinition3.getDisplayName());
	}

	@Test
	public void testAddEventAttributeDefinitionNoDisplayName1() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		EventAttributeDefinition eventAttributeDefinition =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition", null, eventDefinition.getId(),
				"testAttributeDefinition", "testValue");

		Assertions.assertEquals(
			EventAttributeDefinition.DataType.STRING,
			eventAttributeDefinition.getDataType());
		Assertions.assertEquals(
			"Testing attribute definition",
			eventAttributeDefinition.getDescription());
		Assertions.assertEquals(
			"testAttributeDefinition",
			eventAttributeDefinition.getDisplayName());

		List<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions = new ArrayList<>(
				eventAttributeDefinition.
					getEventDefinitionEventAttributeDefinitions());

		EventDefinitionEventAttributeDefinition
			eventDefinitionEventAttributeDefinition =
				eventDefinitionEventAttributeDefinitions.get(0);

		Assertions.assertEquals(
			eventDefinition.getId(),
			eventDefinitionEventAttributeDefinition.getEventDefinitionId());

		Assertions.assertNotNull(eventAttributeDefinition.getId());
	}

	@Test
	public void testAddEventAttributeDefinitionNoDisplayName2() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition 1", null, eventDefinition.getId(),
				"testAttributeDefinition", "testValue");

		Assertions.assertEquals(
			"testAttributeDefinition",
			eventAttributeDefinition1.getDisplayName());

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition", null, eventDefinition.getId(),
				"TestAttributeDefinition", "testValue");

		Assertions.assertEquals(
			"TestAttributeDefinition (1)",
			eventAttributeDefinition2.getDisplayName());
	}

	@Test
	public void testCountEventAttributeDefinitions() {
		Assertions.assertEquals(
			30,
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, null, EventAttributeDefinition.Type.LOCAL));
	}

	@Test
	public void testCountEventAttributeDefinitionsWithKeyword() {
		Assertions.assertEquals(
			2,
			_eventAttributeDefinitionDog.countEventAttributeDefinitions(
				null, "file", EventAttributeDefinition.Type.LOCAL));
	}

	@Test
	public void testFetchEventAttributeDefinitionByDisplayName() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent", EventDefinition.Type.CUSTOM, null);

		EventAttributeDefinition expectedEventAttributeDefinition =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				null, "Test Event Attribute", eventDefinition.getId(),
				"testEventAttribute", "Sample Value");

		Assertions.assertEquals(
			expectedEventAttributeDefinition,
			_eventAttributeDefinitionDog.
				fetchEventAttributeDefinitionByDisplayName(
					"Test Event Attribute"));
	}

	@Test
	public void testFetchEventAttributeDefinitionByDisplayNameIgnoreCase() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.addEventDefinition(
				"CustomEvent", "Testing an event", "Test Event", null,
				"testEvent", EventDefinition.Type.CUSTOM, null);

		EventAttributeDefinition expectedEventAttributeDefinition =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				null, "Test Event Attribute", eventDefinition.getId(),
				"testEventAttribute", "Sample Value");

		Assertions.assertEquals(
			expectedEventAttributeDefinition,
			_eventAttributeDefinitionDog.
				fetchEventAttributeDefinitionByDisplayName(
					"test event attribute"));
	}

	@Test
	public void testFetchEventAttributeDefinitionByDisplayNameNonexistent() {
		Assertions.assertNull(
			_eventAttributeDefinitionDog.
				fetchEventAttributeDefinitionByDisplayName("Does Not Exist"));
	}

	@Test
	public void testFetchEventAttributeDefinitionByName() {
		EventAttributeDefinition eventAttributeDefinition =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");

		Assertions.assertNotNull(eventAttributeDefinition);

		Assertions.assertEquals(
			EventAttributeDefinition.DataType.DURATION,
			eventAttributeDefinition.getDataType());
		Assertions.assertNull(eventAttributeDefinition.getDescription());
		Assertions.assertEquals(
			"viewDuration", eventAttributeDefinition.getDisplayName());

		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageUnloaded");

		List<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions = new ArrayList<>(
				eventAttributeDefinition.
					getEventDefinitionEventAttributeDefinitions());

		EventDefinitionEventAttributeDefinition
			eventDefinitionEventAttributeDefinition =
				eventDefinitionEventAttributeDefinitions.get(0);

		Assertions.assertEquals(
			eventDefinition.getId(),
			eventDefinitionEventAttributeDefinition.getEventDefinitionId());

		Assertions.assertEquals(
			"viewDuration", eventAttributeDefinition.getName());
	}

	@Test
	public void testGetEventAttributeDefinition() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		EventAttributeDefinition expectedEventAttributeDefinition =
			_eventAttributeDefinitionDog.addEventAttributeDefinition(
				"Testing attribute definition", "Test Attribute Defintion",
				eventDefinition.getId(), "testAttributeDefinition",
				"testValue");

		Assertions.assertEquals(
			expectedEventAttributeDefinition,
			_eventAttributeDefinitionDog.getEventAttributeDefinition(
				expectedEventAttributeDefinition.getId()));
	}

	@Test
	public void testGetEventAttributeDefinitionDataTypeBoolean() {
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "FALSE"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "False"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "false"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "TRUE"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "True"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "true"));
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "maybe"));
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "no"));
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.BOOLEAN,
			_eventAttributeDefinitionDog.getDataType("name", "yes"));
	}

	@Test
	public void testGetEventAttributeDefinitionDataTypeDate() {
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.DATE,
			_eventAttributeDefinitionDog.getDataType(
				"name", "2020-12-12T09:20:00.Z"));
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.DATE,
			_eventAttributeDefinitionDog.getDataType(
				"name", "12/31/2020T09:30:00Z"));
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.DATE,
			_eventAttributeDefinitionDog.getDataType(
				"name", "2020 12 31 09:30:10+0130"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.DATE,
			_eventAttributeDefinitionDog.getDataType(
				"name", "2020-12-31T09:30:00.000Z"));
	}

	@Test
	public void testGetEventAttributeDefinitionDataTypeDuration() {
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.DURATION,
			_eventAttributeDefinitionDog.getDataType("name", "10000"));
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.DURATION,
			_eventAttributeDefinitionDog.getDataType("viewDuration", "-10000"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.DURATION,
			_eventAttributeDefinitionDog.getDataType("viewDuration", "10000"));
	}

	@Test
	public void testGetEventAttributeDefinitionDataTypeNull() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _eventAttributeDefinitionDog.getDataType("name", null));
	}

	@Test
	public void testGetEventAttributeDefinitionDataTypeNumber() {
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.NUMBER,
			_eventAttributeDefinitionDog.getDataType("name", "123a"));
		Assertions.assertNotEquals(
			EventAttributeDefinition.DataType.NUMBER,
			_eventAttributeDefinitionDog.getDataType("name", "abc"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.NUMBER,
			_eventAttributeDefinitionDog.getDataType("name", "00000000000"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.NUMBER,
			_eventAttributeDefinitionDog.getDataType("name", "30293094040"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.NUMBER,
			_eventAttributeDefinitionDog.getDataType("name", "30293094040"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.NUMBER,
			_eventAttributeDefinitionDog.getDataType("name", "-1234"));
		Assertions.assertEquals(
			EventAttributeDefinition.DataType.NUMBER,
			_eventAttributeDefinitionDog.getDataType("name", "1234.95"));
	}

	@Test
	public void testGetEventAttributeDefinitions() {
		_assertEventAttributeDefinitions(
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				null, null, 0, 5, Sort.asc("displayName"),
				EventAttributeDefinition.Type.LOCAL),
			new HashMap<String, EventAttributeDefinition.DataType>() {
				{
					put("articleId", EventAttributeDefinition.DataType.STRING);
					put("assetId", EventAttributeDefinition.DataType.STRING);
					put("category", EventAttributeDefinition.DataType.STRING);
					put("className", EventAttributeDefinition.DataType.STRING);
					put("classPK", EventAttributeDefinition.DataType.STRING);
				}
			});
		_assertEventAttributeDefinitions(
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				null, null, 0, 5, Sort.asc("name"),
				EventAttributeDefinition.Type.LOCAL),
			new HashMap<String, EventAttributeDefinition.DataType>() {
				{
					put("articleId", EventAttributeDefinition.DataType.STRING);
					put("assetId", EventAttributeDefinition.DataType.STRING);
					put("category", EventAttributeDefinition.DataType.STRING);
					put("className", EventAttributeDefinition.DataType.STRING);
					put("classPK", EventAttributeDefinition.DataType.STRING);
				}
			});
	}

	@Test
	public void testGetEventAttributeDefinitionsByEventDefinitionId() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		List<EventAttributeDefinition> actualEventAttributeDefinitions =
			_eventAttributeDefinitionDog.
				getEventAttributeDefinitionsByEventDefinitionId(
					eventDefinition.getId());

		Stream<EventAttributeDefinition> stream =
			actualEventAttributeDefinitions.stream();

		Assertions.assertEquals(
			new HashSet<String>() {
				{
					add("canonicalUrl");
					add("formId");
					add("page");
					add("pageDescription");
					add("pageKeywords");
					add("pageTitle");
					add("referrer");
					add("title");
					add("url");
				}
			},
			stream.map(
				EventAttributeDefinition::getName
			).collect(
				Collectors.toSet()
			));
	}

	@Test
	public void testGetEventAttributeDefinitionsByType() {
		String[] globalEventAttributeDefinitionNames = {
			"canonicalUrl", "pageDescription", "pageKeywords", "pageTitle",
			"referrer", "url"
		};

		List<EventAttributeDefinition> eventAttributeDefinitions =
			_eventAttributeDefinitionDog.getEventAttributeDefinitionsByType(
				EventAttributeDefinition.Type.GLOBAL);

		Stream<EventAttributeDefinition> stream =
			eventAttributeDefinitions.stream();

		MatcherAssert.assertThat(
			stream.map(
				EventAttributeDefinition::getName
			).collect(
				Collectors.toList()
			),
			Matchers.containsInAnyOrder(globalEventAttributeDefinitionNames));

		String[] localEventAttributeDefinitionNames = {
			"articleId", "assetId", "category", "className", "classPK",
			"commentId", "depth", "elementId", "entryId", "fieldName",
			"fileEntryId", "fileEntryUUID", "focusDuration", "formId",
			"groupId", "href", "numberOfWords", "page", "pageLoadTime",
			"preview", "ratingType", "score", "sessionId", "src", "tagName",
			"text", "title", "type", "version", "viewDuration"
		};

		eventAttributeDefinitions =
			_eventAttributeDefinitionDog.getEventAttributeDefinitionsByType(
				EventAttributeDefinition.Type.LOCAL);

		stream = eventAttributeDefinitions.stream();

		MatcherAssert.assertThat(
			stream.map(
				EventAttributeDefinition::getName
			).collect(
				Collectors.toList()
			),
			Matchers.containsInAnyOrder(localEventAttributeDefinitionNames));

		String[] allEventAttributeDefinitionNames = ArrayUtils.addAll(
			globalEventAttributeDefinitionNames,
			localEventAttributeDefinitionNames);

		eventAttributeDefinitions =
			_eventAttributeDefinitionDog.getEventAttributeDefinitionsByType(
				EventAttributeDefinition.Type.ALL);

		stream = eventAttributeDefinitions.stream();

		MatcherAssert.assertThat(
			stream.map(
				EventAttributeDefinition::getName
			).collect(
				Collectors.toList()
			),
			Matchers.containsInAnyOrder(allEventAttributeDefinitionNames));
	}

	@SQLResource(
		resourcePath = "test_get_event_attribute_definitions_global.sql"
	)
	@Test
	public void testGetEventAttributeDefinitionsGlobal() {
		_assertEventAttributeDefinitions(
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				null, null, 0, 10, Sort.asc("name"),
				EventAttributeDefinition.Type.GLOBAL),
			new HashMap<String, EventAttributeDefinition.DataType>() {
				{
					put(
						"canonicalUrl",
						EventAttributeDefinition.DataType.STRING);
					put(
						"pageDescription",
						EventAttributeDefinition.DataType.STRING);
					put(
						"pageKeywords",
						EventAttributeDefinition.DataType.STRING);
					put("pageTitle", EventAttributeDefinition.DataType.STRING);
					put("referrer", EventAttributeDefinition.DataType.STRING);
					put("url", EventAttributeDefinition.DataType.STRING);
				}
			});
	}

	@SQLResource(
		resourcePath = "test_get_event_attribute_definitions_with_event_definition_id.sql"
	)
	@Test
	public void testGetEventAttributeDefinitionsWithEventDefinitionId() {
		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("savedToList");

		_assertEventAttributeDefinitions(
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				eventDefinition.getId(), null, 0, 5, Sort.asc("name"),
				EventAttributeDefinition.Type.LOCAL),
			new HashMap<String, EventAttributeDefinition.DataType>() {
				{
					put(
						"itemDescription",
						EventAttributeDefinition.DataType.STRING);
					put(
						"numberOfReviews",
						EventAttributeDefinition.DataType.NUMBER);
				}
			});
	}

	@SQLResource(
		resourcePath = "test_get_event_attribute_definitions_with_keyword.sql"
	)
	@Test
	public void testGetEventAttributeDefinitionsWithKeywordMatchDescription() {
		_assertEventAttributeDefinitions(
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				null, "seller", 0, 5, Sort.asc("name"),
				EventAttributeDefinition.Type.LOCAL),
			new HashMap<String, EventAttributeDefinition.DataType>() {
				{
					put(
						"itemDescription",
						EventAttributeDefinition.DataType.STRING);
				}
			});
	}

	@SQLResource(
		resourcePath = "test_get_event_attribute_definitions_with_keyword.sql"
	)
	@Test
	public void testGetEventAttributeDefinitionsWithKeywordMatchDisplayName() {
		_assertEventAttributeDefinitions(
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				null, "number of", 0, 5, Sort.asc("name"),
				EventAttributeDefinition.Type.LOCAL),
			new HashMap<String, EventAttributeDefinition.DataType>() {
				{
					put(
						"itemQuantity",
						EventAttributeDefinition.DataType.NUMBER);
					put(
						"numberOfReviews",
						EventAttributeDefinition.DataType.NUMBER);
				}
			});
	}

	@SQLResource(
		resourcePath = "test_get_event_attribute_definitions_with_keyword.sql"
	)
	@Test
	public void testGetEventAttributeDefinitionsWithKeywordMatchName() {
		_assertEventAttributeDefinitions(
			_eventAttributeDefinitionDog.getEventAttributeDefinitionPage(
				null, "ity", 0, 5, Sort.asc("name"),
				EventAttributeDefinition.Type.LOCAL),
			new HashMap<String, EventAttributeDefinition.DataType>() {
				{
					put(
						"itemQuality",
						EventAttributeDefinition.DataType.STRING);
					put(
						"itemQuantity",
						EventAttributeDefinition.DataType.NUMBER);
				}
			});
	}

	@Test
	public void testUpdateEventAttributeDefinitionDataType() {
		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				EventAttributeDefinition.DataType.STRING, null, null,
				eventAttributeDefinition1.getId(), null, null);

		Assertions.assertNotEquals(
			eventAttributeDefinition1.getDataType(),
			eventAttributeDefinition2.getDataType());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDescription(),
			eventAttributeDefinition2.getDescription());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDisplayName(),
			eventAttributeDefinition2.getDisplayName());
		Assertions.assertEquals(
			eventAttributeDefinition1.
				getEventDefinitionEventAttributeDefinitions(),
			eventAttributeDefinition2.
				getEventDefinitionEventAttributeDefinitions());
		Assertions.assertEquals(
			eventAttributeDefinition1.getId(),
			eventAttributeDefinition2.getId());
		Assertions.assertEquals(
			eventAttributeDefinition1.getName(),
			eventAttributeDefinition2.getName());
	}

	@Test
	public void testUpdateEventAttributeDefinitionDescription() {
		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				null, "New Description", null,
				eventAttributeDefinition1.getId(), null, null);

		Assertions.assertEquals(
			eventAttributeDefinition1.getDataType(),
			eventAttributeDefinition2.getDataType());
		Assertions.assertNotEquals(
			eventAttributeDefinition1.getDescription(),
			eventAttributeDefinition2.getDescription());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDisplayName(),
			eventAttributeDefinition2.getDisplayName());
		Assertions.assertEquals(
			eventAttributeDefinition1.
				getEventDefinitionEventAttributeDefinitions(),
			eventAttributeDefinition2.
				getEventDefinitionEventAttributeDefinitions());
		Assertions.assertEquals(
			eventAttributeDefinition1.getId(),
			eventAttributeDefinition2.getId());
		Assertions.assertEquals(
			eventAttributeDefinition1.getName(),
			eventAttributeDefinition2.getName());
	}

	@Test
	public void testUpdateEventAttributeDefinitionDescriptionEmpty() {
		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");

		eventAttributeDefinition1.setDescription("New Description");

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				null, "New Description", null,
				eventAttributeDefinition1.getId(), null, null);

		Assertions.assertEquals(
			eventAttributeDefinition1, eventAttributeDefinition2);

		eventAttributeDefinition1.setDescription(null);

		eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				null, "", null, eventAttributeDefinition1.getId(), null, null);

		Assertions.assertEquals(
			eventAttributeDefinition1, eventAttributeDefinition2);
	}

	@Test
	public void testUpdateEventAttributeDefinitionDisplayName() {
		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				null, null, "New Test Display Name",
				eventAttributeDefinition1.getId(), null, null);

		Assertions.assertEquals(
			eventAttributeDefinition1.getDataType(),
			eventAttributeDefinition2.getDataType());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDescription(),
			eventAttributeDefinition2.getDescription());
		Assertions.assertNotEquals(
			eventAttributeDefinition1.getDisplayName(),
			eventAttributeDefinition2.getDisplayName());
		Assertions.assertEquals(
			eventAttributeDefinition1.
				getEventDefinitionEventAttributeDefinitions(),
			eventAttributeDefinition2.
				getEventDefinitionEventAttributeDefinitions());
		Assertions.assertEquals(
			eventAttributeDefinition1.getId(),
			eventAttributeDefinition2.getId());
		Assertions.assertEquals(
			eventAttributeDefinition1.getName(),
			eventAttributeDefinition2.getName());
	}

	@Test
	public void testUpdateEventAttributeDefinitionEventDefinitionEventAttributeDefinitions() {
		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");

		EventDefinition eventDefinition =
			_eventDefinitionDog.fetchEventDefinitionByName("pageViewed");

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				null, null, null, eventAttributeDefinition1.getId(),
				Collections.singleton(
					new EventDefinitionEventAttributeDefinition(
						eventDefinition.getId(), "testValue")),
				null);

		Assertions.assertEquals(
			eventAttributeDefinition1.getDataType(),
			eventAttributeDefinition2.getDataType());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDescription(),
			eventAttributeDefinition2.getDescription());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDisplayName(),
			eventAttributeDefinition2.getDisplayName());
		Assertions.assertNotEquals(
			eventAttributeDefinition1.
				getEventDefinitionEventAttributeDefinitions(),
			eventAttributeDefinition2.
				getEventDefinitionEventAttributeDefinitions());
		Assertions.assertEquals(
			eventAttributeDefinition1.getId(),
			eventAttributeDefinition2.getId());
		Assertions.assertEquals(
			eventAttributeDefinition1.getName(),
			eventAttributeDefinition2.getName());
	}

	@Test
	public void testUpdateEventAttributeDefinitionName() {
		EventAttributeDefinition eventAttributeDefinition1 =
			_eventAttributeDefinitionDog.fetchEventAttributeDefinitionByName(
				"viewDuration");

		EventAttributeDefinition eventAttributeDefinition2 =
			_eventAttributeDefinitionDog.updateEventAttributeDefinition(
				null, null, null, eventAttributeDefinition1.getId(), null,
				"newAttributeName");

		Assertions.assertEquals(
			eventAttributeDefinition1.getDataType(),
			eventAttributeDefinition2.getDataType());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDescription(),
			eventAttributeDefinition2.getDescription());
		Assertions.assertEquals(
			eventAttributeDefinition1.getDisplayName(),
			eventAttributeDefinition2.getDisplayName());
		Assertions.assertEquals(
			eventAttributeDefinition1.
				getEventDefinitionEventAttributeDefinitions(),
			eventAttributeDefinition2.
				getEventDefinitionEventAttributeDefinitions());
		Assertions.assertEquals(
			eventAttributeDefinition1.getId(),
			eventAttributeDefinition2.getId());
		Assertions.assertNotEquals(
			eventAttributeDefinition1.getName(),
			eventAttributeDefinition2.getName());
	}

	private void _assertEventAttributeDefinitions(
		Page<EventAttributeDefinition> actualEventAttributeDefinitions,
		Map<String, EventAttributeDefinition.DataType>
			expectedEventDefinitionAttributeDataTypes) {

		Assertions.assertEquals(
			expectedEventDefinitionAttributeDataTypes.size(),
			actualEventAttributeDefinitions.getNumberOfElements(),
			actualEventAttributeDefinitions.toString());

		for (EventAttributeDefinition actualEventAttributeDefinition :
				actualEventAttributeDefinitions) {

			EventAttributeDefinition.DataType expectedDataType =
				expectedEventDefinitionAttributeDataTypes.get(
					actualEventAttributeDefinition.getName());

			Assertions.assertEquals(
				expectedDataType, actualEventAttributeDefinition.getDataType());
		}
	}

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}