/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.EventAnalysis;
import com.liferay.osb.asah.common.repository.EventAnalysisRepository;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

/**
 * @author Rachael Koestartyo
 */
@Import(JDBCTestConfiguration.class)
public class EventAnalysisRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@SQLResource(resourcePath = "test_event_analysis.sql")
	@Test
	public void testCountEventAnalyses() {
		Assertions.assertEquals(
			1, _eventAnalysisRepository.countEventAnalyses(1L, "1"));
		Assertions.assertEquals(
			2, _eventAnalysisRepository.countEventAnalyses(1L, "Event"));
	}

	@SQLResource(resourcePath = "test_event_analysis.sql")
	@Test
	public void testDeleteByIdIn() {
		Assertions.assertEquals(2, _eventAnalysisRepository.count());

		_eventAnalysisRepository.deleteByIdIn(
			new HashSet<>(Arrays.asList(2345L, 2346L)));

		Assertions.assertEquals(0, _eventAnalysisRepository.count());
	}

	@SQLResource(resourcePath = "test_event_analysis.sql")
	@Test
	public void testSearchEventAnalyses() {
		List<EventAnalysis> eventAnalyses =
			_eventAnalysisRepository.searchEventAnalyses(
				1L, "1", PageRequest.of(0, 10));

		Assertions.assertEquals(
			1, eventAnalyses.size(), eventAnalyses.toString());

		eventAnalyses = _eventAnalysisRepository.searchEventAnalyses(
			1L, "Event", PageRequest.of(0, 10));

		Assertions.assertEquals(
			2, eventAnalyses.size(), eventAnalyses.toString());
	}

	@Autowired
	private EventAnalysisRepository _eventAnalysisRepository;

}