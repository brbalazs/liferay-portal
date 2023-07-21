/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth.verifier.test.internal.activator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.function.Supplier;

import javax.servlet.Servlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * @author Tomas Polesovsky
 */
public abstract class BaseAuthVerifierBundleActivator
	implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		doStart(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		for (ServiceRegistration<?> serviceRegistration :
				serviceRegistrations) {

			try {
				serviceRegistration.unregister();
			}
			catch (Exception e) {
			}
		}
	}

	protected abstract void doStart(BundleContext bundleContext);

	protected void registerServlet(
		Dictionary<String, Object> properties, Supplier<Servlet> supplier) {

		serviceRegistrations.add(
			_bundleContext.registerService(
				Servlet.class,
				new PrototypeServiceFactory<Servlet>() {

					@Override
					public Servlet getService(
						Bundle bundle,
						ServiceRegistration<Servlet> serviceRegistration) {

						return supplier.get();
					}

					@Override
					public void ungetService(
						Bundle bundle,
						ServiceRegistration<Servlet> serviceRegistration,
						Servlet servlet) {
					}

				},
				properties));
	}

	protected void registerServletContextHelper(
		String servletContextName, Dictionary<String, Object> properties) {

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
			servletContextName);
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH,
			"/" + servletContextName);

		serviceRegistrations.add(
			_bundleContext.registerService(
				ServletContextHelper.class,
				new ServletContextHelper(_bundleContext.getBundle()) {
				},
				properties));
	}

	protected final List<ServiceRegistration<?>> serviceRegistrations =
		new ArrayList<>();

	private BundleContext _bundleContext;

}