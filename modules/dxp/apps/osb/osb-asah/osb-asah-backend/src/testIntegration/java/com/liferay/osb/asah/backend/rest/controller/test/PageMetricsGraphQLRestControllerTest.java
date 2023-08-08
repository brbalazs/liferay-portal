/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.SQLResource;

import org.junit.jupiter.api.Disabled;

/**
 * @author André Miranda
 */
@Disabled
@SQLResource(resourcePath = "page_metrics_graphql_rest_controller_test.sql")
public class PageMetricsGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "page_metrics_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "page_metrics_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "page_metrics_query.graphql";
	}

}