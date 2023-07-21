/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.internal.release;

import aQute.bnd.version.Version;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.ReleaseLocalService;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true, service = ReleasePublisher.class)
public final class ReleasePublisher {

	public void publish(Release release) {
		ServiceRegistration<Release> oldServiceRegistration =
			_serviceConfiguratorRegistrations.get(
				release.getServletContextName());

		if (oldServiceRegistration != null) {
			oldServiceRegistration.unregister();
		}

		Dictionary<String, Object> properties = new Hashtable<>();

		properties.put(
			"release.bundle.symbolic.name", release.getBundleSymbolicName());
		properties.put("release.state", release.getState());

		if (Version.isVersion(release.getSchemaVersion())) {
			properties.put(
				"release.schema.version",
				new Version(release.getSchemaVersion()));
		}

		ServiceRegistration<Release> newServiceRegistration =
			_bundleContext.registerService(Release.class, release, properties);

		_serviceConfiguratorRegistrations.put(
			release.getServletContextName(), newServiceRegistration);
	}

	public void publishInProgress(Release release) {
		release.setState(_STATE_IN_PROGRESS);

		publish(release);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		List<Release> releases = _releaseLocalService.getReleases(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Release release : releases) {
			publish(release);
		}
	}

	@Deactivate
	protected void deactivate() {
		for (ServiceRegistration<Release> serviceRegistration :
				_serviceConfiguratorRegistrations.values()) {

			serviceRegistration.unregister();
		}
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(unbind = "-")
	protected void setReleaseLocalService(
		ReleaseLocalService releaseLocalService) {

		_releaseLocalService = releaseLocalService;
	}

	private static final int _STATE_IN_PROGRESS = -1;

	private BundleContext _bundleContext;
	private ReleaseLocalService _releaseLocalService;
	private final Map<String, ServiceRegistration<Release>>
		_serviceConfiguratorRegistrations = new HashMap<>();

}