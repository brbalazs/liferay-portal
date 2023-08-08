/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

/**
 * @author Leslie Wong
 */
public class EventDefinitionsGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "event_definitions_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "event_definitions_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "event_definitions_query.graphql";
	}

}