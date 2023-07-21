/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.functional.test.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.ContentResponseHandler;

/**
 * @author Cheryl Tang
 */
public class FaroResponseHandler implements ResponseHandler<AppResponse> {

	/**
	 * Handles the response from a request.
	 *
	 * @param  httpResponse the response to handle
	 * @return the AppResponse containing the status code and JSON object as a
	 *         String
	 * @throws IOException if an exception occurred
	 */
	@Override
	public AppResponse handleResponse(HttpResponse httpResponse)
		throws IOException {

		StatusLine statusLine = httpResponse.getStatusLine();

		int statusCode = statusLine.getStatusCode();

		String reasonPhrase = statusLine.getReasonPhrase();

		if (statusCode == 204) {
			return new AppResponse(statusCode, reasonPhrase, null);
		}

		HttpEntity httpEntity = httpResponse.getEntity();

		if (httpEntity == null) {
			throw new ClientProtocolException("Response contains no content");
		}

		ContentResponseHandler contentResponseHandler =
			new ContentResponseHandler();

		Content content = contentResponseHandler.handleEntity(httpEntity);

		return new AppResponse(statusCode, reasonPhrase, content.asString());
	}

}