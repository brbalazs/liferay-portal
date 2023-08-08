/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.EventAttributeDefinitionDTO;
import com.liferay.osb.asah.backend.graphql.schema.GlobalEventAttributeDefinitionsDataFetcher;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Alejo Ceballos
 */
@Import(JDBCTestConfiguration.class)
public class GlobalEventAttributeDefinitionsDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testGet() {
		String[] globalEventAttributeDefinitionNames = {
			"canonicalUrl", "pageTitle", "referrer", "url"
		};

		List<EventAttributeDefinitionDTO> eventAttributeDefinitionsDTOs =
			_globalEventAttributeDefinitionsDataFetcher.get(null);

		Stream<EventAttributeDefinitionDTO> stream =
			eventAttributeDefinitionsDTOs.stream();

		MatcherAssert.assertThat(
			stream.map(
				EventAttributeDefinitionDTO::getName
			).collect(
				Collectors.toList()
			),
			Matchers.containsInAnyOrder(globalEventAttributeDefinitionNames));
	}

	@Autowired
	private GlobalEventAttributeDefinitionsDataFetcher
		_globalEventAttributeDefinitionsDataFetcher;

}