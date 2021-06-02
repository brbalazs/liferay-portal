/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.provisioning.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.faro.provisioning.client.model.ErrorResponse;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
public abstract class BaseProvisioningClient {

	protected String execute(Http.Options options) throws Exception {
		String response = http.URLtoString(options);

		if (response.startsWith(StringPool.OPEN_CURLY_BRACE)) {
			ErrorResponse errorResponse = _objectMapper.readValue(
				response, ErrorResponse.class);

			if (Validator.isNotNull(errorResponse.getException())) {
				throw new Exception(errorResponse.getException());
			}
		}

		return response;
	}

	protected <T> T get(
		String path, TypeReference typeReference,
		Map<String, String> parameterMap) {

		try {
			String response = execute(_getOptions(path, parameterMap, false));

			return _objectMapper.readValue(response, typeReference);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return null;
		}
	}

	protected void post(String path, Map<String, String> parameterMap)
		throws Exception {

		execute(_getOptions(path, parameterMap, true));
	}

	@Reference
	protected Http http;

	private Http.Options _getOptions(
		String path, Map<String, String> parameterMap, boolean post) {

		Http.Options options = new Http.Options();

		options.setHeaders(_headers);

		String url = _OSB_API_URL.concat(path);

		for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
			url = http.addParameter(url, entry.getKey(), entry.getValue());
		}

		options.setLocation(url);
		options.setPost(post);

		return options;
	}

	private static final String _OSB_API_URL =
		System.getenv("OSB_API_URL") + "/osb-portlet/api/jsonws/";

	private static final Log _log = LogFactoryUtil.getLog(
		BaseProvisioningClient.class);

	private static final Map<String, String> _headers =
		new HashMap<String, String>() {
			{
				put("OSB_API_Token", System.getenv("OSB_API_TOKEN"));
			}
		};
	private static final ObjectMapper _objectMapper = new ObjectMapper() {
		{
			configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
	};

}