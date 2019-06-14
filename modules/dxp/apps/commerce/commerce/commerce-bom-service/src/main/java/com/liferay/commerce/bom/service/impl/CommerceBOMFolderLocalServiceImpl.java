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
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;

import java.util.List;

/**
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 */
public class CommerceBOMFolderLocalServiceImpl
	extends CommerceBOMFolderLocalServiceBaseImpl {

	@Override
	public CommerceBOMFolder addCommerceBOMFolder(
			long userId, long parentCommerceBOMFolderId, String name,
			long imageId)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long commerceBOMFolderId = counterLocalService.increment();

		CommerceBOMFolder commerceBOMFolder =
			commerceBOMFolderPersistence.create(commerceBOMFolderId);

		commerceBOMFolder.setCompanyId(user.getCompanyId());
		commerceBOMFolder.setUserId(user.getUserId());
		commerceBOMFolder.setUserName(user.getFullName());
		commerceBOMFolder.setParentCommerceBOMFolderId(
			parentCommerceBOMFolderId);
		commerceBOMFolder.setName(name);
		commerceBOMFolder.setImageId(imageId);

		commerceBOMFolder = commerceBOMFolderPersistence.update(
			commerceBOMFolder);

		// Resources

		resourceLocalService.addResources(
			user.getCompanyId(), GroupConstants.DEFAULT_LIVE_GROUP_ID,
			user.getUserId(), CommerceBOMFolder.class.getName(),
			commerceBOMFolder.getCommerceBOMFolderId(), false, false, false);

		return commerceBOMFolder;
	}

	@Override
	public CommerceBOMFolder deleteCommerceBOMFolder(
			CommerceBOMFolder commerceBOMFolder)
		throws PortalException {

		// Resources

		resourceLocalService.deleteResource(
			commerceBOMFolder, ResourceConstants.SCOPE_INDIVIDUAL);

		// Commerce BOM folder

		return commerceBOMFolderPersistence.remove(commerceBOMFolder);
	}

	@Override
	public CommerceBOMFolder deleteCommerceBOMFolder(long commerceBOMFolderId)
		throws PortalException {

		CommerceBOMFolder commerceBOMFolder =
			commerceBOMFolderPersistence.findByPrimaryKey(commerceBOMFolderId);

		return commerceBOMFolderLocalService.deleteCommerceBOMFolder(
			commerceBOMFolder);
	}

	@Override
	public void deleteCommerceBOMFolders(long companyId)
		throws PortalException {

		List<CommerceBOMFolder> commerceBOMFolders =
			commerceBOMFolderPersistence.findByCompany(companyId);

		for (CommerceBOMFolder commerceBOMFolder : commerceBOMFolders) {
			commerceBOMFolderLocalService.deleteCommerceBOMFolder(
				commerceBOMFolder);
		}
	}

	@Override
	public List<CommerceBOMFolder> getCommerceBOMFolders(
		long companyId, long parentCommerceBOMFolderId, int start, int end) {

		return commerceBOMFolderPersistence.findByC_P(
			companyId, parentCommerceBOMFolderId, start, end);
	}

	@Override
	public int getCommerceBOMFoldersCount(
		long companyId, long parentCommerceBOMFolderId) {

		return commerceBOMFolderPersistence.countByC_P(
			companyId, parentCommerceBOMFolderId);
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