/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.JobDTO;
import com.liferay.osb.asah.common.dog.JobDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.JobStatus;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "status", typeName = "Job")
public class JobStatusDataFetcher implements DataFetcher<JobStatus> {

	@Override
	public JobStatus get(DataFetchingEnvironment dataFetchingEnvironment) {
		JobDTO jobDTO = dataFetchingEnvironment.getSource();

		return _jobDog.getJobStatus(Long.valueOf(jobDTO.getId()));
	}

	@Autowired
	private JobDog _jobDog;

}