/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.ExperimentDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "deleteExperiment", typeName = "MutationType")
public class DeleteExperimentMutationDataFetcher
	extends BaseExperimentDataFetcher implements DataFetcher<Boolean> {

	@Override
	public Boolean get(DataFetchingEnvironment dataFetchingEnvironment) {
		return _experimentDog.deleteExperiment(
			Long.valueOf(dataFetchingEnvironment.getArgument("experimentId")));
	}

	@Autowired
	private ExperimentDog _experimentDog;

}