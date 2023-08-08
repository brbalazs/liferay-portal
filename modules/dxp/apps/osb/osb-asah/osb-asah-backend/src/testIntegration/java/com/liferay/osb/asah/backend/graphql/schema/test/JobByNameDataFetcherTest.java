/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.JobDTO;
import com.liferay.osb.asah.backend.graphql.schema.JobByNameDataFetcher;
import com.liferay.osb.asah.common.entity.Job;
import com.liferay.osb.asah.common.entity.JobParameter;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobType;
import com.liferay.osb.asah.common.repository.JobRepository;
import com.liferay.osb.asah.common.util.SetUtil;
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
public class JobByNameDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@AfterEach
	public void tearDown() {
		_jobRepository.deleteAll();
	}

	@Test
	public void testGet() {
		Job job = new Job();

		job.setName("Product Recommendation Job");
		job.setJobParameters(SetUtil.of(new JobParameter("parameter1", "1.2")));
		job.setJobRunDataPeriod(JobRunDataPeriod.LAST_30_DAYS);
		job.setJobRunFrequency(JobRunFrequency.MANUAL);
		job.setJobType(JobType.CONTENT_RECOMMENDATION_ITEM_SIMILARITY);

		job = _jobRepository.save(job);

		JobDTO jobDTO = _jobByNameDataFetcher.get(
			_getDataFetchingEnvironment(job.getName()));

		Assertions.assertEquals(
			job.getJobRunDataPeriod(), jobDTO.getJobRunDataPeriod());
		Assertions.assertEquals(
			job.getJobRunFrequency(), jobDTO.getJobRunFrequency());
		Assertions.assertEquals(job.getJobType(), jobDTO.getJobType());
		Assertions.assertEquals(job.getId(), Long.valueOf(jobDTO.getId()));
		Assertions.assertEquals(job.getName(), jobDTO.getName());
	}

	@Test
	public void testGetWithNoJob() {
		JobDTO jobDTO = _jobByNameDataFetcher.get(
			_getDataFetchingEnvironment("Product Recommendation Job"));

		Assertions.assertNull(jobDTO);
	}

	private DataFetchingEnvironment _getDataFetchingEnvironment(String value) {
		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		Map<String, Object> arguments = new HashMap<>();

		arguments.put("name", value);

		dataFetchingEnvironmentBuilder.arguments(arguments);

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		return dataFetchingEnvironmentBuilder.build();
	}

	@Autowired
	private JobByNameDataFetcher _jobByNameDataFetcher;

	@Autowired
	private JobRepository _jobRepository;

}