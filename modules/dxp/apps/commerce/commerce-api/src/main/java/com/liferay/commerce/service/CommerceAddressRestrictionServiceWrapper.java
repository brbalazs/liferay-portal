/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommerceAddressRestrictionService}.
 *
 * @author Alessio Antonio Rendina
 * @see CommerceAddressRestrictionService
 * @generated
 */
@ProviderType
public class CommerceAddressRestrictionServiceWrapper
	implements CommerceAddressRestrictionService,
		ServiceWrapper<CommerceAddressRestrictionService> {
	public CommerceAddressRestrictionServiceWrapper(
		CommerceAddressRestrictionService commerceAddressRestrictionService) {
		_commerceAddressRestrictionService = commerceAddressRestrictionService;
	}

	@Override
	public com.liferay.commerce.model.CommerceAddressRestriction addCommerceAddressRestriction(
		String className, long classPK, long commerceCountryId,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceAddressRestrictionService.addCommerceAddressRestriction(className,
			classPK, commerceCountryId, serviceContext);
	}

	@Override
	public void deleteCommerceAddressRestriction(
		long commerceAddressRestrictionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		_commerceAddressRestrictionService.deleteCommerceAddressRestriction(commerceAddressRestrictionId);
	}

	@Override
	public java.util.List<com.liferay.commerce.model.CommerceAddressRestriction> getCommerceAddressRestrictions(
		String className, long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.commerce.model.CommerceAddressRestriction> orderByComparator)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceAddressRestrictionService.getCommerceAddressRestrictions(className,
			classPK, start, end, orderByComparator);
	}

	@Override
	public int getCommerceAddressRestrictionsCount(String className,
		long classPK)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceAddressRestrictionService.getCommerceAddressRestrictionsCount(className,
			classPK);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceAddressRestrictionService.getOSGiServiceIdentifier();
	}

	@Override
	public boolean isCommerceShippingMethodRestricted(
		long commerceShippingMethodId, long commerceCountryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceAddressRestrictionService.isCommerceShippingMethodRestricted(commerceShippingMethodId,
			commerceCountryId);
	}

	@Override
	public CommerceAddressRestrictionService getWrappedService() {
		return _commerceAddressRestrictionService;
	}

	@Override
	public void setWrappedService(
		CommerceAddressRestrictionService commerceAddressRestrictionService) {
		_commerceAddressRestrictionService = commerceAddressRestrictionService;
	}

	private CommerceAddressRestrictionService _commerceAddressRestrictionService;
}