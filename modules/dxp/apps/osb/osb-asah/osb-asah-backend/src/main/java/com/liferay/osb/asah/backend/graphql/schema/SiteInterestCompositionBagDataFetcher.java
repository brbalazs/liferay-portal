/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.SiteInterestCompositionDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.CompositionResultBag;

import graphql.schema.DataFetchingEnvironment;

import org.apache.commons.lang3.math.NumberUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
@GraphQLTypeWiring(fieldName = "siteInterests", typeName = "QueryType")
public class SiteInterestCompositionBagDataFetcher
	extends BaseDataFetcher<CompositionResultBag> {

	@Override
	public CompositionResultBag get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		return _siteInterestCompositionDog.getCompositionResultBag(
			NumberUtils.createLong(searchQueryContext.getChannelId()),
			dataFetchingEnvironment.getArgument("size"),
			dataFetchingEnvironment.getArgument("start"),
			searchQueryContext.getTimeRange());
	}

	@Autowired
	private SiteInterestCompositionDog _siteInterestCompositionDog;

}