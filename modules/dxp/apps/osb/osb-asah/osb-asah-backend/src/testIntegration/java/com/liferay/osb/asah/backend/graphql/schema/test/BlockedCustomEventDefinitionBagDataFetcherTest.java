/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.BlockedCustomEventDefinitionDTO;
import com.liferay.osb.asah.backend.graphql.schema.BlockedCustomEventDefinitionBagDataFetcher;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import graphql.schema.DataFetchingEnvironmentBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcos Martins
 */
public class BlockedCustomEventDefinitionBagDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		EventDefinition eventDefinition1 = new EventDefinition();

		eventDefinition1.setBlocked(true);

		Date date = new Date();

		eventDefinition1.setBlockedLastSeenDate(date);

		eventDefinition1.setBlockedLastSeenURL("http://text.com");
		eventDefinition1.setBlockedReasonType(
			EventDefinition.BlockedReasonType.BLOCKED_BY_USER);
		eventDefinition1.setHidden(false);
		eventDefinition1.setName("customEventDefinition 1");
		eventDefinition1.setType(EventDefinition.Type.CUSTOM);

		EventDefinition eventDefinition2 = new EventDefinition();

		eventDefinition2.setBlocked(true);
		eventDefinition2.setBlockedLastSeenDate(DateUtil.addDays(date, -1));
		eventDefinition2.setBlockedLastSeenURL("http://text.com");
		eventDefinition2.setBlockedReasonType(
			EventDefinition.BlockedReasonType.BLOCKED_BY_USER);
		eventDefinition2.setHidden(false);
		eventDefinition2.setName("customEventDefinition 2");
		eventDefinition2.setType(EventDefinition.Type.CUSTOM);

		_eventDefinitionRepository.saveAll(
			Arrays.asList(eventDefinition1, eventDefinition2));
	}

	@Test
	public void testGetSortedByLastSeenDate() {
		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		HashMap<String, String> sort = new HashMap<String, String>() {
			{
				put("column", "lastSeenDate");
				put("type", "ASC");
			}
		};

		Map<String, Object> arguments = new HashMap<String, Object>() {
			{
				put("page", 0);
				put("size", 10);
				put("sort", sort);
			}
		};

		dataFetchingEnvironmentBuilder.arguments(arguments);

		ResultBag<BlockedCustomEventDefinitionDTO>
			blockedCustomEventDefinitionDTOResultBag =
				_blockedCustomEventDefinitionBagDataFetcher.get(
					dataFetchingEnvironmentBuilder.build());

		List<BlockedCustomEventDefinitionDTO> blockedCustomEventDefinitionDTOs =
			blockedCustomEventDefinitionDTOResultBag.getResults();

		Stream<BlockedCustomEventDefinitionDTO> stream =
			blockedCustomEventDefinitionDTOs.stream();

		Assertions.assertArrayEquals(
			new String[] {"customEventDefinition 2", "customEventDefinition 1"},
			stream.map(
				BlockedCustomEventDefinitionDTO::getName
			).toArray(
				String[]::new
			));

		sort.put("type", "DESC");

		blockedCustomEventDefinitionDTOResultBag =
			_blockedCustomEventDefinitionBagDataFetcher.get(
				dataFetchingEnvironmentBuilder.build());

		blockedCustomEventDefinitionDTOs =
			blockedCustomEventDefinitionDTOResultBag.getResults();

		stream = blockedCustomEventDefinitionDTOs.stream();

		Assertions.assertArrayEquals(
			new String[] {"customEventDefinition 1", "customEventDefinition 2"},
			stream.map(
				BlockedCustomEventDefinitionDTO::getName
			).toArray(
				String[]::new
			));
	}

	@Autowired
	private BlockedCustomEventDefinitionBagDataFetcher
		_blockedCustomEventDefinitionBagDataFetcher;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

}