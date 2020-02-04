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

package com.liferay.commerce.order.web.internal.frontend;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.frontend.model.StatusField;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.web.internal.frontend.util.CommerceOrderDataSetDataProviderUtil;
import com.liferay.commerce.order.web.internal.model.Order;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.DateFormat;
import java.text.Format;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 */
public abstract class BaseCommerceOrderDataSetDataProvider
	implements CommerceDataSetDataProvider<Order> {

	@Override
	public int countItems(HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		OrderFilterImpl orderFilterImpl = (OrderFilterImpl)filter;

		Portal portal = getPortal();

		return CommerceOrderDataSetDataProviderUtil.getCommerceOrdersCount(
			getCommerceOrderLocalService(),
			portal.getCompanyId(httpServletRequest), getActiveTab(),
			orderFilterImpl.getOrderStatus(),
			orderFilterImpl.getAdvanceStatus(), orderFilterImpl.getKeywords());
	}

	public abstract String getActiveTab();

	public abstract CommerceOrderLocalService getCommerceOrderLocalService();

	@Override
	public List<Order> getItems(
			HttpServletRequest httpServletRequest, Filter filter,
			Pagination pagination, Sort sort)
		throws PortalException {

		List<Order> orders = new ArrayList<>();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Format dateTimeFormat = FastDateFormatFactoryUtil.getDateTime(
			DateFormat.MEDIUM, DateFormat.MEDIUM, themeDisplay.getLocale(),
			themeDisplay.getTimeZone());

		OrderFilterImpl orderFilterImpl = (OrderFilterImpl)filter;

		Portal portal = getPortal();

		List<CommerceOrder> commerceOrders =
			CommerceOrderDataSetDataProviderUtil.getCommerceOrders(
				getCommerceOrderLocalService(),
				portal.getCompanyId(httpServletRequest), getActiveTab(),
				orderFilterImpl.getOrderStatus(),
				orderFilterImpl.getAdvanceStatus(),
				orderFilterImpl.getKeywords(), pagination.getStartPosition(),
				pagination.getEndPosition(), sort);

		for (CommerceOrder commerceOrder : commerceOrders) {
			CommerceMoney totalMoney = commerceOrder.getTotalMoney();

			orders.add(
				new Order(
					commerceOrder.getCommerceOrderId(),
					CommerceOrderDataSetDataProviderUtil.
						getCommerceOrderDateTime(
							commerceOrder, dateTimeFormat,
							themeDisplay.getLocale()),
					LanguageUtil.get(
						httpServletRequest,
						CommerceOrderConstants.getOrderStatusLabel(
							commerceOrder.getOrderStatus())),
					LanguageUtil.get(
						httpServletRequest,
						CommerceOrderConstants.getPaymentStatusLabel(
							commerceOrder.getPaymentStatus())),
					new StatusField(
						CommerceOrderConstants.getOrderStatusLabelStyle(
							commerceOrder.getOrderStatus()),
						LanguageUtil.get(
							httpServletRequest,
							CommerceOrderConstants.getOrderStatusLabel(
								commerceOrder.getOrderStatus()))),
					commerceOrder.getCommerceAccountName(),
					String.valueOf(commerceOrder.getCommerceAccountId()),
					totalMoney.format(themeDisplay.getLocale())));
		}

		return orders;
	}

	public abstract Portal getPortal();

}