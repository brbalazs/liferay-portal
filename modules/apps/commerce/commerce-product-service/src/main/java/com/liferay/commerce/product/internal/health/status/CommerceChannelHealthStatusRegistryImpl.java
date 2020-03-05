/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.internal.health.status;

import com.liferay.commerce.product.channel.CommerceChannelHealthStatus;
import com.liferay.commerce.product.channel.CommerceChannelHealthStatusRegistry;
import com.liferay.commerce.product.internal.health.status.comparator.CommerceChannelHealthStatusServiceWrapperDisplayOrderComparator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory.ServiceWrapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true, service = CommerceChannelHealthStatusRegistry.class
)
public class CommerceChannelHealthStatusRegistryImpl
	implements CommerceChannelHealthStatusRegistry {

	@Override
	public CommerceChannelHealthStatus getCommerceChannelHealthStatus(
		String key) {

		if (Validator.isNull(key)) {
			return null;
		}

		ServiceWrapper<CommerceChannelHealthStatus>
			CommerceChannelHealthStatusServiceWrapper =
				_CommerceChannelHealthStatusRegistryMap.getService(key);

		if (CommerceChannelHealthStatusServiceWrapper == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"No commerce health status registered with key " + key);
			}

			return null;
		}

		return CommerceChannelHealthStatusServiceWrapper.getService();
	}

	@Override
	public List<CommerceChannelHealthStatus>
		getCommerceChannelHealthStatuses() {

		List<CommerceChannelHealthStatus> CommerceChannelHealthStatuses =
			new ArrayList<>();

		List<ServiceWrapper<CommerceChannelHealthStatus>>
			CommerceChannelHealthStatusServiceWrappers =
				ListUtil.fromCollection(
					_CommerceChannelHealthStatusRegistryMap.values());

		Collections.sort(
			CommerceChannelHealthStatusServiceWrappers,
			_CommerceChannelHealthStatusServiceWrapperDisplayOrderComparator);

		for (ServiceWrapper<CommerceChannelHealthStatus>
				CommerceChannelHealthStatusServiceWrapper :
					CommerceChannelHealthStatusServiceWrappers) {

			CommerceChannelHealthStatuses.add(
				CommerceChannelHealthStatusServiceWrapper.getService());
		}

		return Collections.unmodifiableList(CommerceChannelHealthStatuses);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_CommerceChannelHealthStatusRegistryMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, CommerceChannelHealthStatus.class,
				"commerce.channel.health.status.key",
				ServiceTrackerCustomizerFactory.
					<CommerceChannelHealthStatus>serviceWrapper(bundleContext));
	}

	@Deactivate
	protected void deactivate() {
		_CommerceChannelHealthStatusRegistryMap.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceChannelHealthStatusRegistryImpl.class);

	private static final Comparator<ServiceWrapper<CommerceChannelHealthStatus>>
		_CommerceChannelHealthStatusServiceWrapperDisplayOrderComparator =
			new CommerceChannelHealthStatusServiceWrapperDisplayOrderComparator();

	private ServiceTrackerMap
		<String, ServiceWrapper<CommerceChannelHealthStatus>>
			_CommerceChannelHealthStatusRegistryMap;

}