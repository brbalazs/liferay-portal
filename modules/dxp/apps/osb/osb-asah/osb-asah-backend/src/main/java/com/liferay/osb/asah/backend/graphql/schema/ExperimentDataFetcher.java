/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.ExperimentDog;
import com.liferay.osb.asah.backend.dto.ExperimentDTO;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
@GraphQLTypeWiring(fieldName = "experiment", typeName = "QueryType")
public class ExperimentDataFetcher implements DataFetcher<ExperimentDTO> {

	@Override
	public ExperimentDTO get(DataFetchingEnvironment dataFetchingEnvironment) {
		String experimentId = dataFetchingEnvironment.getArgument(
			"experimentId");

		Map<String, Object> context = dataFetchingEnvironment.getContext();

		context.put("experimentId", experimentId);

		return new ExperimentDTO(
			_experimentDog.fetchExperiment(Long.valueOf(experimentId)));
	}

	@Autowired
	private ExperimentDog _experimentDog;

}