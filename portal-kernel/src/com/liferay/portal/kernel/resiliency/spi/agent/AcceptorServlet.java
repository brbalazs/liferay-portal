/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.resiliency.spi.agent;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.resiliency.spi.SPI;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Shuyang Zhou
 */
public class AcceptorServlet extends HttpServlet {

	protected void doService(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		PortalUtil.setPortalInetSocketAddresses(request);

		ServletContext servletContext = getServletContext();

		String uriPath = PortalUtil.getPathContext();

		if (uriPath.isEmpty()) {
			uriPath = StringPool.SLASH;
		}

		ServletContext portalServletContext = servletContext.getContext(
			uriPath);

		RequestDispatcher requestDispatcher =
			portalServletContext.getRequestDispatcher("/c/portal/resiliency");

		SPI spi = SPIUtil.getSPI();

		SPIAgent spiAgent = spi.getSPIAgent();

		HttpServletRequest spiAgentHttpServletRequest = spiAgent.prepareRequest(
			request);

		HttpServletResponse spiAgentHttpServletResponse =
			spiAgent.prepareResponse(request, response);

		Exception exception = null;

		try {
			requestDispatcher.forward(
				spiAgentHttpServletRequest, spiAgentHttpServletResponse);
		}
		catch (Exception e) {
			exception = e;
		}

		spiAgent.transferResponse(
			spiAgentHttpServletRequest, spiAgentHttpServletResponse, exception);

		HttpSession session = spiAgentHttpServletRequest.getSession();

		session.invalidate();
	}

	@Override
	protected void service(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		try {
			doService(request, response);
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);

			throw ioe;
		}
		catch (RuntimeException re) {
			_log.error(re, re);

			throw re;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AcceptorServlet.class);

}