/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.http.impl;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.http.EmailHttp;
import com.liferay.osb.asah.common.spring.http.Http;

import org.apache.commons.codec.digest.DigestUtils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class EmailHttpImpl implements EmailHttp {

	@Override
	public void sendEmail(JSONObject jsonObject) {
		if (ServiceConstants.URL_FRONTEND != null) {
			_http.exchangeResponseEntity(
				ServiceConstants.URL_FRONTEND, "/o/email", HttpMethod.POST,
				jsonObject.toString(),
				_getHttpHeaders(ServiceConstants.URL_FRONTEND));
		}
	}

	private HttpHeaders _getHttpHeaders(String url) {
		return new HttpHeaders() {
			{
				set(
					HeaderConstants.FARO_BACKEND_SECURITY_SIGNATURE,
					DigestUtils.sha256Hex(_osbAsahSecurityToken.concat(url)));
			}
		};
	}

	@Autowired
	private Http _http;

	@Value("${osb.asah.security.token:}")
	private String _osbAsahSecurityToken;

}