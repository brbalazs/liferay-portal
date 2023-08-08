/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.EventAttributeDefinitionDTO;
import com.liferay.osb.asah.backend.dto.EventAttributeValueDTO;
import com.liferay.osb.asah.common.dog.BQEventDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
@GraphQLTypeWiring(
	fieldName = "recentValues", typeName = "EventAttributeDefinition"
)
public class EventAttributeValuesDataFetcher
	implements DataFetcher<List<EventAttributeValueDTO>> {

	@Override
	public List<EventAttributeValueDTO> get(
		DataFetchingEnvironment environment) {

		EventAttributeDefinitionDTO eventAttributeDefinitionDTO =
			environment.getSource();

		String name = _globalBQEventPropertyNames.get(
			eventAttributeDefinitionDTO.getName());

		if (name != null) {
			List<EventAttributeValueDTO> eventAttributeValueDTOs =
				new ArrayList<>();

			Map<String, Date> recentGlobalBQEventProperyValues =
				_bqEventDog.getRecentGlobalBQEventProperyValues(name, 10);

			for (Map.Entry<String, Date> entry :
					recentGlobalBQEventProperyValues.entrySet()) {

				eventAttributeValueDTOs.add(
					new EventAttributeValueDTO(
						entry.getValue(), entry.getKey()));
			}

			return eventAttributeValueDTOs;
		}

		return ListUtil.map(
			_bqEventDog.getRecentBQEventPropertyValues(
				Long.valueOf(eventAttributeDefinitionDTO.getId()), 10),
			EventAttributeValueDTO::new);
	}

	private static final Map<String, String> _globalBQEventPropertyNames =
		new HashMap<String, String>() {
			{
				put("canonicalUrl", "canonicalUrl");
				put("pageDescription", "description");
				put("pageKeywords", "keywords");
				put("pageTitle", "title");
				put("referrer", "referrer");
				put("url", "url");
			}
		};

	@Autowired
	private BQEventDog _bqEventDog;

}