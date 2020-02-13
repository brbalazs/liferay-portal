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

package com.liferay.commerce.order.web.internal.frontend.application.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.frontend.model.SummaryElement;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommerceOrderPrice;
import com.liferay.commerce.price.CommerceOrderPriceCalculation;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(service = CommerceOrderResource.class)
public class CommerceOrderResource {

	@GET
	@Path("/order/{orderId}/summary/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrderSummary(
		@PathParam("orderId") long commerceOrderId,
		@Context HttpServletRequest httpServletRequest) {

		List<SummaryElement> summary = new ArrayList<>();

		try {
			CommerceOrder commerceOrder =
				_commerceOrderService.fetchCommerceOrder(commerceOrderId);

			if (commerceOrder == null) {
				return getResponse(summary);
			}

			Locale locale = _portal.getLocale(httpServletRequest);

			CommerceContext commerceContext = _commerceContextFactory.create(
				commerceOrder.getCompanyId(), commerceOrder.getGroupId(),
				_portal.getUserId(httpServletRequest),
				commerceOrder.getCommerceOrderId(),
				commerceOrder.getCommerceAccountId());

			SummaryElement itemsSubtotalSummaryElement = new SummaryElement();
			SummaryElement itemsSubtotalDiscountSummaryElement =
				new SummaryElement();
			SummaryElement orderDiscountSummaryElement = new SummaryElement();
			SummaryElement promotionCodeSummaryElement = new SummaryElement();
			SummaryElement estimatedTaxSummaryElement = new SummaryElement();
			SummaryElement shippingAndHandingSummaryElement =
				new SummaryElement();
			SummaryElement shippingAndHandingDiscountSummaryElement =
				new SummaryElement();
			SummaryElement grandTotalSummaryElement = new SummaryElement();

			itemsSubtotalSummaryElement.setLabel(
				LanguageUtil.get(httpServletRequest, "items-subtotal"));

			CommerceOrderPrice commerceOrderPrice =
				_commerceOrderPriceCalculation.getCommerceOrderPrice(
					commerceOrder, commerceContext);

			CommerceMoney subtotal = commerceOrderPrice.getSubtotal();

			if (subtotal != null) {
				itemsSubtotalSummaryElement.setValue(subtotal.format(locale));
			}

			itemsSubtotalDiscountSummaryElement.setLabel(
				LanguageUtil.get(
					httpServletRequest, "items-subtotal-discount"));

			CommerceDiscountValue subtotalDiscountValue =
				commerceOrderPrice.getSubtotalDiscountValue();

			if (subtotalDiscountValue != null) {
				CommerceMoney discountAmount =
					subtotalDiscountValue.getDiscountAmount();

				itemsSubtotalDiscountSummaryElement.setValue(
					discountAmount.format(locale));
			}

			orderDiscountSummaryElement.setLabel(
				LanguageUtil.get(httpServletRequest, "order-discount"));

			CommerceDiscountValue totalDiscountValue =
				commerceOrderPrice.getTotalDiscountValue();

			if (totalDiscountValue != null) {
				CommerceMoney discountAmount =
					totalDiscountValue.getDiscountAmount();

				orderDiscountSummaryElement.setValue(
					discountAmount.format(locale));
			}

			promotionCodeSummaryElement.setLabel(
				LanguageUtil.get(httpServletRequest, "promotion-code"));
			promotionCodeSummaryElement.setValue(
				commerceOrder.getCouponCode(), "--");

			estimatedTaxSummaryElement.setLabel(
				LanguageUtil.get(httpServletRequest, "estimated-tax"));

			CommerceMoney taxValue = commerceOrderPrice.getTaxValue();

			if (taxValue != null) {
				estimatedTaxSummaryElement.setValue(taxValue.format(locale));
			}

			shippingAndHandingSummaryElement.setLabel(
				LanguageUtil.get(httpServletRequest, "shipping-and-handing"));

			CommerceMoney shippingValue = commerceOrderPrice.getShippingValue();

			if (shippingValue != null) {
				shippingAndHandingSummaryElement.setValue(
					shippingValue.format(locale));
			}

			shippingAndHandingDiscountSummaryElement.setLabel(
				LanguageUtil.get(
					httpServletRequest, "shipping-and-handing-discount"));

			CommerceDiscountValue shippingDiscountValue =
				commerceOrderPrice.getShippingDiscountValue();

			if (shippingDiscountValue != null) {
				CommerceMoney discountAmount =
					shippingDiscountValue.getDiscountAmount();

				shippingAndHandingDiscountSummaryElement.setValue(
					discountAmount.format(locale));
			}

			grandTotalSummaryElement.setLabel(
				LanguageUtil.get(httpServletRequest, "grand-total"));
			grandTotalSummaryElement.setStyle("big");

			CommerceMoney total = commerceOrderPrice.getTotal();

			if (total != null) {
				grandTotalSummaryElement.setValue(total.format(locale));
			}

			summary.add(itemsSubtotalSummaryElement);
			summary.add(itemsSubtotalDiscountSummaryElement);
			summary.add(orderDiscountSummaryElement);
			summary.add(promotionCodeSummaryElement);
			summary.add(estimatedTaxSummaryElement);
			summary.add(shippingAndHandingSummaryElement);
			summary.add(shippingAndHandingDiscountSummaryElement);
			summary.add(grandTotalSummaryElement);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return getResponse(summary);
	}

	protected Response getResponse(Object object) {
		if (object == null) {
			return Response.status(
				Response.Status.NOT_FOUND
			).build();
		}

		try {
			String json = _OBJECT_MAPPER.writeValueAsString(object);

			return Response.ok(
				json, MediaType.APPLICATION_JSON
			).build();
		}
		catch (JsonProcessingException jpe) {
			_log.error(jpe, jpe);
		}

		return Response.status(
			Response.Status.NOT_FOUND
		).build();
	}

	private static final ObjectMapper _OBJECT_MAPPER = new ObjectMapper() {
		{
			configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
			disable(SerializationFeature.INDENT_OUTPUT);
		}
	};

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderResource.class);

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderPriceCalculation _commerceOrderPriceCalculation;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private Portal _portal;

}