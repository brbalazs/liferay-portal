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

import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.FilterFactory;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceOrderDataSetConstants.COMMERCE_DATA_SET_KEY_ORDER_ITEMS,
	service = FilterFactory.class
)
public class OrderItemFilterFactoryImpl implements FilterFactory {

	@Override
	public Filter create(HttpServletRequest httpServletRequest) {
		OrderItemFilterImpl orderItemFilterImpl = new OrderItemFilterImpl();

		boolean advancedSearch = ParamUtil.getBoolean(
			httpServletRequest, "advancedSearch");
		boolean andOperator = ParamUtil.getBoolean(
			httpServletRequest, "andOperator", true);
		String keywords = ParamUtil.getString(httpServletRequest, "keywords");
		String name = ParamUtil.getString(httpServletRequest, "name");
		String sku = ParamUtil.getString(httpServletRequest, "sku");

		orderItemFilterImpl.setAdvancedSearch(advancedSearch);
		orderItemFilterImpl.setAndOperator(andOperator);
		orderItemFilterImpl.setKeywords(keywords);
		orderItemFilterImpl.setName(name);
		orderItemFilterImpl.setSku(sku);

		return orderItemFilterImpl;
	}

}