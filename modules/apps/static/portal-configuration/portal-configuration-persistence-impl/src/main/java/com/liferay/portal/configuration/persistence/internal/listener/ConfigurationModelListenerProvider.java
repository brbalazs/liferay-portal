/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.persistence.internal.listener;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListener;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Drew Brokke
 */
@Component(immediate = true, service = {})
public class ConfigurationModelListenerProvider {

	public static ConfigurationModelListener getConfigurationModelListener(
		String configurationModelClassName) {

		if (_serviceTrackerMap == null) {
			return null;
		}

		return _serviceTrackerMap.getService(configurationModelClassName);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, ConfigurationModelListener.class,
			"model.class.name");
	}

	private static ServiceTrackerMap<String, ConfigurationModelListener>
		_serviceTrackerMap;

}