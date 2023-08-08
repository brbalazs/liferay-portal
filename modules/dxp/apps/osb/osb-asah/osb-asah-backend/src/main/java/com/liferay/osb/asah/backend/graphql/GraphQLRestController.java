/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql;

import com.liferay.osb.asah.common.prometheus.PrometheusUtil;
import com.liferay.osb.asah.common.spring.annotation.Cacheable;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;

import io.prometheus.client.Histogram;
import io.prometheus.client.SimpleTimer;

import java.nio.charset.Charset;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcellus Tavares
 */
@CrossOrigin
@RequestMapping("/graphql")
@RestController
public class GraphQLRestController {

	@Cacheable
	public String getGraphQLExecutionResult(
			String operationName, String query, Map<String, Object> variables)
		throws Exception {

		ExecutionInput.Builder builder = ExecutionInput.newExecutionInput();

		ExecutionInput executionInput = builder.context(
			new HashMap<String, Object>()
		).operationName(
			operationName
		).query(
			query
		).variables(
			variables
		).build();

		ExecutionResult executionResult = _graphQL.execute(executionInput);

		return _graphQLSerializer.toString(executionResult);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "")
	public String post(HttpServletRequest httpServletRequest) throws Exception {
		String body = IOUtils.toString(
			httpServletRequest.getInputStream(), Charset.defaultCharset());

		if (_log.isDebugEnabled()) {
			_log.debug("Post body: " + body);
		}

		GraphQLRequest graphQLRequest = _graphQLSerializer.fromString(body);

		SimpleTimer simpleTimer = new SimpleTimer();

		String operationName = null;

		try {
			operationName = graphQLRequest.getOperationName();

			return getGraphQLExecutionResult(
				operationName, graphQLRequest.getQuery(),
				graphQLRequest.getVariables());
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw exception;
		}
		finally {
			if (operationName == null) {
				operationName = "unknown";
			}

			Histogram.Child child = _graphQLRequestsHistogram.labels(
				operationName);

			child.observe(simpleTimer.elapsedSeconds());
		}
	}

	private static final Log _log = LogFactory.getLog(
		GraphQLRestController.class);

	private static final Histogram _graphQLRequestsHistogram =
		PrometheusUtil.histogram(
			"backend_graphql_request_seconds",
			"The number of seconds taken to process the GraphQL requests",
			"operation");

	@Autowired
	private GraphQL _graphQL;

	@Autowired
	private GraphQLSerializer _graphQLSerializer;

}