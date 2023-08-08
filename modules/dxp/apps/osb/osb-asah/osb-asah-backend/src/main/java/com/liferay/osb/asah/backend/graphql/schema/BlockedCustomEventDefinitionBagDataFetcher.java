/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.BlockedCustomEventDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(
	fieldName = "blockedCustomEventDefinitions", typeName = "QueryType"
)
public class BlockedCustomEventDefinitionBagDataFetcher
	implements DataFetcher<ResultBag<BlockedCustomEventDefinitionDTO>> {

	@Override
	public ResultBag<BlockedCustomEventDefinitionDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		String keyword = dataFetchingEnvironment.getArgument("keyword");

		Page<EventDefinition> eventDefinitionPage =
			_eventDefinitionDog.getEventDefinitionPage(
				true, null, keyword,
				dataFetchingEnvironment.getArgument("page"),
				dataFetchingEnvironment.getArgument("size"),
				_getSort(dataFetchingEnvironment.getArgument("sort")),
				EventDefinition.Type.CUSTOM);

		return new ResultBag<>(
			ListUtil.map(
				eventDefinitionPage.getContent(),
				BlockedCustomEventDefinitionDTO::new),
			eventDefinitionPage.getTotalElements());
	}

	private Sort _getSort(Map<String, String> sort) {
		if (Objects.equals(sort.get("column"), "lastSeenDate")) {
			return new Sort("blockedLastSeenDate", sort.get("type"));
		}

		return Sort.of(sort);
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}