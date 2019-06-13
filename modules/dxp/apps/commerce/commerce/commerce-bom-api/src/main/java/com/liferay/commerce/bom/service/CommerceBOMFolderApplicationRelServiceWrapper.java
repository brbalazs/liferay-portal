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

package com.liferay.commerce.bom.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommerceBOMFolderApplicationRelService}.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMFolderApplicationRelService
 * @generated
 */
@ProviderType
public class CommerceBOMFolderApplicationRelServiceWrapper
	implements CommerceBOMFolderApplicationRelService,
		ServiceWrapper<CommerceBOMFolderApplicationRelService> {
	public CommerceBOMFolderApplicationRelServiceWrapper(
		CommerceBOMFolderApplicationRelService commerceBOMFolderApplicationRelService) {
		_commerceBOMFolderApplicationRelService = commerceBOMFolderApplicationRelService;
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceBOMFolderApplicationRelService.getOSGiServiceIdentifier();
	}

	@Override
	public CommerceBOMFolderApplicationRelService getWrappedService() {
		return _commerceBOMFolderApplicationRelService;
	}

	@Override
	public void setWrappedService(
		CommerceBOMFolderApplicationRelService commerceBOMFolderApplicationRelService) {
		_commerceBOMFolderApplicationRelService = commerceBOMFolderApplicationRelService;
	}

	private CommerceBOMFolderApplicationRelService _commerceBOMFolderApplicationRelService;
}