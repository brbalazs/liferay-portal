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

package com.liferay.commerce.internal.price;

import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.price.CommerceProductPriceCalculationRegistry;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Riccardo Alberti
 */
@Component(
	immediate = true, service = CommerceProductPriceCalculationRegistry.class
)
public class CommerceProductPriceCalculationRegistryImpl
	implements CommerceProductPriceCalculationRegistry {

	@Override
	public CommerceProductPriceCalculation getCommerceProductPrice(String key) {
		return _serviceTrackerMap.getService(key);
	}

	@Override
	public Map<String, CommerceProductPriceCalculation>
		getCommerceProductPrices() {

		Map<String, CommerceProductPriceCalculation>
			commerceProductPriceCalculations = new HashMap<>();

		for (String key : _serviceTrackerMap.keySet()) {
			commerceProductPriceCalculations.put(
				key, _serviceTrackerMap.getService(key));
		}

		return Collections.unmodifiableMap(commerceProductPriceCalculations);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, CommerceProductPriceCalculation.class,
			"commerce.price.calculation.key");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceProductPriceCalculationRegistryImpl.class);

	private ServiceTrackerMap<String, CommerceProductPriceCalculation>
		_serviceTrackerMap;

}