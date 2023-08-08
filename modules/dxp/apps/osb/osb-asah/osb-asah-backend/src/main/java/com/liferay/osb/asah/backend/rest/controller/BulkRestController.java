/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.spring.http.Http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 */
@RequestMapping(produces = "application/json", value = "/bulk")
@RestController
public class BulkRestController {

	@PostMapping
	public String post(@RequestBody String json) {
		JSONObject responseJSONObject = new JSONObject();

		JSONArray responseJSONArray = new JSONArray();

		JSONObject jsonObject = new JSONObject(json);

		try {
			JSONArray operationsJSONArray = jsonObject.getJSONArray(
				"operations");

			for (int i = 0; i < operationsJSONArray.length(); i++) {
				JSONObject operationJSONObject =
					operationsJSONArray.getJSONObject(i);

				String response = StringUtils.trim(
					_http.exchange(
						ServiceConstants.URL_BACKEND_INTERNAL,
						operationJSONObject.getString("url"),
						HttpMethod.resolve(
							operationJSONObject.getString("method")),
						operationJSONObject.optJSONObject("body")));

				if (StringUtils.startsWith(response, "[")) {
					responseJSONArray.put(new JSONArray(response));
				}
				else {
					responseJSONArray.put(new JSONObject(response));
				}
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		responseJSONObject.put("responses", responseJSONArray);

		return responseJSONObject.toString();
	}

	private static final Log _log = LogFactory.getLog(BulkRestController.class);

	@Autowired
	private Http _http;

}