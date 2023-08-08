/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.ExperimentDog;
import com.liferay.osb.asah.backend.dto.ExperimentMetricDTO;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "metricsHistogram", typeName = "Experiment")
public class ExperimentMetricsHistogramDataFetcher
	implements DataFetcher<List<ExperimentMetricDTO>> {

	@Override
	public List<ExperimentMetricDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		Map<String, Object> context = dataFetchingEnvironment.getContext();

		String experimentId = (String)context.get("experimentId");

		return ListUtil.map(
			_experimentDog.getExperimentMetrics(Long.valueOf(experimentId)),
			ExperimentMetricDTO::new);
	}

	@Autowired
	private ExperimentDog _experimentDog;

}