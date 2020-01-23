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

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.payment.engine.CommercePaymentEngine;
import com.liferay.commerce.price.CommerceOrderPrice;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Summary;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.language.LanguageResources;

import java.math.BigDecimal;

import java.util.ArrayList;
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

		ExpandoBridge expandoBridge = commerceOrder.getExpandoBridge();

		Locale locale = cartDTOConverterContext.getLocale();

		ResourceBundle resourceBundle = LanguageResources.getResourceBundle(
			locale);

		String workflowStatusLabel = LanguageUtil.get(
			resourceBundle,
			WorkflowConstants.getStatusLabel(commerceOrder.getStatus()));

		String commerceOrderPaymentStatusLabel = LanguageUtil.get(
			resourceBundle,
			CommerceOrderConstants.getPaymentStatusLabel(
				commerceOrder.getPaymentStatus()));

		Cart cart = new Cart() {
			{
				account = commerceOrder.getCommerceAccountName();
				accountId = commerceOrder.getCommerceAccountId();
				author = commerceOrder.getUserName();
				couponCode = commerceOrder.getCouponCode();
				createDate = commerceOrder.getCreateDate();
				customFields = expandoBridge.getAttributes();
				id = commerceOrder.getCommerceOrderId();
				lastPriceUpdateDate = commerceOrder.getLastPriceUpdateDate();
				modifiedDate = commerceOrder.getModifiedDate();
				paymentMethod = commerceOrder.getCommercePaymentMethodKey();
				paymentStatus = commerceOrder.getPaymentStatus();
				paymentStatusLabel = commerceOrderPaymentStatusLabel;
				printedNote = commerceOrder.getPrintedNote();
				purchaseOrderNumber = commerceOrder.getPurchaseOrderNumber();
				status = workflowStatusLabel;
				summary = _getSummary(
					commerceOrder, locale,
					cartDTOConverterContext.getChannelSiteGroupId());
			}
		};

		String paymentMethodKey = commerceOrder.getCommercePaymentMethodKey();

		if ((paymentMethodKey != null) && !paymentMethodKey.isEmpty()) {
			String commerceOrderPaymentMethodName =
				_commercePaymentEngine.getPaymentMethodName(
					paymentMethodKey, locale);

			cart.setPaymentMethodLabel(commerceOrderPaymentMethodName);
		}

		return cart;
	}

	private String[] _getFormattedDiscountPercentages(
			BigDecimal[] discountPercentages, Locale locale)
		throws PortalException {

		List<String> formattedDiscountPercentages = new ArrayList<>();

		for (BigDecimal percentage : discountPercentages) {
			formattedDiscountPercentages.add(
				_commercePriceFormatter.format(percentage, locale));
		}

		return formattedDiscountPercentages.toArray(new String[0]);
	}

	private Summary _getSummary(
			CommerceOrder commerceOrder, Locale locale, long channelSiteGroupId)
		throws PortalException {

		CommerceContext commerceContext = _commerceContextFactory.create(
			commerceOrder.getCompanyId(), channelSiteGroupId,
			commerceOrder.getUserId(), commerceOrder.getCommerceOrderId(),
			commerceOrder.getCommerceAccountId());

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		List<CommerceOrderItem> commerceOrderItems =
			commerceOrder.getCommerceOrderItems();

		CommerceOrderPrice commerceOrderPrice =
			_commerceOrderPriceCalculation.getCommerceOrderPrice(
				commerceOrder, commerceContext);

		CommerceMoney commerceOrderPriceShippingValue =
			commerceOrderPrice.getShippingValue();

		BigDecimal commerceOrderPriceShippingValuePrice =
			commerceOrderPriceShippingValue.getPrice();

		CommerceMoney commerceOrderPriceSubTotal =
			commerceOrderPrice.getSubtotal();

		BigDecimal orderPriceSubTotalPrice =
			commerceOrderPriceSubTotal.getPrice();

		CommerceMoney commerceOrderPriceTaxValue =
			commerceOrderPrice.getTaxValue();

		BigDecimal commerceOrderPriceTaxValuePrice =
			commerceOrderPriceTaxValue.getPrice();

		CommerceMoney commerceOrderPriceTotal = commerceOrderPrice.getTotal();

		BigDecimal orderPriceTotalPrice = commerceOrderPriceTotal.getPrice();

		Summary summary = new Summary() {
			{
				currency = commerceCurrency.getName(locale);
				itemsQuantity = commerceOrderItems.size();
				shippingValue =
					commerceOrderPriceShippingValuePrice.doubleValue();
				shippingValueFormatted = commerceOrderPriceShippingValue.format(
					locale);
				subtotal = orderPriceSubTotalPrice.doubleValue();
				subtotalFormatted = commerceOrderPriceSubTotal.format(locale);
				taxValue = commerceOrderPriceTaxValuePrice.doubleValue();
				taxValueFormatted = commerceOrderPriceTaxValue.format(locale);
				total = orderPriceTotalPrice.doubleValue();
				totalFormatted = commerceOrderPriceTotal.format(locale);
			}
		};

		CommerceDiscountValue shippingDiscountValue =
			commerceOrderPrice.getShippingDiscountValue();

		if (shippingDiscountValue != null) {
			CommerceMoney shippingDiscountValueDiscountAmount =
				shippingDiscountValue.getDiscountAmount();

			BigDecimal shippingDiscountValueDiscountAmountPrice =
				shippingDiscountValueDiscountAmount.getPrice();

			summary.setShippingDiscountValue(
				shippingDiscountValueDiscountAmountPrice.doubleValue());

			summary.setShippingDiscountValueFormatted(
				shippingDiscountValueDiscountAmount.format(locale));

			summary.setShippingDiscountPercentages(
				_getFormattedDiscountPercentages(
					shippingDiscountValue.getPercentages(), locale));
		}

		CommerceDiscountValue subtotalDiscountValue =
			commerceOrderPrice.getSubtotalDiscountValue();

		if (subtotalDiscountValue != null) {
			CommerceMoney subtotalDiscountValueDiscountAmount =
				subtotalDiscountValue.getDiscountAmount();

			BigDecimal subtotalDiscountValueDiscountAmountPrice =
				subtotalDiscountValueDiscountAmount.getPrice();

			summary.setSubtotal(
				subtotalDiscountValueDiscountAmountPrice.doubleValue());

			summary.setSubtotalDiscountValueFormatted(
				subtotalDiscountValueDiscountAmount.format(locale));

			summary.setSubtotalDiscountPercentages(
				_getFormattedDiscountPercentages(
					subtotalDiscountValue.getPercentages(), locale));
		}

		CommerceDiscountValue totalDiscountValue =
			commerceOrderPrice.getTotalDiscountValue();

		if (totalDiscountValue != null) {
			CommerceMoney totalDiscountValueDiscountAmount =
				totalDiscountValue.getDiscountAmount();

			BigDecimal totalDiscountValueDiscountAmountPrice =
				totalDiscountValueDiscountAmount.getPrice();

			summary.setTotal(
				totalDiscountValueDiscountAmountPrice.doubleValue());

			summary.setTotalDiscountValueFormatted(
				totalDiscountValueDiscountAmount.format(locale));

			summary.setTotalDiscountPercentages(
				_getFormattedDiscountPercentages(
					totalDiscountValue.getPercentages(), locale));
		}

		return summary;
	}

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderPriceCalculation _commerceOrderPriceCalculation;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommercePaymentEngine _commercePaymentEngine;

	@Reference
	private CommercePriceFormatter _commercePriceFormatter;

}