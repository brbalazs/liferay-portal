/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.graphql.GraphQLRestController;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;

import graphql.ExecutionInput;
import graphql.GraphQL;

import java.time.LocalDate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Leslie Wong
 * @author Shinn Lok
 */
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		MockitoTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class
	}
)
public class GraphQLRestControllerTest
	implements OSBAsahBackendSpringTestContext {

	@Test
	public void testSkipCacheUserSessionWithSameDates() throws Exception {
		String query = ResourceUtil.readResourceToString(
			"dependencies/cacheable_query.graphql", this);

		Map<String, Object> variables = new HashMap<String, Object>() {
			{
				put("rangeEnd", "2022-02-17");
				put("rangeKey", "90");
				put("rangeStart", "2022-02-17");
			}
		};

		_verify(true, "UserSession", query, variables);
	}

	@Test
	public void testSkipCacheWithCustomRange1() throws Exception {
		String query = ResourceUtil.readResourceToString(
			"dependencies/cacheable_query.graphql", this);

		LocalDate rangeEndLocalDate = LocalDate.now();

		LocalDate rangeStartLocalDate = rangeEndLocalDate.minusDays(3);

		Map<String, Object> variables = new HashMap<String, Object>() {
			{
				put("interval", "D");
				put("rangeEnd", rangeEndLocalDate.toString());
				put("rangeStart", rangeStartLocalDate.toString());
			}
		};

		_verify(true, "SiteMetrics", query, variables);
	}

	@Test
	public void testSkipCacheWithCustomRange2() throws Exception {
		String query = ResourceUtil.readResourceToString(
			"dependencies/cacheable_query.graphql", this);

		LocalDate localDate = LocalDate.now();

		LocalDate rangeEndLocalDate = localDate.minusDays(7);

		LocalDate rangeStartLocalDate = rangeEndLocalDate.minusDays(3);

		Map<String, Object> variables = new HashMap<String, Object>() {
			{
				put("interval", "D");
				put("rangeEnd", rangeEndLocalDate.toString());
				put("rangeStart", rangeStartLocalDate.toString());
			}
		};

		_verify(false, "SiteMetrics", query, variables);
	}

	@Test
	public void testSkipCacheWithNoncacheableQuery() throws Exception {
		String query = ResourceUtil.readResourceToString(
			"dependencies/noncacheable_query.graphql", this);

		_verify(true, "IndividualMetrics", query, Collections.emptyMap());
	}

	@Test
	public void testSkipCacheWithRangeKey() throws Exception {
		String query = ResourceUtil.readResourceToString(
			"dependencies/cacheable_query.graphql", this);

		_verify(true, "SiteMetrics", query, _getVariables(0));
		_verify(false, "SiteMetrics", query, _getVariables(1));
		_verify(false, "SiteMetrics", query, _getVariables(7));
		_verify(false, "SiteMetrics", query, _getVariables(28));
		_verify(false, "SiteMetrics", query, _getVariables(30));
		_verify(false, "SiteMetrics", query, _getVariables(90));
		_verify(false, "SiteMetrics", query, _getVariables(180));
		_verify(false, "SiteMetrics", query, _getVariables(365));
	}

	private Map<String, Object> _getVariables(int rangeKey) {
		Map<String, Object> variables = new HashMap<>();

		variables.put("interval", "D");
		variables.put("rangeKey", rangeKey);

		return variables;
	}

	private void _verify(
			boolean expectedSkipCache, String operationName, String query,
			Map<String, Object> variables)
		throws Exception {

		for (int i = 0; i < 3; i++) {
			_graphQLRestController.getGraphQLExecutionResult(
				operationName, query, variables);
		}

		if (expectedSkipCache) {
			Mockito.verify(
				_qraphQL, Mockito.times(3)
			).execute(
				ArgumentMatchers.any(ExecutionInput.class)
			);
		}
		else {
			Mockito.verify(
				_qraphQL, Mockito.times(1)
			).execute(
				ArgumentMatchers.any(ExecutionInput.class)
			);
		}

		Mockito.reset(_qraphQL);
	}

	@Autowired
	private GraphQLRestController _graphQLRestController;

	@SpyBean
	private GraphQL _qraphQL;

}