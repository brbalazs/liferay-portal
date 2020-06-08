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

package com.liferay.commerce.pricing.web.internal.display.context;

import com.liferay.commerce.pricing.constants.CommercePricingClassActionKeys;
import com.liferay.commerce.pricing.model.CommercePricingClass;
import com.liferay.commerce.pricing.service.CommercePricingClassService;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Riccardo Alberti
 */
public class CommercePricingClassDiscountDisplayContext {

	public CommercePricingClassDiscountDisplayContext(
		HttpServletRequest httpServletRequest,
		CommercePricingClassService commercePricingClassService) {

		_commercePricingClassService = commercePricingClassService;
		_cpRequestHelper = new CPRequestHelper(httpServletRequest);
	}

	public CommercePricingClass getCommercePricingClass()
		throws PortalException {

		long commercePricingClassId = ParamUtil.getLong(
			_cpRequestHelper.getRequest(), "commercePricingClassId");

		if (commercePricingClassId == 0) {
			return null;
		}

		return _commercePricingClassService.fetchCommercePricingClass(
			commercePricingClassId);
	}

	public boolean hasPermission() {
		return PortalPermissionUtil.contains(
			_cpRequestHelper.getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);
	}

	private final CommercePricingClassService _commercePricingClassService;
	private final CPRequestHelper _cpRequestHelper;

}