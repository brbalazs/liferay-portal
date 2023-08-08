/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.servlet.filter;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.servlet.filter.BaseSecurityOncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Shinn Lok
 */
@Component
@ConditionalOnProperty("osb.asah.security.enabled")
public class SecurityOncePerRequestFilter
	extends BaseSecurityOncePerRequestFilter {

	@Override
	protected boolean isInvalidRequest(HttpServletRequest httpServletRequest) {
		String faroBackendSecuritySignature = httpServletRequest.getHeader(
			HeaderConstants.FARO_BACKEND_SECURITY_SIGNATURE);

		if (faroBackendSecuritySignature == null) {
			logInvalidRequest(null, httpServletRequest);

			return true;
		}

		if (!_dataSourceDog.existsDataSource(faroBackendSecuritySignature)) {
			logInvalidRequest(faroBackendSecuritySignature, httpServletRequest);

			return true;
		}

		return false;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest httpServletRequest) {
		String requestURI = httpServletRequest.getRequestURI();

		if (requestURI.equals("/") || requestURI.equals("/identity")) {
			return true;
		}

		return super.shouldNotFilter(httpServletRequest);
	}

	@Autowired
	private DataSourceDog _dataSourceDog;

}