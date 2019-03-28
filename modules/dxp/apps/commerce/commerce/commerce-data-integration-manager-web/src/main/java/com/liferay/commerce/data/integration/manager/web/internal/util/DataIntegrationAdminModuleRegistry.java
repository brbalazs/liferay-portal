/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.manager.web.internal.util;

import com.liferay.commerce.data.integration.manager.web.internal.admin.api.DataIntegrationAdminModule;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author guywandji
 */
@Component(immediate = true, service = DataIntegrationAdminModuleRegistry.class)
public class DataIntegrationAdminModuleRegistry {

	public NavigableMap<String, DataIntegrationAdminModule>
			getDataIntegrationAdminModules(long groupId)
		throws PortalException {

		NavigableMap<String, DataIntegrationAdminModule>
			lrDataIntegrationAdminModules = new TreeMap<>();

		for (String key :
				_dataIntegrationAdminModuleServiceTrackerMap.keySet()) {

			DataIntegrationAdminModule lrDataIntegrationAdminModule =
				_dataIntegrationAdminModuleServiceTrackerMap.getService(key);

			if ((groupId < 0) ||
				lrDataIntegrationAdminModule.isVisible(groupId)) {

				lrDataIntegrationAdminModules.put(
					key, lrDataIntegrationAdminModule);
			}
		}

		return Collections.unmodifiableNavigableMap(
			lrDataIntegrationAdminModules);
	}

	public NavigableMap<String, DataIntegrationAdminModule>
		getLrDataIntegrationModules() {

		NavigableMap<String, DataIntegrationAdminModule>
			dataIntegrationAdminModules = new TreeMap<>();

		try {
			dataIntegrationAdminModules = getDataIntegrationAdminModules(-1);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}
		}

		return dataIntegrationAdminModules;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_dataIntegrationAdminModuleServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, DataIntegrationAdminModule.class,
				"data.integration.admin.module.key");
	}

	@Deactivate
	protected void deactivate() {
		_dataIntegrationAdminModuleServiceTrackerMap.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DataIntegrationAdminModuleRegistry.class);

	private ServiceTrackerMap<String, DataIntegrationAdminModule>
		_dataIntegrationAdminModuleServiceTrackerMap;

}