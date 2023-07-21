/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.remote.jaxrs.whiteboard.test.internal.activator;

import com.liferay.portal.remote.jaxrs.whiteboard.test.internal.service.Addon;
import com.liferay.portal.remote.jaxrs.whiteboard.test.internal.service.Greeter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Carlos Sierra Andrés
 */
public class RemoteJaxrsWhiteboardTestBundleActivator
	implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> properties = new Hashtable<>();

		properties.put("liferay.auth.verifier", false);
		properties.put("liferay.oauth2", false);
		properties.put("osgi.jaxrs.application.base", "/test-rest/greeter1");

		_serviceRegistrations.add(
			context.registerService(
				Application.class, new Greeter(), properties));

		properties.put("liferay.auth.verifier", false);
		properties.put("liferay.oauth2", false);
		properties.put("osgi.jaxrs.application.base", "/test-rest/greeter2");

		_serviceRegistrations.add(
			context.registerService(
				Application.class, new Greeter(), properties));

		properties.put("addonable", Boolean.TRUE);
		properties.put("liferay.auth.verifier", false);
		properties.put("liferay.oauth2", false);
		properties.put("osgi.jaxrs.application.base", "/test-rest/greeter3");

		_serviceRegistrations.add(
			context.registerService(
				Application.class, new Greeter(), properties));

		properties = new Hashtable<>();

		properties.put("osgi.jaxrs.application.select", "(addonable=true)");
		properties.put("osgi.jaxrs.resource", Boolean.TRUE);

		_serviceRegistrations.add(
			context.registerService(Object.class, new Addon(), properties));
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		for (ServiceRegistration<?> serviceRegistration :
				_serviceRegistrations) {

			try {
				serviceRegistration.unregister();
			}
			catch (Exception e) {
			}
		}
	}

	private final List<ServiceRegistration<?>> _serviceRegistrations =
		new ArrayList<>();

}