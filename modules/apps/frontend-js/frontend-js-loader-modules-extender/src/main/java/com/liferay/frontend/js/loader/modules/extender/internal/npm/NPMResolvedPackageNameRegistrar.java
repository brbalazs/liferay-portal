/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.internal.npm;

import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolvedPackageNameUtil;
import com.liferay.portal.kernel.util.StringBundler;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Iván Zaera Avellón
 */
public class NPMResolvedPackageNameRegistrar
	implements ServiceTrackerCustomizer<ServletContext, ServletContext> {

	public NPMResolvedPackageNameRegistrar(
			BundleContext bundleContext, Bundle bundle,
			String npmResolvedPackageName)
		throws InvalidSyntaxException {

		_bundleContext = bundleContext;
		_npmResolvedPackageName = npmResolvedPackageName;

		_serviceTracker = new ServiceTracker<>(
			_bundleContext, _getServletContextFilter(bundle), this);
	}

	@Override
	public ServletContext addingService(
		ServiceReference<ServletContext> serviceReference) {

		try {
			ServletContext servletContext = _bundleContext.getService(
				serviceReference);

			NPMResolvedPackageNameUtil.set(
				servletContext, _npmResolvedPackageName);
		}
		finally {
			_bundleContext.ungetService(serviceReference);
		}

		close();

		return null;
	}

	public void close() {
		_serviceTracker.close();
	}

	@Override
	public void modifiedService(
		ServiceReference<ServletContext> reference, ServletContext service) {
	}

	public void open() {
		_serviceTracker.open();
	}

	@Override
	public void removedService(
		ServiceReference<ServletContext> reference, ServletContext service) {
	}

	private Filter _getServletContextFilter(Bundle bundle)
		throws InvalidSyntaxException {

		return _bundleContext.createFilter(
			StringBundler.concat(
				"(&(objectClass=", ServletContext.class.getName(), ")",
				"(service.bundleid=", String.valueOf(bundle.getBundleId()),
				"))"));
	}

	private final BundleContext _bundleContext;
	private final String _npmResolvedPackageName;
	private final ServiceTracker<ServletContext, ServletContext>
		_serviceTracker;

}