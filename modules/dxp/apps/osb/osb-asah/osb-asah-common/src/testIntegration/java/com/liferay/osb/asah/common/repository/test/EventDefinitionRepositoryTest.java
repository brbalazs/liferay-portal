/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Import(JDBCTestConfiguration.class)
public class EventDefinitionRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@SQLResource(resourcePath = "test_event_definition.sql")
	@Test
	public void testSearchHiddenBlockedEventDefinitions() {
		List<EventDefinition> eventDefinitions =
			_eventDefinitionRepository.searchEventDefinitions(
				true, null, true, "CustomEventNotification",
				PageRequest.of(0, 3), EventDefinition.Type.CUSTOM);

		Assertions.assertEquals(
			3, eventDefinitions.size(), eventDefinitions.toString());
	}

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

}