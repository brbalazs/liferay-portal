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

package com.liferay.commerce.application.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommerceApplicationModelService}.
 *
 * @author Luca Pellizzon
 * @see CommerceApplicationModelService
 * @generated
 */
@ProviderType
public class CommerceApplicationModelServiceWrapper
	implements CommerceApplicationModelService,
		ServiceWrapper<CommerceApplicationModelService> {
	public CommerceApplicationModelServiceWrapper(
		CommerceApplicationModelService commerceApplicationModelService) {
		_commerceApplicationModelService = commerceApplicationModelService;
	}

	@Override
	public com.liferay.commerce.application.model.CommerceApplicationModel addCommerceApplicationModel(
		long userId, long commerceApplicationBrandId, long cProductId,
		String name, String year)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceApplicationModelService.addCommerceApplicationModel(userId,
			commerceApplicationBrandId, cProductId, name, year);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceApplicationModelService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.commerce.application.model.CommerceApplicationModel updateCommerceApplicationModel(
		long commerceApplicationModelId, long commerceApplicationBrandId,
		String name, String year)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceApplicationModelService.updateCommerceApplicationModel(commerceApplicationModelId,
			commerceApplicationBrandId, name, year);
	}

	@Override
	public CommerceApplicationModelService getWrappedService() {
		return _commerceApplicationModelService;
	}

	@Override
	public void setWrappedService(
		CommerceApplicationModelService commerceApplicationModelService) {
		_commerceApplicationModelService = commerceApplicationModelService;
	}

	private CommerceApplicationModelService _commerceApplicationModelService;
}