/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.ExperimentDog;
import com.liferay.osb.asah.backend.dto.ExperimentMetricDTO;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "metrics", typeName = "Experiment")
public class ExperimentMetricsDataFetcher
	implements DataFetcher<ExperimentMetricDTO> {

	@Override
	public ExperimentMetricDTO get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		Map<String, Object> context = dataFetchingEnvironment.getContext();

		String experimentId = (String)context.get("experimentId");

		return new ExperimentMetricDTO(
			_experimentDog.getExperimentMetric(Long.valueOf(experimentId)));
	}

	@Autowired
	private ExperimentDog _experimentDog;

}