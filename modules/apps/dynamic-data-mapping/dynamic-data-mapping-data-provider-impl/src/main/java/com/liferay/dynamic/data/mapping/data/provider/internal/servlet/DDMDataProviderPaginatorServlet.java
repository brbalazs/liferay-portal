/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider.internal.servlet;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderInvoker;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponseOutput;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.path=/dynamic-data-mapping-data-provider-paginator",
		"osgi.http.whiteboard.servlet.name=com.liferay.dynamic.data.mapping.data.provider.internal.servlet.DDMDataProviderPaginatorServlet",
		"osgi.http.whiteboard.servlet.pattern=/dynamic-data-mapping-data-provider-paginator/*"
	},
	service = {DDMDataProviderPaginatorServlet.class, Servlet.class}
)
public class DDMDataProviderPaginatorServlet extends HttpServlet {

	protected void addParametersFromRequest(
			DDMDataProviderRequest ddmDataProviderRequest,
			HttpServletRequest request)
		throws Exception {

		JSONObject inputParametersJSONObject = getInputParametersJSONObject(
			request);

		Iterator<String> iterator = inputParametersJSONObject.keys();

		iterator.forEachRemaining(
			inputParameterName -> ddmDataProviderRequest.queryString(
				inputParameterName,
				inputParametersJSONObject.getString(inputParameterName)));
	}

	@Override
	protected void doPost(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		List<Map<String, String>> dataProviderResult = executeDataProvider(
			request, response);

		if (dataProviderResult == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);

			return;
		}

		JSONSerializer jsonSerializer = _jsonFactory.createJSONSerializer();

		response.setContentType(ContentTypes.APPLICATION_JSON);
		response.setStatus(HttpServletResponse.SC_OK);

		ServletResponseUtil.write(
			response, jsonSerializer.serializeDeep(dataProviderResult));
	}

	protected List<Map<String, String>> executeDataProvider(
		HttpServletRequest request, HttpServletResponse response) {

		String dataProviderInstanceUUID = ParamUtil.getString(
			request, "dataProviderInstanceUUID");

		String outputParameterId = ParamUtil.getString(
			request, "outputParameterId");

		int start = ParamUtil.getInteger(request, "start");
		int end = ParamUtil.getInteger(request, "end");

		if (Validator.isNull(dataProviderInstanceUUID) ||
			Validator.isNull(outputParameterId) || (start < 0) || (end < 0) ||
			(end < start) || (start == end)) {

			return null;
		}

		List<Map<String, String>> dataProviderResult = new ArrayList<>();

		try {
			DDMDataProviderRequest ddmDataProviderRequest =
				new DDMDataProviderRequest(dataProviderInstanceUUID, request);

			addParametersFromRequest(ddmDataProviderRequest, request);

			ddmDataProviderRequest.queryString(
				"paginationStart", String.valueOf(start));
			ddmDataProviderRequest.queryString(
				"paginationEnd", String.valueOf(end));

			DDMDataProviderResponse ddmDataProviderResponse =
				_ddmDataProviderInvoker.invoke(ddmDataProviderRequest);

			DDMDataProviderResponseOutput ddmDataProviderResponseOutput =
				ddmDataProviderResponse.get(outputParameterId);

			if (ddmDataProviderResponseOutput == null) {
				return dataProviderResult;
			}

			List<KeyValuePair> keyValuePairs =
				ddmDataProviderResponseOutput.getValue(List.class);

			for (KeyValuePair keyValuePair : keyValuePairs) {
				Map<String, String> result = new HashMap<>();

				result.put("label", keyValuePair.getValue());
				result.put("value", keyValuePair.getKey());

				dataProviderResult.add(result);
			}
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}

		return dataProviderResult;
	}

	protected JSONObject getInputParametersJSONObject(
			HttpServletRequest request)
		throws JSONException {

		String inputParameters = ParamUtil.getString(
			request, "inputParameters");

		if (Validator.isNull(inputParameters)) {
			return _jsonFactory.createJSONObject();
		}

		return _jsonFactory.createJSONObject(inputParameters);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMDataProviderPaginatorServlet.class);

	private static final long serialVersionUID = 1L;

	@Reference
	private DDMDataProviderInvoker _ddmDataProviderInvoker;

	@Reference
	private JSONFactory _jsonFactory;

}