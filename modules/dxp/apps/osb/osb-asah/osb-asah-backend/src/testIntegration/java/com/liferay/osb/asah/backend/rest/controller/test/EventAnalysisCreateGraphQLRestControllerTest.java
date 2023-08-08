/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.SQLResource;

/**
 * @author Rachael Koestartyo
 */
@SQLResource(
	resourcePath = "event_analysis_create_graphql_rest_controller_test.sql"
)
public class EventAnalysisCreateGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "event_analysis_create_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "expected_event_analysis_create.json";
	}

	@Override
	public String getQueryPath() {
		return "event_analysis_create_query.graphql";
	}

}