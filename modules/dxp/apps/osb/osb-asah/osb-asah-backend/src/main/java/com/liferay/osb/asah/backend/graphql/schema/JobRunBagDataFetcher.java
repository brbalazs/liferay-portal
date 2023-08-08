/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.JobRunDTO;
import com.liferay.osb.asah.common.dog.JobRunDog;
import com.liferay.osb.asah.common.entity.JobRun;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "jobRuns", typeName = "QueryType")
public class JobRunBagDataFetcher implements DataFetcher<ResultBag<JobRunDTO>> {

	@Override
	public ResultBag<JobRunDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		int size = dataFetchingEnvironment.getArgument("size");
		int start = dataFetchingEnvironment.getArgument("start");

		Page<JobRun> jobRunPage = _jobRunDog.getJobRunPage(
			Long.valueOf(dataFetchingEnvironment.getArgument("jobId")),
			start / size, size,
			Sort.of(dataFetchingEnvironment.getArgument("sort")));

		return new ResultBag<>(
			ListUtil.map(jobRunPage.getContent(), JobRunDTO::new),
			jobRunPage.getTotalElements());
	}

	@Autowired
	private JobRunDog _jobRunDog;

}