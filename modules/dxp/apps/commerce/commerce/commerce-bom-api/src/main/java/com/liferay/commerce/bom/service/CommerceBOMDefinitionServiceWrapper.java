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
 * Provides a wrapper for {@link CommerceBOMDefinitionService}.
 *
 * @author Luca Pellizzon
 * @see CommerceBOMDefinitionService
 * @generated
 */
@ProviderType
public class CommerceBOMDefinitionServiceWrapper
	implements CommerceBOMDefinitionService,
		ServiceWrapper<CommerceBOMDefinitionService> {
	public CommerceBOMDefinitionServiceWrapper(
		CommerceBOMDefinitionService commerceBOMDefinitionService) {
		_commerceBOMDefinitionService = commerceBOMDefinitionService;
	}

	@Override
	public com.liferay.commerce.bom.model.CommerceBOMDefinition addCommerceBOMDefinition(
		long userId, String name, long imageId, String friendlyUrl,
		long commerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceBOMDefinitionService.addCommerceBOMDefinition(userId,
			name, imageId, friendlyUrl, commerceBOMFolderId);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceBOMDefinitionService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.commerce.bom.model.CommerceBOMDefinition updateCommerceBOMDefinition(
		long commerceBOMDefinitionId, String name, long imageId,
		String friendlyUrl, long commerceBOMFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _commerceBOMDefinitionService.updateCommerceBOMDefinition(commerceBOMDefinitionId,
			name, imageId, friendlyUrl, commerceBOMFolderId);
	}

	@Override
	public CommerceBOMDefinitionService getWrappedService() {
		return _commerceBOMDefinitionService;
	}

	@Override
	public void setWrappedService(
		CommerceBOMDefinitionService commerceBOMDefinitionService) {
		_commerceBOMDefinitionService = commerceBOMDefinitionService;
	}

	private CommerceBOMDefinitionService _commerceBOMDefinitionService;
}