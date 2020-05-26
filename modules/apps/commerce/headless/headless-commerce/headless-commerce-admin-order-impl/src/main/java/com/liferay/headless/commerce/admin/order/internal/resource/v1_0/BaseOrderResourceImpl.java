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

package com.liferay.headless.commerce.admin.order.internal.resource.v1_0;

import com.liferay.headless.commerce.admin.order.dto.v1_0.Order;
import com.liferay.headless.commerce.admin.order.resource.v1_0.OrderResource;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.VulcanBatchEngineTaskItemDelegate;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.io.Serializable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.constraints.NotNull;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
@Path("/v1.0")
public abstract class BaseOrderResourceImpl
	implements OrderResource, EntityModelResource,
			   VulcanBatchEngineTaskItemDelegate<Order> {

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.QUERY, name = "filter"),
			@Parameter(in = ParameterIn.QUERY, name = "page"),
			@Parameter(in = ParameterIn.QUERY, name = "pageSize"),
			@Parameter(in = ParameterIn.QUERY, name = "sort")
		}
	)
	@Path("/orders")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Page<Order> getOrdersPage(
			@Context Filter filter, @Context Pagination pagination,
			@Context Sort[] sorts)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders' -d $'{"accountExternalReferenceCode": ___, "accountId": ___, "advanceStatus": ___, "billingAddress": ___, "billingAddressId": ___, "channelId": ___, "couponCode": ___, "createDate": ___, "currencyCode": ___, "customFields": ___, "externalReferenceCode": ___, "id": ___, "lastPriceUpdateDate": ___, "modifiedDate": ___, "orderDate": ___, "orderItems": ___, "orderStatus": ___, "paymentMethod": ___, "paymentStatus": ___, "printedNote": ___, "purchaseOrderNumber": ___, "requestedDeliveryDate": ___, "shippingAddress": ___, "shippingAddressId": ___, "shippingAmount": ___, "shippingAmountFormatted": ___, "shippingAmountValue": ___, "shippingDiscountAmount": ___, "shippingDiscountAmountFormatted": ___, "shippingDiscountPercentageLevel1": ___, "shippingDiscountPercentageLevel2": ___, "shippingDiscountPercentageLevel3": ___, "shippingDiscountPercentageLevel4": ___, "shippingMethod": ___, "shippingOption": ___, "subtotal": ___, "subtotalAmount": ___, "subtotalDiscountAmount": ___, "subtotalDiscountAmountFormatted": ___, "subtotalDiscountPercentageLevel1": ___, "subtotalDiscountPercentageLevel2": ___, "subtotalDiscountPercentageLevel3": ___, "subtotalDiscountPercentageLevel4": ___, "subtotalFormatted": ___, "taxAmount": ___, "taxAmountFormatted": ___, "total": ___, "totalAmount": ___, "totalDiscountAmount": ___, "totalDiscountAmountFormatted": ___, "totalDiscountPercentageLevel1": ___, "totalDiscountPercentageLevel2": ___, "totalDiscountPercentageLevel3": ___, "totalDiscountPercentageLevel4": ___, "totalFormatted": ___, "transactionId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@POST
	@Path("/orders")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Order postOrder(Order order) throws Exception {
		return new Order();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'POST' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/batch'  -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes("application/json")
	@POST
	@Parameters(
		value = {@Parameter(in = ParameterIn.QUERY, name = "callbackURL")}
	)
	@Path("/orders/batch")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Order")})
	public Response postOrderBatch(
			@Parameter(hidden = true) @QueryParam("callbackURL") String
				callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.postImportTask(
				Order.class.getName(), callbackURL, null, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/by-externalReferenceCode/{externalReferenceCode}'  -u 'test@liferay.com:test'
	 */
	@Override
	@DELETE
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "externalReferenceCode")
		}
	)
	@Path("/orders/by-externalReferenceCode/{externalReferenceCode}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Response deleteOrderByExternalReferenceCode(
			@NotNull @Parameter(hidden = true)
			@PathParam("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/by-externalReferenceCode/{externalReferenceCode}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "externalReferenceCode")
		}
	)
	@Path("/orders/by-externalReferenceCode/{externalReferenceCode}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Order getOrderByExternalReferenceCode(
			@NotNull @Parameter(hidden = true)
			@PathParam("externalReferenceCode") String externalReferenceCode)
		throws Exception {

		return new Order();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/by-externalReferenceCode/{externalReferenceCode}' -d $'{"accountExternalReferenceCode": ___, "accountId": ___, "advanceStatus": ___, "billingAddress": ___, "billingAddressId": ___, "channelId": ___, "couponCode": ___, "createDate": ___, "currencyCode": ___, "customFields": ___, "externalReferenceCode": ___, "id": ___, "lastPriceUpdateDate": ___, "modifiedDate": ___, "orderDate": ___, "orderItems": ___, "orderStatus": ___, "paymentMethod": ___, "paymentStatus": ___, "printedNote": ___, "purchaseOrderNumber": ___, "requestedDeliveryDate": ___, "shippingAddress": ___, "shippingAddressId": ___, "shippingAmount": ___, "shippingAmountFormatted": ___, "shippingAmountValue": ___, "shippingDiscountAmount": ___, "shippingDiscountAmountFormatted": ___, "shippingDiscountPercentageLevel1": ___, "shippingDiscountPercentageLevel2": ___, "shippingDiscountPercentageLevel3": ___, "shippingDiscountPercentageLevel4": ___, "shippingMethod": ___, "shippingOption": ___, "subtotal": ___, "subtotalAmount": ___, "subtotalDiscountAmount": ___, "subtotalDiscountAmountFormatted": ___, "subtotalDiscountPercentageLevel1": ___, "subtotalDiscountPercentageLevel2": ___, "subtotalDiscountPercentageLevel3": ___, "subtotalDiscountPercentageLevel4": ___, "subtotalFormatted": ___, "taxAmount": ___, "taxAmountFormatted": ___, "total": ___, "totalAmount": ___, "totalDiscountAmount": ___, "totalDiscountAmountFormatted": ___, "totalDiscountPercentageLevel1": ___, "totalDiscountPercentageLevel2": ___, "totalDiscountPercentageLevel3": ___, "totalDiscountPercentageLevel4": ___, "totalFormatted": ___, "transactionId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@PATCH
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "externalReferenceCode")
		}
	)
	@Path("/orders/by-externalReferenceCode/{externalReferenceCode}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Response patchOrderByExternalReferenceCode(
			@NotNull @Parameter(hidden = true)
			@PathParam("externalReferenceCode") String externalReferenceCode,
			Order order)
		throws Exception {

		Order existingOrder = getOrder(externalReferenceCode);

		if (order.getAccountExternalReferenceCode() != null) {
			existingOrder.setAccountExternalReferenceCode(
				order.getAccountExternalReferenceCode());
		}

		if (order.getAccountId() != null) {
			existingOrder.setAccountId(order.getAccountId());
		}

		if (order.getAdvanceStatus() != null) {
			existingOrder.setAdvanceStatus(order.getAdvanceStatus());
		}

		if (order.getBillingAddressId() != null) {
			existingOrder.setBillingAddressId(order.getBillingAddressId());
		}

		if (order.getChannelId() != null) {
			existingOrder.setChannelId(order.getChannelId());
		}

		if (order.getCouponCode() != null) {
			existingOrder.setCouponCode(order.getCouponCode());
		}

		if (order.getCreateDate() != null) {
			existingOrder.setCreateDate(order.getCreateDate());
		}

		if (order.getCurrencyCode() != null) {
			existingOrder.setCurrencyCode(order.getCurrencyCode());
		}

		if (order.getCustomFields() != null) {
			existingOrder.setCustomFields(order.getCustomFields());
		}

		if (order.getExternalReferenceCode() != null) {
			existingOrder.setExternalReferenceCode(
				order.getExternalReferenceCode());
		}

		if (order.getLastPriceUpdateDate() != null) {
			existingOrder.setLastPriceUpdateDate(
				order.getLastPriceUpdateDate());
		}

		if (order.getModifiedDate() != null) {
			existingOrder.setModifiedDate(order.getModifiedDate());
		}

		if (order.getOrderDate() != null) {
			existingOrder.setOrderDate(order.getOrderDate());
		}

		if (order.getOrderStatus() != null) {
			existingOrder.setOrderStatus(order.getOrderStatus());
		}

		if (order.getPaymentMethod() != null) {
			existingOrder.setPaymentMethod(order.getPaymentMethod());
		}

		if (order.getPaymentStatus() != null) {
			existingOrder.setPaymentStatus(order.getPaymentStatus());
		}

		if (order.getPrintedNote() != null) {
			existingOrder.setPrintedNote(order.getPrintedNote());
		}

		if (order.getPurchaseOrderNumber() != null) {
			existingOrder.setPurchaseOrderNumber(
				order.getPurchaseOrderNumber());
		}

		if (order.getRequestedDeliveryDate() != null) {
			existingOrder.setRequestedDeliveryDate(
				order.getRequestedDeliveryDate());
		}

		if (order.getShippingAddressId() != null) {
			existingOrder.setShippingAddressId(order.getShippingAddressId());
		}

		if (order.getShippingAmount() != null) {
			existingOrder.setShippingAmount(order.getShippingAmount());
		}

		if (order.getShippingAmountFormatted() != null) {
			existingOrder.setShippingAmountFormatted(
				order.getShippingAmountFormatted());
		}

		if (order.getShippingAmountValue() != null) {
			existingOrder.setShippingAmountValue(
				order.getShippingAmountValue());
		}

		if (order.getShippingDiscountAmount() != null) {
			existingOrder.setShippingDiscountAmount(
				order.getShippingDiscountAmount());
		}

		if (order.getShippingDiscountAmountFormatted() != null) {
			existingOrder.setShippingDiscountAmountFormatted(
				order.getShippingDiscountAmountFormatted());
		}

		if (order.getShippingDiscountPercentageLevel1() != null) {
			existingOrder.setShippingDiscountPercentageLevel1(
				order.getShippingDiscountPercentageLevel1());
		}

		if (order.getShippingDiscountPercentageLevel2() != null) {
			existingOrder.setShippingDiscountPercentageLevel2(
				order.getShippingDiscountPercentageLevel2());
		}

		if (order.getShippingDiscountPercentageLevel3() != null) {
			existingOrder.setShippingDiscountPercentageLevel3(
				order.getShippingDiscountPercentageLevel3());
		}

		if (order.getShippingDiscountPercentageLevel4() != null) {
			existingOrder.setShippingDiscountPercentageLevel4(
				order.getShippingDiscountPercentageLevel4());
		}

		if (order.getShippingMethod() != null) {
			existingOrder.setShippingMethod(order.getShippingMethod());
		}

		if (order.getShippingOption() != null) {
			existingOrder.setShippingOption(order.getShippingOption());
		}

		if (order.getSubtotal() != null) {
			existingOrder.setSubtotal(order.getSubtotal());
		}

		if (order.getSubtotalAmount() != null) {
			existingOrder.setSubtotalAmount(order.getSubtotalAmount());
		}

		if (order.getSubtotalDiscountAmount() != null) {
			existingOrder.setSubtotalDiscountAmount(
				order.getSubtotalDiscountAmount());
		}

		if (order.getSubtotalDiscountAmountFormatted() != null) {
			existingOrder.setSubtotalDiscountAmountFormatted(
				order.getSubtotalDiscountAmountFormatted());
		}

		if (order.getSubtotalDiscountPercentageLevel1() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel1(
				order.getSubtotalDiscountPercentageLevel1());
		}

		if (order.getSubtotalDiscountPercentageLevel2() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel2(
				order.getSubtotalDiscountPercentageLevel2());
		}

		if (order.getSubtotalDiscountPercentageLevel3() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel3(
				order.getSubtotalDiscountPercentageLevel3());
		}

		if (order.getSubtotalDiscountPercentageLevel4() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel4(
				order.getSubtotalDiscountPercentageLevel4());
		}

		if (order.getSubtotalFormatted() != null) {
			existingOrder.setSubtotalFormatted(order.getSubtotalFormatted());
		}

		if (order.getTaxAmount() != null) {
			existingOrder.setTaxAmount(order.getTaxAmount());
		}

		if (order.getTaxAmountFormatted() != null) {
			existingOrder.setTaxAmountFormatted(order.getTaxAmountFormatted());
		}

		if (order.getTotal() != null) {
			existingOrder.setTotal(order.getTotal());
		}

		if (order.getTotalAmount() != null) {
			existingOrder.setTotalAmount(order.getTotalAmount());
		}

		if (order.getTotalDiscountAmount() != null) {
			existingOrder.setTotalDiscountAmount(
				order.getTotalDiscountAmount());
		}

		if (order.getTotalDiscountAmountFormatted() != null) {
			existingOrder.setTotalDiscountAmountFormatted(
				order.getTotalDiscountAmountFormatted());
		}

		if (order.getTotalDiscountPercentageLevel1() != null) {
			existingOrder.setTotalDiscountPercentageLevel1(
				order.getTotalDiscountPercentageLevel1());
		}

		if (order.getTotalDiscountPercentageLevel2() != null) {
			existingOrder.setTotalDiscountPercentageLevel2(
				order.getTotalDiscountPercentageLevel2());
		}

		if (order.getTotalDiscountPercentageLevel3() != null) {
			existingOrder.setTotalDiscountPercentageLevel3(
				order.getTotalDiscountPercentageLevel3());
		}

		if (order.getTotalDiscountPercentageLevel4() != null) {
			existingOrder.setTotalDiscountPercentageLevel4(
				order.getTotalDiscountPercentageLevel4());
		}

		if (order.getTotalFormatted() != null) {
			existingOrder.setTotalFormatted(order.getTotalFormatted());
		}

		if (order.getTransactionId() != null) {
			existingOrder.setTransactionId(order.getTransactionId());
		}

		preparePatch(order, existingOrder);

		return putOrder(externalReferenceCode, existingOrder);
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/{id}'  -u 'test@liferay.com:test'
	 */
	@Override
	@DELETE
	@Parameters(value = {@Parameter(in = ParameterIn.PATH, name = "id")})
	@Path("/orders/{id}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Response deleteOrder(
			@NotNull @Parameter(hidden = true) @PathParam("id") Long id)
		throws Exception {

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'DELETE' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/{id}/batch'  -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes("application/json")
	@DELETE
	@Parameters(
		value = {
			@Parameter(in = ParameterIn.PATH, name = "id"),
			@Parameter(in = ParameterIn.QUERY, name = "callbackURL")
		}
	)
	@Path("/orders/{id}/batch")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Order")})
	public Response deleteOrderBatch(
			@NotNull @Parameter(hidden = true) @PathParam("id") Long id,
			@Parameter(hidden = true) @QueryParam("callbackURL") String
				callbackURL,
			Object object)
		throws Exception {

		vulcanBatchEngineImportTaskResource.setContextAcceptLanguage(
			contextAcceptLanguage);
		vulcanBatchEngineImportTaskResource.setContextCompany(contextCompany);
		vulcanBatchEngineImportTaskResource.setContextHttpServletRequest(
			contextHttpServletRequest);
		vulcanBatchEngineImportTaskResource.setContextUriInfo(contextUriInfo);
		vulcanBatchEngineImportTaskResource.setContextUser(contextUser);

		Response.ResponseBuilder responseBuilder = Response.accepted();

		return responseBuilder.entity(
			vulcanBatchEngineImportTaskResource.deleteImportTask(
				Order.class.getName(), callbackURL, object)
		).build();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'GET' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/{id}'  -u 'test@liferay.com:test'
	 */
	@Override
	@GET
	@Parameters(value = {@Parameter(in = ParameterIn.PATH, name = "id")})
	@Path("/orders/{id}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Order getOrder(
			@NotNull @Parameter(hidden = true) @PathParam("id") Long id)
		throws Exception {

		return new Order();
	}

	/**
	 * Invoke this method with the command line:
	 *
	 * curl -X 'PATCH' 'http://localhost:8080/o/headless-commerce-admin-order/v1.0/orders/{id}' -d $'{"accountExternalReferenceCode": ___, "accountId": ___, "advanceStatus": ___, "billingAddress": ___, "billingAddressId": ___, "channelId": ___, "couponCode": ___, "createDate": ___, "currencyCode": ___, "customFields": ___, "externalReferenceCode": ___, "id": ___, "lastPriceUpdateDate": ___, "modifiedDate": ___, "orderDate": ___, "orderItems": ___, "orderStatus": ___, "paymentMethod": ___, "paymentStatus": ___, "printedNote": ___, "purchaseOrderNumber": ___, "requestedDeliveryDate": ___, "shippingAddress": ___, "shippingAddressId": ___, "shippingAmount": ___, "shippingAmountFormatted": ___, "shippingAmountValue": ___, "shippingDiscountAmount": ___, "shippingDiscountAmountFormatted": ___, "shippingDiscountPercentageLevel1": ___, "shippingDiscountPercentageLevel2": ___, "shippingDiscountPercentageLevel3": ___, "shippingDiscountPercentageLevel4": ___, "shippingMethod": ___, "shippingOption": ___, "subtotal": ___, "subtotalAmount": ___, "subtotalDiscountAmount": ___, "subtotalDiscountAmountFormatted": ___, "subtotalDiscountPercentageLevel1": ___, "subtotalDiscountPercentageLevel2": ___, "subtotalDiscountPercentageLevel3": ___, "subtotalDiscountPercentageLevel4": ___, "subtotalFormatted": ___, "taxAmount": ___, "taxAmountFormatted": ___, "total": ___, "totalAmount": ___, "totalDiscountAmount": ___, "totalDiscountAmountFormatted": ___, "totalDiscountPercentageLevel1": ___, "totalDiscountPercentageLevel2": ___, "totalDiscountPercentageLevel3": ___, "totalDiscountPercentageLevel4": ___, "totalFormatted": ___, "transactionId": ___}' --header 'Content-Type: application/json' -u 'test@liferay.com:test'
	 */
	@Override
	@Consumes({"application/json", "application/xml"})
	@PATCH
	@Parameters(value = {@Parameter(in = ParameterIn.PATH, name = "id")})
	@Path("/orders/{id}")
	@Produces({"application/json", "application/xml"})
	@Tags(value = {@Tag(name = "Order")})
	public Response patchOrder(
			@NotNull @Parameter(hidden = true) @PathParam("id") Long id,
			Order order)
		throws Exception {

		Order existingOrder = getOrder(id);

		if (order.getAccountExternalReferenceCode() != null) {
			existingOrder.setAccountExternalReferenceCode(
				order.getAccountExternalReferenceCode());
		}

		if (order.getAccountId() != null) {
			existingOrder.setAccountId(order.getAccountId());
		}

		if (order.getAdvanceStatus() != null) {
			existingOrder.setAdvanceStatus(order.getAdvanceStatus());
		}

		if (order.getBillingAddressId() != null) {
			existingOrder.setBillingAddressId(order.getBillingAddressId());
		}

		if (order.getChannelId() != null) {
			existingOrder.setChannelId(order.getChannelId());
		}

		if (order.getCouponCode() != null) {
			existingOrder.setCouponCode(order.getCouponCode());
		}

		if (order.getCreateDate() != null) {
			existingOrder.setCreateDate(order.getCreateDate());
		}

		if (order.getCurrencyCode() != null) {
			existingOrder.setCurrencyCode(order.getCurrencyCode());
		}

		if (order.getCustomFields() != null) {
			existingOrder.setCustomFields(order.getCustomFields());
		}

		if (order.getExternalReferenceCode() != null) {
			existingOrder.setExternalReferenceCode(
				order.getExternalReferenceCode());
		}

		if (order.getLastPriceUpdateDate() != null) {
			existingOrder.setLastPriceUpdateDate(
				order.getLastPriceUpdateDate());
		}

		if (order.getModifiedDate() != null) {
			existingOrder.setModifiedDate(order.getModifiedDate());
		}

		if (order.getOrderDate() != null) {
			existingOrder.setOrderDate(order.getOrderDate());
		}

		if (order.getOrderStatus() != null) {
			existingOrder.setOrderStatus(order.getOrderStatus());
		}

		if (order.getPaymentMethod() != null) {
			existingOrder.setPaymentMethod(order.getPaymentMethod());
		}

		if (order.getPaymentStatus() != null) {
			existingOrder.setPaymentStatus(order.getPaymentStatus());
		}

		if (order.getPrintedNote() != null) {
			existingOrder.setPrintedNote(order.getPrintedNote());
		}

		if (order.getPurchaseOrderNumber() != null) {
			existingOrder.setPurchaseOrderNumber(
				order.getPurchaseOrderNumber());
		}

		if (order.getRequestedDeliveryDate() != null) {
			existingOrder.setRequestedDeliveryDate(
				order.getRequestedDeliveryDate());
		}

		if (order.getShippingAddressId() != null) {
			existingOrder.setShippingAddressId(order.getShippingAddressId());
		}

		if (order.getShippingAmount() != null) {
			existingOrder.setShippingAmount(order.getShippingAmount());
		}

		if (order.getShippingAmountFormatted() != null) {
			existingOrder.setShippingAmountFormatted(
				order.getShippingAmountFormatted());
		}

		if (order.getShippingAmountValue() != null) {
			existingOrder.setShippingAmountValue(
				order.getShippingAmountValue());
		}

		if (order.getShippingDiscountAmount() != null) {
			existingOrder.setShippingDiscountAmount(
				order.getShippingDiscountAmount());
		}

		if (order.getShippingDiscountAmountFormatted() != null) {
			existingOrder.setShippingDiscountAmountFormatted(
				order.getShippingDiscountAmountFormatted());
		}

		if (order.getShippingDiscountPercentageLevel1() != null) {
			existingOrder.setShippingDiscountPercentageLevel1(
				order.getShippingDiscountPercentageLevel1());
		}

		if (order.getShippingDiscountPercentageLevel2() != null) {
			existingOrder.setShippingDiscountPercentageLevel2(
				order.getShippingDiscountPercentageLevel2());
		}

		if (order.getShippingDiscountPercentageLevel3() != null) {
			existingOrder.setShippingDiscountPercentageLevel3(
				order.getShippingDiscountPercentageLevel3());
		}

		if (order.getShippingDiscountPercentageLevel4() != null) {
			existingOrder.setShippingDiscountPercentageLevel4(
				order.getShippingDiscountPercentageLevel4());
		}

		if (order.getShippingMethod() != null) {
			existingOrder.setShippingMethod(order.getShippingMethod());
		}

		if (order.getShippingOption() != null) {
			existingOrder.setShippingOption(order.getShippingOption());
		}

		if (order.getSubtotal() != null) {
			existingOrder.setSubtotal(order.getSubtotal());
		}

		if (order.getSubtotalAmount() != null) {
			existingOrder.setSubtotalAmount(order.getSubtotalAmount());
		}

		if (order.getSubtotalDiscountAmount() != null) {
			existingOrder.setSubtotalDiscountAmount(
				order.getSubtotalDiscountAmount());
		}

		if (order.getSubtotalDiscountAmountFormatted() != null) {
			existingOrder.setSubtotalDiscountAmountFormatted(
				order.getSubtotalDiscountAmountFormatted());
		}

		if (order.getSubtotalDiscountPercentageLevel1() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel1(
				order.getSubtotalDiscountPercentageLevel1());
		}

		if (order.getSubtotalDiscountPercentageLevel2() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel2(
				order.getSubtotalDiscountPercentageLevel2());
		}

		if (order.getSubtotalDiscountPercentageLevel3() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel3(
				order.getSubtotalDiscountPercentageLevel3());
		}

		if (order.getSubtotalDiscountPercentageLevel4() != null) {
			existingOrder.setSubtotalDiscountPercentageLevel4(
				order.getSubtotalDiscountPercentageLevel4());
		}

		if (order.getSubtotalFormatted() != null) {
			existingOrder.setSubtotalFormatted(order.getSubtotalFormatted());
		}

		if (order.getTaxAmount() != null) {
			existingOrder.setTaxAmount(order.getTaxAmount());
		}

		if (order.getTaxAmountFormatted() != null) {
			existingOrder.setTaxAmountFormatted(order.getTaxAmountFormatted());
		}

		if (order.getTotal() != null) {
			existingOrder.setTotal(order.getTotal());
		}

		if (order.getTotalAmount() != null) {
			existingOrder.setTotalAmount(order.getTotalAmount());
		}

		if (order.getTotalDiscountAmount() != null) {
			existingOrder.setTotalDiscountAmount(
				order.getTotalDiscountAmount());
		}

		if (order.getTotalDiscountAmountFormatted() != null) {
			existingOrder.setTotalDiscountAmountFormatted(
				order.getTotalDiscountAmountFormatted());
		}

		if (order.getTotalDiscountPercentageLevel1() != null) {
			existingOrder.setTotalDiscountPercentageLevel1(
				order.getTotalDiscountPercentageLevel1());
		}

		if (order.getTotalDiscountPercentageLevel2() != null) {
			existingOrder.setTotalDiscountPercentageLevel2(
				order.getTotalDiscountPercentageLevel2());
		}

		if (order.getTotalDiscountPercentageLevel3() != null) {
			existingOrder.setTotalDiscountPercentageLevel3(
				order.getTotalDiscountPercentageLevel3());
		}

		if (order.getTotalDiscountPercentageLevel4() != null) {
			existingOrder.setTotalDiscountPercentageLevel4(
				order.getTotalDiscountPercentageLevel4());
		}

		if (order.getTotalFormatted() != null) {
			existingOrder.setTotalFormatted(order.getTotalFormatted());
		}

		if (order.getTransactionId() != null) {
			existingOrder.setTransactionId(order.getTransactionId());
		}

		preparePatch(order, existingOrder);

		return putOrder(id, existingOrder);
	}

	@Override
	@SuppressWarnings("PMD.UnusedLocalVariable")
	public void create(
			java.util.Collection<Order> orders,
			Map<String, Serializable> parameters)
		throws Exception {

		for (Order order : orders) {
			postOrder(order);
		}
	}

	@Override
	public void delete(
			java.util.Collection<Order> orders,
			Map<String, Serializable> parameters)
		throws Exception {

		for (Order order : orders) {
			deleteOrder(order.getId());
		}
	}

	@Override
	public EntityModel getEntityModel(Map<String, List<String>> multivaluedMap)
		throws Exception {

		return getEntityModel(
			new MultivaluedHashMap<String, Object>(multivaluedMap));
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return null;
	}

	@Override
	public Page<Order> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		return getOrdersPage(filter, pagination, sorts);
	}

	@Override
	public void setLanguageId(String languageId) {
		this.contextAcceptLanguage = new AcceptLanguage() {

			@Override
			public List<Locale> getLocales() {
				return null;
			}

			@Override
			public String getPreferredLanguageId() {
				return languageId;
			}

			@Override
			public Locale getPreferredLocale() {
				return LocaleUtil.fromLanguageId(languageId);
			}

		};
	}

	@Override
	public void update(
			java.util.Collection<Order> orders,
			Map<String, Serializable> parameters)
		throws Exception {
	}

	public void setContextAcceptLanguage(AcceptLanguage contextAcceptLanguage) {
		this.contextAcceptLanguage = contextAcceptLanguage;
	}

	public void setContextCompany(
		com.liferay.portal.kernel.model.Company contextCompany) {

		this.contextCompany = contextCompany;
	}

	public void setContextHttpServletRequest(
		HttpServletRequest contextHttpServletRequest) {

		this.contextHttpServletRequest = contextHttpServletRequest;
	}

	public void setContextHttpServletResponse(
		HttpServletResponse contextHttpServletResponse) {

		this.contextHttpServletResponse = contextHttpServletResponse;
	}

	public void setContextUriInfo(UriInfo contextUriInfo) {
		this.contextUriInfo = contextUriInfo;
	}

	public void setContextUser(
		com.liferay.portal.kernel.model.User contextUser) {

		this.contextUser = contextUser;
	}

	protected Map<String, String> addAction(
		String actionName, GroupedModel groupedModel, String methodName) {

		return ActionUtil.addAction(
			actionName, getClass(), groupedModel, methodName,
			contextScopeChecker, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, Long id, String methodName, Long ownerId,
		String permissionName, Long siteId) {

		return ActionUtil.addAction(
			actionName, getClass(), id, methodName, contextScopeChecker,
			ownerId, permissionName, siteId, contextUriInfo);
	}

	protected Map<String, String> addAction(
		String actionName, String methodName, String permissionName,
		Long siteId) {

		return addAction(
			actionName, siteId, methodName, null, permissionName, siteId);
	}

	protected void preparePatch(Order order, Order existingOrder) {
	}

	protected <T, R> List<R> transform(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R> R[] transform(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R> R[] transformToArray(
		java.util.Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	protected AcceptLanguage contextAcceptLanguage;
	protected com.liferay.portal.kernel.model.Company contextCompany;
	protected HttpServletRequest contextHttpServletRequest;
	protected HttpServletResponse contextHttpServletResponse;
	protected Object contextScopeChecker;
	protected UriInfo contextUriInfo;
	protected com.liferay.portal.kernel.model.User contextUser;
	protected GroupLocalService groupLocalService;
	protected ResourceActionLocalService resourceActionLocalService;
	protected ResourcePermissionLocalService resourcePermissionLocalService;
	protected RoleLocalService roleLocalService;
	protected VulcanBatchEngineImportTaskResource
		vulcanBatchEngineImportTaskResource;

}