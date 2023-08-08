/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.JobDTO;
import com.liferay.osb.asah.common.dog.JobDog;
import com.liferay.osb.asah.common.entity.JobParameter;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.JobRunDataPeriod;
import com.liferay.osb.asah.common.model.JobRunFrequency;
import com.liferay.osb.asah.common.model.JobType;
import com.liferay.osb.asah.common.util.SetUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "createJob", typeName = "MutationType")
public class CreateJobMutationDataFetcher implements DataFetcher<JobDTO> {

	@Override
	public JobDTO get(DataFetchingEnvironment dataFetchingEnvironment) {
		return new JobDTO(
			_jobDog.addJob(
				SetUtil.map(
					dataFetchingEnvironment.getArgument("parameters"),
					JobParameter::of),
				JobRunDataPeriod.valueOf(
					dataFetchingEnvironment.getArgument("runDataPeriod")),
				JobRunFrequency.valueOf(
					dataFetchingEnvironment.getArgument("runFrequency")),
				JobType.valueOf(dataFetchingEnvironment.getArgument("type")),
				dataFetchingEnvironment.getArgument("name"),
				dataFetchingEnvironment.getArgument("runNow")));
	}

	@Autowired
	private JobDog _jobDog;

}