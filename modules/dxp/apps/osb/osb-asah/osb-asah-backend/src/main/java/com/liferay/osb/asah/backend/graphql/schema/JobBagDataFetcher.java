/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.JobDTO;
import com.liferay.osb.asah.common.dog.JobDog;
import com.liferay.osb.asah.common.entity.Job;
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
@GraphQLTypeWiring(fieldName = "jobs", typeName = "QueryType")
public class JobBagDataFetcher implements DataFetcher<ResultBag<JobDTO>> {

	@Override
	public ResultBag<JobDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		int size = dataFetchingEnvironment.getArgument("size");
		int start = dataFetchingEnvironment.getArgument("start");

		Page<Job> jobPage = _jobDog.getJobPage(
			dataFetchingEnvironment.getArgument("keywords"), start / size, size,
			Sort.of(dataFetchingEnvironment.getArgument("sort")));

		return new ResultBag<>(
			ListUtil.map(jobPage.getContent(), JobDTO::new),
			jobPage.getTotalElements());
	}

	@Autowired
	private JobDog _jobDog;

}