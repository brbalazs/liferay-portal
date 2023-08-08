/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.DataSourceDTO;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetchingEnvironment;

import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(fieldName = "sitesSyncDetails", typeName = "DataSource")
public class SitesSyncDetailsDataFetcher
	extends BaseDataFetcher<DataSourceDTO.ProviderDTO.DetailDTO> {

	@Override
	public DataSourceDTO.ProviderDTO.DetailDTO get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		DataSourceDTO dataSourceDTO = dataFetchingEnvironment.getSource();

		return dataSourceDTO.getSitesSyncDetailDTO();
	}

}