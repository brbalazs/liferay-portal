/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.exception;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Leslie Wong
 */
@Component
public class OSBAsahErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(
		WebRequest webRequest, ErrorAttributeOptions errorAttributeOptions) {

		Throwable throwable = getError(webRequest);

		if (throwable != null) {
			while ((throwable instanceof ServletException) &&
				   (throwable.getCause() != null)) {

				throwable = throwable.getCause();
			}
		}

		ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;

		HttpServletRequest httpServletRequest = servletWebRequest.getRequest();

		_log.error(
			String.format(
				"Unable to process the request with origin %s to path %s " +
					"with error %s",
				_getOrigin(httpServletRequest), _getPath(httpServletRequest),
				getMessage(webRequest, throwable)),
			throwable);

		OSBAsahError osbAsahError = new OSBAsahError(
			_environment.getActiveProfiles());

		osbAsahError.setErrorAttributes(
			super.getErrorAttributes(webRequest, errorAttributeOptions));

		return osbAsahError.getErrorAttributes();
	}

	private String _getOrigin(HttpServletRequest httpServletRequest) {
		String origin = httpServletRequest.getHeader(HttpHeaders.ORIGIN);

		if (StringUtils.isNotBlank(origin)) {
			return origin;
		}

		String referer = httpServletRequest.getHeader(HttpHeaders.REFERER);

		if (StringUtils.isNotBlank(referer)) {
			return referer;
		}

		return httpServletRequest.getRemoteAddr();
	}

	private String _getPath(HttpServletRequest httpServletRequest) {
		String forwardRequestURI = String.valueOf(
			httpServletRequest.getAttribute(
				RequestDispatcher.FORWARD_REQUEST_URI));

		if (StringUtils.isNotBlank(forwardRequestURI)) {
			return forwardRequestURI;
		}

		String forwardServletPath = String.valueOf(
			httpServletRequest.getAttribute(
				RequestDispatcher.FORWARD_SERVLET_PATH));

		if (StringUtils.isNotBlank(forwardServletPath)) {
			return forwardServletPath;
		}

		return String.valueOf(
			httpServletRequest.getAttribute(
				RequestDispatcher.ERROR_REQUEST_URI));
	}

	private static final Log _log = LogFactory.getLog(
		OSBAsahErrorAttributes.class);

	@Autowired
	private Environment _environment;

}