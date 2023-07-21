/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Leonardo Barros
 */
public class DDMDataProviderRequest {

	public DDMDataProviderRequest(
		String ddmDataProviderInstanceId,
		HttpServletRequest httpServletRequest) {

		_ddmDataProviderInstanceId = ddmDataProviderInstanceId;
		_httpServletRequest = httpServletRequest;
	}

	public DDMDataProviderContext getDDMDataProviderContext() {
		return _ddmDataProviderContext;
	}

	public String getDDMDataProviderInstanceId() {
		return _ddmDataProviderInstanceId;
	}

	public HttpServletRequest getHttpServletRequest() {
		return _httpServletRequest;
	}

	public Locale getLocale() {
		return _httpServletRequest.getLocale();
	}

	public String getParameter(String name) {
		return _parameters.get(name);
	}

	public <T> Optional<T> getParameterOptional(String name, Class<?> clazz) {
		Object value = _parameters.get(name);

		if (value == null) {
			return Optional.empty();
		}

		Class<?> valueClass = value.getClass();

		if (clazz.isAssignableFrom(valueClass)) {
			return Optional.of((T)value);
		}

		return Optional.empty();
	}

	public Map<String, String> getParameters() {
		return _parameters;
	}

	public void queryString(Map<String, String> parameters) {
		_parameters.putAll(parameters);
	}

	public void queryString(String name, String value) {
		_parameters.put(name, value);
	}

	public void setDDMDataProviderContext(
		DDMDataProviderContext ddmDataProviderContext) {

		_ddmDataProviderContext = ddmDataProviderContext;
	}

	private DDMDataProviderContext _ddmDataProviderContext;
	private final String _ddmDataProviderInstanceId;
	private final HttpServletRequest _httpServletRequest;
	private final Map<String, String> _parameters = new HashMap<>();

}