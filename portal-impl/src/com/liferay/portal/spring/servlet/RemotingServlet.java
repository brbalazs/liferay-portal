/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.servlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.access.control.AccessControlThreadLocal;
import com.liferay.portal.spring.context.TunnelApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Brian Wing Shun Chan
 */
public class RemotingServlet extends DispatcherServlet {

	public static final String CONTEXT_CLASS =
		TunnelApplicationContext.class.getName();

	public static final String CONTEXT_CONFIG_LOCATION =
		"/WEB-INF/remoting-servlet.xml,/WEB-INF/remoting-servlet-ext.xml";

	@Override
	public Class<?> getContextClass() {
		try {
			return Class.forName(CONTEXT_CLASS);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return null;
	}

	@Override
	public String getContextConfigLocation() {
		return CONTEXT_CONFIG_LOCATION;
	}

	@Override
	public void service(
			HttpServletRequest request, HttpServletResponse response)
		throws ServletException {

		boolean remoteAccess = AccessControlThreadLocal.isRemoteAccess();

		try {
			AccessControlThreadLocal.setRemoteAccess(true);

			super.service(request, response);
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
		finally {
			AccessControlThreadLocal.setRemoteAccess(remoteAccess);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RemotingServlet.class);

}