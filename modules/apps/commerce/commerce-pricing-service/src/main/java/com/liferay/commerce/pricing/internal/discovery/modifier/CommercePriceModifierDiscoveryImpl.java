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

package com.liferay.commerce.pricing.internal.discovery.modifier;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.pricing.discovery.modifier.CommercePriceModifierDiscovery;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.service.CommercePriceModifierLocalService;
import com.liferay.commerce.pricing.type.CommercePriceModifierType;
import com.liferay.commerce.pricing.type.CommercePriceModifierTypeRegistry;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
@Component(service = CommercePriceModifierDiscovery.class)
public class CommercePriceModifierDiscoveryImpl
	implements CommercePriceModifierDiscovery {

	@Override
	public CommerceMoney applyCommercePriceModifier(
			long commercePriceListId, long cpDefinitionId,
			CommerceMoney originalCommerceMoney,
			CommerceCurrency commerceCurrency)
		throws PortalException {

		List<CommercePriceModifier> commercePriceModifiers =
			_commercePriceModifierLocalService.
				getQualifiedCommercePriceModifiers(
					commercePriceListId, cpDefinitionId);

		BigDecimal lowestPrice = originalCommerceMoney.getPrice();

		if ((commercePriceModifiers != null) &&
			!commercePriceModifiers.isEmpty()) {

			for (CommercePriceModifier commercePriceModifier :
					commercePriceModifiers) {

				CommercePriceModifierType commercePriceModifierType =
					_commercePriceModifierTypeRegistry.
						getCommercePriceModifierType(
							commercePriceModifier.getModifierType());

				CommerceMoney actualCommerceMoney =
					commercePriceModifierType.evaluate(
						originalCommerceMoney, commercePriceModifier,
						commerceCurrency);

				BigDecimal actualPrice = actualCommerceMoney.getPrice();

				if (actualPrice.compareTo(lowestPrice) < 0) {
					lowestPrice = actualPrice;
				}
			}
		}

		return _commerceMoneyFactory.create(commerceCurrency, lowestPrice);
	}

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommercePriceModifierLocalService
		_commercePriceModifierLocalService;

	@Reference
	private CommercePriceModifierTypeRegistry
		_commercePriceModifierTypeRegistry;

}