/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.client;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * @author Shinn Lok
 */
public class OSBAsahHttpComponentsClientHttpRequestFactory
	extends HttpComponentsClientHttpRequestFactory {

	@Override
	protected HttpUriRequest createHttpUriRequest(
		HttpMethod httpMethod, URI uri) {

		if (httpMethod == HttpMethod.GET) {
			return new HttpEntityEnclosingRequestBase() {
				{
					setURI(uri);
				}

				@Override
				public String getMethod() {
					return HttpMethod.GET.name();
				}

			};
		}

		return super.createHttpUriRequest(httpMethod, uri);
	}

}