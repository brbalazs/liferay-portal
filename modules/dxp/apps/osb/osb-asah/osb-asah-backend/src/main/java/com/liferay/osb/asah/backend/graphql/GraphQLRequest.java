/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class GraphQLRequest {

	public String getOperationName() {
		return _operationName;
	}

	public String getQuery() {
		return _query;
	}

	public Map<String, Object> getVariables() {
		return _variables;
	}

	@SuppressWarnings("unused")
	public void setOperationName(String operationName) {
		_operationName = operationName;
	}

	@SuppressWarnings("unused")
	public void setQuery(String query) {
		_query = query;
	}

	@SuppressWarnings("unused")
	public void setVariables(Map<String, Object> variables) {
		_variables = variables;
	}

	private String _operationName;
	private String _query;
	private Map<String, Object> _variables = new HashMap<>();

}