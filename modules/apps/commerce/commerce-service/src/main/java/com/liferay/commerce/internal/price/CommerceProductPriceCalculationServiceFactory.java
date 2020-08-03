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
import com.liferay.commerce.price.CommerceProductPriceCalculationFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(immediate = true, service = ServiceFactory.class)
public class CommerceProductPriceCalculationServiceFactory
	implements ServiceFactory<CommerceProductPriceCalculation> {

	@Activate
	public void activate(BundleContext bundleContext) {
		_serviceRegistration = bundleContext.registerService(
			CommerceProductPriceCalculation.class, this,
			new Hashtable<String, Object>());
	}

	@Deactivate
	public void deactivate() {
		_serviceRegistration.unregister();
	}

	@Override
	public CommerceProductPriceCalculation getService(
		Bundle bundle,
		ServiceRegistration<CommerceProductPriceCalculation>
			serviceRegistration) {

		return _commerceProductPriceCalculationFactory.
			getCommerceProductPriceCalculation();
	}

	@Override
	public void ungetService(
		Bundle bundle,
		ServiceRegistration<CommerceProductPriceCalculation>
			serviceRegistration,
		CommerceProductPriceCalculation commerceProductPriceCalculation) {
	}

	@Reference
	private CommerceProductPriceCalculationFactory
		_commerceProductPriceCalculationFactory;

	private ServiceRegistration<CommerceProductPriceCalculation>
		_serviceRegistration;

}