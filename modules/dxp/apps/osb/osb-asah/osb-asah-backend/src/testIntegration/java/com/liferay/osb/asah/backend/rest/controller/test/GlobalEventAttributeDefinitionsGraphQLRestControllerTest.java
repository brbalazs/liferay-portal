/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import org.springframework.context.annotation.Import;

/**
 * @author Alejo Ceballos
 */
@Import(JDBCTestConfiguration.class)
public class GlobalEventAttributeDefinitionsGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "global_event_attribute_definition_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "global_event_attribute_definition_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "global_event_attribute_definitions_query.graphql";
	}

}