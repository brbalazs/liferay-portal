/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.js.loader.modules.extender.internal.resolution;

import com.liferay.frontend.js.loader.modules.extender.npm.JSBundle;
import com.liferay.frontend.js.loader.modules.extender.npm.JSBundleTracker;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMRegistry;
import com.liferay.osgi.util.ServiceTrackerFactory;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Iván Zaera
 */
@Component(immediate = true, service = JSBundleTracker.class)
public class BrowserModuleNameMapperCacheInvalidator
	implements JSBundleTracker {

	@Override
	public void addedJSBundle(
		JSBundle jsBundle, Bundle bundle, NPMRegistry npmRegistry) {

		BrowserModuleNameMapper browserModuleNameMapper =
			_serviceTracker.getService();

		if (browserModuleNameMapper != null) {
			browserModuleNameMapper.clearCache();
		}
	}

	@Override
	public void removedJSBundle(
		JSBundle jsBundle, Bundle bundle, NPMRegistry npmRegistry) {

		BrowserModuleNameMapper browserModuleNameMapper =
			_serviceTracker.getService();

		if (browserModuleNameMapper != null) {
			browserModuleNameMapper.clearCache();
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTracker = ServiceTrackerFactory.create(
			bundleContext, BrowserModuleNameMapper.class, null);

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private ServiceTracker<BrowserModuleNameMapper, BrowserModuleNameMapper>
		_serviceTracker;

}