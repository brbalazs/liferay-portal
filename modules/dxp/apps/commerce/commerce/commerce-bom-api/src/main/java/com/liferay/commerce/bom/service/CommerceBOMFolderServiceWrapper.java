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
 * Provides a wrapper for {@link CommerceBOMFolderService}.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMFolderService
 * @generated
 */
@ProviderType
public class CommerceBOMFolderServiceWrapper implements CommerceBOMFolderService,
	ServiceWrapper<CommerceBOMFolderService> {
	public CommerceBOMFolderServiceWrapper(
		CommerceBOMFolderService commerceBOMFolderService) {
		_commerceBOMFolderService = commerceBOMFolderService;
	}

	@Override
	public com.liferay.commerce.bom.model.CommerceBOMFolder addCommerceBOMFolder(
		long userId, long commerceApplicationModelId, String name, long imageId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceBOMFolderService.addCommerceBOMFolder(userId,
			commerceApplicationModelId, name, imageId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceBOMFolderService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.commerce.bom.model.CommerceBOMFolder updateCommerceBOMFolder(
		long commerceBOMFolderId, String name, long imageId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceBOMFolderService.updateCommerceBOMFolder(commerceBOMFolderId,
			name, imageId);
	}

	@Override
	public CommerceBOMFolderService getWrappedService() {
		return _commerceBOMFolderService;
	}

	@Override
	public void setWrappedService(
		CommerceBOMFolderService commerceBOMFolderService) {
		_commerceBOMFolderService = commerceBOMFolderService;
	}

	private CommerceBOMFolderService _commerceBOMFolderService;
}