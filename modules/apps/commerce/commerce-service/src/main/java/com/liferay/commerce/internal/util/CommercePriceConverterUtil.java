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
import com.liferay.commerce.currency.model.CommerceMoneyFactoryUtil;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.model.CommerceOrder;
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
			long commerceChannelGroupId, long cpInstanceId,
			long commerceBillingAddressId, long commerceShippingAddressId,
			CommerceDiscountValue commerceDiscountValue, boolean withTaxAmount,
			CommerceTaxCalculation commerceTaxCalculation,
			RoundingMode roundingMode)
		throws PortalException {

		CommerceMoney discountAmount =
			commerceDiscountValue.getDiscountAmount();

		BigDecimal discountAmountPrice = discountAmount.getPrice();

		BigDecimal convertedDiscountAmountPrice = getConvertedPrice(
			commerceChannelGroupId, cpInstanceId, commerceBillingAddressId,
			commerceShippingAddressId, discountAmountPrice, withTaxAmount,
			commerceTaxCalculation);

		return new CommerceDiscountValue(
			commerceDiscountValue.getId(),
			CommerceMoneyFactoryUtil.create(
				discountAmount.getCommerceCurrency(),
				convertedDiscountAmountPrice),
			_getPercentage(
				commerceDiscountValue.getDiscountPercentage(), withTaxAmount,
				discountAmountPrice, convertedDiscountAmountPrice,
				roundingMode),
			_getPercentages(
				commerceDiscountValue.getPercentages(), withTaxAmount,
				discountAmountPrice, convertedDiscountAmountPrice,
				roundingMode));
	}

	public static BigDecimal getConvertedPrice(
			long commerceChannelGroupId, long cpInstanceId,
			long commerceBillingAddressId, long commerceShippingAddressId,
			BigDecimal price, boolean withTaxAmount,
			CommerceTaxCalculation commerceTaxCalculation)
		throws PortalException {

		List<CommerceTaxValue> commerceTaxValues =
			commerceTaxCalculation.getCommerceTaxValues(
				commerceChannelGroupId, cpInstanceId, commerceBillingAddressId,
				commerceShippingAddressId, price, withTaxAmount);

		if ((commerceTaxValues == null) || commerceTaxValues.isEmpty()) {
			return BigDecimal.ZERO;
		}

		BigDecimal taxAmount = BigDecimal.ZERO;

		for (CommerceTaxValue commerceTaxValue : commerceTaxValues) {
			taxAmount = taxAmount.add(commerceTaxValue.getAmount());
		}

		if (withTaxAmount) {
			return price.subtract(taxAmount);
		}

		return price.add(taxAmount);
	}

	public static CommerceDiscountValue getConvertedShippingDiscountValue(
		CommerceDiscountValue commerceDiscountValue, boolean withTaxAmount,
		CommerceMoney convertedDiscountAmount, RoundingMode roundingMode) {

		CommerceMoney discountAmount =
			commerceDiscountValue.getDiscountAmount();

		return new CommerceDiscountValue(
			commerceDiscountValue.getId(), convertedDiscountAmount,
			_getPercentage(
				commerceDiscountValue.getDiscountPercentage(), withTaxAmount,
				discountAmount.getPrice(), convertedDiscountAmount.getPrice(),
				roundingMode),
			_getPercentages(
				commerceDiscountValue.getPercentages(), withTaxAmount,
				discountAmount.getPrice(), convertedDiscountAmount.getPrice(),
				roundingMode));
	}

	public static BigDecimal getShippingWithTaxAmount(
			CommerceOrder commerceOrder,
			CommerceTaxCalculation commerceTaxCalculation)
		throws PortalException {

		BigDecimal shippingAmount = commerceOrder.getShippingAmount();

		List<CommerceTaxValue> commerceTaxValues =
			commerceTaxCalculation.getShippingTaxValue(commerceOrder);

		if ((commerceTaxValues == null) || commerceTaxValues.isEmpty()) {
			return BigDecimal.ZERO;
		}

		BigDecimal taxAmount = BigDecimal.ZERO;

		for (CommerceTaxValue commerceTaxValue : commerceTaxValues) {
			taxAmount = taxAmount.add(commerceTaxValue.getAmount());
		}

		return shippingAmount.add(taxAmount);
	}

	private static BigDecimal _getPercentage(
		BigDecimal value, boolean withTaxAmount, BigDecimal discountAmountPrice,
		BigDecimal convertedDiscountAmountPrice, RoundingMode roundingMode) {

		BigDecimal result;

		if (withTaxAmount) {
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
		BigDecimal[] values, boolean withTaxAmount,
		BigDecimal discountAmountPrice, BigDecimal convertedDiscountAmountPrice,
		RoundingMode roundingMode) {

		for (int i = 0; i < values.length; i++) {
			if ((values[i] != null) &&
				(values[i].compareTo(BigDecimal.ZERO) != 0)) {

				values[i] = _getPercentage(
					values[i], withTaxAmount, discountAmountPrice,
					convertedDiscountAmountPrice, roundingMode);
			}
		}

		return values;
	}

	private static final BigDecimal _ONE_HUNDRED = BigDecimal.valueOf(100);

}