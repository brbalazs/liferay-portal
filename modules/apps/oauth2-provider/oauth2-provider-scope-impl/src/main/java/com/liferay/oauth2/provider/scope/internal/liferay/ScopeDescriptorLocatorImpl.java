/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.oauth2.provider.scope.internal.liferay;

import com.liferay.oauth2.provider.scope.internal.constants.OAuth2ProviderScopeConstants;
import com.liferay.oauth2.provider.scope.liferay.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.spi.scope.descriptor.ScopeDescriptor;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Sierra Andrés
 */
@Component(service = ScopeDescriptorLocator.class)
public class ScopeDescriptorLocatorImpl implements ScopeDescriptorLocator {

	@Override
	public ScopeDescriptor getScopeDescriptor(String applicationName) {
		ScopeDescriptor scopeDescriptor =
			_scopeDescriptorsByApplicationName.getService(applicationName);

		if (scopeDescriptor == null) {
			return _defaultScopeDescriptor;
		}

		return scopeDescriptor;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_scopeDescriptorsByApplicationName =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeDescriptor.class,
				OAuth2ProviderScopeConstants.OSGI_JAXRS_NAME);
		_scopeDescriptorsByCompany =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ScopeDescriptor.class, "company.id");
	}

	@Deactivate
	protected void deactivate() {
		_scopeDescriptorsByApplicationName.close();

		_scopeDescriptorsByCompany.close();
	}

	@Reference(target = "(default=true)")
	private ScopeDescriptor _defaultScopeDescriptor;

	private ServiceTrackerMap<String, ScopeDescriptor>
		_scopeDescriptorsByApplicationName;
	private ServiceTrackerMap<String, ScopeDescriptor>
		_scopeDescriptorsByCompany;

}