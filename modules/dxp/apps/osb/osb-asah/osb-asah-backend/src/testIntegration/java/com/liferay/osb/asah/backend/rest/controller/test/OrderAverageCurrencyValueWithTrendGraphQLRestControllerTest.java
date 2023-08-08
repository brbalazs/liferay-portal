/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

/**
 * @author Riccardo Ferrari
 */
@BQSQLResource(resourcePath = "currency_value_graphql_rest_controller_test.sql")
public class OrderAverageCurrencyValueWithTrendGraphQLRestControllerTest
	extends BaseCurrencyValueGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "order_average_currency_value_with_trend_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "order_average_currency_value_with_trend_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "order_average_currency_value_with_trend_query.graphql";
	}

}