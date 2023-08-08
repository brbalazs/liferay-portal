/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.backend.dto.JobRunDTO;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
@GraphQLTypeWiring(fieldName = "context", typeName = "JobRun")
public class JobRunContextDataFetcher
	implements DataFetcher<List<Pair<String, String>>> {

	@Override
	public List<Pair<String, String>> get(
		DataFetchingEnvironment dataFetchingEnvironment) {

		JobRunDTO jobRunDTO = dataFetchingEnvironment.getSource();

		return _toPairs(jobRunDTO.getContext());
	}

	private List<Pair<String, String>> _toPairs(Map<String, Object> context) {
		List<Pair<String, String>> pairs = new ArrayList<>();

		for (Map.Entry<String, Object> entry : context.entrySet()) {
			pairs.add(
				Pair.of(entry.getKey(), String.valueOf(entry.getValue())));
		}

		return pairs;
	}

}