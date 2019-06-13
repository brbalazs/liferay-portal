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

package com.liferay.commerce.bom.service.impl;

import com.liferay.commerce.bom.constants.CommerceBOMActionKeys;
import com.liferay.commerce.bom.model.CommerceBOMDefinition;
import com.liferay.commerce.bom.service.base.CommerceBOMDefinitionServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;

/**
 * @author Luca Pellizzon
 */
public class CommerceBOMDefinitionServiceImpl
	extends CommerceBOMDefinitionServiceBaseImpl {

	@Override
	public CommerceBOMDefinition addCommerceBOMDefinition(
			long userId, String name, long imageId, String friendlyUrl,
			long commerceBOMFolderId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceBOMActionKeys.ADD_COMMERCE_BOM_DEFINITION);

		return commerceBOMDefinitionLocalService.addCommerceBOMDefinition(
			userId, name, imageId, friendlyUrl, commerceBOMFolderId);
	}

	@Override
	public CommerceBOMDefinition updateCommerceBOMDefinition(
			long commerceBOMDefinitionId, String name, long imageId,
			String friendlyUrl, long commerceBOMFolderId)
		throws PortalException {

		_commerceBOMDefinitionModelResourcePermission.check(
			getPermissionChecker(), commerceBOMDefinitionId, ActionKeys.UPDATE);

		return commerceBOMDefinitionLocalService.updateCommerceBOMDefinition(
			commerceBOMDefinitionId, name, imageId, friendlyUrl,
			commerceBOMFolderId);
	}

	private static volatile ModelResourcePermission<CommerceBOMDefinition>
		_commerceBOMDefinitionModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				CommerceBOMDefinitionServiceImpl.class,
				"_commerceBOMDefinitionModelResourcePermission",
				CommerceBOMDefinition.class);

}