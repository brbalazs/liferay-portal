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

package com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartItem;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.OrderItem;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Price;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.math.BigDecimal;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Sbarra
 */
@Component(
	property = "model.class.name=com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartItem",
	service = {CartItemDTOConverter.class, DTOConverter.class}
)
public class CartItemDTOConverter implements DTOConverter {

	@Override
	public String getContentType() {
		return OrderItem.class.getSimpleName();
	}

	@Override
	public CartItem toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		CartItemDTOConverterContext cartItemDTOConverterContext =
			(CartItemDTOConverterContext)dtoConverterContext;

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemService.getCommerceOrderItem(
				cartItemDTOConverterContext.getResourcePrimKey());

		Locale locale = cartItemDTOConverterContext.getLocale();

		CommerceContext commerceContext =
			cartItemDTOConverterContext.getCommerceContext();

		String languageId = LanguageUtil.getLanguageId(locale);

		return new CartItem() {
			{
				id = commerceOrderItem.getCommerceOrderItemId();
				name = commerceOrderItem.getName(languageId);
				price = _getPrice(commerceOrderItem, commerceContext, locale);
				quantity = commerceOrderItem.getQuantity();
			}
		};
	}

	private Price _getPrice(
			CommerceOrderItem commerceOrderItem,
			CommerceContext commerceContext, Locale locale)
		throws PortalException {

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		CommerceMoney unitPriceMoney = commerceOrderItem.getUnitPriceMoney();

		BigDecimal unitPrice = unitPriceMoney.getPrice();

		CommerceMoney unitPromoPriceMoney =
			commerceOrderItem.getPromoPriceMoney();
		BigDecimal unitPromoPrice = commerceOrderItem.getUnitPrice();

		CommerceMoney discountAmountMoney =
			commerceOrderItem.getDiscountAmountMoney();
		BigDecimal discountAmount = commerceOrderItem.getDiscountAmount();

		CommerceMoney finalPriceMoney = commerceOrderItem.getFinalPriceMoney();
		BigDecimal finalPrice = commerceOrderItem.getFinalPrice();

		Price price = new Price() {
			{
				currency = commerceCurrency.getName(locale);
				price = unitPrice.doubleValue();
				priceFormatted = unitPriceMoney.format(locale);
			}
		};

		if (unitPromoPrice != null) {
			price.setPromoPrice(unitPromoPrice.doubleValue());
			price.setPromoPriceFormatted(unitPromoPriceMoney.format(locale));
		}

		if (discountAmount != null) {
			price.setDiscountFormatted(discountAmountMoney.format(locale));
			price.setDiscount(discountAmount.doubleValue());
		}

		if (finalPrice != null) {
			price.setFinalPriceFormatted(finalPriceMoney.format(locale));
			price.setFinalPrice(finalPrice.doubleValue());
		}

		return price;
	}

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

}