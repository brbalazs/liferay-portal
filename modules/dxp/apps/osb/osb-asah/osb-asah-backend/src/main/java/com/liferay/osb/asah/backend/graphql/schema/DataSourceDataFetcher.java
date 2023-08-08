/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.DataSourceDTO;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
@GraphQLTypeWiring(fieldName = "dataSource", typeName = "QueryType")
public class DataSourceDataFetcher implements DataFetcher<DataSourceDTO> {

	@Override
	public DataSourceDTO get(DataFetchingEnvironment dataFetchingEnvironment) {
		String dataSourceId = dataFetchingEnvironment.getArgument(
			"dataSourceId");

		DataSource dataSource = _dataSourceDog.fetchDataSource(
			Long.valueOf(dataSourceId));

		if (dataSource != null) {
			return new DataSourceDTO(dataSource);
		}

		return null;
	}

	@Autowired
	private DataSourceDog _dataSourceDog;

}