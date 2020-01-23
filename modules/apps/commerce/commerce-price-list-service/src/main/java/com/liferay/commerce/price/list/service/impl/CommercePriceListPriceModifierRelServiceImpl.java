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

package com.liferay.commerce.price.list.service.impl;

import com.liferay.commerce.price.list.constants.CommercePriceListActionKeys;
import com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel;
import com.liferay.commerce.price.list.service.base.CommercePriceListPriceModifierRelServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;

import java.util.List;

/**
 * @author Riccardo Alberti
 * @see CommercePriceListPriceModifierRelServiceBaseImpl
 */
public class CommercePriceListPriceModifierRelServiceImpl
	extends CommercePriceListPriceModifierRelServiceBaseImpl {

	@Override
	public CommercePriceListPriceModifierRel
			addCommercePriceListPriceModifierRel(
				long commercePriceListId, long commercePriceModifierId,
				int order, ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePriceListActionKeys.MANAGE_COMMERCE_PRICE_LISTS);

		return commercePriceListPriceModifierRelLocalService.
			addCommercePriceListPriceModifierRel(
				commercePriceListId, commercePriceModifierId, order,
				serviceContext);
	}

	@Override
	public void deleteCommercePriceListPriceModifierRel(
			long commercePriceListPriceModifierRelId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePriceListActionKeys.MANAGE_COMMERCE_PRICE_LISTS);

		commercePriceListPriceModifierRelLocalService.
			deleteCommercePriceListPriceModifierRel(
				commercePriceListPriceModifierRelId);
	}

	@Override
	public CommercePriceListPriceModifierRel
			fetchCommercePriceListPriceModifierRel(
				long commercePriceModifierId, long commercePriceListId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePriceListActionKeys.MANAGE_COMMERCE_PRICE_LISTS);

		return commercePriceListPriceModifierRelLocalService.
			fetchCommercePriceListPriceModifierRel(
				commercePriceModifierId, commercePriceListId);
	}

	@Override
	public List<CommercePriceListPriceModifierRel>
			getCommercePriceListPriceModifierRels(long commercePriceListId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePriceListActionKeys.MANAGE_COMMERCE_PRICE_LISTS);

		return commercePriceListPriceModifierRelLocalService.
			getCommercePriceListPriceModifierRels(commercePriceListId);
	}

}