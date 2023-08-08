/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.InterestCompositionDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.Sort;

import graphql.execution.ExecutionTypeInfo;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;

import org.apache.commons.lang3.math.NumberUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
@GraphQLTypeWiring(fieldName = "individualInterests", typeName = "QueryType")
@GraphQLTypeWiring(
	fieldName = "individualSegmentInterests", typeName = "QueryType"
)
public class InterestCompositionBagDataFetcher
	implements DataFetcher<CompositionResultBag> {

	@Override
	public CompositionResultBag get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		ExecutionTypeInfo executionTypeInfo =
			dataFetchingEnvironment.getFieldTypeInfo();

		GraphQLFieldDefinition graphQLFieldDefinition =
			executionTypeInfo.getFieldDefinition();

		String name = graphQLFieldDefinition.getName();

		if (name.equals("individualInterests")) {
			return _interestCompositionDog.getIndividualCompositionResultBag(
				NumberUtils.createLong(
					dataFetchingEnvironment.getArgument("channelId")),
				dataFetchingEnvironment.getArgument("keywords"),
				dataFetchingEnvironment.getArgument("size"),
				Sort.of(dataFetchingEnvironment.getArgument("sort")),
				dataFetchingEnvironment.getArgument("start"));
		}

		return _interestCompositionDog.getIndividualSegmentCompositionResultBag(
			dataFetchingEnvironment.getArgument("active"),
			NumberUtils.createLong(
				dataFetchingEnvironment.getArgument("channelId")),
			dataFetchingEnvironment.getArgument("keywords"),
			NumberUtils.createLong(
				dataFetchingEnvironment.getArgument("individualSegmentId")),
			dataFetchingEnvironment.getArgument("size"),
			Sort.of(dataFetchingEnvironment.getArgument("sort")),
			dataFetchingEnvironment.getArgument("start"));
	}

	@Autowired
	private InterestCompositionDog _interestCompositionDog;

}