/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.http.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.http.ChannelHttp;
import com.liferay.osb.asah.common.spring.http.Http;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import org.apache.commons.codec.digest.DigestUtils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author Geyson Silva
 */
@Component
public class ChannelHttpImpl implements ChannelHttp {

	@Override
	public void addChannel(Channel channel) {
		if (ServiceConstants.URL_FRONTEND != null) {
			_http.exchangeResponseEntity(
				ServiceConstants.URL_FRONTEND,
				String.format(
					"/o/faro/asah/%s/channel",
					ProjectIdThreadLocal.getProjectId()),
				HttpMethod.POST,
				_objectMapper.convertValue(channel, JSONObject.class),
				_getHttpHeaders());
		}
	}

	private HttpHeaders _getHttpHeaders() {
		return new HttpHeaders() {
			{
				set(
					HeaderConstants.FARO_BACKEND_SECURITY_SIGNATURE,
					DigestUtils.sha256Hex(
						_osbAsahSecurityToken.concat(
							ServiceConstants.URL_FRONTEND)));
			}
		};
	}

	@Autowired
	private Http _http;

	@Autowired
	private ObjectMapper _objectMapper;

	@Value("${osb.asah.security.token:}")
	private String _osbAsahSecurityToken;

}