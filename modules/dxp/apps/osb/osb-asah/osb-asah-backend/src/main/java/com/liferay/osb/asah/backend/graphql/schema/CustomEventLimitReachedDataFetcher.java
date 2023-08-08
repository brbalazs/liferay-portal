/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.constants.EventDefinitionConstants;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Component
@GraphQLTypeWiring(
	fieldName = "customEventLimitReached", typeName = "QueryType"
)
public class CustomEventLimitReachedDataFetcher
	implements DataFetcher<Boolean> {

	@Override
	public Boolean get(DataFetchingEnvironment dataFetchingEnvironment) {
		long blockedEventDefinitionsCount =
			_eventDefinitionDog.countEventDefinitions(
				true, EventDefinition.BlockedReasonType.THRESHOLD_OVERFLOW,
				null, null, EventDefinition.Type.CUSTOM);

		long eventDefinitionsCount = 0;

		if (blockedEventDefinitionsCount > 0) {
			eventDefinitionsCount = _eventDefinitionDog.countEventDefinitions(
				false, null, null, EventDefinition.Type.CUSTOM);
		}

		return (blockedEventDefinitionsCount > 0) &&
			   (eventDefinitionsCount >=
				   EventDefinitionConstants.EVENT_DEFINITION_THRESHOLD);
	}

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

}