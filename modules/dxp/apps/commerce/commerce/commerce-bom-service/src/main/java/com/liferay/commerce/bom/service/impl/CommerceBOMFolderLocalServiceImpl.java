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

import com.liferay.commerce.bom.model.CommerceBOMFolder;
import com.liferay.commerce.bom.service.base.CommerceBOMFolderLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;

/**
 * @author Luca Pellizzon
 */
public class CommerceBOMFolderLocalServiceImpl
	extends CommerceBOMFolderLocalServiceBaseImpl {

	@Override
	public CommerceBOMFolder addCommerceBOMFolder(
			long userId, long commerceApplicationModelId, String name,
			long imageId)
		throws PortalException {

		CommerceBOMFolder commerceBOMFolder =
			commerceBOMFolderLocalService.addCommerceBOMFolder(
				userId, name, imageId);

		commerceBOMFolderApplicationRelLocalService.
			addCommerceBOMFolderApplicationRel(
				userId, commerceBOMFolder.getCommerceBOMFolderId(),
				commerceApplicationModelId);

		return commerceBOMFolder;
	}

	@Override
	public CommerceBOMFolder addCommerceBOMFolder(
			long userId, String name, long imageId)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long commerceBOMFolderId = counterLocalService.increment();

		CommerceBOMFolder commerceBOMFolder =
			commerceBOMFolderPersistence.create(commerceBOMFolderId);

		commerceBOMFolder.setCompanyId(user.getCompanyId());
		commerceBOMFolder.setUserId(user.getUserId());
		commerceBOMFolder.setUserName(user.getFullName());
		commerceBOMFolder.setName(name);
		commerceBOMFolder.setImageId(imageId);

		return commerceBOMFolderPersistence.update(commerceBOMFolder);
	}

	@Override
	public CommerceBOMFolder updateCommerceBOMFolder(
			long commerceBOMFolderId, String name, long imageId)
		throws PortalException {

		CommerceBOMFolder commerceBOMFolder =
			commerceBOMFolderLocalService.getCommerceBOMFolder(
				commerceBOMFolderId);

		commerceBOMFolder.setName(name);
		commerceBOMFolder.setImageId(imageId);

		return commerceBOMFolderPersistence.update(commerceBOMFolder);
	}

}