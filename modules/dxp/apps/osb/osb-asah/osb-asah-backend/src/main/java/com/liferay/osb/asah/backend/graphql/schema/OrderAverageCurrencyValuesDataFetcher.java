/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dto.CurrencyValueDTO;
import com.liferay.osb.asah.common.dog.CommerceDashboardDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.CurrencyValue;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.language.Field;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Riccardo Ferrari
 */
@Component
@GraphQLTypeWiring(
	fieldName = "orderAverageCurrencyValues", typeName = "QueryType"
)
public class OrderAverageCurrencyValuesDataFetcher
	extends BaseDataFetcher<List<CurrencyValueDTO>> {

	@Override
	public List<CurrencyValueDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment,
		SearchQueryContext searchQueryContext) {

		DataFetchingFieldSelectionSet dataFetchingFieldSelectionSet =
			dataFetchingEnvironment.getSelectionSet();

		Map<String, List<Field>> dataFetchingFieldSelectionSetFields =
			dataFetchingFieldSelectionSet.get();

		Map<String, CurrencyValue> orderAverageCurrencyValues =
			_commerceDashboardDog.getOrderAverageCurrencyValues(
				Long.valueOf(dataFetchingEnvironment.getArgument("channelId")),
				dataFetchingFieldSelectionSetFields.containsKey("trend"),
				searchQueryContext.getTimeRange());

		return ListUtil.map(
			orderAverageCurrencyValues.values(), CurrencyValueDTO::new);
	}

	@Autowired
	private CommerceDashboardDog _commerceDashboardDog;

}