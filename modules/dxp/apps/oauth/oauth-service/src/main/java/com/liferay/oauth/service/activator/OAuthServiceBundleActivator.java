/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth.service.activator;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.upgrade.release.BaseUpgradeServiceModuleRelease;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Carlos Sierra Andrés
 */
public class OAuthServiceBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		Filter filter = context.createFilter(
			StringBundler.concat(
				"(&(objectClass=", ModuleServiceLifecycle.class.getName(), ")",
				ModuleServiceLifecycle.DATABASE_INITIALIZED, ")"));

		_serviceTracker = new ServiceTracker<Object, Object>(
			context, filter, null) {

			@Override
			public Object addingService(ServiceReference<Object> reference) {
				try {
					OAuthUpgradeServiceModuleRelease
						upgradeServiceModuleRelease =
							new OAuthUpgradeServiceModuleRelease();

					upgradeServiceModuleRelease.upgrade();

					return null;
				}
				catch (UpgradeException ue) {
					throw new RuntimeException(ue);
				}
			}

		};

		_serviceTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		_serviceTracker.close();
	}

	private ServiceTracker<Object, Object> _serviceTracker;

	private static class OAuthUpgradeServiceModuleRelease
		extends BaseUpgradeServiceModuleRelease {

		@Override
		protected String getNamespace() {
			return "OAuth";
		}

		@Override
		protected String getNewBundleSymbolicName() {
			return "com.liferay.oauth.service";
		}

		@Override
		protected String getOldBundleSymbolicName() {
			return "oauth-portlet";
		}

	}

}