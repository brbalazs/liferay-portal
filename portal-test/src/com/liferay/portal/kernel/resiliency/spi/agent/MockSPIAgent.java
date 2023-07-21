/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.resiliency.spi.agent;

import com.liferay.portal.kernel.nio.intraband.RegistrationReference;
import com.liferay.portal.kernel.resiliency.spi.SPI;
import com.liferay.portal.kernel.resiliency.spi.SPIConfiguration;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shuyang Zhou
 */
public class MockSPIAgent implements SPIAgent {

	public MockSPIAgent(
		SPIConfiguration spiConfiguration,
		RegistrationReference registrationReference) {
	}

	@Override
	public void destroy() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void init(SPI spi) {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServletRequest prepareRequest(HttpServletRequest request)
		throws IOException {

		throw new UnsupportedOperationException();
	}

	@Override
	public HttpServletResponse prepareResponse(
		HttpServletRequest request, HttpServletResponse response) {

		throw new UnsupportedOperationException();
	}

	@Override
	public void service(
		HttpServletRequest request, HttpServletResponse response) {

		throw new UnsupportedOperationException();
	}

	@Override
	public void transferResponse(
		HttpServletRequest request, HttpServletResponse response, Exception e) {

		throw new UnsupportedOperationException();
	}

}