/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.SiteMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.AcquisitionType;
import com.liferay.osb.asah.common.model.CompositionResultBag;

import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
@GraphQLTypeWiring(fieldName = "acquisitions", typeName = "QueryType")
public class AcquisitionCompositionBagDataFetcher
	extends BaseDataFetcher<CompositionResultBag> {

	@Override
	public CompositionResultBag get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		return _siteMetricDog.getAcquisitionsMetricsCompositionResultBag(
			AcquisitionType.valueOf(
				dataFetchingEnvironment.getArgument("acquisitionType")),
			searchQueryContext.getChannelId(),
			dataFetchingEnvironment.getArgument("size"),
			dataFetchingEnvironment.getArgument("start"),
			searchQueryContext.getTimeRange());
	}

	@Autowired
	private SiteMetricDog _siteMetricDog;

}