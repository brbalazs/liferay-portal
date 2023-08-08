/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.EventAnalysisDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
@GraphQLTypeWiring(fieldName = "deleteEventAnalyses", typeName = "MutationType")
public class DeleteEventAnalysesMutationDataFetcher
	implements DataFetcher<Void> {

	@Override
	public Void get(DataFetchingEnvironment dataFetchingEnvironment) {
		List<String> eventAnalysisIds = dataFetchingEnvironment.getArgument(
			"eventAnalysisIds");

		_eventAnalysisDog.deleteEventAnalyses(
			ListUtil.map(eventAnalysisIds, Long::valueOf));

		return null;
	}

	@Autowired
	private EventAnalysisDog _eventAnalysisDog;

}