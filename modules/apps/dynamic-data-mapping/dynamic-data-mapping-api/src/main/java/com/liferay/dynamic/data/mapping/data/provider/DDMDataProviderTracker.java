/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Marcellus Tavares
 */
@Component(immediate = true, service = DDMDataProviderTracker.class)
public class DDMDataProviderTracker {

	public DDMDataProvider getDDMDataProvider(String type) {
		return _ddmDataProviderTypeTrackerMap.getService(type);
	}

	public DDMDataProvider getDDMDataProviderByInstanceId(String instanceId) {
		return _ddmDataProviderInstanceIdTrackerMap.getService(instanceId);
	}

	public List<DDMDataProviderContextContributor>
		getDDMDataProviderContextContributors(String type) {

		List<DDMDataProviderContextContributor>
			ddmDataProviderContextContributors =
				_ddmDataProviderContextContributorTrackerMap.getService(type);

		if (ddmDataProviderContextContributors != null) {
			return ddmDataProviderContextContributors;
		}

		return Collections.emptyList();
	}

	public Set<String> getDDMDataProviderTypes() {
		return _ddmDataProviderTypeTrackerMap.keySet();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_ddmDataProviderContextContributorTrackerMap =
			ServiceTrackerMapFactory.openMultiValueMap(
				bundleContext, DDMDataProviderContextContributor.class,
				"ddm.data.provider.type");

		_ddmDataProviderInstanceIdTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, DDMDataProvider.class,
				"ddm.data.provider.instance.id");

		_ddmDataProviderTypeTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, DDMDataProvider.class, "ddm.data.provider.type");
	}

	@Deactivate
	protected void deactivate() {
		_ddmDataProviderContextContributorTrackerMap.close();

		_ddmDataProviderInstanceIdTrackerMap.close();

		_ddmDataProviderTypeTrackerMap.close();
	}

	private ServiceTrackerMap<String, List<DDMDataProviderContextContributor>>
		_ddmDataProviderContextContributorTrackerMap;
	private ServiceTrackerMap<String, DDMDataProvider>
		_ddmDataProviderInstanceIdTrackerMap;
	private ServiceTrackerMap<String, DDMDataProvider>
		_ddmDataProviderTypeTrackerMap;

}