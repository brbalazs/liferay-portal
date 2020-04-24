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
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.Validator;

import java.math.BigDecimal;

import java.util.List;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Matija Petanjek
 */
public abstract class BaseCommerceProductPriceCalculation
	implements CommerceProductPriceCalculation {

	@Override
	public CommerceMoney getCPDefinitionMinimumPrice(
			long cpDefinitionId, CommerceContext commerceContext)
		throws PortalException {

		BigDecimal cpDefinitionMinimumPrice = BigDecimal.ZERO;

		CommerceMoney commerceMoney = getUnitMinPrice(
			cpDefinitionId, 1, commerceContext);

		cpDefinitionMinimumPrice = cpDefinitionMinimumPrice.add(
			commerceMoney.getPrice());

		List<CPDefinitionOptionRel> cpDefinitionOptionRels =
			cpDefinitionOptionRelLocalService.getCPDefinitionOptionRels(
				cpDefinitionId);

		for (CPDefinitionOptionRel cpDefinitionOptionRel :
				cpDefinitionOptionRels) {

			String priceType = cpDefinitionOptionRel.getPriceType();

			if (!_isPriceContributor(cpDefinitionOptionRel)) {
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

			cpDefinitionMinimumPrice = cpDefinitionMinimumPrice.add(
				cpDefinitionOptionValueMinPrice);
		}

		return commerceMoneyFactory.create(
			commerceContext.getCommerceCurrency(), cpDefinitionMinimumPrice);
	}

	@Override
	public CommerceMoney getCPDefinitionOptionValueRelativePrice(
			CPDefinitionOptionValueRel cpDefinitionOptionValueRel,
			CPDefinitionOptionValueRel selectedCPDefinitionOptionValueRel,
			CommerceContext commerceContext)
		throws PortalException {

		_validate(
			cpDefinitionOptionValueRel, selectedCPDefinitionOptionValueRel);

		CPDefinitionOptionRel cpDefinitionOptionRel =
			cpDefinitionOptionValueRel.getCPDefinitionOptionRel();

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		String priceType = cpDefinitionOptionRel.getPriceType();

		if (Validator.isNull(priceType)) {
			return commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(), BigDecimal.ZERO);
		}

		BigDecimal price = cpDefinitionOptionValueRel.getPrice();
		BigDecimal relativePrice = null;

		if (priceType.equals(CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC)) {
			if (selectedCPDefinitionOptionValueRel != null) {
				relativePrice = price.subtract(
					selectedCPDefinitionOptionValueRel.getPrice());
			}

			return commerceMoneyFactory.create(
				commerceCurrency,
				relativePrice.multiply(commerceCurrency.getRate()));
		}

		BigDecimal cpInstanceFinalPrice = _getCPInstanceFinalPrice(
			cpDefinitionOptionValueRel.getCProductId(),
			cpDefinitionOptionValueRel.getCPInstanceUuid(),
			cpDefinitionOptionValueRel.getQuantity(), commerceContext);

		if (selectedCPDefinitionOptionValueRel == null) {
			return commerceMoneyFactory.create(
				commerceCurrency, cpInstanceFinalPrice);
		}

		BigDecimal selectedCPInstanceFinalPrice = _getCPInstanceFinalPrice(
			selectedCPDefinitionOptionValueRel.getCProductId(),
			selectedCPDefinitionOptionValueRel.getCPInstanceUuid(),
			selectedCPDefinitionOptionValueRel.getQuantity(), commerceContext);

		return commerceMoneyFactory.create(
			commerceCurrency,
			cpInstanceFinalPrice.subtract(selectedCPInstanceFinalPrice));
	}

	@Reference
	protected CommerceMoneyFactory commerceMoneyFactory;

	@Reference
	protected CPDefinitionOptionRelLocalService
		cpDefinitionOptionRelLocalService;

	@Reference
	protected CPInstanceLocalService cpInstanceLocalService;

	private BigDecimal _getCPDefinitionOptionMinDynamicPrice(
			CPDefinitionOptionRel cpDefinitionOptionRel,
			CommerceContext commerceContext)
		throws PortalException {

		BigDecimal cpDefinitionOptionMinDynamicPrice = BigDecimal.ZERO;

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
				cpDefinitionOptionValueRel.getQuantity(), commerceContext);

			if (cpDefinitionOptionMinDynamicPrice.compareTo(
					cpInstanceFinalPrice) > 0) {

				cpDefinitionOptionMinDynamicPrice = cpInstanceFinalPrice;
			}
		}

		return cpDefinitionOptionMinDynamicPrice;
	}

	private BigDecimal _getCPDefinitionOptionMinStaticPrice(
			CPDefinitionOptionRel cpDefinitionOptionRel,
			CommerceContext commerceContext)
		throws PortalException {

		BigDecimal cpDefinitionOptionMinStaticPrice = BigDecimal.ZERO;

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

			BigDecimal cpDefinitionOptionValueFinalPrice =
				_getCPDefinitionOptionValueFinalPrice(
					cpDefinitionOptionValueRel.getPrice(),
					cpDefinitionOptionValueRel.getQuantity());

			if (cpDefinitionOptionMinStaticPrice.compareTo(
					cpDefinitionOptionValueFinalPrice) > 0) {

				cpDefinitionOptionMinStaticPrice =
					cpDefinitionOptionValueFinalPrice;
			}
		}

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		return cpDefinitionOptionMinStaticPrice.multiply(
			commerceCurrency.getRate());
	}

	private BigDecimal _getCPDefinitionOptionValueFinalPrice(
		BigDecimal price, int quantity) {

		return price.multiply(BigDecimal.valueOf(quantity));
	}

	private BigDecimal _getCPInstanceFinalPrice(
			long cProductId, String cpInstanceUuid, int quantity,
			CommerceContext commerceContext)
		throws PortalException {

		CPInstance cpInstance = cpInstanceLocalService.getCProductInstance(
			cProductId, cpInstanceUuid);

		CommerceMoney commerceMoney = getFinalPrice(
			cpInstance.getCPInstanceId(), quantity, commerceContext);

		return commerceMoney.getPrice();
	}

	private boolean _isPriceContributor(
		CPDefinitionOptionRel cpDefinitionOptionRel) {

		if (Validator.isNull(cpDefinitionOptionRel.getPriceType()) ||
			!(cpDefinitionOptionRel.isRequired() ||
			  cpDefinitionOptionRel.isSkuContributor())) {

			return false;
		}

		return true;
	}

	private void _validate(
		CPDefinitionOptionValueRel cpDefinitionOptionValueRel,
		CPDefinitionOptionValueRel selectedCPDefinitionOptionValueRel) {

		if ((selectedCPDefinitionOptionValueRel != null) &&
			(cpDefinitionOptionValueRel.getCPDefinitionOptionRelId() !=
				selectedCPDefinitionOptionValueRel.
					getCPDefinitionOptionRelId())) {

			throw new IllegalArgumentException(
				"Provided CPDefinitionOptionValueRel parameters must belong " +
					"to the same CPDefinitionOptionRel");
		}
	}

}