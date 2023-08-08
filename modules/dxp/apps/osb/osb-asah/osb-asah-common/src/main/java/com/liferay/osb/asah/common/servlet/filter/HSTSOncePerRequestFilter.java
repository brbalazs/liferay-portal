/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Riccardo Ferrari
 */
@Component
public class HSTSOncePerRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws IOException, ServletException {

		if (httpServletRequest.isSecure()) {
			httpServletResponse.setHeader(
				"Strict-Transport-Security",
				"max-age=31536000; includeSubDomains");
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

}