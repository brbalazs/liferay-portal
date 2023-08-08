/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.external.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.api.external.RecommendationRestController;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.ItemRecommendationRepository;
import com.liferay.osb.asah.common.repository.JobRepository;
import com.liferay.osb.asah.common.repository.JobRunRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Marcellus Tavares
 */
@Disabled
public class RecommendationRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		RequestContextHolder.setRequestAttributes(
			new ServletRequestAttributes(new MockHttpServletRequest()));
	}

	@AfterEach
	public void tearDown() {
		RequestContextHolder.resetRequestAttributes();
	}

	@RepositoryResource(
		repositoryClass = ItemRecommendationRepository.class,
		resourcePath = "osbasahfaroinfo/recommended_items_info.json"
	)
	@RepositoryResource(
		repositoryClass = JobRepository.class,
		resourcePath = "osbasahfaroinfo/jobs.json"
	)
	@RepositoryResource(
		repositoryClass = JobRunRepository.class,
		resourcePath = "osbasahfaroinfo/job_runs.json"
	)
	@Test
	public void testGetPageRecommendationEntityModel() {
		Assertions.assertNotNull(
			_recommendationRestController.getPageRecommendationEntityModel(
				JSONUtil.put(
					"modelId", "1"
				).put(
					"url", "https://page-a"
				).toString()));
	}

	@Autowired
	private RecommendationRestController _recommendationRestController;

}