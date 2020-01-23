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

import com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel;
import com.liferay.commerce.price.list.service.base.CommercePriceListPriceModifierRelLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.List;

/**
 * @author Riccardo Alberti
 * @see CommercePriceListPriceModifierRelLocalServiceBaseImpl
 */
public class CommercePriceListPriceModifierRelLocalServiceImpl
	extends CommercePriceListPriceModifierRelLocalServiceBaseImpl {

	@Override
	public CommercePriceListPriceModifierRel
			addCommercePriceListPriceModifierRel(
				long commercePriceListId, long commercePriceModifierId,
				int order, ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(serviceContext.getUserId());

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			commercePriceListPriceModifierRelPersistence.create(
				counterLocalService.increment());

		commercePriceListPriceModifierRel.setCompanyId(user.getCompanyId());
		commercePriceListPriceModifierRel.setUserId(user.getUserId());
		commercePriceListPriceModifierRel.setUserName(user.getFullName());
		commercePriceListPriceModifierRel.setCommercePriceModifierId(
			commercePriceModifierId);
		commercePriceListPriceModifierRel.setCommercePriceListId(
			commercePriceListId);
		commercePriceListPriceModifierRel.setOrder(order);
		commercePriceListPriceModifierRel.setExpandoBridgeAttributes(
			serviceContext);

		// Cache

		commercePriceListLocalService.cleanPriceListCache(
			serviceContext.getCompanyId());

		return commercePriceListPriceModifierRelPersistence.update(
			commercePriceListPriceModifierRel);
	}

	@Override
	public CommercePriceListPriceModifierRel
			deleteCommercePriceListPriceModifierRel(
				CommercePriceListPriceModifierRel
					commercePriceListPriceModifierRel)
		throws PortalException {

		commercePriceListPriceModifierRelPersistence.remove(
			commercePriceListPriceModifierRel);

		// Cache

		commercePriceListLocalService.cleanPriceListCache(
			commercePriceListPriceModifierRel.getCompanyId());

		return commercePriceListPriceModifierRel;
	}

	@Override
	public CommercePriceListPriceModifierRel
			deleteCommercePriceListPriceModifierRel(
				long commercePriceListPriceModifierRelId)
		throws PortalException {

		CommercePriceListPriceModifierRel commercePriceListPriceModifierRel =
			commercePriceListPriceModifierRelPersistence.findByPrimaryKey(
				commercePriceListPriceModifierRelId);

		return commercePriceListPriceModifierRelLocalService.
			deleteCommercePriceListPriceModifierRel(
				commercePriceListPriceModifierRel);
	}

	@Override
	public void deleteCommercePriceListPriceModifierRels(
		long commercePriceListId) {

		commercePriceListPriceModifierRelPersistence.
			removeByCommercePriceListId(commercePriceListId);
	}

	@Override
	public CommercePriceListPriceModifierRel
		fetchCommercePriceListPriceModifierRel(
			long commercePriceModifierId, long commercePriceListId) {

		return commercePriceListPriceModifierRelPersistence.fetchByC_C(
			commercePriceModifierId, commercePriceListId);
	}

	@Override
	public List<CommercePriceListPriceModifierRel>
		getCommercePriceListPriceModifierRels(long commercePriceListId) {

		return commercePriceListPriceModifierRelPersistence.
			findByCommercePriceListId(commercePriceListId);
	}

}