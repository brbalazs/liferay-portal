/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "urls", typeName = "BlogMetric")
@GraphQLTypeWiring(fieldName = "urls", typeName = "DocumentMetric")
@GraphQLTypeWiring(fieldName = "urls", typeName = "FormMetric")
@GraphQLTypeWiring(fieldName = "urls", typeName = "JournalMetric")
@GraphQLTypeWiring(fieldName = "urls", typeName = "PageMetric")
public class URLDataFetcher implements DataFetcher<List<String>> {

	@Override
	public List<String> get(DataFetchingEnvironment dataFetchingEnvironment) {
		AssetMetric assetMetric = dataFetchingEnvironment.getSource();

		return assetMetric.getURLs();
	}

}