/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.DataSourceDTO;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 * @author André Miranda
 */
@Component
@GraphQLTypeWiring(fieldName = "dataSources", typeName = "QueryType")
public class DataSourcesDataFetcher
	implements DataFetcher<List<DataSourceDTO>> {

	@Override
	public List<DataSourceDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		Sort sort = null;

		Map<String, String> sortMap = dataFetchingEnvironment.getArgument(
			"sort");

		if (sortMap != null) {
			sort = Sort.by(
				new Sort.Order(
					Sort.Direction.fromString(sortMap.get("type")),
					sortMap.get("column")));
		}

		return ListUtil.map(
			_dataSourceDog.getDataSources(
				dataFetchingEnvironment.getArgument("credentialsType"),
				dataFetchingEnvironment.getArgument("type"),
				dataFetchingEnvironment.getArgument("size"), sort),
			DataSourceDTO::new);
	}

	@Autowired
	private DataSourceDog _dataSourceDog;

}