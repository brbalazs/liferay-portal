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

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.internal.util.CommercePriceConverterUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommerceOrderPrice;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.price.CommerceOrderPriceImpl;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Riccardo Alberti
 */
public abstract class BaseCommerceOrderPriceCalculation
	implements CommerceOrderPriceCalculation {

	protected CommerceOrderPrice getCommerceOrderPriceFromOrder(
			CommerceOrder commerceOrder)
		throws PortalException {

		CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

		CommerceDiscountValue shippingDiscountValue =
			_createCommerceDiscountValue(
				commerceOrder.getShippingAmount(), commerceCurrency,
				commerceOrder.getShippingDiscountAmount(),
				commerceOrder.getShippingDiscountPercentageLevel1(),
				commerceOrder.getShippingDiscountPercentageLevel2(),
				commerceOrder.getShippingDiscountPercentageLevel3(),
				commerceOrder.getShippingDiscountPercentageLevel4());

		CommerceDiscountValue shippingDiscountValueWithTaxAmount =
			_createCommerceDiscountValue(
				commerceOrder.getShippingWithTaxAmount(), commerceCurrency,
				commerceOrder.getShippingDiscountWithTaxAmount(),
				commerceOrder.
					getShippingDiscountPercentageLevel1WithTaxAmount(),
				commerceOrder.
					getShippingDiscountPercentageLevel2WithTaxAmount(),
				commerceOrder.
					getShippingDiscountPercentageLevel3WithTaxAmount(),
				commerceOrder.
					getShippingDiscountPercentageLevel4WithTaxAmount());

		CommerceDiscountValue subtotalDiscountValue =
			_createCommerceDiscountValue(
				commerceOrder.getSubtotal(), commerceCurrency,
				commerceOrder.getSubtotalDiscountAmount(),
				commerceOrder.getSubtotalDiscountPercentageLevel1(),
				commerceOrder.getSubtotalDiscountPercentageLevel2(),
				commerceOrder.getSubtotalDiscountPercentageLevel3(),
				commerceOrder.getSubtotalDiscountPercentageLevel4());

		CommerceDiscountValue subtotalDiscountValueWithTaxAmount =
			_createCommerceDiscountValue(
				commerceOrder.getSubtotalWithTaxAmount(), commerceCurrency,
				commerceOrder.getSubtotalDiscountWithTaxAmount(),
				commerceOrder.
					getSubtotalDiscountPercentageLevel1WithTaxAmount(),
				commerceOrder.
					getSubtotalDiscountPercentageLevel2WithTaxAmount(),
				commerceOrder.
					getSubtotalDiscountPercentageLevel3WithTaxAmount(),
				commerceOrder.
					getSubtotalDiscountPercentageLevel4WithTaxAmount());

		CommerceDiscountValue totalDiscountValue = _createCommerceDiscountValue(
			commerceOrder.getTotal(), commerceCurrency,
			commerceOrder.getTotalDiscountAmount(),
			commerceOrder.getTotalDiscountPercentageLevel1(),
			commerceOrder.getTotalDiscountPercentageLevel2(),
			commerceOrder.getTotalDiscountPercentageLevel3(),
			commerceOrder.getTotalDiscountPercentageLevel4());

		CommerceDiscountValue totalDiscountValueWithTaxAmount =
			_createCommerceDiscountValue(
				commerceOrder.getTotalWithTaxAmount(), commerceCurrency,
				commerceOrder.getTotalDiscountWithTaxAmount(),
				commerceOrder.getTotalDiscountPercentageLevel1WithTaxAmount(),
				commerceOrder.getTotalDiscountPercentageLevel2WithTaxAmount(),
				commerceOrder.getTotalDiscountPercentageLevel3WithTaxAmount(),
				commerceOrder.getTotalDiscountPercentageLevel4WithTaxAmount());

		CommerceOrderPriceImpl commerceOrderPriceImpl =
			new CommerceOrderPriceImpl();

		commerceOrderPriceImpl.setShippingDiscountValue(shippingDiscountValue);
		commerceOrderPriceImpl.setShippingDiscountValueWithTaxAmount(
			shippingDiscountValueWithTaxAmount);

		commerceOrderPriceImpl.setSubtotalDiscountValue(subtotalDiscountValue);
		commerceOrderPriceImpl.setSubtotalDiscountValueWithTaxAmount(
			subtotalDiscountValueWithTaxAmount);

		commerceOrderPriceImpl.setTotalDiscountValue(totalDiscountValue);
		commerceOrderPriceImpl.setTotalDiscountValueWithTaxAmount(
			totalDiscountValueWithTaxAmount);

		commerceOrderPriceImpl.setShippingValue(
			commerceMoneyFactory.create(
				commerceOrder.getCommerceCurrency(),
				commerceOrder.getShippingAmount()));
		commerceOrderPriceImpl.setShippingValueWithTaxAmount(
			commerceMoneyFactory.create(
				commerceOrder.getCommerceCurrency(),
				commerceOrder.getShippingWithTaxAmount()));

		commerceOrderPriceImpl.setSubtotal(
			commerceMoneyFactory.create(
				commerceOrder.getCommerceCurrency(),
				commerceOrder.getSubtotal()));
		commerceOrderPriceImpl.setSubtotalWithTaxAmount(
			commerceMoneyFactory.create(
				commerceOrder.getCommerceCurrency(),
				commerceOrder.getSubtotalWithTaxAmount()));

		commerceOrderPriceImpl.setTaxValue(
			commerceMoneyFactory.create(
				commerceOrder.getCommerceCurrency(),
				commerceOrder.getTaxAmount()));

		commerceOrderPriceImpl.setTotal(
			commerceMoneyFactory.create(
				commerceOrder.getCommerceCurrency(), commerceOrder.getTotal()));
		commerceOrderPriceImpl.setTotalWithTaxAmount(
			commerceMoneyFactory.create(
				commerceOrder.getCommerceCurrency(),
				commerceOrder.getTotalWithTaxAmount()));

		return commerceOrderPriceImpl;
	}

	protected CommerceOrderPriceImpl getEmptyCommerceOrderPrice(
		CommerceCurrency commerceCurrency) {

		CommerceMoney zero = commerceMoneyFactory.create(
			commerceCurrency, BigDecimal.ZERO);

		CommerceOrderPriceImpl commerceOrderPriceImpl =
			new CommerceOrderPriceImpl();

		commerceOrderPriceImpl.setShippingDiscountValue(null);
		commerceOrderPriceImpl.setShippingDiscountValueWithTaxAmount(null);

		commerceOrderPriceImpl.setSubtotalDiscountValue(null);
		commerceOrderPriceImpl.setSubtotalDiscountValueWithTaxAmount(null);

		commerceOrderPriceImpl.setTotalDiscountValue(null);
		commerceOrderPriceImpl.setTotalDiscountValueWithTaxAmount(null);

		commerceOrderPriceImpl.setShippingValue(zero);
		commerceOrderPriceImpl.setShippingValueWithTaxAmount(zero);

		commerceOrderPriceImpl.setSubtotal(zero);
		commerceOrderPriceImpl.setSubtotalWithTaxAmount(zero);

		commerceOrderPriceImpl.setTaxValue(zero);

		commerceOrderPriceImpl.setTotal(zero);
		commerceOrderPriceImpl.setTotalWithTaxAmount(zero);

		return commerceOrderPriceImpl;
	}

	protected void setDiscountValues(
			boolean discountsTargetNetPrice, BigDecimal shippingAmount,
			BigDecimal shippingDiscounted,
			CommerceDiscountValue orderShippingCommerceDiscountValue,
			BigDecimal subtotalAmount, BigDecimal subtotalDiscounted,
			CommerceDiscountValue orderSubtotalCommerceDiscountValue,
			BigDecimal totalAmount, BigDecimal totalDiscounted,
			CommerceDiscountValue orderTotalCommerceDiscountValue,
			CommerceOrderPriceImpl commerceOrderPriceImpl,
			CommerceOrder commerceOrder)
		throws PortalException {

		CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

		if (discountsTargetNetPrice) {
			commerceOrderPriceImpl.setShippingDiscountValue(
				orderShippingCommerceDiscountValue);

			commerceOrderPriceImpl.setSubtotalDiscountValue(
				orderSubtotalCommerceDiscountValue);

			commerceOrderPriceImpl.setTotalDiscountValue(
				orderTotalCommerceDiscountValue);
		}
		else {
			RoundingMode roundingMode = RoundingMode.valueOf(
				commerceCurrency.getRoundingMode());

			commerceOrderPriceImpl.setShippingDiscountValue(
				CommercePriceConverterUtil.getConvertedCommerceDiscountValue(
					orderShippingCommerceDiscountValue, shippingAmount,
					shippingDiscounted, commerceMoneyFactory, roundingMode));

			commerceOrderPriceImpl.setSubtotalDiscountValue(
				CommercePriceConverterUtil.getConvertedCommerceDiscountValue(
					orderSubtotalCommerceDiscountValue, subtotalAmount,
					subtotalDiscounted, commerceMoneyFactory, roundingMode));

			commerceOrderPriceImpl.setTotalDiscountValue(
				CommercePriceConverterUtil.getConvertedCommerceDiscountValue(
					orderTotalCommerceDiscountValue, totalAmount,
					totalDiscounted, commerceMoneyFactory, roundingMode));
		}
	}

	protected void setDiscountValuesWithTaxAmount(
			boolean discountsTargetNetPrice, BigDecimal shippingWithTaxAmount,
			BigDecimal shippingDiscountedWithTaxAmount,
			CommerceDiscountValue orderShippingCommerceDiscountValue,
			BigDecimal subtotalWithTaxAmount,
			BigDecimal subtotalDiscountedWithTaxAmount,
			CommerceDiscountValue orderSubtotalCommerceDiscountValue,
			BigDecimal totalWithTaxAmount,
			BigDecimal totalDiscountedWithTaxAmount,
			CommerceDiscountValue orderTotalCommerceDiscountValue,
			CommerceOrderPriceImpl commerceOrderPriceImpl,
			CommerceOrder commerceOrder)
		throws PortalException {

		CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();

		RoundingMode roundingMode = RoundingMode.valueOf(
			commerceCurrency.getRoundingMode());

		if (discountsTargetNetPrice) {
			commerceOrderPriceImpl.setShippingDiscountValueWithTaxAmount(
				CommercePriceConverterUtil.getConvertedCommerceDiscountValue(
					orderShippingCommerceDiscountValue, shippingWithTaxAmount,
					shippingDiscountedWithTaxAmount, commerceMoneyFactory,
					roundingMode));

			commerceOrderPriceImpl.setSubtotalDiscountValueWithTaxAmount(
				CommercePriceConverterUtil.getConvertedCommerceDiscountValue(
					orderSubtotalCommerceDiscountValue, subtotalWithTaxAmount,
					subtotalDiscountedWithTaxAmount, commerceMoneyFactory,
					roundingMode));

			commerceOrderPriceImpl.setTotalDiscountValueWithTaxAmount(
				CommercePriceConverterUtil.getConvertedCommerceDiscountValue(
					orderTotalCommerceDiscountValue, totalWithTaxAmount,
					totalDiscountedWithTaxAmount, commerceMoneyFactory,
					roundingMode));
		}
		else {
			commerceOrderPriceImpl.setShippingDiscountValueWithTaxAmount(
				orderShippingCommerceDiscountValue);
			commerceOrderPriceImpl.setSubtotalDiscountValueWithTaxAmount(
				orderSubtotalCommerceDiscountValue);
			commerceOrderPriceImpl.setTotalDiscountValueWithTaxAmount(
				orderTotalCommerceDiscountValue);
		}
	}

	@Reference
	protected CommerceMoneyFactory commerceMoneyFactory;

	private CommerceDiscountValue _createCommerceDiscountValue(
		BigDecimal amount, CommerceCurrency commerceCurrency,
		BigDecimal discountAmount, BigDecimal level1, BigDecimal level2,
		BigDecimal level3, BigDecimal level4) {

		if ((discountAmount == null) || (amount == null) ||
			(amount.compareTo(BigDecimal.ZERO) <= 0)) {

			return new CommerceDiscountValue(
				0,
				commerceMoneyFactory.create(commerceCurrency, BigDecimal.ZERO),
				BigDecimal.ZERO,
				new BigDecimal[] {
					BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
					BigDecimal.ZERO
				});
		}

		BigDecimal[] discountPercentageValues = {
			level1, level2, level3, level4
		};

		BigDecimal discountPercentage = discountAmount.divide(
			amount, RoundingMode.valueOf(commerceCurrency.getRoundingMode()));

		discountPercentage = discountPercentage.multiply(
			BigDecimal.valueOf(100));

		return new CommerceDiscountValue(
			0, commerceMoneyFactory.create(commerceCurrency, discountAmount),
			discountPercentage, discountPercentageValues);
	}

}