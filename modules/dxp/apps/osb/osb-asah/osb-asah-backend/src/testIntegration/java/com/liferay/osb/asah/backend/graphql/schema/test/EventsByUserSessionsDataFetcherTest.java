/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.BQEventDTO;
import com.liferay.osb.asah.backend.dto.EventsByUserSessionDTO;
import com.liferay.osb.asah.backend.dto.UserSessionDTO;
import com.liferay.osb.asah.backend.graphql.schema.EventsByUserSessionsDataFetcher;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@BQSQLResource(resourcePath = "test_events_by_user_sessions_data_fetcher.sql")
@Import(JDBCTestConfiguration.class)
public class EventsByUserSessionsDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testGet() {
		EventsByUserSessionDTO eventsByUserSessionDTO =
			_eventsByUserSessionsDataFetcher.get(
				_getDataFetchingEnvironment(),
				new SearchQueryContext() {
					{
						setTimeRange(TimeRange.LAST_24_HOURS);
					}
				});

		Assertions.assertEquals(2, eventsByUserSessionDTO.getTotalEvents());

		List<UserSessionDTO> userSessionDTOs =
			eventsByUserSessionDTO.getUserSessionDTOs();

		Assertions.assertEquals(
			1, userSessionDTOs.size(), userSessionDTOs.toString());

		UserSessionDTO userSessionDTO = userSessionDTOs.get(0);

		List<BQEventDTO> bqEventDTOs = userSessionDTO.getBQEventDTOs();

		Assertions.assertEquals(2, bqEventDTOs.size(), bqEventDTOs.toString());

		Assertions.assertEquals("pt-BR", userSessionDTO.getLanguageId());

		bqEventDTOs.forEach(
			bqEventDTO -> Assertions.assertEquals(
				"canonicalUrlValue", bqEventDTO.getCanonicalUrl()));
	}

	private DataFetchingEnvironment _getDataFetchingEnvironment() {
		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		Map<String, Object> arguments = new HashMap<>();

		arguments.put("channelId", "1");
		arguments.put("entityId", "1");
		arguments.put("page", 0);
		arguments.put("size", 10);

		dataFetchingEnvironmentBuilder.arguments(arguments);

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		return dataFetchingEnvironmentBuilder.build();
	}

	@Autowired
	private EventsByUserSessionsDataFetcher _eventsByUserSessionsDataFetcher;

}