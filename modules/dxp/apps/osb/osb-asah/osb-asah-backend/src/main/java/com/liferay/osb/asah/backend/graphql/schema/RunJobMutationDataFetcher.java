/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.JobDTO;
import com.liferay.osb.asah.common.dog.JobDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "runJob", typeName = "MutationType")
public class RunJobMutationDataFetcher implements DataFetcher<JobDTO> {

	@Override
	public JobDTO get(DataFetchingEnvironment dataFetchingEnvironment) {
		return new JobDTO(
			_jobDog.runJob(
				Long.valueOf(dataFetchingEnvironment.getArgument("jobId")),
				JobRunDataPeriod.valueOf(
					dataFetchingEnvironment.getArgument("runDataPeriod"))));
	}

	@Autowired
	private JobDog _jobDog;

}