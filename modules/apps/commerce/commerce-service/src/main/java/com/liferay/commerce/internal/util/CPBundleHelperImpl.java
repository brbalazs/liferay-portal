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

package com.liferay.commerce.internal.util;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPDefinitionOptionRelLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.util.CPBundleHelper;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matija Petanjek
 */
@Component(immediate = true, service = CPBundleHelper.class)
public class CPBundleHelperImpl implements CPBundleHelper {

	@Override
	public CommerceMoney getCPBundleMinPrice(
			long cpDefinitionId, CommerceContext commerceContext)
		throws PortalException {

		BigDecimal cpBundleMinPrice = BigDecimal.ZERO;

		CommerceMoney commerceMoney =
			_commerceProductPriceCalculation.getUnitMinPrice(
				cpDefinitionId, 1, commerceContext);

		cpBundleMinPrice = cpBundleMinPrice.add(commerceMoney.getPrice());

		List<CPDefinitionOptionRel> cpDefinitionOptionRels =
			_cpDefinitionOptionRelLocalService.getCPDefinitionOptionRels(
				cpDefinitionId);

		for (CPDefinitionOptionRel cpDefinitionOptionRel :
				cpDefinitionOptionRels) {

			String priceType = cpDefinitionOptionRel.getPriceType();

			if (!cpDefinitionOptionRel.isRequired() || (priceType == null)) {
				continue;
			}

			BigDecimal cpDefinitionOptionValueMinPrice = BigDecimal.ZERO;

			if (priceType.equals(
					CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC)) {

				cpDefinitionOptionValueMinPrice =
					_getCPDefinitionOptionMinStaticPrice(
						cpDefinitionOptionRel, commerceContext);
			}
			else if (priceType.equals(
						CPConstants.PRODUCT_OPTION_PRICE_TYPE_DYNAMIC)) {

				cpDefinitionOptionValueMinPrice =
					_getCPDefinitionOptionMinDynamicPrice(
						cpDefinitionOptionRel, commerceContext);
			}

			cpBundleMinPrice = cpBundleMinPrice.add(
				cpDefinitionOptionValueMinPrice);
		}

		return _commerceMoneyFactory.create(
			commerceContext.getCommerceCurrency(), cpBundleMinPrice);
	}

	private BigDecimal _getCPDefinitionOptionMinDynamicPrice(
			CPDefinitionOptionRel cpDefinitionOptionRel,
			CommerceContext commerceContext)
		throws PortalException {

		BigDecimal cpDefinitionOptionMinDynamicPrice = null;

		List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
			cpDefinitionOptionRel.getCPDefinitionOptionValueRels();

		if (cpDefinitionOptionValueRels.isEmpty()) {
			return BigDecimal.ZERO;
		}

		for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
				cpDefinitionOptionValueRels) {

			BigDecimal cpInstanceFinalPrice = _getCPInstanceFinalPrice(
				cpDefinitionOptionValueRel.getCProductId(),
				cpDefinitionOptionValueRel.getCPInstanceUuid(),
				commerceContext);

			if ((cpDefinitionOptionMinDynamicPrice == null) ||
				(cpDefinitionOptionMinDynamicPrice.compareTo(
					cpInstanceFinalPrice) > 0)) {

				cpDefinitionOptionMinDynamicPrice = cpInstanceFinalPrice;
			}
		}

		return cpDefinitionOptionMinDynamicPrice;
	}

	private BigDecimal _getCPDefinitionOptionMinStaticPrice(
			CPDefinitionOptionRel cpDefinitionOptionRel,
			CommerceContext commerceContext)
		throws PortalException {

		BigDecimal cpDefinitionOptionMinStaticPrice = null;

		List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
			cpDefinitionOptionRel.getCPDefinitionOptionValueRels();

		if (cpDefinitionOptionValueRels.isEmpty()) {
			return BigDecimal.ZERO;
		}

		for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
				cpDefinitionOptionValueRels) {

			if (cpDefinitionOptionValueRel.getPrice() == null) {
				continue;
			}

			if ((cpDefinitionOptionMinStaticPrice == null) ||
				(cpDefinitionOptionMinStaticPrice.compareTo(
					cpDefinitionOptionValueRel.getPrice()) > 0)) {

				cpDefinitionOptionMinStaticPrice =
					cpDefinitionOptionValueRel.getPrice();
			}
		}

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		return cpDefinitionOptionMinStaticPrice.multiply(
			commerceCurrency.getRate());
	}

	private BigDecimal _getCPInstanceFinalPrice(
			long cProductId, String cpInstanceUuid,
			CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = _cpInstanceLocalService.getCProductInstance(
			cProductId, cpInstanceUuid);

		CommerceMoney commerceMoney =
			_commerceProductPriceCalculation.getFinalPrice(
				cpInstance.getCPInstanceId(), 1, commerceContext);

		return commerceMoney.getPrice();
	}

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommerceProductPriceCalculation _commerceProductPriceCalculation;

	@Reference
	private CPDefinitionOptionRelLocalService
		_cpDefinitionOptionRelLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

}