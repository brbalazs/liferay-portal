/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.ExperimentDTO;
import com.liferay.osb.asah.backend.graphql.schema.ExperimentDataFetcher;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentBuilder;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcos Martins
 */
public class ExperimentDataFetcherTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testGet() {
		Channel channel = new Channel();

		channel.setId(12345L);
		channel.setIsNew(Boolean.TRUE);

		_channelRepository.save(channel);

		Experiment experiment = new Experiment();

		experiment.setChannelId(12345L);
		experiment.setDataSourceId(331238757269547423L);
		experiment.setDXPExperienceId("1");
		experiment.setDXPExperienceName("Experience Name");
		experiment.setDXPSegmentId("123");
		experiment.setDXPSegmentName("Segment Name");
		experiment.setName("Experiment Name");

		ExperimentDTO expectedExperimentDTO = new ExperimentDTO(
			_experimentRepository.save(experiment));

		ExperimentDTO actualExperimentDTO = _experimentDataFetcher.get(
			_getDataFetchingEnvironment(expectedExperimentDTO.getId()));

		Assertions.assertEquals(
			expectedExperimentDTO.getDataSourceId(),
			actualExperimentDTO.getDataSourceId());
		Assertions.assertEquals(
			expectedExperimentDTO.getDXPExperienceId(),
			actualExperimentDTO.getDXPExperienceId());
		Assertions.assertEquals(
			expectedExperimentDTO.getDXPExperienceName(),
			actualExperimentDTO.getDXPExperienceName());
		Assertions.assertEquals(
			expectedExperimentDTO.getDXPSegmentId(),
			actualExperimentDTO.getDXPSegmentId());
		Assertions.assertEquals(
			expectedExperimentDTO.getDXPSegmentName(),
			actualExperimentDTO.getDXPSegmentName());
		Assertions.assertEquals(
			expectedExperimentDTO.getName(), actualExperimentDTO.getName());
	}

	private DataFetchingEnvironment _getDataFetchingEnvironment(String value) {
		DataFetchingEnvironmentBuilder dataFetchingEnvironmentBuilder =
			DataFetchingEnvironmentBuilder.newDataFetchingEnvironment();

		Map<String, Object> arguments = new HashMap<>();

		arguments.put("experimentId", value);

		dataFetchingEnvironmentBuilder.arguments(arguments);

		dataFetchingEnvironmentBuilder.context(new HashMap<>());

		return dataFetchingEnvironmentBuilder.build();
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private ExperimentDataFetcher _experimentDataFetcher;

	@Autowired
	private ExperimentRepository _experimentRepository;

}