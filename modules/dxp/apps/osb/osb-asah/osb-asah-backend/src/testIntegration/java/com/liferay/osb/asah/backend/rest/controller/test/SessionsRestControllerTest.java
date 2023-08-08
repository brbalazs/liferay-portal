/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.SessionsRestController;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rachael Koestartyo
 */
public class SessionsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@RepositoryResource(
		repositoryClass = BQSessionRepository.class,
		resourcePath = "osbasahcerebroinfo/user_sessions_info.json"
	)
	@Test
	public void testGetSessionValues() throws Exception {
		JSONAssert.assertEquals(
			JSONUtil.putAll("California", "Indiana"),
			(JSONArray)JSONUtil.getValue(
				new JSONObject(
					_sessionsRestController.getBQSessionFieldValuePageDTO(
						"context/region", "context/country eq 'United States'",
						0, 20, null)),
				"JSONObject/_embedded", "JSONArray/session-values"),
			false);
		JSONAssert.assertEquals(
			JSONUtil.putAll("Budapest", "California", "Indiana", "Tokyo"),
			(JSONArray)JSONUtil.getValue(
				new JSONObject(
					_sessionsRestController.getBQSessionFieldValuePageDTO(
						"context/region", null, 0, 20, null)),
				"JSONObject/_embedded", "JSONArray/session-values"),
			false);
		JSONAssert.assertEquals(
			JSONUtil.put("California"),
			(JSONArray)JSONUtil.getValue(
				new JSONObject(
					_sessionsRestController.getBQSessionFieldValuePageDTO(
						"context/region", null, 0, 20, "cal")),
				"JSONObject/_embedded", "JSONArray/session-values"),
			false);
	}

	@Autowired
	private SessionsRestController _sessionsRestController;

}