/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.registry.internal;

import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.collections.ServiceTrackerMapFactory;
import com.liferay.registry.collections.ServiceTrackerMapFactoryUtil;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Raymond Augé
 */
public class RegistryImplBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Registry registry = new RegistryImpl(bundleContext);

		RegistryUtil.setRegistry(registry);

		_registryServiceRegistration = bundleContext.registerService(
			Registry.class, registry, null);

		ServiceTrackerMapFactoryImpl serviceTrackerMapFactory =
			new ServiceTrackerMapFactoryImpl(bundleContext);

		ServiceTrackerMapFactoryUtil.setServiceTrackerMapFactory(
			serviceTrackerMapFactory);

		_serviceTrackerMapFactoryServiceRegistration =
			bundleContext.registerService(
				ServiceTrackerMapFactory.class, serviceTrackerMapFactory, null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		_registryServiceRegistration.unregister();
		_serviceTrackerMapFactoryServiceRegistration.unregister();

		RegistryUtil.setRegistry(null);

		ServiceTrackerMapFactoryUtil.setServiceTrackerMapFactory(null);
	}

	private ServiceRegistration<Registry> _registryServiceRegistration;
	private ServiceRegistration<ServiceTrackerMapFactory>
		_serviceTrackerMapFactoryServiceRegistration;

}