/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import org.springframework.context.annotation.Import;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@BQSQLResource(
	resourcePath = "test_events_by_user_session_graphql_rest_controller_test.sql"
)
@Import(JDBCTestConfiguration.class)
public class EventsByUserSessionGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "events_by_session_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "events_by_sessions_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "events_by_session_query.graphql";
	}

}