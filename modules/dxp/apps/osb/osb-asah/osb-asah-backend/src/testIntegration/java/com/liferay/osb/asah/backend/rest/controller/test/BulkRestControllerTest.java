/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.BulkRestController;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.http.Http;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;

import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Vishal Reddy
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
public class BulkRestControllerTest implements OSBAsahBackendSpringTestContext {

	@Test
	public void testPost() {
		_mockExchange("1", null, HttpMethod.GET, "/dummy");
		_mockExchange("2", null, HttpMethod.POST, "/dummy?value=2");
		_mockExchange("3", null, HttpMethod.PUT, "/dummy");
		_mockExchange("4", null, HttpMethod.DELETE, "/dummy");
		_mockExchange(
			"5", JSONUtil.put("dummy", 5), HttpMethod.PATCH, "/dummy");

		JSONObject jsonObject = new JSONObject(
			_bulkRestController.post(
				JSONUtil.put(
					"operations",
					JSONUtil.putAll(
						JSONUtil.put(
							"method", "DELETE"
						).put(
							"url", "/dummy"
						),
						JSONUtil.put(
							"method", "GET"
						).put(
							"url", "/dummy"
						),
						JSONUtil.put(
							"body", JSONUtil.put("dummy", 5)
						).put(
							"method", "PATCH"
						).put(
							"url", "/dummy"
						),
						JSONUtil.put(
							"method", "POST"
						).put(
							"url", "/dummy?value=2"
						),
						JSONUtil.put(
							"method", "PUT"
						).put(
							"url", "/dummy"
						))
				).toString()));

		JSONAssert.assertEquals(
			JSONUtil.put(
				"responses",
				JSONUtil.putAll(
					JSONUtil.put("4"), JSONUtil.put("1"), JSONUtil.put("5"),
					JSONUtil.put("2"), JSONUtil.put("3"))),
			jsonObject, true);
	}

	private void _mockExchange(
		String answer, JSONObject bodyJSONObject, HttpMethod httpMethod,
		String url) {

		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.put(
				answer
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.eq(ServiceConstants.URL_BACKEND_INTERNAL),
			ArgumentMatchers.eq(url), ArgumentMatchers.eq(httpMethod),
			ArgumentMatchers.refEq(bodyJSONObject)
		);
	}

	@Autowired
	private BulkRestController _bulkRestController;

	@MockBean
	private Http _http;

}