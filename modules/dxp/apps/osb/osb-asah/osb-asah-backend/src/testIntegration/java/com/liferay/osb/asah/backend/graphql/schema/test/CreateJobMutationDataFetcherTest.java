/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.JobDTO;
import com.liferay.osb.asah.backend.graphql.schema.CreateJobMutationDataFetcher;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobType;
import com.liferay.osb.asah.common.repository.JobRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentBuilder;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
public class CreateJobMutationDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		_jobRepository.deleteAll();
	}

	@Test
	public void testGet() {
		JobDTO jobDTO = _createJobMutationDataFetcher.get(
			_getDataFetchingEnvironment("Model Name"));

		Assertions.assertNotNull(jobDTO.getId());
		Assertions.assertEquals("Model Name", jobDTO.getName());
	}

	private DataFetchingEnvironment _getDataFetchingEnvironment(String value) {
		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		Map<String, Object> arguments = new HashMap<>();

		arguments.put("name", value);
		arguments.put("runDataPeriod", JobRunDataPeriod.LAST_7_DAYS.name());
		arguments.put("runFrequency", JobRunFrequency.MANUAL.name());
		arguments.put("runNow", false);
		arguments.put(
			"type", JobType.CONTENT_RECOMMENDATION_ITEM_SIMILARITY.name());

		dataFetchingEnvironmentBuilder.arguments(arguments);

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		return dataFetchingEnvironmentBuilder.build();
	}

	@Autowired
	private CreateJobMutationDataFetcher _createJobMutationDataFetcher;

	@Autowired
	private JobRepository _jobRepository;

}