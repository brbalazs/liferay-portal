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
import com.liferay.commerce.pricing.service.base.CommercePricingClassRelServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * @author Riccardo Alberti
 * @see CommercePricingClassRelServiceBaseImpl
 */
public class CommercePricingClassRelServiceImpl
	extends CommercePricingClassRelServiceBaseImpl {

	@Override
	public CommercePricingClassRel addCommerceDiscountRel(
			long commercePricingClassId, String className, long classPK,
			ServiceContext serviceContext)
		throws PortalException {

		_commercePricingClassRelResourcePermission.check(
			getPermissionChecker(), commercePricingClassId, ActionKeys.UPDATE);

		return commercePricingClassRelLocalService.addCommercePricingClassRel(
			commercePricingClassId, className, classPK, serviceContext);
	}

	@Override
	public void deleteCommerceDiscountRel(long commercePricingClassId)
		throws PortalException {

		CommercePricingClassRel commercePricingClassRel =
			commercePricingClassRelLocalService.getCommercePricingClassRel(
				commercePricingClassId);

		_commercePricingClassRelResourcePermission.check(
			getPermissionChecker(),
			commercePricingClassRel.getCommercePricingClassId(),
			ActionKeys.UPDATE);

		commercePricingClassRelLocalService.deleteCommercePricingClassRel(
			commercePricingClassRel);
	}

	@Override
	public CommercePricingClassRel fetchCommerceDiscountRel(
			String className, long classPK)
		throws PortalException {

		CommercePricingClassRel commercePricingClassRel =
			commercePricingClassRelLocalService.fetchCommercePricingClassRel(
				className, classPK);

		if (commercePricingClassRel != null) {
			_commercePricingClassRelResourcePermission.check(
				getPermissionChecker(),
				commercePricingClassRel.getCommercePricingClassId(),
				ActionKeys.UPDATE);
		}

		return commercePricingClassRel;
	}

	@Override
	public long[] getClassPKs(long commercePricingClassId, String className)
		throws PortalException {

		_commercePricingClassRelResourcePermission.check(
			getPermissionChecker(), commercePricingClassId, ActionKeys.UPDATE);

		return commercePricingClassRelLocalService.getClassPKs(
			commercePricingClassId, className);
	}

	@Override
	public CommercePricingClassRel getCommerceDiscountRel(
			long commercePricingClassId)
		throws PortalException {

		CommercePricingClassRel commercePricingClassRel =
			commercePricingClassRelLocalService.getCommercePricingClassRel(
				commercePricingClassId);

		_commercePricingClassRelResourcePermission.check(
			getPermissionChecker(),
			commercePricingClassRel.getCommercePricingClassId(),
			ActionKeys.UPDATE);

		return commercePricingClassRel;
	}

	@Override
	public List<CommercePricingClassRel> getCommerceDiscountRels(
			long commercePricingClassId, String className)
		throws PortalException {

		_commercePricingClassRelResourcePermission.check(
			getPermissionChecker(), commercePricingClassId, ActionKeys.UPDATE);

		return commercePricingClassRelLocalService.getCommercePricingClassRels(
			commercePricingClassId, className);
	}

	@Override
	public List<CommercePricingClassRel> getCommerceDiscountRels(
			long commercePricingClassId, String className, int start, int end,
			OrderByComparator<CommercePricingClassRel> orderByComparator)
		throws PortalException {

		_commercePricingClassRelResourcePermission.check(
			getPermissionChecker(), commercePricingClassId, ActionKeys.UPDATE);

		return commercePricingClassRelLocalService.getCommercePricingClassRels(
			commercePricingClassId, className, start, end, orderByComparator);
	}

	@Override
	public int getCommerceDiscountRelsCount(
			long commercePricingClassId, String className)
		throws PortalException {

		_commercePricingClassRelResourcePermission.check(
			getPermissionChecker(), commercePricingClassId, ActionKeys.UPDATE);

		return commercePricingClassRelLocalService.
			getCommercePricingClassRelsCount(commercePricingClassId, className);
	}

	private static volatile ModelResourcePermission<CommercePricingClassRel>
		_commercePricingClassRelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				CommercePricingClassRelServiceImpl.class,
				"_commercePricingClassRelResourcePermission",
				CommercePricingClassRel.class);

}