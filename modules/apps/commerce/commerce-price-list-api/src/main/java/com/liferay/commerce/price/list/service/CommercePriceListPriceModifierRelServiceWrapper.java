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

package com.liferay.commerce.price.list.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommercePriceListPriceModifierRelService}.
 *
 * @author Alessio Antonio Rendina
 * @see CommercePriceListPriceModifierRelService
 * @generated
 */
public class CommercePriceListPriceModifierRelServiceWrapper
	implements CommercePriceListPriceModifierRelService,
			   ServiceWrapper<CommercePriceListPriceModifierRelService> {

	public CommercePriceListPriceModifierRelServiceWrapper(
		CommercePriceListPriceModifierRelService
			commercePriceListPriceModifierRelService) {

		_commercePriceListPriceModifierRelService =
			commercePriceListPriceModifierRelService;
	}

	@Override
	public
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
				addCommercePriceListPriceModifierRel(
					long commercePriceListId, long commercePriceModifierId,
					int order,
					com.liferay.portal.kernel.service.ServiceContext
						serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePriceListPriceModifierRelService.
			addCommercePriceListPriceModifierRel(
				commercePriceListId, commercePriceModifierId, order,
				serviceContext);
	}

	@Override
	public void deleteCommercePriceListPriceModifierRel(
			long commercePriceListPriceModifierRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_commercePriceListPriceModifierRelService.
			deleteCommercePriceListPriceModifierRel(
				commercePriceListPriceModifierRelId);
	}

	@Override
	public
		com.liferay.commerce.price.list.model.CommercePriceListPriceModifierRel
				fetchCommercePriceListPriceModifierRel(
					long commercePriceModifierId, long commercePriceListId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePriceListPriceModifierRelService.
			fetchCommercePriceListPriceModifierRel(
				commercePriceModifierId, commercePriceListId);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.price.list.model.
			CommercePriceListPriceModifierRel>
					getCommercePriceListPriceModifierRels(
						long commercePriceListId)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commercePriceListPriceModifierRelService.
			getCommercePriceListPriceModifierRels(commercePriceListId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _commercePriceListPriceModifierRelService.
			getOSGiServiceIdentifier();
	}

	@Override
	public CommercePriceListPriceModifierRelService getWrappedService() {
		return _commercePriceListPriceModifierRelService;
	}

	@Override
	public void setWrappedService(
		CommercePriceListPriceModifierRelService
			commercePriceListPriceModifierRelService) {

		_commercePriceListPriceModifierRelService =
			commercePriceListPriceModifierRelService;
	}

	private CommercePriceListPriceModifierRelService
		_commercePriceListPriceModifierRelService;

}