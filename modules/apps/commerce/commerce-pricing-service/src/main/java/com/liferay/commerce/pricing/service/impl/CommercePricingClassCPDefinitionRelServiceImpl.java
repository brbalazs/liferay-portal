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
import com.liferay.commerce.pricing.model.CommercePricingClassCPDefinitionRel;
import com.liferay.commerce.pricing.service.base.CommercePricingClassCPDefinitionRelServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * @author Riccardo Alberti
 * @see CommercePricingClassCPDefinitionRelServiceBaseImpl
 */
public class CommercePricingClassCPDefinitionRelServiceImpl
	extends CommercePricingClassCPDefinitionRelServiceBaseImpl {

	@Override
	public CommercePricingClassCPDefinitionRel
			addCommercePricingClassCPDefinitionRel(
				long commercePricingClassId, long cpDefinitionId,
				ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			addCommercePricingClassCPDefinitionRel(
				commercePricingClassId, cpDefinitionId, serviceContext);
	}

	@Override
	public int countByCommercePricingClassId(
			long commercePricingClassId, String name, String languageId)
		throws PrincipalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			countByCommercePricingClassId(
				commercePricingClassId, name, languageId);
	}

	@Override
	public CommercePricingClassCPDefinitionRel
			deleteCommercePricingClassCPDefinitionRel(
				CommercePricingClassCPDefinitionRel
					commercePricingClassCPDefinitionRel)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			deleteCommercePricingClassCPDefinitionRel(
				commercePricingClassCPDefinitionRel);
	}

	@Override
	public CommercePricingClassCPDefinitionRel
			deleteCommercePricingClassCPDefinitionRel(
				long commercePricingClassCPDefinitionRelId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			deleteCommercePricingClassCPDefinitionRel(
				commercePricingClassCPDefinitionRelId);
	}

	@Override
	public CommercePricingClassCPDefinitionRel
			fetchCommercePricingClassCPDefinitionRel(
				long commercePricingClassId, long cpDefinitionId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			fetchCommercePricingClassCPDefinitionRel(
				commercePricingClassId, cpDefinitionId);
	}

	@Override
	public CommercePricingClassCPDefinitionRel
			getCommercePricingClassCPDefinitionRel(
				long commercePricingClassCPDefinitionRelId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			fetchCommercePricingClassCPDefinitionRel(
				commercePricingClassCPDefinitionRelId);
	}

	@Override
	public List<CommercePricingClassCPDefinitionRel>
			getCommercePricingClassCPDefinitionRelByClassId(
				long commercePricingClassId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			getCommercePricingClassCPDefinitionRels(commercePricingClassId);
	}

	@Override
	public List<CommercePricingClassCPDefinitionRel>
			getCommercePricingClassCPDefinitionRels(
				long commercePricingClassId, int start, int end,
				OrderByComparator<CommercePricingClassCPDefinitionRel>
					orderByComparator)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			getCommercePricingClassCPDefinitionRels(
				commercePricingClassId, start, end, orderByComparator);
	}

	@Override
	public int getCommercePricingClassCPDefinitionRelsCount(
			long commercePricingClassId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			getCommercePricingClassCPDefinitionRelsCount(
				commercePricingClassId);
	}

	@Override
	public long[] getCPDefinitionIds(long commercePricingClassId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			getCPDefinitionIds(commercePricingClassId);
	}

	@Override
	public List<CommercePricingClassCPDefinitionRel>
			searchByCommercePricingClassId(
				long commercePricingClassId, String name, String languageId,
				int start, int end)
		throws PrincipalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassCPDefinitionRelLocalService.
			searchByCommercePricingClassId(
				commercePricingClassId, name, languageId, start, end);
	}

}