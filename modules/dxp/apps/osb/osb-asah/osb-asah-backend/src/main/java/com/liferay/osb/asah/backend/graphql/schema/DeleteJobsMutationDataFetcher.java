/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.JobDog;
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
@GraphQLTypeWiring(fieldName = "deleteJobs", typeName = "MutationType")
public class DeleteJobsMutationDataFetcher
	extends BaseExperimentDataFetcher implements DataFetcher<Void> {

	@Override
	public Void get(DataFetchingEnvironment dataFetchingEnvironment) {
		List<String> jobIds = dataFetchingEnvironment.getArgument("jobIds");

		_jobDog.deleteJobs(ListUtil.map(jobIds, Long::valueOf));

		return null;
	}

	@Autowired
	private JobDog _jobDog;

}