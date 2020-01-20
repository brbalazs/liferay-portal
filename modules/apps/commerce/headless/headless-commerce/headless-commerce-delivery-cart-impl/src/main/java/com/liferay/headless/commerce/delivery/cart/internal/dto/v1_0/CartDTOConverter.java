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
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.price.CommerceOrderPrice;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Summary;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.language.LanguageResources;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Sbarra
 */
@Component(
	property = "model.class.name=com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart",
	service = {CartDTOConverter.class, DTOConverter.class}
)
public class CartDTOConverter implements DTOConverter {

	@Override
	public String getContentType() {
		return Cart.class.getSimpleName();
	}

	public Cart toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		CartDTOConverterContext cartDTOConverterContext =
			(CartDTOConverterContext)dtoConverterContext;

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			cartDTOConverterContext.getResourcePrimKey());

		Locale locale = cartDTOConverterContext.getLocale();

		ResourceBundle resourceBundle = LanguageResources.getResourceBundle(
			locale);

		String workflowStatusLabel = LanguageUtil.get(
			resourceBundle,
			WorkflowConstants.getStatusLabel(commerceOrder.getStatus()));

		return new Cart() {
			{
				id = commerceOrder.getCommerceOrderId();
				account = commerceOrder.getCommerceAccountName();
				accountId = commerceOrder.getCommerceAccountId();
				author = commerceOrder.getUserName();
				createDate = commerceOrder.getCreateDate();
				status = workflowStatusLabel;
				summary = _getSummary(
					commerceOrder, locale,
					cartDTOConverterContext.getChannelSiteGroupId());
			}
		};
	}

	private Summary _getSummary(
			CommerceOrder commerceOrder, Locale locale, long channelSiteGroupId)
		throws PortalException {

		CommerceContext commerceContext = _commerceContextFactory.create(
			commerceOrder.getCompanyId(), channelSiteGroupId,
			commerceOrder.getUserId(), commerceOrder.getCommerceOrderId(),
			commerceOrder.getCommerceAccountId());

		List<CommerceOrderItem> commerceOrderItems =
			commerceOrder.getCommerceOrderItems();

		CommerceOrderPrice commerceOrderPrice =
			_commerceOrderPriceCalculation.getCommerceOrderPrice(
				commerceOrder, commerceContext);

		CommerceMoney _shippingValue = commerceOrderPrice.getShippingValue();

		CommerceMoney commerceOrderPriceSubTotal =
			commerceOrderPrice.getSubtotal();

		CommerceMoney _taxValue = commerceOrderPrice.getTaxValue();

		CommerceMoney commerceOrderPriceTotal = commerceOrderPrice.getTotal();

		Summary summary = new Summary() {
			{
				itemsQuantity = commerceOrderItems.size();
				shippingValue = _shippingValue.format(locale);
				subtotal = commerceOrderPriceSubTotal.format(locale);
				taxValue = _taxValue.format(locale);
				total = commerceOrderPriceTotal.format(locale);
			}
		};

		CommerceDiscountValue shippingDiscountValue =
			commerceOrderPrice.getShippingDiscountValue();

		if (shippingDiscountValue != null) {
			CommerceMoney shippingDiscountValueDiscountAmount =
				shippingDiscountValue.getDiscountAmount();

			summary.setShippingDiscountValue(
				shippingDiscountValueDiscountAmount.format(locale));
		}

		CommerceDiscountValue subtotalDiscountValue =
			commerceOrderPrice.getSubtotalDiscountValue();

		if (subtotalDiscountValue != null) {
			CommerceMoney subtotalDiscountValueDiscountAmount =
				subtotalDiscountValue.getDiscountAmount();

			summary.setSubtotalDiscountValue(
				subtotalDiscountValueDiscountAmount.format(locale));
		}

		CommerceDiscountValue totalDiscountValue =
			commerceOrderPrice.getTotalDiscountValue();

		if (totalDiscountValue != null) {
			CommerceMoney totalDiscountValueDiscountAmount =
				totalDiscountValue.getDiscountAmount();

			summary.setTotalDiscountValue(
				totalDiscountValueDiscountAmount.format(locale));
		}

		return summary;
	}

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderPriceCalculation _commerceOrderPriceCalculation;

	@Reference
	private CommerceOrderService _commerceOrderService;

}