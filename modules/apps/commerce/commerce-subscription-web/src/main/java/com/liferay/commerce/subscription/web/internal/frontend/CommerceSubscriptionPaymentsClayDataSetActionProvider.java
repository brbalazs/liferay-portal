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

import com.liferay.commerce.frontend.clay.data.set.ClayDataSetAction;
import com.liferay.commerce.frontend.clay.data.set.ClayDataSetActionProvider;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Luca Pellizzon
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceSubscriptionDataSetConstants.COMMERCE_DATA_SET_KEY_SUBSCRIPTION_PAYMENTS,
	service = ClayDataSetActionProvider.class
)
public class CommerceSubscriptionPaymentsClayDataSetActionProvider
	implements ClayDataSetActionProvider {

	@Override
	public List<ClayDataSetAction> clayDataSetActions(
			HttpServletRequest httpServletRequest, long groupId, Object model)
		throws PortalException {

		// TODO Auto-generated method stub

		return null;
	}

}