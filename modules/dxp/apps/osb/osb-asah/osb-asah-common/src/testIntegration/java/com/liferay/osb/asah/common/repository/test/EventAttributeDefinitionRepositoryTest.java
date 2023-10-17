/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.entity.EventDefinitionEventAttributeDefinition;
import com.liferay.osb.asah.common.repository.EventAttributeDefinitionRepository;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Leilany Ulisses
 */
@Import(JDBCTestConfiguration.class)
public class EventAttributeDefinitionRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testSearchEventAttributeDefinitions() {
		Optional<EventDefinition> eventDefinitionOptional =
			_eventDefinitionRepository.findByDisplayNameIgnoreCase(
				"assetClicked");

		EventDefinition eventDefinition = eventDefinitionOptional.get();

		List<EventAttributeDefinition> eventAttributeDefinitions =
			_eventAttributeDefinitionRepository.searchEventAttributeDefinitions(
				eventDefinition.getId(), null, PageRequest.of(1, 10), null);

		Assertions.assertNotNull(eventAttributeDefinitions.get(0));

		EventAttributeDefinition eventAttributeDefinition =
			eventAttributeDefinitions.get(0);

		Set<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions =
				eventAttributeDefinition.
					getEventDefinitionEventAttributeDefinitions();

		Stream<EventDefinitionEventAttributeDefinition> stream =
			eventDefinitionEventAttributeDefinitions.stream();

		Assertions.assertNotNull(
			stream.filter(
				eventDefinitionEventAttributeDefinition -> Objects.equals(
					eventDefinitionEventAttributeDefinition.
						getEventDefinitionId(),
					eventDefinition.getId())
			).map(
				EventDefinitionEventAttributeDefinition::getEventDefinitionId
			).findFirst(
			).orElse(
				null
			));
	}

	@Autowired
	private EventAttributeDefinitionRepository
		_eventAttributeDefinitionRepository;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

}