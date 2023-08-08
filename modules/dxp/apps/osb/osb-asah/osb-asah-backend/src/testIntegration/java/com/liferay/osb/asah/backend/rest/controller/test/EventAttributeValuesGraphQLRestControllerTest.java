/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.SQLResource;

/**
 * @author Alejo Ceballos
 */
@BQSQLResource(
	resourcePath = "event_attribute_values_graphql_rest_controller_test_2.sql"
)
@SQLResource(
	resourcePath = "event_attribute_values_graphql_rest_controller_test_1.sql"
)
public class EventAttributeValuesGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "event_attribute_values_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "expected_event_attribute_values_result.json";
	}

	@Override
	public String getQueryPath() {
		return "event_attribute_values_query.graphql";
	}

}