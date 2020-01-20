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

import com.liferay.commerce.pricing.constants.CommercePricingClassActionKeys;
import com.liferay.commerce.pricing.model.CommercePricingClass;
import com.liferay.commerce.pricing.service.base.CommercePricingClassServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;

/**
 * @author Riccardo Alberti
 * @see CommercePricingClassServiceBaseImpl
 */
public class CommercePricingClassServiceImpl
	extends CommercePricingClassServiceBaseImpl {

	@Override
	public CommercePricingClass addCommercePricingClass(
			long userId, long groupId, String name, String title,
			String description, ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.ADD_COMMERCE_PRICING_CLASS);

		return commercePricingClassLocalService.addCommercePricingClass(
			userId, groupId, name, title, description, serviceContext);
	}

	@Override
	public CommercePricingClass deleteCommercePricingClass(
			long commercePricingClassId)
		throws PortalException {

		_commercePricingClassResourcePermission.check(
			getPermissionChecker(), commercePricingClassId, ActionKeys.DELETE);

		return commercePricingClassLocalService.deleteCommercePricingClass(
			commercePricingClassId);
	}

	@Override
	public CommercePricingClass fetchByExternalReferenceCode(
			long companyId, String externalReferenceCode)
		throws PortalException {

		CommercePricingClass commercePricingClass =
			commercePricingClassLocalService.fetchByExternalReferenceCode(
				companyId, externalReferenceCode);

		if (commercePricingClass != null) {
			_commercePricingClassResourcePermission.check(
				getPermissionChecker(), commercePricingClass, ActionKeys.VIEW);
		}

		return commercePricingClass;
	}

	@Override
	public CommercePricingClass fetchCommercePricingClass(
			long commercePricingClassId)
		throws PortalException {

		CommercePricingClass commercePricingClass =
			commercePricingClassLocalService.fetchCommercePricingClass(
				commercePricingClassId);

		if (commercePricingClass != null) {
			_commercePricingClassResourcePermission.check(
				getPermissionChecker(), commercePricingClass, ActionKeys.VIEW);
		}

		return commercePricingClass;
	}

	@Override
	public CommercePricingClass getCommercePricingClass(
			long commercePricingClassId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASS);

		return commercePricingClassLocalService.getCommercePricingClass(
			commercePricingClassId);
	}

	@Override
	public CommercePricingClass updateCommercePricingClass(
			long commercePricingClassId, long userId, long groupId, String name,
			String title, String description, ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.ADD_COMMERCE_PRICING_CLASS);

		return commercePricingClassLocalService.updateCommercePricingClass(
			commercePricingClassId, userId, groupId, name, title, description,
			serviceContext);
	}

	@Override
	public CommercePricingClass upsertCommercePricingClass(
			long commercePricingClassId, long userId, long groupId, String name,
			String title, String description, String externalReferenceCode,
			ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.ADD_COMMERCE_PRICING_CLASS);

		return commercePricingClassLocalService.upsertCommercePricingClass(
			commercePricingClassId, userId, groupId, name, title, description,
			externalReferenceCode, serviceContext);
	}

	private static volatile ModelResourcePermission<CommercePricingClass>
		_commercePricingClassResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				CommercePricingClassServiceImpl.class,
				"_commercePricingClassResourcePermission",
				CommercePricingClass.class);

}