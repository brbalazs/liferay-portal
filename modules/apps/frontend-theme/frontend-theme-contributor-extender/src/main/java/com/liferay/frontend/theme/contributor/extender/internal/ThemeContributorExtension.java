/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.theme.contributor.extender.internal;

import com.liferay.frontend.theme.contributor.extender.BundleWebResources;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.servlet.PortalWebResourceConstants;
import com.liferay.portal.kernel.servlet.PortalWebResources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.apache.felix.utils.extender.Extension;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 * @author Michael Bradford
 */
public class ThemeContributorExtension implements Extension {

	public ThemeContributorExtension(
		Bundle bundle, BundleWebResourcesImpl bundleWebResources, int weight) {

		_bundle = bundle;
		_bundleWebResources = bundleWebResources;
		_weight = weight;
	}

	@Override
	public void destroy() throws Exception {
		_serviceTracker.close();
	}

	@Override
	public void start() throws Exception {
		final BundleContext bundleContext = _bundle.getBundleContext();

		String filter = StringBundler.concat(
			"(&(objectClass=", ServletContext.class.getName(),
			")(osgi.web.symbolicname=", _bundle.getSymbolicName(), "))");

		final Dictionary<String, Object> properties = new Hashtable<>();

		properties.put("service.ranking", _weight);

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, filter,
			new ServiceTrackerCustomizer
				<ServletContext, Collection<ServiceRegistration<?>>>() {

				@Override
				public Collection<ServiceRegistration<?>> addingService(
					ServiceReference<ServletContext> serviceReference) {

					Collection<ServiceRegistration<?>> serviceRegistrations =
						new ArrayList<>();

					ServletContext servletContext = bundleContext.getService(
						serviceReference);

					serviceRegistrations.add(
						bundleContext.registerService(
							PortalWebResources.class.getName(),
							new ThemeContributorPortalWebResources(
								servletContext),
							properties));

					String contextPath = servletContext.getContextPath();

					_bundleWebResources.setServletContextPath(contextPath);

					serviceRegistrations.add(
						bundleContext.registerService(
							BundleWebResources.class, _bundleWebResources,
							properties));

					return serviceRegistrations;
				}

				@Override
				public void modifiedService(
					ServiceReference<ServletContext> serviceReference,
					Collection<ServiceRegistration<?>> service) {

					removedService(serviceReference, service);

					addingService(serviceReference);
				}

				@Override
				public void removedService(
					ServiceReference<ServletContext> serviceReference,
					Collection<ServiceRegistration<?>> serviceRegistrations) {

					for (ServiceRegistration<?> serviceRegistration :
							serviceRegistrations) {

						serviceRegistration.unregister();
					}

					bundleContext.ungetService(serviceReference);
				}

			});
	}

	private final Bundle _bundle;
	private final BundleWebResourcesImpl _bundleWebResources;
	private ServiceTracker<ServletContext, Collection<ServiceRegistration<?>>>
		_serviceTracker;
	private final int _weight;

	private class ThemeContributorPortalWebResources
		implements PortalWebResources {

		@Override
		public String getContextPath() {
			return _servletContext.getContextPath();
		}

		@Override
		public long getLastModified() {
			return _bundle.getLastModified();
		}

		@Override
		public String getResourceType() {
			return PortalWebResourceConstants.RESOURCE_TYPE_THEME_CONTRIBUTOR;
		}

		@Override
		public ServletContext getServletContext() {
			return _servletContext;
		}

		private ThemeContributorPortalWebResources(
			ServletContext servletContext) {

			_servletContext = servletContext;
		}

		private final ServletContext _servletContext;

	}

}