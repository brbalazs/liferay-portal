/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

/**
 * @author Leslie Wong
 */
@BQSQLResource(resourcePath = "page_path_graphql_rest_controller_test.sql")
public class PagePathNoResultsGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "page_path_no_results_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "page_path_no_results_expected_results.json";
	}

	@Override
	public String getQueryPath() {
		return "page_path_query.graphql";
	}

}