/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.PagesRestController;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.LocalDate;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Shinn Lok
 */
public class PagesRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_pages_rest_controller_test.sql")
	@Test
	public void testGetReadCounts() {
		LocalDate localDate = LocalDate.now();

		JSONObject responseJSONObject = new JSONObject(
			_pagesRestController.getReadCounts(
				"https://liferay.com", localDate, Interval.DAY.getKey(),
				localDate.minusDays(2)));

		Assertions.assertEquals(3, responseJSONObject.get("value"));

		JSONObject histogramMetricJSONObject = (JSONObject)JSONUtil.getValue(
			responseJSONObject, "JSONArray/histogram", "Object/0");

		Assertions.assertTrue(histogramMetricJSONObject.has("key"));
		Assertions.assertTrue(histogramMetricJSONObject.has("value"));
	}

	@BQSQLResource(resourcePath = "test_bq_pages_rest_controller_test.sql")
	@Test
	public void testGetReadsCount() {
		Assertions.assertEquals(
			"3", _pagesRestController.getReadsCount("https://liferay.com"));
	}

	@BQSQLResource(resourcePath = "test_bq_pages_rest_controller_test.sql")
	@Test
	public void testGetViewCounts() {
		LocalDate localDate = LocalDate.now();

		JSONObject responseJSONObject = new JSONObject(
			_pagesRestController.getViewCounts(
				"https://liferay.com", localDate, Interval.DAY.getKey(),
				localDate.minusDays(2)));

		Assertions.assertEquals(6, responseJSONObject.get("value"));

		JSONObject histogramMetricJSONObject = (JSONObject)JSONUtil.getValue(
			responseJSONObject, "JSONArray/histogram", "Object/0");

		Assertions.assertTrue(histogramMetricJSONObject.has("key"));
		Assertions.assertTrue(histogramMetricJSONObject.has("value"));
	}

	@BQSQLResource(resourcePath = "test_bq_pages_rest_controller_test.sql")
	@Test
	public void testGetViewsCount() {
		Assertions.assertEquals(
			"6", _pagesRestController.getViewsCount("https://liferay.com"));
	}

	@Autowired
	private PagesRestController _pagesRestController;

}