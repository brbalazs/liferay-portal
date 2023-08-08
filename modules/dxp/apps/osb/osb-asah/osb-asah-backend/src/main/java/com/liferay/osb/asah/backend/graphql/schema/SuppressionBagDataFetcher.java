/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.SuppressionDog;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
@GraphQLTypeWiring(fieldName = "suppressions", typeName = "QueryType")
public class SuppressionBagDataFetcher
	implements DataFetcher<ResultBag<Suppression>> {

	@Override
	public ResultBag<Suppression> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		int size = dataFetchingEnvironment.getArgument("size");
		int start = dataFetchingEnvironment.getArgument("start");

		Page<Suppression> suppressionPage = _suppressionDog.getSuppressionPage(
			dataFetchingEnvironment.getArgument("keywords"), start / size, size,
			Sort.of(dataFetchingEnvironment.getArgument("sort")));

		return new ResultBag<>(
			suppressionPage.getContent(), suppressionPage.getTotalElements());
	}

	@Autowired
	private SuppressionDog _suppressionDog;

}