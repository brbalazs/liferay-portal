/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.JobDog;
import com.liferay.osb.asah.common.entity.Job;
import com.liferay.osb.asah.common.entity.JobParameter;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.util.SetUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "updateJob", typeName = "MutationType")
public class UpdateJobMutationDataFetcher implements DataFetcher<Job> {

	@Override
	public Job get(DataFetchingEnvironment dataFetchingEnvironment) {
		return _jobDog.updateJob(
			Long.valueOf(dataFetchingEnvironment.getArgument("jobId")),
			SetUtil.map(
				dataFetchingEnvironment.getArgument("parameters"),
				JobParameter::of),
			JobRunDataPeriod.valueOf(
				dataFetchingEnvironment.getArgument("runDataPeriod")),
			JobRunFrequency.valueOf(
				dataFetchingEnvironment.getArgument("runFrequency")),
			dataFetchingEnvironment.getArgument("name"),
			dataFetchingEnvironment.getArgument("runNow"));
	}

	@Autowired
	private JobDog _jobDog;

}