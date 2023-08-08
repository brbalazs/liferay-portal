/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.TimeZone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.context.annotation.Import;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@BQSQLResource(
	resourcePath = "test_event_histogram_graphql_rest_controller_test.sql"
)
@Import(JDBCTestConfiguration.class)
public class EventHistogramGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "event_histogram_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "event_histogram_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "event_histogram_query.graphql";
	}

	@BeforeEach
	public void setUp() throws Exception {
		_timeZone = TimeZone.getDefault();

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@AfterEach
	public void tearDown() throws Exception {
		TimeZone.setDefault(_timeZone);
	}

	private TimeZone _timeZone;

}