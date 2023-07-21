/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.security.auth;

import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tomas Polesovsky
 * @author Raymond Augé
 */
public class AccessControlContext {

	public AuthVerifierResult getAuthVerifierResult() {
		return _authVerifierResult;
	}

	public HttpServletRequest getRequest() {
		return _request;
	}

	public HttpServletResponse getResponse() {
		return _response;
	}

	public Map<String, Object> getSettings() {
		return _settings;
	}

	public void setAuthVerifierResult(AuthVerifierResult authVerifierResult) {
		_authVerifierResult = authVerifierResult;
	}

	public void setRequest(HttpServletRequest request) {
		_request = request;
	}

	public void setResponse(HttpServletResponse response) {
		_response = response;
	}

	public static enum Settings {

		SERVICE_DEPTH

	}

	private AuthVerifierResult _authVerifierResult;
	private HttpServletRequest _request;
	private HttpServletResponse _response;
	private final Map<String, Object> _settings = new HashMap<>();

}