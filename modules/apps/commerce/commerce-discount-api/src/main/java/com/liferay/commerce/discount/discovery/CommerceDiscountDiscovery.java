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

package com.liferay.commerce.discount.discovery;

import aQute.bnd.annotation.ProviderType;

import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.discount.CommerceDiscountLevel;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommercePriceValue;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import java.util.List;

/**
 * @author Riccardo Alberti
 */
@ProviderType
public interface CommerceDiscountDiscovery {

	public BigDecimal applyCommerceDiscounts(
			BigDecimal commercePrice,
			CommerceDiscountLevel[] commerceDiscountLevels)
		throws PortalException;

	public List<CommerceDiscount> getOrderCommerceDiscount(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId, String commerceDiscountTargetType)
		throws PortalException;

	public List<CommerceDiscount> getOrderCommerceDiscountByHierarchy(
			CommerceContext commerceContext, String commerceDiscountTargetType)
		throws PortalException;

	public List<CommerceDiscount> getOrderCommerceDiscountByHierarchy(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId, String commerceDiscountTargetType)
		throws PortalException;

	public CommerceDiscountValue getOrderShippingCommerceDiscountValue(
			CommerceOrder commerceOrder, BigDecimal shippingAmount,
			CommerceContext commerceContext)
		throws PortalException;

	public CommerceDiscountValue getOrderSubtotalCommerceDiscountValue(
			CommerceOrder commerceOrder, BigDecimal subtotalAmount,
			CommerceContext commerceContext)
		throws PortalException;

	public CommerceDiscountValue getOrderTotalCommerceDiscountValue(
			CommerceOrder commerceOrder, BigDecimal totalAmount,
			CommerceContext commerceContext)
		throws PortalException;

	public List<CommerceDiscount> getProductCommerceDiscount(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId, long cpDefinitionId)
		throws PortalException;

	public List<CommerceDiscount> getProductCommerceDiscountByHierarchy(
			CommerceContext commerceContext, long cpDefinitionId)
		throws PortalException;

	public List<CommerceDiscount> getProductCommerceDiscountByHierarchy(
			long commerceAccountId, long[] commerceAccountGroupIds,
			long commerceChannelId, long cpDefinitionId)
		throws PortalException;

	public CommerceDiscountLevel[] getProductCommerceDiscountLevels(
			long commercePriceListId, BigDecimal commercePrice, int quantity,
			CommerceContext commerceContext, long cpInstanceId)
		throws PortalException;

	public CommerceDiscountValue getProductCommerceDiscountValue(
			BigDecimal finalPrice, BigDecimal finalDiscountedPrice,
			List<CommercePriceValue> finalPriceValues,
			CommerceCurrency commerceCurrency)
		throws PortalException;

}