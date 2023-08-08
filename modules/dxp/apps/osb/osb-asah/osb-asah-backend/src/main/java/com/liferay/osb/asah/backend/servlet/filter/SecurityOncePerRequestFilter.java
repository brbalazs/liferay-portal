/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.servlet.filter;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.servlet.filter.BaseSecurityOncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

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

		if (StringUtils.equals(httpServletRequest.getMethod(), "GET") &&
			StringUtils.contains(
				httpServletRequest.getRequestURI(), "/api/1.0/data-sources")) {

			return false;
		}

		if (StringUtils.contains(httpServletRequest.getRequestURI(), "/api/") &&
			!StringUtils.contains(
				httpServletRequest.getRequestURI(), "/recommendations") &&
			!StringUtils.contains(
				httpServletRequest.getRequestURI(), "/reports")) {

			if (!_dataSourceDog.existsDataSource(
					faroBackendSecuritySignature)) {

				logInvalidRequest(
					faroBackendSecuritySignature, httpServletRequest);

				return true;
			}

			return false;
		}

		return super.isInvalidRequest(httpServletRequest);
	}

	@Autowired
	private DataSourceDog _dataSourceDog;

}