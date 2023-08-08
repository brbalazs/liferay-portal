/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.client;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * @author Shinn Lok
 */
public class OSBAsahResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse clientHttpResponse)
		throws IOException {

		HttpStatus httpStatus = clientHttpResponse.getStatusCode();

		if (hasError(clientHttpResponse)) {
			throw new HttpClientErrorException(httpStatus);
		}
	}

	@Override
	public boolean hasError(ClientHttpResponse clientHttpResponse)
		throws IOException {

		HttpStatus httpStatus = clientHttpResponse.getStatusCode();

		if ((httpStatus.series() == HttpStatus.Series.CLIENT_ERROR) ||
			(httpStatus.series() == HttpStatus.Series.SERVER_ERROR)) {

			return true;
		}

		return false;
	}

}