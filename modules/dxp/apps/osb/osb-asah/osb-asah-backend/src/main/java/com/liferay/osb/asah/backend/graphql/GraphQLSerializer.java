/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.util.ListUtil;

import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.GraphqlErrorHelper;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class GraphQLSerializer {

	public GraphQLRequest fromString(String string) throws IOException {
		return _objectMapper.readValue(string, GraphQLRequest.class);
	}

	public String toString(ExecutionResult executionResult) throws IOException {
		Map<String, Object> map = new HashMap<>();

		map.put("data", executionResult.getData());

		List<GraphQLError> graphQLErrors = executionResult.getErrors();

		if ((graphQLErrors != null) && !graphQLErrors.isEmpty()) {
			map.put(
				"errors",
				ListUtil.map(
					graphQLErrors,
					graphQLError -> {
						Map<String, Object> specification =
							GraphqlErrorHelper.toSpecification(graphQLError);

						JSONObject jsonObject = _objectMapper.convertValue(
							graphQLError, JSONObject.class);

						if (jsonObject.has("exception")) {
							JSONObject exceptionJSONObject =
								jsonObject.getJSONObject("exception");

							if (exceptionJSONObject.has("messageKey")) {
								specification.put(
									"messageKey",
									exceptionJSONObject.getString(
										"messageKey"));
							}
						}

						return specification;
					}));
		}

		return _objectMapper.writeValueAsString(map);
	}

	@Autowired
	private ObjectMapper _objectMapper;

}