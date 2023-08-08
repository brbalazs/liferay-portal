/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.JobDTO;
import com.liferay.osb.asah.common.dog.JobRunDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "runDate", typeName = "Job")
public class JobRunDateDataFetcher implements DataFetcher<String> {

	@Override
	public String get(DataFetchingEnvironment dataFetchingEnvironment) {
		JobDTO jobDTO = dataFetchingEnvironment.getSource();

		return _jobRunDog.fetchLatestJobRunPublishedDateString(
			Long.valueOf(jobDTO.getId()));
	}

	@Autowired
	private JobRunDog _jobRunDog;

}