/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.PageDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "pagesCount", typeName = "QueryType")
public class PagesCountDataFetcher implements DataFetcher<Long> {

	@Override
	public Long get(DataFetchingEnvironment dataFetchingEnvironment) {
		String fromDateString = dataFetchingEnvironment.getArgument("fromDate");
		String toDateString = dataFetchingEnvironment.getArgument("toDate");

		return _pageDog.getViewsMetricValue(
			null, LocalDateTime.parse(fromDateString),
			LocalDateTime.parse(toDateString));
	}

	@Autowired
	private PageDog _pageDog;

}