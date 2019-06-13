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
 * Provides a wrapper for {@link CommerceApplicationModelCProductRelService}.
 *
 * @author Luca Pellizzon
 * @see CommerceApplicationModelCProductRelService
 * @generated
 */
@ProviderType
public class CommerceApplicationModelCProductRelServiceWrapper
	implements CommerceApplicationModelCProductRelService,
		ServiceWrapper<CommerceApplicationModelCProductRelService> {
	public CommerceApplicationModelCProductRelServiceWrapper(
		CommerceApplicationModelCProductRelService commerceApplicationModelCProductRelService) {
		_commerceApplicationModelCProductRelService = commerceApplicationModelCProductRelService;
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceApplicationModelCProductRelService.getOSGiServiceIdentifier();
	}

	@Override
	public CommerceApplicationModelCProductRelService getWrappedService() {
		return _commerceApplicationModelCProductRelService;
	}

	@Override
	public void setWrappedService(
		CommerceApplicationModelCProductRelService commerceApplicationModelCProductRelService) {
		_commerceApplicationModelCProductRelService = commerceApplicationModelCProductRelService;
	}

	private CommerceApplicationModelCProductRelService _commerceApplicationModelCProductRelService;
}