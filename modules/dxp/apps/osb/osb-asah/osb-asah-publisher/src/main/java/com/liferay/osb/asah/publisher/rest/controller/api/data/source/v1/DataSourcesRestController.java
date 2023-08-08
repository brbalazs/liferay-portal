/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller.api.data.source.v1;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.DataSource;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Inácio Nery
 * @deprecated As of 3.0.1
 */
@Deprecated
@RequestMapping(produces = "application/json", value = "/api/1.0/data-sources")
@RestController
public class DataSourcesRestController {

	/**
	 * @deprecated As of 3.0.1
	 */
	@Deprecated
	@GetMapping("/{id}")
	public String fetchDataSource(@PathVariable Long id) {
		DataSource dataSource = _dataSourceDog.fetchDataSource(id);

		if (dataSource == null) {
			return null;
		}

		_sanitize(dataSource);

		JSONObject dataSourceJSONObject = _objectMapper.convertValue(
			dataSource, JSONObject.class);

		return dataSourceJSONObject.toString();
	}

	private void _sanitize(DataSource dataSource) {
		dataSource.setFaroBackendSecuritySignature(null);
		dataSource.setPrivateKey(null);
	}

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private ObjectMapper _objectMapper;

}