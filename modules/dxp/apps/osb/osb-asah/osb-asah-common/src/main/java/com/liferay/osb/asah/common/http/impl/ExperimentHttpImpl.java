/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.http.impl;

import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.http.ExperimentHttp;
import com.liferay.osb.asah.common.spring.http.Http;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class ExperimentHttpImpl implements ExperimentHttp {

	@Override
	public JSONObject getExperimentMetricsJSONObject(String id) {
		String path = String.format(
			"/api/1.0/experiments/%s/calculate-metrics", id);

		String response = _http.exchange(
			ServiceConstants.URL_BACKEND_INTERNAL, path, HttpMethod.GET, null);

		return new JSONObject(response);
	}

	@Autowired
	private Http _http;

}