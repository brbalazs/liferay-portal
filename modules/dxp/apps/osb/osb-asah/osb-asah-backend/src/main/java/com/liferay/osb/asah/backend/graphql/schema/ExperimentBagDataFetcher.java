/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dog.ExperimentDog;
import com.liferay.osb.asah.backend.dto.ExperimentDTO;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.model.ResultBag;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.util.ListUtil;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
@GraphQLTypeWiring(fieldName = "experiments", typeName = "QueryType")
public class ExperimentBagDataFetcher
	implements DataFetcher<ResultBag<ExperimentDTO>> {

	@Override
	public ResultBag<ExperimentDTO> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		String channelId = dataFetchingEnvironment.getArgument("channelId");
		String keywords = dataFetchingEnvironment.getArgument("keywords");
		Map<String, String> sort = dataFetchingEnvironment.getArgument("sort");
		int size = dataFetchingEnvironment.getArgument("size");
		int start = dataFetchingEnvironment.getArgument("start");

		Page<Experiment> experimentPage = _experimentDog.getExperimentPage(
			Long.valueOf(channelId), keywords, start, size, Sort.of(sort));

		return new ResultBag<>(
			ListUtil.map(experimentPage.getContent(), ExperimentDTO::new),
			experimentPage.getTotalElements());
	}

	@Autowired
	private ExperimentDog _experimentDog;

}