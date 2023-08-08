/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.TimeRange;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Collection;

import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
@GraphQLTypeWiring(fieldName = "timeRange", typeName = "QueryType")
public class TimeRangeDataFetcher
	implements DataFetcher<Collection<TimeRange>> {

	@Override
	public Collection<TimeRange> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		return TimeRange.values();
	}

}