/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.ActivitiesRestController;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Shinn Lok
 */
public class ActivitiesRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "bq_page_events.sql")
	@Test
	public void testGetBQAssetDTOPageDTO1() {
		JSONObject jsonObject = _objectMapper.convertValue(
			_activitiesRestController.getBQAssetDTOPageDTO(
				"contains(assetTitle, 'clicks')", 0, 10,
				new String[] {"count", "desc"}),
			JSONObject.class);

		Assertions.assertEquals(
			3L,
			JSONUtil.getValue(
				jsonObject, "JSONObject/_embedded", "JSONArray/activities",
				"Object/0", "Object/count"));

		jsonObject = _objectMapper.convertValue(
			_activitiesRestController.getBQAssetDTOPageDTO(
				"(channelId eq 2) and contains(assetTitle, 'clicks')", 0, 10,
				new String[] {"count", "desc"}),
			JSONObject.class);

		Assertions.assertEquals(
			1L,
			JSONUtil.getValue(
				jsonObject, "JSONObject/_embedded", "JSONArray/activities",
				"Object/0", "Object/count"));
	}

	@BQSQLResource(resourcePath = "bq_page_events.sql")
	@Test
	public void testGetBQAssetDTOPageDTO2() {
		JSONObject jsonObject = _objectMapper.convertValue(
			_activitiesRestController.getBQAssetDTOPageDTO(
				"contains(assetTitle, 'random')", 0, 10,
				new String[] {"count", "desc"}),
			JSONObject.class);

		Assertions.assertEquals(
			0L,
			JSONUtil.getValue(
				jsonObject, "JSONObject/page", "Object/totalElements"));
	}

	@Autowired
	private ActivitiesRestController _activitiesRestController;

	@Autowired
	private ObjectMapper _objectMapper;

}