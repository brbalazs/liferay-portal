/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(
	fieldName = "blockCustomEventDefinitions", typeName = "MutationType"
)
public class BlockCustomEventDefinitionsMutationDataFetcher
	implements DataFetcher<Void> {

	@Override
	public Void get(DataFetchingEnvironment dataFetchingEnvironment) {
		List<String> eventDefinitionIds = dataFetchingEnvironment.getArgument(
			"eventDefinitionIds");

		_eventDefinitionDog.blockEventDefinitions(
			ListUtil.map(eventDefinitionIds, Long::valueOf));

		return null;
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}