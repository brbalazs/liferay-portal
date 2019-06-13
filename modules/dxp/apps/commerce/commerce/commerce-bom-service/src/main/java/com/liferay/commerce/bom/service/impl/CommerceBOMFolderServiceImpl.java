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
import com.liferay.commerce.bom.model.CommerceBOMFolder;
import com.liferay.commerce.bom.service.base.CommerceBOMFolderServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;

/**
 * @author Luca Pellizzon
 */
public class CommerceBOMFolderServiceImpl
	extends CommerceBOMFolderServiceBaseImpl {

	@Override
	public CommerceBOMFolder addCommerceBOMFolder(
			long userId, long commerceApplicationModelId, String name,
			long imageId)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(),
			CommerceBOMActionKeys.ADD_COMMERCE_BOM_FOLDER);

		return commerceBOMFolderLocalService.addCommerceBOMFolder(
			userId, commerceApplicationModelId, name, imageId);
	}

	@Override
	public CommerceBOMFolder updateCommerceBOMFolder(
			long commerceBOMFolderId, String name, long imageId)
		throws PortalException {

		_commerceBOMFolderModelResourcePermission.check(
			getPermissionChecker(), commerceBOMFolderId, ActionKeys.UPDATE);

		return commerceBOMFolderLocalService.updateCommerceBOMFolder(
			commerceBOMFolderId, name, imageId);
	}

	private static volatile ModelResourcePermission<CommerceBOMFolder>
		_commerceBOMFolderModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				CommerceBOMFolderServiceImpl.class,
				"_commerceBOMFolderModelResourcePermission",
				CommerceBOMFolder.class);

}