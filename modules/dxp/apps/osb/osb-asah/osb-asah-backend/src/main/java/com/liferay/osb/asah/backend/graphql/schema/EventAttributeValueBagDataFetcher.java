/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.EventPropertyDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author Alejo Ceballos
 */
@Component
@GraphQLTypeWiring(fieldName = "eventAttributeValues", typeName = "QueryType")
public class EventAttributeValueBagDataFetcher
	implements DataFetcher<ResultBag<String>> {

	@Override
	public ResultBag<String> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		Page<String> bqEventPropertyValuePage =
			_eventPropertyDog.getBQEventPropertyValuePage(
				Long.valueOf(dataFetchingEnvironment.getArgument("channelId")),
				Long.valueOf(
					dataFetchingEnvironment.getArgument(
						"eventAttributeDefinitionId")),
				Long.valueOf(
					dataFetchingEnvironment.getArgument("eventDefinitionId")),
				dataFetchingEnvironment.getArgument("keywords"),
				dataFetchingEnvironment.getArgument("size"),
				dataFetchingEnvironment.getArgument("start"));

		return new ResultBag<>(
			bqEventPropertyValuePage.getContent(),
			bqEventPropertyValuePage.getTotalElements());
	}

	@Autowired
	private EventPropertyDog _eventPropertyDog;

}