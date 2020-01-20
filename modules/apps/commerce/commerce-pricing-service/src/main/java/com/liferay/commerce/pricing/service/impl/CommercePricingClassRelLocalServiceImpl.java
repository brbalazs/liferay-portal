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

package com.liferay.commerce.pricing.service.impl;

import com.liferay.commerce.pricing.model.CommercePricingClassRel;
import com.liferay.commerce.pricing.service.base.CommercePricingClassRelLocalServiceBaseImpl;
import com.liferay.commerce.pricing.util.comparator.CommercePricingClassRelCreateDateComparator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * @author Riccardo Alberti
 * @see CommercePricingClassRelLocalServiceBaseImpl
 */
public class CommercePricingClassRelLocalServiceImpl
	extends CommercePricingClassRelLocalServiceBaseImpl {

	@Override
	public CommercePricingClassRel addCommercePricingClassRel(
			long commercePricingClassId, String className, long classPK,
			ServiceContext serviceContext)
		throws PortalException {

		// Commerce pricing class rel

		User user = userLocalService.getUser(serviceContext.getUserId());

		long commercePricingClassRelId = counterLocalService.increment();

		CommercePricingClassRel commercePricingClassRel =
			commercePricingClassRelPersistence.create(
				commercePricingClassRelId);

		commercePricingClassRel.setCompanyId(user.getCompanyId());
		commercePricingClassRel.setUserId(user.getUserId());
		commercePricingClassRel.setUserName(user.getFullName());
		commercePricingClassRel.setCommercePricingClassId(
			commercePricingClassId);
		commercePricingClassRel.setClassName(className);
		commercePricingClassRel.setClassPK(classPK);

		return commercePricingClassRelPersistence.update(
			commercePricingClassRel);
	}

	@Override
	public CommercePricingClassRel deleteCommercePricingClassRel(
			CommercePricingClassRel commercePricingClassRel)
		throws PortalException {

		return commercePricingClassRelPersistence.remove(
			commercePricingClassRel);
	}

	@Override
	public CommercePricingClassRel deleteCommercePricingClassRel(
			long commercePricingClassRelId)
		throws PortalException {

		CommercePricingClassRel commercePricingClassRel =
			commercePricingClassRelPersistence.findByPrimaryKey(
				commercePricingClassRelId);

		return commercePricingClassRelLocalService.
			deleteCommercePricingClassRel(commercePricingClassRel);
	}

	@Override
	public void deleteCommercePricingClassRels(long commercePricingClassId)
		throws PortalException {

		List<CommercePricingClassRel> commercePricingClassRels =
			commercePricingClassRelPersistence.findByCommercePricingClassId(
				commercePricingClassId);

		for (CommercePricingClassRel commercePricingClassRel :
				commercePricingClassRels) {

			commercePricingClassRelLocalService.deleteCommercePricingClassRel(
				commercePricingClassRel);
		}
	}

	@Override
	public void deleteCommercePricingClassRels(String className, long classPK)
		throws PortalException {

		List<CommercePricingClassRel> commercePricingClassRels =
			commercePricingClassRelPersistence.findByCN_CPK(
				classNameLocalService.getClassNameId(className), classPK);

		for (CommercePricingClassRel commercePricingClassRel :
				commercePricingClassRels) {

			commercePricingClassRelLocalService.deleteCommercePricingClassRel(
				commercePricingClassRel);
		}
	}

	@Override
	public CommercePricingClassRel fetchCommercePricingClassRel(
		String className, long classPK) {

		return commercePricingClassRelPersistence.fetchByCN_CPK_First(
			classNameLocalService.getClassNameId(className), classPK,
			new CommercePricingClassRelCreateDateComparator());
	}

	@Override
	public long[] getClassPKs(long commercePricingClassId, String className) {
		return ListUtil.toLongArray(
			commercePricingClassRelPersistence.findByCPC_CN(
				commercePricingClassId,
				classNameLocalService.getClassNameId(className)),
			CommercePricingClassRel::getClassPK);
	}

	@Override
	public List<CommercePricingClassRel> getCommercePricingClassRels(
		long commercePricingClassId, String className) {

		return commercePricingClassRelPersistence.findByCPC_CN(
			commercePricingClassId,
			classNameLocalService.getClassNameId(className));
	}

	@Override
	public List<CommercePricingClassRel> getCommercePricingClassRels(
		long commercePricingClassId, String className, int start, int end,
		OrderByComparator<CommercePricingClassRel> orderByComparator) {

		return commercePricingClassRelPersistence.findByCPC_CN(
			commercePricingClassId,
			classNameLocalService.getClassNameId(className), start, end,
			orderByComparator);
	}

	@Override
	public int getCommercePricingClassRelsCount(
		long commercePricingClassId, String className) {

		return commercePricingClassRelPersistence.countByCPC_CN(
			commercePricingClassId,
			classNameLocalService.getClassNameId(className));
	}

}