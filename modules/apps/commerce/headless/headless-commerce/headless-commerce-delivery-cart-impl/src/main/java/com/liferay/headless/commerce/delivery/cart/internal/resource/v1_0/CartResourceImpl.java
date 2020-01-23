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

package com.liferay.headless.commerce.delivery.cart.internal.resource.v1_0;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceShippingMethod;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.commerce.service.CommerceShippingMethodService;
import com.liferay.headless.commerce.core.util.ExpandoUtil;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.BillingAddress;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartItemPost;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CartPost;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.CouponCode;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.ShippingAddress;
import com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0.CartDTOConverter;
import com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0.CartDTOConverterContext;
import com.liferay.headless.commerce.delivery.cart.internal.v1_0.BillingAddressUtil;
import com.liferay.headless.commerce.delivery.cart.internal.v1_0.ShippingAddressUtil;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartResource;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Andrea Sbarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/cart.properties",
	scope = ServiceScope.PROTOTYPE, service = CartResource.class
)
public class CartResourceImpl extends BaseCartResourceImpl {

	@Override
	public Response deleteChannelCart(
			@NotNull Long channelId, @NotNull Long cartId)
		throws Exception {

		_commerceOrderService.deleteCommerceOrder(cartId);

		Response.ResponseBuilder responseBuilder = Response.noContent();

		return responseBuilder.build();
	}

	@Override
	public Cart getChannelCart(@NotNull Long channelId, @NotNull Long cartId)
		throws Exception {

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			cartId);

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		return _toCart(commerceOrder, commerceChannel.getSiteGroupId());
	}

	@Override
	public Page<Cart> getChannelCartsPage(
			@NotNull Long channelId, Pagination pagination)
		throws Exception {

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		List<CommerceOrder> commerceOrders =
			_commerceOrderService.getUserPendingCommerceOrders(
				contextCompany.getCompanyId(), commerceChannel.getGroupId(),
				null, pagination.getStartPosition(),
				pagination.getEndPosition());

		long pendingCommerceOrdersCount =
			_commerceOrderService.getPendingCommerceOrdersCount(
				contextCompany.getCompanyId(), commerceChannel.getGroupId());

		return Page.of(
			_toCarts(commerceOrders, commerceChannel.getSiteGroupId()),
			pagination, pendingCommerceOrdersCount);
	}

	@Override
	public Cart patchChannelCart(
			@NotNull Long channelId, @NotNull Long cartId, CartPost cartPost)
		throws Exception {

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			cartId);

		CommerceContext commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceChannel.getSiteGroupId(),
			contextUser.getUserId(), cartId, cartPost.getAccountId());

		commerceOrder = _updateOrder(
			commerceOrder, cartPost, commerceChannel, commerceContext);

		return _toCart(commerceOrder, commerceChannel.getSiteGroupId());
	}

	@Override
	public Cart postChannelCart(@NotNull Long channelId, CartPost cartPost)
		throws Exception {

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		CommerceContext commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceChannel.getSiteGroupId(),
			contextUser.getUserId(), 0, cartPost.getAccountId());

		CommerceOrder commerceOrder = _addCommerceOrder(
			commerceContext, commerceChannel.getGroupId(),
			contextUser.getUserId());

		commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceChannel.getSiteGroupId(),
			contextUser.getUserId(), commerceOrder.getCommerceOrderId(),
			cartPost.getAccountId());

		_upsertNestedResources(
			cartPost, commerceOrder, commerceContext,
			commerceChannel.getGroupId());

		return _toCart(commerceOrder, commerceChannel.getSiteGroupId());
	}

	@Override
	public Cart postChannelCartCouponCode(
			@NotNull Long channelId, @NotNull Long cartId,
			CouponCode couponCode)
		throws Exception {

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			cartId);

		CommerceContext commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceChannel.getSiteGroupId(),
			contextUser.getUserId(), commerceOrder.getCommerceOrderId(),
			commerceOrder.getCommerceAccountId());

		return _toCart(
			_commerceOrderService.applyCouponCode(
				cartId, couponCode.getCode(), commerceContext),
			commerceChannel.getSiteGroupId());
	}

	private CommerceOrder _addCommerceOrder(
			CommerceContext commerceContext, long commerceChannelGroupId,
			long userId)
		throws PortalException {

		CommerceOrder commerceOrder = null;

		long commerceCurrencyId = 0;

		CommerceCurrency commerceCurrency =
			commerceContext.getCommerceCurrency();

		if (commerceCurrency != null) {
			commerceCurrencyId = commerceCurrency.getCommerceCurrencyId();
		}

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		if (commerceAccount != null) {
			commerceOrder = _commerceOrderService.addCommerceOrder(
				userId, commerceChannelGroupId,
				commerceAccount.getCommerceAccountId(), commerceCurrencyId);
		}

		return commerceOrder;
	}

	private Cart _toCart(CommerceOrder commerceOrder, long channelSiteGroupId)
		throws Exception {

		return _cartDTOConverter.toDTO(
			new CartDTOConverterContext(
				contextAcceptLanguage.getPreferredLocale(),
				commerceOrder.getCommerceOrderId(), channelSiteGroupId));
	}

	private List<Cart> _toCarts(
			List<CommerceOrder> commerceOrders, long channelSiteGroupId)
		throws Exception {

		List<Cart> carts = new ArrayList<>();

		for (CommerceOrder commerceOrder : commerceOrders) {
			carts.add(_toCart(commerceOrder, channelSiteGroupId));
		}

		return carts;
	}

	private CommerceOrder _updateOrder(
			CommerceOrder commerceOrder, CartPost cartPost,
			CommerceChannel commerceChannel, CommerceContext commerceContext)
		throws Exception {

		long commerceShippingMethodId =
			commerceOrder.getCommerceShippingMethodId();

		CommerceShippingMethod commerceShippingMethod =
			_commerceShippingMethodService.fetchCommerceShippingMethod(
				commerceChannel.getSiteGroupId(), cartPost.getShippingMethod());

		if (commerceShippingMethod != null) {
			commerceShippingMethodId =
				commerceShippingMethod.getCommerceShippingMethodId();
		}

		commerceOrder = _commerceOrderService.updateCommerceOrder(
			commerceOrder.getCommerceOrderId(),
			GetterUtil.get(
				cartPost.getBillingAddressId(),
				commerceOrder.getBillingAddressId()),
			GetterUtil.get(
				cartPost.getShippingAddressId(),
				commerceOrder.getShippingAddressId()),
			GetterUtil.get(
				cartPost.getPaymentMethod(),
				commerceOrder.getCommercePaymentMethodKey()),
			commerceShippingMethodId,
			GetterUtil.get(
				cartPost.getShippingOption(),
				commerceOrder.getShippingOptionName()),
			commerceOrder.getPurchaseOrderNumber(), commerceOrder.getSubtotal(),
			commerceOrder.getShippingAmount(), commerceOrder.getTotal(),
			commerceOrder.getAdvanceStatus(), commerceContext);

		// Expando

		Map<String, ?> customFields = cartPost.getCustomFields();

		if ((customFields != null) && !customFields.isEmpty()) {
			ExpandoUtil.updateExpando(
				contextCompany.getCompanyId(), CommerceOrder.class,
				commerceOrder.getPrimaryKey(), customFields);
		}

		// Update nested resources

		_upsertNestedResources(
			cartPost, commerceOrder, commerceContext,
			commerceChannel.getGroupId());

		return commerceOrder;
	}

	private void _upsertCommerceOrderItem(
			CPInstanceService cpInstanceService,
			CommerceOrderItemService commerceOrderItemService,
			CartItemPost cartItemPost, CommerceOrder commerceOrder,
			CommerceContext commerceContext, ServiceContext serviceContext)
		throws Exception {

		CPInstance cpInstance = null;

		if (cartItemPost.getSkuId() != null) {
			cpInstance = cpInstanceService.getCPInstance(
				cartItemPost.getSkuId());
		}

		commerceOrderItemService.upsertCommerceOrderItem(
			commerceOrder.getCommerceOrderId(), cpInstance.getCPInstanceId(),
			GetterUtil.get(cartItemPost.getQuantity(), 0), 0,
			cpInstance.getJson(), commerceContext, serviceContext);
	}

	private CommerceOrder _upsertNestedResources(
			CartPost cartPost, CommerceOrder commerceOrder,
			CommerceContext commerceContex, long commerceChannelGroupId)
		throws Exception {

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			commerceChannelGroupId);

		// Order items

		CartItemPost[] orderItems = cartPost.getCartItemPosts();

		if (orderItems != null) {
			_commerceOrderItemService.deleteCommerceOrderItems(
				commerceOrder.getCommerceOrderId());

			for (CartItemPost cartItemPost : orderItems) {
				_upsertCommerceOrderItem(
					_cpInstanceService, _commerceOrderItemService, cartItemPost,
					commerceOrder, commerceContex, serviceContext);
			}
		}

		// Billing Address

		BillingAddress billingAddress = cartPost.getBillingAddress();

		if (billingAddress != null) {
			commerceOrder = BillingAddressUtil.upsertBillingAddress(
				_commerceAddressService, _commerceOrderService, commerceOrder,
				billingAddress, serviceContext);
		}

		// Shipping Address

		ShippingAddress shippingAddress = cartPost.getShippingAddress();

		if (shippingAddress != null) {
			commerceOrder = ShippingAddressUtil.upsertShippingAddress(
				_commerceAddressService, _commerceOrderService, commerceOrder,
				shippingAddress, serviceContext);
		}

		return commerceOrder;
	}

	@Reference
	private CartDTOConverter _cartDTOConverter;

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderItemService _commerceOrderItemService;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private CommerceShippingMethodService _commerceShippingMethodService;

	@Reference
	private CPInstanceService _cpInstanceService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

}