/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Function;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author André de Oliveira
 */
public class ServiceContextFunction
	implements Function<String, ServiceContext> {

	public ServiceContextFunction(HttpServletRequest request) {
		_request = request;

		_portletRequest = null;
	}

	public ServiceContextFunction(PortletRequest portletRequest) {
		_portletRequest = portletRequest;

		_request = null;
	}

	@Override
	public ServiceContext apply(String className) {
		try {
			if (_portletRequest != null) {
				return ServiceContextFactory.getInstance(
					className, _portletRequest);
			}

			return ServiceContextFactory.getInstance(className, _request);
		}
		catch (PortalException pe) {
			throw new RuntimeException(pe);
		}
	}

	private final PortletRequest _portletRequest;
	private final HttpServletRequest _request;

}