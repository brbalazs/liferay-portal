/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.functional.test.util;

/**
 * @author Cheryl Tang
 */
public class AppResponse {

	public AppResponse(
		int httpStatusCode, String reasonPhrase, String responseBody) {

		_httpStatusCode = httpStatusCode;
		_reasonPhrase = reasonPhrase;
		_responseBody = responseBody;
	}

	public int getHttpStatusCode() {
		return _httpStatusCode;
	}

	public String getReasonPhrase() {
		return _reasonPhrase;
	}

	public String getResponseBody() {
		return _responseBody;
	}

	private final int _httpStatusCode;
	private final String _reasonPhrase;
	private final String _responseBody;

}