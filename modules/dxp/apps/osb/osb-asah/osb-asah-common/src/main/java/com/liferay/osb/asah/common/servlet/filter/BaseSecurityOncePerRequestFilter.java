/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.servlet.filter;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.servlet.util.ServletRequestUtil;

import java.io.IOException;

import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Vishal Reddy
 */
public abstract class BaseSecurityOncePerRequestFilter
	extends OncePerRequestFilter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilterInternal(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws IOException, ServletException {

		try {
			if (isInvalidRequest(httpServletRequest)) {
				String header = httpServletRequest.getHeader(
					HeaderConstants.FARO_BACKEND_SECURITY_SIGNATURE);

				if (header != null) {
					httpServletResponse.sendError(
						HttpServletResponse.SC_FORBIDDEN, "INVALID_TOKEN");
				}
				else {
					httpServletResponse.sendError(
						HttpServletResponse.SC_FORBIDDEN);
				}

				return;
			}

			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		catch (IOException | ServletException exception) {
			throw exception;
		}
		catch (Exception exception) {
			throw new ServletException(exception);
		}
	}

	protected boolean isInvalidRequest(HttpServletRequest httpServletRequest) {
		String faroBackendSecuritySignature = httpServletRequest.getHeader(
			HeaderConstants.FARO_BACKEND_SECURITY_SIGNATURE);

		if (faroBackendSecuritySignature == null) {
			logInvalidRequest(null, httpServletRequest);

			return true;
		}

		if (!Objects.equals(
				faroBackendSecuritySignature,
				DigestUtils.sha256Hex(
					_osbAsahSecurityToken.concat(
						ServletRequestUtil.getOriginalURL(
							httpServletRequest))))) {

			logInvalidRequest(faroBackendSecuritySignature, httpServletRequest);

			return true;
		}

		return false;
	}

	protected void logInvalidRequest(
		String faroBackendSecuritySignature,
		HttpServletRequest httpServletRequest) {

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"%s attempted to access %s with an invalid security " +
						"signature %s",
					httpServletRequest.getRemoteAddr(),
					httpServletRequest.getRequestURI(),
					faroBackendSecuritySignature));
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest httpServletRequest) {
		if (ServiceConstants.isInternalServiceURL(
				ServletRequestUtil.getURL(httpServletRequest))) {

			return true;
		}

		String method = httpServletRequest.getMethod();
		String requestURI = httpServletRequest.getRequestURI();

		if (method.equals(HttpMethod.GET.name()) &&
			(requestURI.equals("/") || requestURI.equals("/context"))) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactory.getLog(
		BaseSecurityOncePerRequestFilter.class);

	@Value("${osb.asah.security.token}")
	private String _osbAsahSecurityToken;

}