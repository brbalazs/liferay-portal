/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.servlet.PortalWebResourceConstants;
import com.liferay.portal.kernel.servlet.PortalWebResources;
import com.liferay.portal.servlet.delegate.ServletContextDelegate;

import java.util.Map;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Raymond Augé
 */
@Component(
	configurationPid = "com.liferay.frontend.js.loader.modules.extender.internal.Details",
	enabled = false, immediate = true,
	service = JSLoaderModulesPortalWebResources.class
)
public class JSLoaderModulesPortalWebResources {

	@Activate
	@Modified
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_bundleContext = bundleContext;

		Details details = ConfigurableUtil.createConfigurable(
			Details.class, properties);

		_portalWebResources = new InternalPortalWebResources(
			_jsLoaderModulesServlet.getServletContext());

		_setEnabled(!details.useLoaderVersion4x());
	}

	@Deactivate
	protected void deactivate() {
		_setEnabled(false);
	}

	private synchronized void _setEnabled(boolean enabled) {
		if (enabled && (_serviceRegistration == null)) {
			_serviceRegistration = _bundleContext.registerService(
				PortalWebResources.class, _portalWebResources, null);
		}
		else if (!enabled && (_serviceRegistration != null)) {
			_serviceRegistration.unregister();

			_serviceRegistration = null;
		}
	}

	private BundleContext _bundleContext;

	@Reference
	private JSLoaderModulesServlet _jsLoaderModulesServlet;

	@Reference
	private JSLoaderModulesTracker _jsLoaderModulesTracker;

	private InternalPortalWebResources _portalWebResources;
	private ServiceRegistration<?> _serviceRegistration;

	private class InternalPortalWebResources implements PortalWebResources {

		@Override
		public String getContextPath() {
			return _servletContext.getContextPath();
		}

		@Override
		public long getLastModified() {
			return _jsLoaderModulesTracker.getLastModified();
		}

		@Override
		public String getResourceType() {
			return PortalWebResourceConstants.RESOURCE_TYPE_JS_LOADER_MODULES;
		}

		@Override
		public ServletContext getServletContext() {
			return _servletContext;
		}

		private InternalPortalWebResources(ServletContext servletContext) {
			_servletContext = ServletContextDelegate.create(servletContext);
		}

		private final ServletContext _servletContext;

	}

}