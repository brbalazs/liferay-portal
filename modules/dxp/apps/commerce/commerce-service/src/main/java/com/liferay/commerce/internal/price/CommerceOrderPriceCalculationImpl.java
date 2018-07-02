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
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.model.CommerceMoneyFactory;
import com.liferay.commerce.discount.CommerceDiscountCalculation;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.price.CommerceOrderPrice;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.tax.CommerceTaxCalculation;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component
public class CommerceOrderPriceCalculationImpl
	implements CommerceOrderPriceCalculation {

	@Override
	public CommerceOrderPrice getCommerceOrderPrice(
			CommerceOrder commerceOrder, CommerceContext commerceContext)
		throws PortalException {

		if (commerceOrder == null) {
			return null;
		}

		CommerceMoney shippingValue = getShippingValue(
			commerceOrder, commerceContext);
		CommerceMoney subtotal = getSubtotal(commerceOrder, commerceContext);
		CommerceMoney taxValue = getTaxValue(commerceOrder, commerceContext);
		CommerceMoney total = getTotal(commerceOrder, commerceContext);

		CommerceDiscountValue orderShippingCommerceDiscountValue =
			_commerceDiscountCalculation.getOrderShippingCommerceDiscountValue(
				shippingValue.getPrice(), commerceContext);
		CommerceDiscountValue orderSubtotalCommerceDiscountValue =
			_commerceDiscountCalculation.getOrderSubtotalCommerceDiscountValue(
				subtotal.getPrice(), commerceContext);
		CommerceDiscountValue orderTotalCommerceDiscountValue =
			_commerceDiscountCalculation.getOrderTotalCommerceDiscountValue(
				total.getPrice(), commerceContext);

		CommerceOrderPriceImpl commerceOrderPrice =
			new CommerceOrderPriceImpl();

		commerceOrderPrice.setShippingDiscountValue(
			orderShippingCommerceDiscountValue);
		commerceOrderPrice.setSubtotalDiscountValue(
			orderSubtotalCommerceDiscountValue);
		commerceOrderPrice.setTotalDiscountValue(
			orderTotalCommerceDiscountValue);
		commerceOrderPrice.setShippingValue(shippingValue);
		commerceOrderPrice.setSubtotal(subtotal);
		commerceOrderPrice.setTaxValue(taxValue.getPrice());
		commerceOrderPrice.setTotal(total);

		return commerceOrderPrice;
	}

	@Override
	public CommerceMoney getShippingValue(
			CommerceOrder commerceOrder, CommerceContext commerceContext)
		throws PortalException {

		if (commerceOrder == null) {
			return null;
		}

		return commerceOrder.getShippingMoney();
	}

	@Override
	public CommerceMoney getSubtotal(
			CommerceOrder commerceOrder, CommerceContext commerceContext)
		throws PortalException {

		BigDecimal subtotal = BigDecimal.ZERO;

		if (commerceOrder == null) {
			return _commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(), subtotal);
		}

		if (!commerceOrder.isOpen()) {
			return _commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(),
				commerceOrder.getSubtotal());
		}

		for (CommerceOrderItem commerceOrderItem :
				commerceOrder.getCommerceOrderItems()) {

			subtotal = subtotal.add(commerceOrderItem.getFinalPrice());
		}

		return _commerceMoneyFactory.create(
			commerceContext.getCommerceCurrency(), subtotal);
	}

	@Override
	public CommerceMoney getTaxValue(
			CommerceOrder commerceOrder, CommerceContext commerceContext)
		throws PortalException {

		if (commerceOrder == null) {
			return null;
		}

		BigDecimal taxValue = BigDecimal.ZERO;

		for (CommerceOrderItem commerceOrderItem :
				commerceOrder.getCommerceOrderItems()) {
		}

		return _commerceMoneyFactory.create(
			commerceContext.getCommerceCurrency(), taxValue);
	}

	@Override
	public CommerceMoney getTotal(
			CommerceOrder commerceOrder, CommerceContext commerceContext)
		throws PortalException {

		BigDecimal total = BigDecimal.ZERO;

		if (commerceOrder == null) {
			return _commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(), total);
		}

		if (!commerceOrder.isOpen()) {
			return _commerceMoneyFactory.create(
				commerceContext.getCommerceCurrency(),
				commerceOrder.getTotal());
		}

		CommerceMoney shippingValue = getShippingValue(
			commerceOrder, commerceContext);
		CommerceMoney subtotal = getSubtotal(commerceOrder, commerceContext);
		CommerceMoney taxValue = getTaxValue(commerceOrder, commerceContext);

		total = total.add(shippingValue.getPrice());
		total = total.add(subtotal.getPrice());
		total = total.add(taxValue.getPrice());

		return _commerceMoneyFactory.create(
			commerceContext.getCommerceCurrency(), total);
	}

	@Reference
	private CommerceDiscountCalculation _commerceDiscountCalculation;

	@Reference
	private CommerceMoneyFactory _commerceMoneyFactory;

	@Reference
	private CommerceProductPriceCalculation _commerceProductPriceCalculation;

	@Reference
	private CommerceTaxCalculation _commerceTaxCalculation;

}