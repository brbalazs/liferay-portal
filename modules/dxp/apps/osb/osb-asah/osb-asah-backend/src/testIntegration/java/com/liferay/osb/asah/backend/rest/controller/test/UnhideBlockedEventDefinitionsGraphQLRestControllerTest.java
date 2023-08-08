/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.SQLResource;

/**
 * @author Marcellus Tavares
 */
@SQLResource(
	resourcePath = "unhide_blocked_event_definitions_graphql_rest_controller_test.sql"
)
public class UnhideBlockedEventDefinitionsGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "unhide_blocked_event_definitions_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "unhide_blocked_event_definitions_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "unhide_blocked_event_definitions_query.graphql";
	}

}