/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.tck.bridge;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.util.ThreadUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.struts.StrutsActionRegistryUtil;
import com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration;
import com.liferay.portlet.tck.bridge.struts.PortletTCKStrutsAction;

import java.io.IOException;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import java.nio.charset.Charset;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletContext;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Tambara
 */
@Component(
	configurationPid = "com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE, service = {}
)
public class PortletTCKBridge {

	@Activate
	@Modified
	protected void activate(ComponentContext componentContext) {
		deactivate();

		StrutsActionRegistryUtil.register(_PATH, new PortletTCKStrutsAction());

		FutureTask<Void> futureTask = new FutureTask<>(
			new HandshakeServerCallable(
				ConfigurableUtil.createConfigurable(
					PortletTCKBridgeConfiguration.class,
					componentContext.getProperties())));

		_handshakeServerFuture = futureTask;

		Thread handshakeServerThread = new Thread(
			futureTask, "Handshake Server Thread");

		handshakeServerThread.setDaemon(true);

		handshakeServerThread.start();
	}

	@Deactivate
	protected void deactivate() {
		Future<Void> handshakeServerFuture = _handshakeServerFuture;

		if (handshakeServerFuture != null) {
			handshakeServerFuture.cancel(true);
		}

		StrutsActionRegistryUtil.unregister(_PATH);
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	private static final String _PATH = "/portal/tck";

	private static final Log _log = LogFactoryUtil.getLog(
		PortletTCKBridge.class);

	private volatile Future<Void> _handshakeServerFuture;

	private static class HandshakeServerCallable implements Callable<Void> {

		@Override
		public Void call() throws IOException {
			long startTime = System.currentTimeMillis();

			for (String servletContextName :
					_portletTCKBridgeConfiguration.servletContextNames()) {

				_waitForDeployment(
					servletContextName, startTime,
					_portletTCKBridgeConfiguration.timeout() * Time.SECOND);
			}

			try (ServerSocket serverSocket = new ServerSocket(
					_portletTCKBridgeConfiguration.handshakeServerPort())) {

				serverSocket.setSoTimeout(100);

				while (!Thread.interrupted()) {
					try (Socket socket = serverSocket.accept();
						OutputStream outputStream = socket.getOutputStream()) {

						outputStream.write(
							"Portlet TCK Bridge is ready".getBytes(
								Charset.defaultCharset()));
					}
					catch (SocketTimeoutException ste) {
					}
				}
			}

			return null;
		}

		private HandshakeServerCallable(
			PortletTCKBridgeConfiguration portletTCKBridgeConfiguration) {

			_portletTCKBridgeConfiguration = portletTCKBridgeConfiguration;
		}

		private void _waitForDeployment(
			String servletContextName, long startTime, long timeout) {

			while ((System.currentTimeMillis() - startTime) < timeout) {
				ServletContext serviceContext = ServletContextPool.get(
					servletContextName);

				if ((serviceContext == null) ||
					(serviceContext.getAttribute(WebKeys.PLUGIN_PORTLETS) ==
						null)) {

					try {
						Thread.sleep(100);
					}
					catch (InterruptedException ie) {
					}
				}
				else {
					return;
				}
			}

			_log.error("Timeout on waiting " + servletContextName);

			_log.error(ThreadUtil.threadDump());
		}

		private final PortletTCKBridgeConfiguration
			_portletTCKBridgeConfiguration;

	}

}