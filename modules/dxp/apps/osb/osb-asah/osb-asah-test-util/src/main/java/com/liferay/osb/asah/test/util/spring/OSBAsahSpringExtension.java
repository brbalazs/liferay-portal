/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.spring;

import com.liferay.osb.asah.common.constants.ServiceConstants;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.TimeZone;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.jupiter.api.extension.ExtensionContext;

import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Marcellus Tavares
 * @author André Miranda
 */
public class OSBAsahSpringExtension extends SpringExtension {

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
	}

	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
	}

	@Override
	public void afterTestExecution(ExtensionContext extensionContext)
		throws Exception {
	}

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		if (!_isPostgreSQLUp()) {
			throw new IllegalStateException(
				"Integration test infrastructure is not up. Please run " +
					"\"docker-compose -f docker-compose.integration-test.yml " +
						"up -d\" from the root project directory.");
		}

		System.setProperty(
			"spring.autoconfigure.exclude",
			String.join(
				",", JooqAutoConfiguration.class.getName(),
				ManagementWebSecurityAutoConfiguration.class.getName(),
				SecurityAutoConfiguration.class.getName()));
		System.setProperty(
			"spring.main.allow-bean-definition-overriding", "true");
		System.setProperty("spring.profiles.active", "test");

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
	}

	@Override
	public void beforeTestExecution(ExtensionContext extensionContext)
		throws Exception {
	}

	@Override
	public void postProcessTestInstance(
			Object testInstance, ExtensionContext extensionContext)
		throws Exception {
	}

	private boolean _isPostgreSQLUp() {
		return _pingHost(ServiceConstants.POSTGRESQL_SERVER_IP, 5432, 3000);
	}

	private boolean _pingHost(String hostname, int port, int timeout) {
		SocketFactory socketFactory = SSLSocketFactory.getDefault();

		try (Socket socket = socketFactory.createSocket()) {
			socket.connect(new InetSocketAddress(hostname, port), timeout);

			return true;
		}
		catch (IOException ioException) {
			_log.error(ioException, ioException);

			return false;
		}
	}

	private static final Log _log = LogFactory.getLog(
		OSBAsahSpringExtension.class);

}