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
import com.liferay.commerce.exception.NoSuchOrderException;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.BillingAddress;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Order;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.OrderItem;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.ShippingAddress;
import com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0.CartDTOConverter;
import com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0.CartDTOConverterContext;
import com.liferay.headless.commerce.delivery.cart.internal.v1_0.BillingAddressUtil;
import com.liferay.headless.commerce.delivery.cart.internal.v1_0.OrderItemUtil;
import com.liferay.headless.commerce.delivery.cart.internal.v1_0.ShippingAddressUtil;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.CartResource;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.vulcan.pagination.Page;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

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
	public Cart getChannelCart(@NotNull Long channelId, @NotNull Long cartId)
		throws Exception {

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			cartId);

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		if (commerceChannel.getGroupId() != commerceOrder.getGroupId()) {
			throw new NoSuchOrderException("Can't find order on channel");
		}

		return _toCart(commerceOrder, commerceChannel.getSiteGroupId());
	}

	@Override
	public Page<Cart> getChannelCartsPage(@NotNull Long channelId)
		throws Exception {

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		List<CommerceOrder> commerceOrders =
			_commerceOrderService.getUserPendingCommerceOrders(
				contextCompany.getCompanyId(), commerceChannel.getGroupId(),
				null, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		return Page.of(
			_toCarts(commerceOrders, commerceChannel.getSiteGroupId()));
	}

	@Override
	public Cart postChannelCart(@NotNull Long channelId, Order order)
		throws Exception {

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		CommerceContext commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceChannel.getSiteGroupId(),
			contextUser.getUserId(), 0, order.getAccountId());

		CommerceOrder commerceOrder = _addCommerceOrder(
			commerceContext, commerceChannel.getGroupId(),
			contextUser.getUserId());

		commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceChannel.getSiteGroupId(),
			contextUser.getUserId(), commerceOrder.getCommerceOrderId(),
			order.getAccountId());

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			commerceChannel.getGroupId());

		_updateNestedResources(
			order, commerceOrder, commerceContext, serviceContext);

		return _toCart(commerceOrder, commerceChannel.getSiteGroupId());
	}

	@Override
	public Cart postChannelCartCartItem(
			@NotNull Long channelId, @NotNull Long cartId, OrderItem orderItem)
		throws Exception {

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			commerceChannel.getGroupId());

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			cartId);

		CommerceContext commerceContext = _commerceContextFactory.create(
			contextCompany.getCompanyId(), commerceChannel.getSiteGroupId(),
			contextUser.getUserId(), commerceOrder.getCommerceOrderId(),
			commerceOrder.getCommerceAccountId());

		CPInstance cpInstance = _cpInstanceService.getCPInstance(
			orderItem.getProductId());

		_commerceOrderItemService.upsertCommerceOrderItem(
			commerceOrder.getCommerceOrderId(), orderItem.getProductId(),
			orderItem.getQuantity(), 0, cpInstance.getJson(), commerceContext,
			serviceContext);

		return _toCart(commerceOrder, commerceChannel.getSiteGroupId());
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

	private CommerceOrder _updateNestedResources(
			Order order, CommerceOrder commerceOrder,
			CommerceContext commerceContext, ServiceContext serviceContext)
		throws Exception {

		// Order items

		OrderItem[] orderItems = order.getOrderItems();

		if (orderItems != null) {
			_commerceOrderItemService.deleteCommerceOrderItems(
				commerceOrder.getCommerceOrderId());

			for (OrderItem orderItem : orderItems) {
				OrderItemUtil.upsertCommerceOrderItem(
					_cpInstanceService, _commerceOrderItemService, orderItem,
					commerceOrder,
					_commerceContextFactory.create(
						contextCompany.getCompanyId(),
						commerceOrder.getGroupId(), contextUser.getUserId(),
						commerceOrder.getCommerceOrderId(),
						commerceOrder.getCommerceAccountId()),
					serviceContext);
			}
		}

		// Billing Address

		BillingAddress billingAddress = order.getBillingAddress();

		if (billingAddress != null) {
			commerceOrder = BillingAddressUtil.upsertBillingAddress(
				_commerceAddressService, _commerceOrderService, commerceOrder,
				billingAddress, serviceContext);
		}

		// Shipping Address

		ShippingAddress shippingAddress = order.getShippingAddress();

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
	private CPInstanceService _cpInstanceService;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

}