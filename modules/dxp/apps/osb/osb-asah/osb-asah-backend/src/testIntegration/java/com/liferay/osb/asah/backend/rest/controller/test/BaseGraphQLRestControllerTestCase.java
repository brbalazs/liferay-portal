/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.graphql.GraphQLRestController;
import com.liferay.osb.asah.backend.spring.OSBAsahBackendSpringBootApplication;
import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.test.util.spring.OSBAsahBQSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

/**
 * @author André Miranda
 */
@ContextConfiguration(classes = OSBAsahBackendSpringBootApplication.class)
@ExtendWith(OSBAsahSpringExtension.class)
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class,
		OSBAsahBQSQLTestExecutionListener.class,
		ServletTestExecutionListener.class
	}
)
@TestPropertySource(properties = "osb.asah.security.enabled=false")
@WebMvcTest(GraphQLRestController.class)
public abstract class BaseGraphQLRestControllerTestCase {

	public abstract String getBodyPath();

	public abstract String getExpectedResultPath();

	public abstract String getQueryPath();

	@Test
	public void testQuery() throws Exception {
		try {
			String body = ResourceUtil.readResourceToString(
				"dependencies/" + getBodyPath(), this);

			JSONObject bodyJSONObject = new JSONObject(body);

			String query = ResourceUtil.readResourceToString(
				"dependencies/" + getQueryPath(), this);

			bodyJSONObject.put("query", query);

			JSONObject responseJSONObject = _request(bodyJSONObject);

			Assertions.assertFalse(responseJSONObject.has("errors"));

			String expectedResultPath = getExpectedResultPath();

			JSONAssert.assertEquals(
				ResourceUtil.readResourceToJSONObject(
					"dependencies/" + expectedResultPath, this),
				responseJSONObject, false);
		}
		finally {
			ProjectIdThreadLocal.setProjectId("test");
		}
	}

	private void _expectContentTypeJSON(ResultActions resultActions)
		throws Exception {

		ContentResultMatchers contentResultMatchers =
			MockMvcResultMatchers.content();

		resultActions.andExpect(
			contentResultMatchers.contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	private void _expectStatusOK(ResultActions resultActions) throws Exception {
		StatusResultMatchers statusResultMatchers =
			MockMvcResultMatchers.status();

		resultActions.andExpect(statusResultMatchers.isOk());
	}

	private JSONObject _request(JSONObject jsonObject) throws Exception {
		MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
			MockMvcRequestBuilders.post("/graphql");

		mockHttpServletRequestBuilder.accept(
			MediaType.APPLICATION_JSON_UTF8_VALUE);
		mockHttpServletRequestBuilder.content(jsonObject.toString());
		mockHttpServletRequestBuilder.contentType(
			MediaType.APPLICATION_JSON_UTF8);
		mockHttpServletRequestBuilder.header(
			HeaderConstants.PROJECT_ID, "test");

		ResultActions resultActions = _mockMvc.perform(
			mockHttpServletRequestBuilder);

		_expectContentTypeJSON(resultActions);
		_expectStatusOK(resultActions);

		MvcResult mvcResult = resultActions.andReturn();

		MockHttpServletResponse mockHttpServletResponse =
			mvcResult.getResponse();

		return new JSONObject(mockHttpServletResponse.getContentAsString());
	}

	@Autowired
	private MockMvc _mockMvc;

}