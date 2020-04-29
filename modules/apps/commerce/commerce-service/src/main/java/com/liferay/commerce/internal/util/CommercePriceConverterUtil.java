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

import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.currency.model.CommerceMoneyFactoryUtil;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.tax.CommerceTaxCalculation;
import com.liferay.commerce.tax.CommerceTaxValue;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;

/**
 * @author Riccardo Alberti
 */
public class CommercePriceConverterUtil {

	public static CommerceDiscountValue getConvertedCommerceDiscountValue(
		CommerceDiscountValue commerceDiscountValue, boolean includeTax,
		BigDecimal taxValue, RoundingMode roundingMode,
		CommerceMoneyFactory commerceMoneyFactory) {

		CommerceMoney discountAmount =
			commerceDiscountValue.getDiscountAmount();

		BigDecimal discountAmountPrice = discountAmount.getPrice();

		BigDecimal convertedDiscountAmountPrice;

		if (includeTax) {
			convertedDiscountAmountPrice = discountAmountPrice.subtract(
				taxValue);
		}
		else {
			convertedDiscountAmountPrice = discountAmountPrice.add(taxValue);
		}

		CommerceMoney convertedDiscountAmount = commerceMoneyFactory.create(
			discountAmount.getCommerceCurrency(), convertedDiscountAmountPrice);

		return new CommerceDiscountValue(
			commerceDiscountValue.getId(), convertedDiscountAmount,
			_getPercentage(
				commerceDiscountValue.getDiscountPercentage(), includeTax,
				discountAmountPrice, convertedDiscountAmountPrice,
				roundingMode),
			_getPercentages(
				commerceDiscountValue.getPercentages(), includeTax,
				discountAmountPrice, convertedDiscountAmountPrice,
				roundingMode));
	}

	public static CommerceDiscountValue getConvertedCommerceDiscountValue(
			long commerceChannelGroupId, long cpInstanceId,
			long commerceBillingAddressId, long commerceShippingAddressId,
			CommerceDiscountValue commerceDiscountValue, boolean includeTax,
			CommerceTaxCalculation commerceTaxCalculation,
			RoundingMode roundingMode)
		throws PortalException {

		CommerceMoney discountAmount =
			commerceDiscountValue.getDiscountAmount();

		BigDecimal discountAmountPrice = discountAmount.getPrice();

		BigDecimal convertedDiscountAmountPrice = getConvertedPrice(
			commerceChannelGroupId, cpInstanceId, commerceBillingAddressId,
			commerceShippingAddressId, discountAmountPrice, includeTax,
			commerceTaxCalculation);

		return new CommerceDiscountValue(
			commerceDiscountValue.getId(),
			CommerceMoneyFactoryUtil.create(
				discountAmount.getCommerceCurrency(),
				convertedDiscountAmountPrice),
			_getPercentage(
				commerceDiscountValue.getDiscountPercentage(), includeTax,
				discountAmountPrice, convertedDiscountAmountPrice,
				roundingMode),
			_getPercentages(
				commerceDiscountValue.getPercentages(), includeTax,
				discountAmountPrice, convertedDiscountAmountPrice,
				roundingMode));
	}

	public static BigDecimal getConvertedPrice(
			long commerceChannelGroupId, long cpInstanceId,
			long commerceBillingAddressId, long commerceShippingAddressId,
			BigDecimal price, boolean includeTax,
			CommerceTaxCalculation commerceTaxCalculation)
		throws PortalException {

		List<CommerceTaxValue> commerceTaxValues =
			commerceTaxCalculation.getCommerceTaxValues(
				commerceChannelGroupId, cpInstanceId, commerceBillingAddressId,
				commerceShippingAddressId, price, includeTax);

		if ((commerceTaxValues == null) || commerceTaxValues.isEmpty()) {
			return BigDecimal.ZERO;
		}

		BigDecimal taxAmount = BigDecimal.ZERO;

		for (CommerceTaxValue commerceTaxValue : commerceTaxValues) {
			taxAmount = taxAmount.add(commerceTaxValue.getAmount());
		}

		if (includeTax) {
			return price.subtract(taxAmount);
		}

		return price.add(taxAmount);
	}

	private static BigDecimal _getPercentage(
		BigDecimal value, boolean includeTax, BigDecimal discountAmountPrice,
		BigDecimal convertedDiscountAmountPrice, RoundingMode roundingMode) {

		BigDecimal result;

		if (includeTax) {
			BigDecimal taxPercentage = discountAmountPrice.divide(
				convertedDiscountAmountPrice, roundingMode);

			taxPercentage = taxPercentage.subtract(BigDecimal.ONE);

			BigDecimal difference = _ONE_HUNDRED.subtract(value);

			difference = difference.multiply(taxPercentage);

			result = value.subtract(difference);
		}
		else {
			BigDecimal taxPercentage = convertedDiscountAmountPrice.divide(
				discountAmountPrice, roundingMode);

			taxPercentage = taxPercentage.subtract(BigDecimal.ONE);

			BigDecimal numerator = taxPercentage.multiply(_ONE_HUNDRED);

			numerator = numerator.add(value);

			BigDecimal denominator = taxPercentage.add(BigDecimal.ONE);

			result = numerator.divide(denominator, roundingMode);
		}

		return result;
	}

	private static BigDecimal[] _getPercentages(
		BigDecimal[] values, boolean includeTax, BigDecimal discountAmountPrice,
		BigDecimal convertedDiscountAmountPrice, RoundingMode roundingMode) {

		for (int i = 0; i < values.length; i++) {
			if ((values[i] != null) &&
				(values[i].compareTo(BigDecimal.ZERO) != 0)) {

				values[i] = _getPercentage(
					values[i], includeTax, discountAmountPrice,
					convertedDiscountAmountPrice, roundingMode);
			}
		}

		return values;
	}

	private static final BigDecimal _ONE_HUNDRED = BigDecimal.valueOf(100);

}