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

import com.liferay.commerce.price.list.model.CommercePriceListRel;
import com.liferay.commerce.price.list.service.base.CommercePriceListRelLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;

import java.util.List;

/**
 * @author Riccardo Alberti
 * @see CommercePriceListRelLocalServiceBaseImpl
 */
public class CommercePriceListRelLocalServiceImpl
	extends CommercePriceListRelLocalServiceBaseImpl {

	@Override
	public CommercePriceListRel addCommercePriceListRel(
			long commercePriceListId, String className, long classPK, int order,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(serviceContext.getUserId());

		CommercePriceListRel commercePriceListRel =
			commercePriceListRelPersistence.create(
				counterLocalService.increment());

		commercePriceListRel.setCompanyId(user.getCompanyId());
		commercePriceListRel.setUserId(user.getUserId());
		commercePriceListRel.setUserName(user.getFullName());
		commercePriceListRel.setCommercePriceListId(commercePriceListId);
		commercePriceListRel.setClassName(className);
		commercePriceListRel.setClassPK(classPK);
		commercePriceListRel.setOrder(order);
		commercePriceListRel.setExpandoBridgeAttributes(serviceContext);

		// Cache

		commercePriceListLocalService.cleanPriceListCache(
			serviceContext.getCompanyId());

		return commercePriceListRelPersistence.update(commercePriceListRel);
	}

	@Override
	public CommercePriceListRel deleteCommercePriceListRel(
			CommercePriceListRel commercePriceListRel)
		throws PortalException {

		commercePriceListRelPersistence.remove(commercePriceListRel);

		// Cache

		commercePriceListLocalService.cleanPriceListCache(
			commercePriceListRel.getCompanyId());

		return commercePriceListRel;
	}

	@Override
	public CommercePriceListRel deleteCommercePriceListRel(
			long commercePriceListRelId)
		throws PortalException {

		CommercePriceListRel commercePriceListRel =
			commercePriceListRelPersistence.findByPrimaryKey(
				commercePriceListRelId);

		return commercePriceListRelLocalService.deleteCommercePriceListRel(
			commercePriceListRel);
	}

	@Override
	public void deleteCommercePriceListRels(long commercePriceListId) {
		commercePriceListRelPersistence.removeByCommercePriceListId(
			commercePriceListId);
	}

	@Override
	public CommercePriceListRel fetchCommercePriceListRel(
		long commercePriceListId, String className, long classPK) {

		long classNameId = classNameLocalService.getClassNameId(className);

		return commercePriceListRelPersistence.fetchByC_C_C(
			commercePriceListId, classNameId, classPK);
	}

	@Override
	public List<CommercePriceListRel> getCommercePriceListRels(
		long commercePriceListId) {

		return commercePriceListRelPersistence.findByCommercePriceListId(
			commercePriceListId);
	}

	@Override
	public List<CommercePriceListRel> getCommercePriceListRels(
		String className, long classPK) {

		long classNameId = classNameLocalService.getClassNameId(className);

		return commercePriceListRelPersistence.findByC_C(classNameId, classPK);
	}

}