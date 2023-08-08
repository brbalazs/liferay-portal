/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(
	fieldName = "activityEventContextKeys", typeName = "QueryType"
)
public class ActivityEventContextKeysDataFetcher
	implements DataFetcher<List<String>> {

	@Override
	public List<String> get(DataFetchingEnvironment dataFetchingEnvironment) {
		return Collections.emptyList();
	}

}