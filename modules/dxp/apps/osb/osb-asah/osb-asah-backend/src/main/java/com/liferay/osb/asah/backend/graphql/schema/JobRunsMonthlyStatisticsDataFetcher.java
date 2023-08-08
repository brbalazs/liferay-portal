/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.JobDog;
import com.liferay.osb.asah.common.dog.JobRunDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.JobRunsMonthlyStatistics;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(
	fieldName = "jobRunsMonthlyStatistics", typeName = "QueryType"
)
public class JobRunsMonthlyStatisticsDataFetcher
	implements DataFetcher<JobRunsMonthlyStatistics> {

	@Override
	public JobRunsMonthlyStatistics get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		return _jobRunDog.getJobRunsMonthlyStatistics(
			_jobDog.getJob(
				Long.valueOf(dataFetchingEnvironment.getArgument("jobId"))));
	}

	@Autowired
	private JobDog _jobDog;

	@Autowired
	private JobRunDog _jobRunDog;

}