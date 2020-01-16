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

import com.liferay.commerce.exception.NoSuchOrderException;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.headless.commerce.core.dto.v1_0.converter.DefaultDTOConverterContext;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.Cart;
import com.liferay.headless.commerce.delivery.cart.dto.v1_0.OrderItem;
import com.liferay.headless.commerce.delivery.cart.internal.dto.v1_0.OrderItemDTOConverter;
import com.liferay.headless.commerce.delivery.cart.resource.v1_0.OrderItemResource;
import com.liferay.portal.vulcan.fields.NestedField;
import com.liferay.portal.vulcan.fields.NestedFieldId;
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
	properties = "OSGI-INF/liferay/rest/v1_0/order-item.properties",
	scope = ServiceScope.PROTOTYPE, service = OrderItemResource.class
)
public class OrderItemResourceImpl extends BaseOrderItemResourceImpl {

	@Override
	public OrderItem getChannelCartOrderItem(
			@NotNull Long channelId, @NotNull Long cartId,
			@NotNull Long orderItemId)
		throws Exception {

		return super.getChannelCartOrderItem(channelId, cartId, orderItemId);
	}

	@NestedField(parentClass = Cart.class, value = "orderItems")
	@Override
	public Page<OrderItem> getChannelCartOrderItemsPage(
			@NotNull Long channelId, @NestedFieldId("id") @NotNull Long cartId)
		throws Exception {

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			cartId);

		CommerceChannel commerceChannel =
			_commerceChannelService.getCommerceChannel(channelId);

		if (commerceChannel.getGroupId() != commerceOrder.getGroupId()) {
			throw new NoSuchOrderException("Can't find order on channel");
		}

		return Page.of(_toOrderItems(commerceOrder.getCommerceOrderItems()));
	}

	private OrderItem _toOrderItem(CommerceOrderItem commerceOrderItem)
		throws Exception {

		return _orderItemDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.getPreferredLocale(),
				commerceOrderItem.getCommerceOrderItemId()));
	}

	private List<OrderItem> _toOrderItems(
			List<CommerceOrderItem> commerceOrderItems)
		throws Exception {

		List<OrderItem> orderItems = new ArrayList<>();

		for (CommerceOrderItem commerceOrderItem : commerceOrderItems) {
			orderItems.add(_toOrderItem(commerceOrderItem));
		}

		return orderItems;
	}

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private OrderItemDTOConverter _orderItemDTOConverter;

}