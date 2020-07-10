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
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Riccardo Alberti
 * @see CommercePricingClassServiceBaseImpl
 */
public class CommercePricingClassServiceImpl
	extends CommercePricingClassServiceBaseImpl {

	@Override
	public CommercePricingClass addCommercePricingClass(
			long userId, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.addCommercePricingClass(
			userId, titleMap, descriptionMap, null, serviceContext);
	}

	@Override
	public CommercePricingClass addCommercePricingClass(
			long userId, Map<Locale, String> titleMap,
			Map<Locale, String> descriptionMap, String externalReferenceCode,
			ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.addCommercePricingClass(
			userId, title, description, serviceContext);
	}

	@Override
	public CommercePricingClass deleteCommercePricingClass(
			long commercePricingClassId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

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
			PortalPermissionUtil.check(
				getPermissionChecker(),
				CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);
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
			PortalPermissionUtil.check(
				getPermissionChecker(),
				CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);
		}

		return commercePricingClass;
	}

	@Override
	public CommercePricingClass getCommercePricingClass(
			long commercePricingClassId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.getCommercePricingClass(
			commercePricingClassId);
	}

	@Override
	public long[] getCommercePricingClassByCPDefinition(long cpDefinitionId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.
			getCommercePricingClassByCPDefinition(cpDefinitionId);
	}

	@Override
	public int getCommercePricingClassesCount(long cpDefinitionId, String title)
		throws PrincipalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.getCommercePricingClassesCount(
			cpDefinitionId, title);
	}

	@Override
	public List<CommercePricingClass> getCommercePricingClasses(
			long companyId, int start, int end,
			OrderByComparator<CommercePricingClass> orderByComparator)
		throws PortalException {

		return commercePricingClassPersistence.filterFindByCompanyId(
			companyId, start, end, orderByComparator);
	}

	@Override
	public int getCommercePricingClassesCount(long companyId)
		throws PortalException {

		return commercePricingClassPersistence.filterCountByCompanyId(
			companyId);
	}

	@Override
	public List<CommercePricingClass> searchByCPDefinitionId(
			long cpDefinitionId, String title, int start, int end)
		throws PrincipalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.searchByCPDefinitionId(
			cpDefinitionId, title, start, end);
	}

	@Override
	public BaseModelSearchResult<CommercePricingClass>
			searchCommercePricingClasses(
				long companyId, String keywords, int start, int end, Sort sort)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.VIEW_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.searchCommercePricingClasses(
			companyId, keywords, start, end, sort);
	}

	@Override
	public CommercePricingClass updateCommercePricingClass(
			long commercePricingClassId, long userId,
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.updateCommercePricingClass(
			commercePricingClassId, userId, titleMap, descriptionMap,
			serviceContext);
	}

	@Override
	public CommercePricingClass updateCommercePricingClass(
			long commercePricingClassId, long userId, long groupId,
			String title, String description, ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.updateCommercePricingClass(
			commercePricingClassId, userId, titleMap, descriptionMap,
			serviceContext);
	}

	@Override
	public CommercePricingClass upsertCommercePricingClass(
			long commercePricingClassId, long userId,
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			String externalReferenceCode, ServiceContext serviceContext)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommercePricingClassActionKeys.MANAGE_COMMERCE_PRICING_CLASSES);

		return commercePricingClassLocalService.upsertCommercePricingClass(
			commercePricingClassId, userId, titleMap, descriptionMap,
			externalReferenceCode, serviceContext);
	}

	@Override
	public CommercePricingClass upsertCommercePricingClass(
			long commercePricingClassId, long userId, long groupId,
			String title, String description, String externalReferenceCode,
			ServiceContext serviceContext)
		throws PortalException {

		if (commercePricingClassId > 0) {
			try {
				return updateCommercePricingClass(
					commercePricingClassId, userId, titleMap,
					descriptionMap, serviceContext);
			}
			catch (NoSuchPricingClassException nspc) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Unable to find pricing class with ID: " +
							commercePricingClassId);
				}
			}
		}

		if (!Validator.isBlank(externalReferenceCode)) {
			CommercePricingClass commercePricingClass =
				commercePricingClassPersistence.fetchByC_ERC(
					serviceContext.getCompanyId(), externalReferenceCode);

			if (commercePricingClass != null) {
				return updateCommercePricingClass(
					commercePricingClassId, userId, titleMap,
					descriptionMap, serviceContext);
			}
		}

		return addCommercePricingClass(
			userId, titleMap, descriptionMap, externalReferenceCode,
			serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePricingClassServiceImpl.class);

	private static volatile ModelResourcePermission<CommercePricingClass>
		_commercePricingClassResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				CommercePricingClassServiceImpl.class,
				"_commercePricingClassResourcePermission",
				CommercePricingClass.class);
}