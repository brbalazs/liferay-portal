/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.resiliency.spi.agent;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.ActionResult;
import com.liferay.portal.kernel.resiliency.PortalResiliencyException;
import com.liferay.portal.kernel.resiliency.spi.SPI;
import com.liferay.portal.kernel.resiliency.spi.agent.SPIAgent;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collections;

import javax.portlet.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shuyang Zhou
 */
public class ErrorSPIAgent implements SPIAgent {

	@Override
	public void destroy() {
	}

	@Override
	public void init(SPI spi) {
	}

	@Override
	public HttpServletRequest prepareRequest(HttpServletRequest request) {
		return request;
	}

	@Override
	public HttpServletResponse prepareResponse(
		HttpServletRequest request, HttpServletResponse response) {

		return response;
	}

	@Override
	public void service(
			HttpServletRequest request, HttpServletResponse response)
		throws PortalResiliencyException {

		SPIAgent.Lifecycle lifecycle = (SPIAgent.Lifecycle)request.getAttribute(
			WebKeys.SPI_AGENT_LIFECYCLE);

		if (lifecycle.equals(SPIAgent.Lifecycle.ACTION)) {
			request.setAttribute(
				WebKeys.SPI_AGENT_ACTION_RESULT,
				new ActionResult(
					Collections.<Event>emptyList(), StringPool.SLASH));
		}
		else if (lifecycle.equals(SPIAgent.Lifecycle.EVENT)) {
			request.setAttribute(
				WebKeys.SPI_AGENT_EVENT_RESULT, Collections.emptyList());
		}
		else if (lifecycle.equals(SPIAgent.Lifecycle.RENDER)) {
			try {
				PrintWriter printWriter = response.getWriter();

				printWriter.write("<div class=\"alert alert-error\">");
				printWriter.write("SPI is temporarily unavailable.");
				printWriter.write("</div>");
			}
			catch (IOException ioe) {
				throw new PortalResiliencyException(ioe);
			}
		}
	}

	@Override
	public void transferResponse(
		HttpServletRequest request, HttpServletResponse response, Exception e) {
	}

}