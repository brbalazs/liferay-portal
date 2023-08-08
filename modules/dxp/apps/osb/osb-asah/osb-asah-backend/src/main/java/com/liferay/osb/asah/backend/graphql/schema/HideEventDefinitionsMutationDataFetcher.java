/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavates
 */
@Component
@GraphQLTypeWiring(
	fieldName = "hideEventDefinitions", typeName = "MutationType"
)
public class HideEventDefinitionsMutationDataFetcher
	implements DataFetcher<List<EventDefinitionDTO>> {

	@Override
	public List<EventDefinitionDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		List<Long> eventDefinitionIds = _getEventDefinitionIds(
			dataFetchingEnvironment);

		_eventDefinitionDog.hideEventDefinitions(eventDefinitionIds);

		return ListUtil.map(
			_eventDefinitionDog.fetchEventDefinitions(eventDefinitionIds),
			EventDefinitionDTO::new);
	}

	private List<Long> _getEventDefinitionIds(
		DataFetchingEnvironment dataFetchingEnvironment) {

		List<String> eventDefinitionIds = dataFetchingEnvironment.getArgument(
			"eventDefinitionIds");

		return ListUtil.map(eventDefinitionIds, Long::valueOf);
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}