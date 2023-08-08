/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.BlockedCustomEventDefinitionDTO;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(
	fieldName = "hideBlockedEventDefinitions", typeName = "MutationType"
)
public class HideBlockedEventDefinitionsMutationDataFetcher
	implements DataFetcher<List<BlockedCustomEventDefinitionDTO>> {

	@Override
	public List<BlockedCustomEventDefinitionDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		List<Long> blockedEventDefinitionIds = _getBlockedEventDefinitionIds(
			dataFetchingEnvironment);

		_eventDefinitionDog.hideEventDefinitions(blockedEventDefinitionIds);

		return ListUtil.map(
			_eventDefinitionDog.fetchEventDefinitions(
				blockedEventDefinitionIds),
			BlockedCustomEventDefinitionDTO::new);
	}

	private List<Long> _getBlockedEventDefinitionIds(
		DataFetchingEnvironment dataFetchingEnvironment) {

		List<String> blockedEventDefinitionIds =
			dataFetchingEnvironment.getArgument("blockedEventDefinitionIds");

		return ListUtil.map(blockedEventDefinitionIds, Long::valueOf);
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}