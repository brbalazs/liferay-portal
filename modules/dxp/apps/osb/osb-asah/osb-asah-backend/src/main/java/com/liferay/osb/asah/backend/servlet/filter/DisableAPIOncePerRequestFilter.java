/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Leslie Wong
 */
@Component
@ConditionalOnProperty("osb.asah.disable.api")
public class DisableAPIOncePerRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws IOException, ServletException {

		String method = httpServletRequest.getMethod();
		String requestURI = httpServletRequest.getRequestURI();

		if (requestURI.startsWith("/api/1.0") ||
			requestURI.startsWith("/api/recommendations") ||
			requestURI.startsWith("/api/reports") ||
			(requestURI.startsWith("/data-sources") &&
			 (StringUtils.equals(method, HttpMethod.DELETE.name()) ||
			  StringUtils.equals(method, HttpMethod.PATCH.name()) ||
			  StringUtils.equals(method, HttpMethod.POST.name())))) {

			httpServletResponse.sendError(
				HttpServletResponse.SC_SERVICE_UNAVAILABLE);

			return;
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

}