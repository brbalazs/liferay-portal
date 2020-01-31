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

package com.liferay.commerce.subscription.web.internal.frontend;

import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.FilterFactory;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceSubscriptionDataSetConstants.COMMERCE_DATA_SET_KEY_SUBSCRIPTION_ENTRIES,
	service = FilterFactory.class
)
public class SubscriptionEntryFilterFactoryImpl implements FilterFactory {

	@Override
	public Filter create(HttpServletRequest httpServletRequest) {
		SubscriptionEntryFilterImpl subscriptionEntryFilterImpl =
			new SubscriptionEntryFilterImpl();

		boolean advancedSearch = ParamUtil.getBoolean(
			httpServletRequest, "advancedSearch");
		String keywords = ParamUtil.getString(httpServletRequest, "keywords");
		long paymentSubscriptionRemainingCycles = ParamUtil.getLong(
			httpServletRequest, "paymentSubscriptionRemainingCycles");
		int paymentSubscriptionStatus = ParamUtil.getInteger(
			httpServletRequest, "paymentSubscriptionStatus");

		subscriptionEntryFilterImpl.setAdvancedSearch(advancedSearch);
		subscriptionEntryFilterImpl.setKeywords(keywords);
		subscriptionEntryFilterImpl.setPaymentSubscriptionRemainingCycles(
			paymentSubscriptionRemainingCycles);
		subscriptionEntryFilterImpl.setPaymentSubscriptionStatus(
			paymentSubscriptionStatus);

		return subscriptionEntryFilterImpl;
	}

}