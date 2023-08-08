/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;

/**
 * @author Leslie Wong
 */
@BQSQLResource(
	resourcePath = "event_attribute_definition_graphql_rest_controller_test_2.sql"
)
@SQLResource(
	resourcePath = "event_attribute_definition_graphql_rest_controller_test_1.sql"
)
public class EventAttributeDefinitionGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "event_attribute_definition_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "event_attribute_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "event_attribute_definition_query.graphql";
	}

}