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

package com.liferay.commerce.pricing.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommercePricingClassRelService}.
 *
 * @author Riccardo Alberti
 * @see CommercePricingClassRelService
 * @generated
 */
public class CommercePricingClassRelServiceWrapper
	implements CommercePricingClassRelService,
			   ServiceWrapper<CommercePricingClassRelService> {

	public CommercePricingClassRelServiceWrapper(
		CommercePricingClassRelService commercePricingClassRelService) {

		_commercePricingClassRelService = commercePricingClassRelService;
	}

	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
			addCommerceDiscountRel(
				long commercePricingClassId, String className, long classPK,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelService.addCommerceDiscountRel(
			commercePricingClassId, className, classPK, serviceContext);
	}

	@Override
	public void deleteCommerceDiscountRel(long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_commercePricingClassRelService.deleteCommerceDiscountRel(
			commercePricingClassId);
	}

	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
			fetchCommerceDiscountRel(String className, long classPK)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelService.fetchCommerceDiscountRel(
			className, classPK);
	}

	@Override
	public long[] getClassPKs(long commercePricingClassId, String className)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelService.getClassPKs(
			commercePricingClassId, className);
	}

	@Override
	public com.liferay.commerce.pricing.model.CommercePricingClassRel
			getCommerceDiscountRel(long commercePricingClassId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelService.getCommerceDiscountRel(
			commercePricingClassId);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
				getCommerceDiscountRels(
					long commercePricingClassId, String className)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelService.getCommerceDiscountRels(
			commercePricingClassId, className);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.pricing.model.CommercePricingClassRel>
				getCommerceDiscountRels(
					long commercePricingClassId, String className, int start,
					int end,
					com.liferay.portal.kernel.util.OrderByComparator
						<com.liferay.commerce.pricing.model.
							CommercePricingClassRel> orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelService.getCommerceDiscountRels(
			commercePricingClassId, className, start, end, orderByComparator);
	}

	@Override
	public int getCommerceDiscountRelsCount(
			long commercePricingClassId, String className)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePricingClassRelService.getCommerceDiscountRelsCount(
			commercePricingClassId, className);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _commercePricingClassRelService.getOSGiServiceIdentifier();
	}

	@Override
	public CommercePricingClassRelService getWrappedService() {
		return _commercePricingClassRelService;
	}

	@Override
	public void setWrappedService(
		CommercePricingClassRelService commercePricingClassRelService) {

		_commercePricingClassRelService = commercePricingClassRelService;
	}

	private CommercePricingClassRelService _commercePricingClassRelService;

}